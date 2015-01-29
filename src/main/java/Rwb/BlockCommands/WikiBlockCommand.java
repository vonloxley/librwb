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
import Rwb.Commands.WikiCommand;

/**
 * Interface for all Commands that can work within a block. The Command works on
 * all pages returned by the PageGenerator.
 *
 * @see Generators.PageGenerator.generatePages
 *
 */
public interface WikiBlockCommand extends WikiCommand {

    /**
     *
     * @param pagegen Pagegenerator to work with.
     */
    void setPageGenerator(PageGenerator pagegen);
}
