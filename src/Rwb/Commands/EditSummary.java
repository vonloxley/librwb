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
public class EditSummary implements WikiCommand {

    private String summary;

    public EditSummary() {
        this.summary = null;
    }

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        if (summary == null) {
            throw new ParameterException(SUMMARY_ERR);
        }

        rwiki.setSummary(summary);
    }

    @Override
    public void setParameter(Deque<Object> paramter) {
        if (paramter.size() != 1) {
            throw new ParameterException(SUMMARY_ERR);
        }
        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(SUMMARY_ERR);
        }
        summary = (String) paramter.pop();
    }
    private static final String SUMMARY_ERR = "Summary needs one parameter: String.";

    @Override
    public String toString() {
        return "[EditSummary: " + summary + "]";
    }

}
