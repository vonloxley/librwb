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

import Rwb.Wiki;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author Niki Hansche
 */
public interface PageGenerator {
    /**
     * Called by BlockCommands. Returns the pages the command shall work on.
     * Must work together with it’a parent-generator.
     * @param rwiki
     * @return A List of pages.
     */
    public List<String> generatePages(Wiki rwiki);
    /**
     * Sets this generators parent-generator.
     * Normally a generator will generate only those pages, that are also generated 
     * by its parent.
     * @param parentpg 
     */
    public void setParentPageGenerator(PageGenerator parentpg);
    
    /**
     * Sets this generators paramters.
     * @param parameter A stack of parameters for this generator.
     */
    public void setParameter(Deque<Object> parameter);
    
}
