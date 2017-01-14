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
package Rwb.Categories;

import Rwb.Page;
import Rwb.Wiki;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wikiutils.ParseUtils;

/**
 *
 * @author Niki Hansche
 */
public class Categories {

    private static final String LINK_START = "\\[\\[";
    private static final String LINK_END = "\\]\\]\\r?\\n?";
    private static final String CATEGORY_RE = LINK_START + "\\s*(?:Kategorie|Category)\\s*:\\s*([^:\\]|]+)\\s*(?:|.+?)" + LINK_END;

    private Categories() {
    }

    public static Page removeAllCategoryLinks(String page, Wiki wiki) {
        Map<Integer, Integer> ip;
        List<String> rmC = new ArrayList<>();
        Map<String, String> r = getAllCategories(page);
        StringBuilder sb = new StringBuilder(page);
        for (String s : r.keySet()) {
            int start = -1;
            int indexStart;
            while ((indexStart = sb.indexOf(s, start)) > -1) {
                ip = wiki.getIgnorePositions(sb.toString());
                if (ParseUtils.isIgnorePosition(ip, indexStart)) {
                    start += indexStart + 1;
                    continue;
                }
                sb.replace(indexStart, indexStart + s.length(), "");
                rmC.add(s);
            }
        }
        return new Page(sb.toString(), rmC);
    }

    public static Map<String, String> getAllCategories(String page) {
        Map<String, String> r = new HashMap<>();
        Matcher m = Pattern.compile(CATEGORY_RE).matcher(page);
        while (m.find()) {
            r.put(m.group(), m.group(1));
        }
        return r;
    }

    public static String putOrRemoveCategory(String pagetext, String dst, boolean put, Wiki wiki) {
        Page page = Categories.removeAllCategoryLinks(pagetext, wiki);
        String pageTextWithoutCats = page.getText();

        if (put) {
            page.getRemovedCategories().add("[[Kategorie:" + dst + "]]");
        } else {
            for (Iterator<String> it = page.getRemovedCategories().iterator(); it.hasNext();) {
                String string = it.next();
                if (string.contains(":" + dst + "|") || string.contains(":" + dst + "]]")) {
                    it.remove();
                }

            }
        }
        Collections.sort(page.getRemovedCategories());
        for (String c : page.getRemovedCategories()) {
            pageTextWithoutCats = pageTextWithoutCats + c.trim() + "\n";
        }
        return pageTextWithoutCats;
    }

    public static String putCategory(String pagetext, String dst, Wiki wiki) {
        return putOrRemoveCategory(pagetext, dst, true, wiki);
    }

    public static String removeCategory(String pagetext, String dst, Wiki wiki) {
        return putOrRemoveCategory(pagetext, dst, false, wiki);
    }

}
