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
import java.io.IOException;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Niki Hansche
 */
public class Commit implements WikiCommand {

    private final boolean compressed;

    public Commit(boolean comressed) {
        this.compressed = comressed;
    }

    public Commit() {
        this.compressed = false;
    }

    @Override
    public void execute(Wiki rwiki) throws CommandException {
        try {
            rwiki.commitPagecache(compressed);
        } catch (IOException | LoginException ex) {
            Logger.getLogger(Commit.class.getName()).log(Level.SEVERE, null, ex);
            throw new CommandException(ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return "[Commit pagecache(compressed: " + compressed + ")]";
    }

    @Override
    public void setParameter(Deque<Object> parameter) {
    }

}
