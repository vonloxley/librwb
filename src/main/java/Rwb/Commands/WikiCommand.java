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

import Rwb.Wiki;
import java.util.Deque;

/**
 * Represents a RezepteWiki-command. Commands my throw a ParamterNotFound
 * Exception
 *
 * @author Niki Hansche
 */
public interface WikiCommand {

    /**
     * Called when this command should execute.
     *
     * @param rwiki Wiki-instance to work with.
     * @throws CommandException
     */
    void execute(Wiki rwiki) throws CommandException;

    /**
     * Sets this commands parameters.
     *
     * @param parameter A stack of parameters for this generator.
     */
    void setParameter(Deque<Object> parameter);
}
