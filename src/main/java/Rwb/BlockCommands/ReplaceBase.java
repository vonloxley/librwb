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

import Rwb.Generators.PageGenerator;
import Rwb.Wiki;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wikiutils.ParseUtils;

/**
 *
 * @author Niki Hansche
 */
class ReplaceBase {
    protected String search;
    protected String replace;
    protected int maxreplacements;
    protected boolean replaceall = true;
    protected PageGenerator pg;

    public ReplaceBase() {
    }

    protected String doReplace(String text, Wiki rwiki) {
        Map<Integer, Integer> ip;
        Pattern p = Pattern.compile(search);
        Matcher m = p.matcher(text);
        ip = rwiki.getIgnorePositions(text);
        StringBuffer sbout = new StringBuffer();
        int replacements = 0;
        while (m.find()) {
            if (!ParseUtils.isIgnorePosition(ip, m.start())) {
                m.appendReplacement(sbout, replace);
                if (!replaceall && ++replacements >= maxreplacements) {
                    break;
                }
            }
        }
        m.appendTail(sbout);
        return sbout.toString();
    }

    public void setPageGenerator(PageGenerator pagegen) {
        pg = pagegen;
    }
    
}
