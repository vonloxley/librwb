package Rwb;

import difflib.DiffRow;
import difflib.DiffRowGenerator;
import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.wikiutils.ParseUtils;

/**
 *
 * @author Niki Hansche
 */
public class Wiki extends org.wikipedia.Wiki {

    private String summary = "";
    private Boolean allgo = false;
    private Map<String, List<String>> catcache;
    private List<CommitRecord> commitcache;
    private Map<String, String> pagecache;
    private List<IgnorePosition> ignorepositions;

    public Wiki(String protocoll, String domain, String scriptPath) {
        super(protocoll, domain, scriptPath);
    }

    /**
     * Set standard blocks to be ignored in commands that replace or search data
     * in pages.
     */
    public final void reInitIgnorePositions() {
        ignorepositions = new ArrayList<>(4);
        addIgnorePosition("<nowiki>", "</nowiki>");
        addIgnorePosition("<!--", "-->");
        addIgnorePosition("\\r?\\n=+", "\\r?\\n?=+");
        addIgnorePosition("<pre>", "</pre>");
        addIgnorePosition("<gallery", "</gallery>");
    }
    
    /**
     * Add a block to the ignored blocks.
     * Start and end may be regular expressions.
     *
     * @param start
     * @param end 
     */
    public void addIgnorePosition(String start, String end) {
        if (ignorepositions == null) {
            reInitIgnorePositions();
        }

        IgnorePosition ip = new IgnorePosition(start, end);
        ignorepositions.add(ip);
    }

    @Override
    public void edit(String title, String text, String summary, boolean minor, boolean bot, int section, Calendar basetime) throws IOException, LoginException {
        if (commitcache == null) {
            commitcache = new ArrayList<>();
        }
        if (pagecache == null) {
            pagecache = new HashMap<>();
        }
        
        commitcache.add(new CommitRecord(title, text, summary, minor, bot, section, basetime));
        pagecache.remove(title); //Invalidate pagecache early on.
    }

    @Override
    public String getPageText(String title) throws IOException {
        if (commitcache != null) {
            for (int i = commitcache.size() - 1; i >= 0; i--) {
                CommitRecord pg = commitcache.get(i);
                if (pg.title.equals(title)) {
                    return pg.text;
                }
            }
        }
        if (pagecache == null) {
            pagecache = new HashMap<>();
        }

        String p = pagecache.get(title);
        if (p == null) {
            p = super.getPageText(title);
            pagecache.put(title, p);
        }

        return p;
    }

    /**
     * Write cached pages to the wiki.
     * 
     * @param compressed - Write only one edit containing all changes.
     * @throws IOException
     * @throws LoginException 
     */
    public void commitPagecache(boolean compressed) throws IOException, LoginException {
        if (commitcache != null) {
            if (compressed) {
                Map<String, CommitRecord> found = new HashMap<>();
                for (int i = commitcache.size() - 1; i >= 0; i--) {
                    CommitRecord r = commitcache.get(i);
                    if (!found.containsKey(r.title)) {
                        found.put(r.title, r);
                    }
                }
                commitcache = new ArrayList<>(found.values());
            }

            List<CommitRecord> todel = new ArrayList<>();
            try {
                for (CommitRecord pageCache : commitcache) {
                    try {
                        System.out.println("Speichere: " + pageCache);
                        super.edit(pageCache.title, pageCache.text, pageCache.summary, pageCache.minor, pageCache.bot, pageCache.section, pageCache.basetime);
                    } catch (IOException ex) {
                        Logger.getLogger("rwiki").log(Level.SEVERE, pageCache.title + " konnte nicht gesichert werden.", ex);
                        throw ex;
                    }
                    todel.add(pageCache);
                }
            } finally {
                commitcache.removeAll(todel);
            }
        }
    }

    /**
     * Print diffs between pages.
     *
     * @param title - Title of page.
     * @param pageOrig - Old page.
     * @param pageNew - New page.
     * @param os - OutputStream to print to.
     */
    public static void printDiff(String title, String pageOrig, String pageNew, OutputStream os) {
        ArrayList<String> ol = new ArrayList<>(Arrays.asList(pageOrig.split("(?:\\r?\\n)")));
        ArrayList<String> nl = new ArrayList<>(Arrays.asList(pageNew.split("(?:\\r?\\n)")));

        DiffRowGenerator generator = new DiffRowGenerator.Builder()
                .showInlineDiffs(false)
                .ignoreWhiteSpaces(false)
                .columnWidth(999)
                .build();

        PrintStream ps = new PrintStream(os);
        ps.println(ansi().a(title).reset());
        for (DiffRow r : generator.generateDiffRows(ol, nl)) {
            switch (r.getTag()) {
                case CHANGE:
                    ps.println(ansi().a("--- ").fg(Ansi.Color.RED).a(r.getOldLine()).reset());
                    ps.println(ansi().a("+++ ").fg(Ansi.Color.GREEN).a(r.getNewLine()).reset());
                    break;
                case DELETE:
                    ps.println(ansi().a("--- ").fg(Ansi.Color.RED).a(r.getOldLine()).reset());
                    break;
                case INSERT:
                    ps.println(ansi().a("+++ ").fg(Ansi.Color.GREEN).a(r.getNewLine()).reset());
                    break;
                default:
                //ps.println("    "+ r.getOldLine());
            }
        }
        ps.flush();
    }

    /**
     * Print diff between two pages and ask the user to accept or decline the edit.
     * 
     * @param title
     * @param pageOrig
     * @param pageNew
     * @return True if the user accepted the edit, false if not.
     */
    public static boolean acceptDiff(String title, String pageOrig, String pageNew) {
        Console c = System.console();
        String ins;
        boolean ret = false;

        if (c != null) {
            printDiff(title, pageOrig, pageNew, System.out);
            System.out.print("Accept? ");
            ins = c.readLine().toLowerCase();
            ret = ins.matches("^[jy]|yes|ja$");
        } else {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                printDiff(title, pageOrig, pageNew, new org.fusesource.jansi.HtmlAnsiOutputStream(os));
                //Todo: Fix "replace"-hack
                JLabel l = new JLabel("<html>" + os.toString().replace("</span>", "</span><br />") + "</html>");
                ret = (JOptionPane.showConfirmDialog(null, l, "Titel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
            } catch (HeadlessException e) {
                Logger.getLogger("rwiki").log(Level.SEVERE, "Keine Benutzerinteraktion möglich.", e);
            }
        }

        return ret;
    }

    /**
     * Get begin- and end-indices of all ignored blocks in text.
     * @param text
     * @return Map of begin/end pairs.
     */
    public Map<Integer, Integer> getIgnorePositions(String text) {
        Map<Integer, Integer> im, imn;
        im = new HashMap<>();
    
        if (ignorepositions == null) {
            reInitIgnorePositions();
        }

        for (IgnorePosition i : ignorepositions) {
            imn = ParseUtils.getIgnorePositions(text, i.getStart(), i.getEnd());
            if (imn != null && imn.size() > 0) {
                im.putAll(imn);
            }
        }

        return im;
    }

    /**
     * Get all members of category cat and its sub-cats in MAIN_NAMESPACE.
     *
     * @param cat
     * @return A Callable that returns a {@code List<String>}.
     */
    public Callable<List<String>> getCat(final String cat) {
        return new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> r;
                r = getFromCache(cat);
                if (r == null) {
                    r = new ArrayList<>(Arrays.asList(getCategoryMembers(cat, true, Wiki.MAIN_NAMESPACE)));
                }
                putToCache(cat, r);
                return r;
            }
        };
    }

    /**
     * Save this  instance to FileName.
     * @param FileName 
     */
    public void saveThis(String FileName) {
        ObjectOutputStream out = null;
        try {
            File f = new File(FileName);
            f.setReadable(true, true);
            f.setWritable(true, true);
            out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(this);
        } catch (IOException ex) {
            Logger.getLogger("wiki").log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger("wiki").log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the allgo
     */
    public Boolean getAllgo() {
        return allgo;
    }

    /**
     * @param allgo the allgo to set
     */
    public void setAllgo(Boolean allgo) {
        this.allgo = allgo;
    }

    /**
     * retrive a {@code List<String>} from cache.
     * @param ident
     * @return 
     */
    public List<String> getFromCache(String ident) {
        List<String> r = null;
        try {
            r = catcache.get(ident);
        } catch (NullPointerException ex) {
        }
        return r;
    }

    /**
     * Cache a {@code List<String>}
     * @param ident
     * @param payload 
     */
    public void putToCache(String ident, List<String> payload) {
        synchronized(this){
            if (catcache == null) {
                catcache = new ConcurrentHashMap<>();
            }
        }
        catcache.put(ident, payload);
    }

    /**
     * Delete a {@code List<String>} from cache.
     * @param ident 
     */
    public void removeFromCache(String ident) {
        if (catcache != null) {
            catcache.remove(ident);
        }
    }

    /**
     * Gets a console or aborts the program.
     * @return 
     */
    public static Console getConsole() {
        Console c = System.console();
        if (c == null) {
            Logger.getLogger(Wiki.class.getName()).log(Level.SEVERE, "Konnte keine Konsole bekommen.");
            new Throwable().printStackTrace();
            System.exit(1);
        }
        return c;
    }

    /**
     * Ask the user to go ahead or not. May {@link setAllgo}
     * @return 
     */
    public boolean getGo() {
        String s;
        boolean go = this.getAllgo();
        if (go) {
            return go;
        }
        Console c = Wiki.getConsole();
        rl:
        while (!this.getAllgo()) {
            s = c.readLine("Ändern? [j(a), n(ein), a(lle): ").trim();
            switch (s) {
                case "j":
                    go = true;
                    break rl;
                case "n":
                    go = false;
                    break rl;
                case "a":
                    go = true;
                    this.setAllgo(true);
                    break rl;
            }
        }
        return go;
    }

    public static class Page {

        public String text;
        public List<String> removedCategories;

        public Page(String text, List<String> removedCategories) {
            this.text = text;
            this.removedCategories = removedCategories;
        }
    }

    class IgnorePosition {

        private String start;
        private String end;

        public IgnorePosition(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getEnd() {
            return end;
        }

        public String getStart() {
            return start;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public void setStart(String start) {
            this.start = start;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ")";
        }

    }

    private static class CommitRecord {

        final String title;
        final String text;
        final String summary;
        final boolean minor;
        final boolean bot;
        final int section;
        final Calendar basetime;

        public CommitRecord(String title, String text, String summary, boolean minor, boolean bot, int section, Calendar basetime) {
            this.title = title;
            this.text = text;
            this.summary = summary;
            this.minor = minor;
            this.bot = bot;
            this.section = section;
            this.basetime = basetime;
        }

        @Override
        public String toString() {
            return String.format("'%s' | '%s'", title, summary);
        }

    }
}
