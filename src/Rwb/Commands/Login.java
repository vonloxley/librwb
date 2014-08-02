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
import java.io.Console;
import java.io.IOException;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.FailedLoginException;

/**
 *
 * @author Niki Hansche
 */
public class Login implements WikiCommand {

    String user, password;

    public Login(String user) {
        this.user = user;
        this.password = null;
    }

    public Login() {
        this.user = null;
        this.password = null;
    }

    @Override
    public void execute(Wiki rw) throws CommandException {
        if (user == null) {
            throw new ParameterException(LOGIN_ERR);
        }

        if (password == null) {
            Console c = Wiki.getConsole();
            password = String.valueOf(c.readPassword("Passwort: "));
        }

        try {
            rw.login(user, password);
            rw.saveThis("logindat.rwb");
        } catch (IOException | FailedLoginException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            throw new CommandException(ex.getMessage());
        }
    }

    @Override
    public void setParameter(Deque<Object> paramter) {
        if (paramter.size() < 1) {
            throw new ParameterException(LOGIN_ERR);
        }
        if (!(paramter.peek() instanceof String)) {
            throw new ParameterException(LOGIN_ERR);
        }
        user = (String) paramter.pop();
        password = (String) paramter.peek();

    }
    private static final String LOGIN_ERR = "Login needs one or two parameters: String [, String].";

    @Override
    public String toString() {
        return "[Login: " + user + "]";
    }

}
