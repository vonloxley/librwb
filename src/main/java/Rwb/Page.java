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

package Rwb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Niki Hansche
 */
    public class Page {
        private final String text;
        private final List<String> removedCategories;

        public Page(String text, List<String> removedCategories) {
            this.text = text;
            this.removedCategories = removedCategories;
        }

        public Page(String text) {
            this.text = text;
            this.removedCategories = null;
        }

        public String getText() {
            return text;
        }

        public List<String> getRemovedCategories() {
            if (removedCategories != null) {
                return removedCategories;
            } else {
                return new ArrayList<>();
            }
        }

    }
