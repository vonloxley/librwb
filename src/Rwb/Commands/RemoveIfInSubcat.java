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
import Rwb.ParameterException;
import Rwb.Wiki;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Niki Hansche
 */
public class RemoveIfInSubcat implements WikiCommand {

    String cat = null;

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        if (cat == null) {
            throw new ParameterException("Removeifinsubcat needs one Parameter: String.");
        }
        
        try {
            List<String> cats = new ArrayList<>(Arrays.asList(rwiki.getCategoryMembers(cat, 14)));
            List<String> pages = new ArrayList<>(Arrays.asList(rwiki.getCategoryMembers(cat, 0)));
            List<String> dest = new ArrayList<>();

            for (String cat : cats) {
                putIntoDstIfInSrc(rwiki, cat, pages, dest);
            }

            for (String title : dest) {
                String pageText = "", pageTextWithoutCats = "";
                try {
                    pageText = rwiki.getPageText(title);
                    pageTextWithoutCats = Categories.putOrRemoveCategory(pageText, cat, false, rwiki);
                } catch (java.io.FileNotFoundException ex) {
                    continue;
                } catch (IOException ex) {
                    Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (pageText.equals(pageTextWithoutCats)) {
                    continue;
                }

                Wiki.printDiff(title, pageText, pageTextWithoutCats, org.fusesource.jansi.AnsiConsole.out);
                boolean go = rwiki.getGo();
                if (go) {
                    try {
                        rwiki.edit(title, pageTextWithoutCats, rwiki.getSummary());
                    } catch (IOException | LoginException ex) {
                        Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RemoveIfInSubcat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        if (parameter.size() < 1) {
            throw new ParameterException("Removeifinsubcat needs one Parameter: String.");
        }
        cat = (String) parameter.pop();
    }

    private void putIntoDstIfInSrc(Wiki rwiki, String cat, List<String> src, List<String> dst) throws IOException {
        List<String> p = new ArrayList<>(Arrays.asList(rwiki.getCategoryMembers(cat, 0)));
        p.retainAll(src);
        dst.addAll(p);
        List<String> c = Arrays.asList(rwiki.getCategoryMembers(cat, 14));
        for (String string : c) {
            putIntoDstIfInSrc(rwiki, string, src, dst);

        }
    }

}
