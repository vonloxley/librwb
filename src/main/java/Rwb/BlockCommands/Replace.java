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

import Rwb.Commands.CatOrganize;
import Rwb.Commands.CommandException;
import Rwb.ParameterException;
import Rwb.Wiki;
import java.io.IOException;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Niki Hansche
 */
public class Replace extends ReplaceBase implements WikiBlockCommand {

    public Replace() {
        this.search = null;
        this.replace = null;
        this.pg = null;
    }

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        if (pg == null) {
            throw new NullPointerException("PageGenerator can’t be null");
        }
        if (search == null || replace == null) {
            throw new ParameterException(PARM_ERR);
        }

        for (String p : pg.generatePages(rwiki)) {
            try {
                String pagetext = rwiki.getPageText(p);
                String newpagetext = doReplace(pagetext, rwiki);

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
                continue;
            } catch (IOException ex) {
                Logger.getLogger(CatPutRemove.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        if (parameter.size() < 2 || parameter.size() > 3) {
            throw new ParameterException(PARM_ERR);
        }
        if (!(parameter.peek() instanceof String)) {
            throw new ParameterException(PARM_ERR);
        }
        search = (String) parameter.pop();
        if (!(parameter.peek() instanceof String)) {
            throw new ParameterException(PARM_ERR);
        }
        replace = (String) parameter.pop();

        if (parameter.peek() instanceof Integer) {
            maxreplacements = (Integer) parameter.pop();
            replaceall = false;
        }
    }

    protected static final String PARM_ERR = "Replace needs two or three parameters: String, String [, Integer].";

    @Override
    public String toString() {
        return "[Replace: '" + search + "' with '" + replace + "']";
    }

    public static void main(String[] args) {
        /*        Replace r = new Replace("Test(\\d{2})", "Answer$1", null);
         System.out.println(r.doReplace("<nowiki>Test48</nowiki>Test48<nowiki>Test48</nowiki>Test48"));
         */
    }
}
