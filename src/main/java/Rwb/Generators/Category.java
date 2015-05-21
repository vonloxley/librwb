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
package Rwb.Generators;

import Rwb.ParameterException;
import Rwb.Wiki;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Category implements PageGenerator {

    private PageGenerator parentpg;
    private String destpage;

    public Category() {
        this.destpage = null;
        this.parentpg = null;
    }

    @Override
    public List<String> generatePages(Wiki rwiki) {
        List<String> list = new ArrayList();

        if (destpage == null) {
            throw new ParameterException(RECENT_ERR);
        }

        try {
            String[] r = rwiki.getCategoryMembers(destpage, 999, new int[]{Wiki.MAIN_NAMESPACE});
            for (String revision : r) {
                if (!list.contains(revision)) {
                    list.add(revision);
                }
            }

            if (parentpg != null) {
                list.retainAll(parentpg.generatePages(rwiki));
            }
        } catch (IOException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public void setParentPageGenerator(PageGenerator parentpg) {
        this.parentpg = parentpg;
    }

    @Override
    public void setParameter(Deque<Object> paramter) {
        if (paramter.size() != 1) {
            throw new ParameterException(RECENT_ERR);
        }
        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(RECENT_ERR);
        }
        destpage = (String) paramter.pop();
    }
    private static final String RECENT_ERR = "Category needs one parameter: String.";

    @Override
    public String toString() {
        return "[Category: " + destpage + "]";
    }

}
