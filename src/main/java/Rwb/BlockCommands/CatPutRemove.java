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
package Rwb.BlockCommands;

import Rwb.Categories.Categories;
import Rwb.Commands.CatOrganize;
import Rwb.Commands.CommandException;
import Rwb.Generators.PageGenerator;
import Rwb.ParameterException;
import Rwb.Wiki;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Niki Hansche
 */
public class CatPutRemove implements WikiBlockCommand {

    boolean put;
    List<String> category;
    PageGenerator pagegen;

    public CatPutRemove(boolean put) {
        this.put = put;
        this.category = new ArrayList<>();
    }

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        if (pagegen == null) {
            throw new NullPointerException("Pages can’t be null");
        }

        for (String p : pagegen.generatePages(rwiki)) {
            try {
                String pagetext = rwiki.getPageText(p);
                String newpagetext = pagetext;

                for (String string : category) {
                    newpagetext = Categories.putOrRemoveCategory(newpagetext, string, put, rwiki);
                }


                if (!pagetext.equals(newpagetext)) {
                    Wiki.printDiff(p, pagetext, newpagetext, org.fusesource.jansi.AnsiConsole.out);

                    if (rwiki.getGo()) {
                        try {
                            rwiki.edit(p, newpagetext, rwiki.getSummary());
                        } catch (IOException | LoginException ex) {
                            Logger.getLogger(CatOrganize.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            } catch (java.io.FileNotFoundException ex) {
                // continue
            } catch (IOException ex) {
                Logger.getLogger(CatPutRemove.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String toString() {
        return "[CatPutRemove: " + (put ? "Put into: " : "Remove from: ") + category + ". Pages: [" + pagegen.toString() + "]]";
    }

    @Override
    public void setPageGenerator(PageGenerator pagegen) {
        this.pagegen = pagegen;
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        for (Object o : parameter) {
            if (!(o instanceof String)) {
                throw new ParameterException("Catput/remove needs n parameters: String.");
            }

            category.add((String) o);
        }

    }
}
