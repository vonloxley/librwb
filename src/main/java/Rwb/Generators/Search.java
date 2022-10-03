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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Search implements PageGenerator {

    private PageGenerator parentpg;
    private String searchterm;

    public Search() {
        this.searchterm = null;
        this.parentpg = null;
    }

    @Override
    public List<String> generatePages(Wiki rwiki) {
        List<String> list = new ArrayList<>();

        if (searchterm == null) {
            throw new ParameterException(SEARCH_ERR);
        }

        try {
            List<Map<String, Object>> r = rwiki.search(searchterm, rwiki.getWorkingNamespaces());
            for (Map<String, Object> s : r) {
                String t = (String) s.get("title");
                if (!list.contains(t)) {
                    list.add(t);
                }
            }

            if (parentpg != null) {
                list.retainAll(parentpg.generatePages(rwiki));
            }
        } catch (IOException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    private static final String SEARCH_ERR = "Search needs one parameter: String.";

    @Override
    public void setParentPageGenerator(PageGenerator parentpg) {
        this.parentpg = parentpg;
    }

    @Override
    public void setParameter(Deque<Object> paramter) {
        if (paramter.size() != 1) {
            throw new ParameterException(SEARCH_ERR);
        }
        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(SEARCH_ERR);
        }
        searchterm = (String) paramter.pop();
    }

    @Override
    public String toString() {
        return "[Search: " + searchterm + "]";
    }
}
