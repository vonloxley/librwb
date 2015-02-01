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
import Rwb.Utils;
import Rwb.Wiki;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author Niki Hansche
 */
public class SetNamespaces implements WikiCommand {

    private static final String SETNS_ERR = "SetNamespaces needs n parameters: Int, …";
    List<Integer> namespaces = new ArrayList<>();

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        rwiki.setWorkingNamespaces(namespaces);
    }

    @Override
    public String toString() {
        return "[SetNamespaces ("+ Utils.joinToString(namespaces, ", ") +")]";
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
        
        if (parameter.size() < 1) {
            throw new ParameterException(SETNS_ERR);
        }
        
        while (!parameter.isEmpty()) {
            if (!(parameter.peek() instanceof Integer)) {
                throw new ParameterException(SETNS_ERR);
            }
            namespaces.add((Integer) parameter.pop());
        }
    }
}
