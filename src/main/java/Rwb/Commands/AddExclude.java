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
package Rwb.Commands;

import Rwb.ParameterException;
import Rwb.Wiki;
import java.util.Deque;

/**
 *
 * @author Niki Hansche
 */
public class AddExclude implements WikiCommand {

    private String start, end;

    public AddExclude(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public AddExclude() {
        this.start = null;
        this.end = null;
    }

    @Override
    public void execute(Wiki rw) throws CommandException {
        if (start == null || end == null) {
            throw new ParameterException(ADDEXCL_ERR);
        }

        rw.addIgnorePosition(start, end);
    }

    @Override
    public void setParameter(Deque<Object> paramter) {
        if (paramter.size() < 2) {
            throw new ParameterException(ADDEXCL_ERR);
        }
        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(ADDEXCL_ERR);
        }
        start = (String) paramter.pop();

        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(ADDEXCL_ERR);
        }
        end = (String) paramter.pop();

    }
    private static final String ADDEXCL_ERR = "Addexclude needs two parameters: String, String.";

    @Override
    public String toString() {
        return "[Addexclude: " + start + ", " + end + "]";
    }

}
