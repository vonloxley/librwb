/*
 * Copyright (C) 2014 niki
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
import Rwb.Utils;
import Rwb.Wiki;
import difflib.StringUtills;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchNS implements PageGenerator {

	private PageGenerator parentpg;
	private String searchterm;
	private final List<Integer> namespaces;

	public SearchNS() {
		this.searchterm = null;
		this.parentpg = null;
		this.namespaces = new ArrayList<>();
	}

	@Override
	public List<String> generatePages(Wiki rwiki) {
		List<String> list = new ArrayList();

		if (searchterm == null) {
			throw new ParameterException(SEARCH_ERR);
		}

		try {
			String[][] r = rwiki.search(searchterm, Utils.toIntArray(namespaces));
			for (String[] s : r) {
				if (!list.contains(s[0])) {
					list.add(s[0]);
				}
			}

			if (parentpg != null) {
				list.retainAll(parentpg.generatePages(rwiki));
			}
		} catch (IOException ex) {
			Logger.getLogger(SearchNS.class.getName()).log(Level.SEVERE, null, ex);
		}
		return list;
	}
	private static final String SEARCH_ERR = "Search needs at least two parameters: String, Integer, ….";

	@Override
	public void setParentPageGenerator(PageGenerator parentpg) {
		this.parentpg = parentpg;
	}

	@Override
	public void setParameter(Deque<Object> paramter) {
		if (paramter.size() < 2) {
			throw new ParameterException(SEARCH_ERR);
		}
		if (!(paramter.peek() instanceof String)) {
			throw new ParameterException(SEARCH_ERR);
		}
		searchterm = (String) paramter.pop();

		while (!paramter.isEmpty()) {
			if (!(paramter.peek() instanceof Integer)) {
				throw new ParameterException(SEARCH_ERR);
			}
			namespaces.add((Integer) paramter.pop());
		}
	}

	@Override
	public String toString() {
		return "[SearchNS: " + searchterm + "(" + StringUtills.join(namespaces, ", ") + ")" + "]";
	}
}
