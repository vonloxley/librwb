/*
 * Copyright (C) 2014 Niki Hansche
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Rwb.Commands;

import Rwb.Categories.Categories;
import Rwb.Wiki;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Niki Hansche
 */
public class CatOrganize implements WikiCommand {

    List<String> src;
    String dst;
    Boolean addtodest;

    public CatOrganize(List<String> src, String dst, Boolean addtodest) {
        this.src = src;
        this.dst = dst;
        this.addtodest = addtodest;
    }

    @Override
    public void execute(Wiki context) throws CommandException {
        if (src == null) {
            throw new CommandException("Paramter <source> needed.");
        }
        if (dst == null) {
            throw new CommandException("Paramter <dest> needed.");
        }
        if (src.size() < 2) {
            throw new CommandException("Need at least two source-categories.");
        }
        if (addtodest == null) {
            addtodest = true;
        }

        List<String> sc = processCategories(context, src, dst, addtodest);

        Collections.sort(sc);

        String lastns = "";
        for (String ns : sc) {
            if (lastns.equals(ns)) {
                continue;
            }
            lastns = ns;
            String pageText = "", pageTextWithoutCats = "";
            try {
                pageText = context.getPageText(ns);
                pageTextWithoutCats = Categories.putOrRemoveCategory(pageText, dst, addtodest, context);
            } catch (java.io.FileNotFoundException ex) {
                continue;
            } catch (IOException ex) {
                Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (pageText.equals(pageTextWithoutCats)) {
                continue;
            }

            Wiki.printDiff(ns, pageText, pageTextWithoutCats, org.fusesource.jansi.AnsiConsole.out);
            boolean go = context.getGo();
            if (go) {
                try {
                    context.edit(ns, pageTextWithoutCats, context.getSummary());
                } catch (IOException | LoginException ex) {
                    Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        context.removeFromCache(dst);
    }


    /**
     *
     * @param src List of all categories a page must be in.
     * @param dst Category the page shall be in.
     * @return List of Pages that are in all src categories but not in dst.
     */
    private List<String> processCategories(Wiki rw, List<String> src, String dst, boolean addtodest) {
        List<Future<List<String>>> sourcecats = new ArrayList<>();
        Future<List<String>> destcat;
        ExecutorService exe = Executors.newFixedThreadPool(3);

        try {
            for (String cat : src) {
                sourcecats.add(exe.submit(rw.getCat(cat)));
            }
            destcat = exe.submit(rw.getCat(dst));
        } finally {
            exe.shutdown();
        }

        List<String> sc = new ArrayList<>();
        List<String> dc = new ArrayList<>();
        try {
            sc.addAll(sourcecats.get(0).get());

            for (int i = 1; i < sourcecats.size(); i++) {
                sc.retainAll(sourcecats.get(i).get());
            }
            dc.addAll(destcat.get());

            if (addtodest) {
                sc.removeAll(dc);
            } else {
                dc.removeAll(sc);
                sc = dc;
            }

        } catch (ExecutionException | InterruptedException ex) {
            Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sc;
    }

    @Override

    public String toString() {
        StringBuilder sb = new StringBuilder(100);

        sb.append("[CatOrganize: From: [");
        for (String string : src) {
            sb.append(string).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("] To: [").append(dst).append("]");
        if (!addtodest) {
            sb.append(" Remove");
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
