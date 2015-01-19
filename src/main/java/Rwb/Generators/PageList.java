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
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PageList implements PageGenerator {

    private final List<String> plist;
    private PageGenerator parentpg;

    public PageList() {
        plist = new ArrayList<>();
        parentpg = null;
    }

    @Override
    public List<String> generatePages(Wiki rwiki) {
        if (parentpg != null) {
            plist.retainAll(parentpg.generatePages(rwiki));
        }

        return plist;
    }

    @Override
    public void setParentPageGenerator(PageGenerator parentpg) {
        this.parentpg = parentpg;
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        for (Object o : parameter) {
            if (!(o instanceof String)) {
                throw new ParameterException(PAGELIST_ERR);
            }

            plist.add((String) o);
        }
    }
    private static final String PAGELIST_ERR = "Pagelist needs n parameters: String.";

    @Override
    public String toString() {
        return "[" + String.join(", ", plist) + (parentpg!=null?parentpg.toString():"") +"]";
    }
}
