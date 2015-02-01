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
public class RenameCat implements WikiCommand {

    String fromcat = null;
    String tocat = null;

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        if (fromcat == null || tocat == null) {
            throw new ParameterException("Renamecat needs two parametera: String, String.");
        }

        try {
            List<String> pages = new ArrayList<>(Arrays.asList(rwiki.getCategoryMembers(fromcat, rwiki.getWorkingNamespaces())));

            for (String page : pages) {
                String pagetext;
                String origpagetext;
                try {
                    pagetext = rwiki.getPageText(page);
                    origpagetext = pagetext;

                    Categories.removeCategory(pagetext, fromcat, rwiki);
                    Categories.putCategory(pagetext, tocat, rwiki);
                } catch (java.io.FileNotFoundException ex) {
                    continue;
                } catch (IOException ex) {
                    Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }

                Wiki.printDiff(page, origpagetext, pagetext, org.fusesource.jansi.AnsiConsole.out);
                boolean go = rwiki.getGo();
                if (go) {
                    try {
                        rwiki.edit(page, pagetext, rwiki.getSummary());
                    } catch (IOException | LoginException ex) {
                        Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(RenameCat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        if (parameter.size() != 2) {
            throw new ParameterException("Removeifinsubcat needs two parameters: String, String.");
        }
        fromcat = (String) parameter.pop();
        tocat = (String) parameter.pop();
    }

}
