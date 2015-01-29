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
package Rwb.Parser;

import Rwb.BlockCommands.CatPutRemove;
import Rwb.BlockCommands.PrintTitle;
import Rwb.BlockCommands.Replace;
import Rwb.BlockCommands.ReplaceIfNotContains;
import Rwb.BlockCommands.WikiBlockCommand;
import Rwb.Commands.AddExclude;
import Rwb.Commands.Commit;
import Rwb.Commands.EditSummary;
import Rwb.Commands.Login;
import Rwb.Commands.RemoveIfInSubcat;
import Rwb.Commands.RenameCat;
import Rwb.Commands.ResetExcludes;
import Rwb.Commands.SetAllGo;
import Rwb.Commands.WikiCommand;
import Rwb.Generators.LinksTo;
import Rwb.Generators.PageGenerator;
import Rwb.Generators.PageList;
import Rwb.Generators.RecentChanges;
import Rwb.Generators.Search;
import Rwb.Generators.SearchNS;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The "Ast"-generator.
 *
 * Tightly coupled with {@link CatBot}.
 *
 * Right now there are only sequences and iterations, so it is a list rather
 * than a tree.
 *
 * To add a command extend createCommand, to add a generator extend
 * createGenerator and change createAndAddCommand.
 *
 * @author Niki Hansche
 */
class BotAst {

    protected final Deque<PageGenerator> generatorstack = new ArrayDeque<>();
    /**
     * List of Commands, i.e. the AST. To be replaced by a tree-struture.
     *
     * Is returned via CatBot.Input();
     */
    protected final List<WikiCommand> cl = new ArrayList<>();

    public BotAst() {

    }

    /**
     * Decide wether to create a generator or a command.
     *
     * @param command A command-token.
     * @param parameters A Deque of parameters.
     * @throws ParseException
     */
    protected void createAndAddCommand(Token command, Deque parameters) throws ParseException {
        if (command.image.equals("search")
                || command.image.equals("searchns")
                || command.image.equals("pagelist")
                || command.image.equals("linksto")
                || command.image.equals("recent")) {
            createGenerator(command, parameters);
        } else {
            createCommand(command, parameters);
        }
    }

    /**
     * Cretae a command or block-command.
     *
     * @param command
     * @param parameters
     * @throws ParseException
     */
    private void createCommand(Token command, Deque parameters) throws ParseException {
        WikiCommand wc = null;
        PageGenerator pg = null;

        if (!generatorstack.isEmpty()) {
            pg = generatorstack.peekFirst();
        }
        switch (command.image) {
            // Commands
            case "summary":
                wc = new EditSummary();
                break;
            case "login":
                wc = new Login();
                break;
            case "setallgo":
                wc = new SetAllGo();
                break;
            case "commit":
                wc = new Commit();
                break;
            case "commitcompressed":
                wc = new Commit(true);
                break;
            case "resetexcludes":
                wc = new ResetExcludes();
                break;
            case "addexclude":
                wc = new AddExclude();
                break;
            case "renamecat":
                wc = new RenameCat();
                break;

            //Blockcommnads
            case "putintocat":
                wc = new CatPutRemove(true);
                ((WikiBlockCommand) wc).setPageGenerator(pg);
                break;
            case "removefromcat":
                wc = new CatPutRemove(false);
                ((WikiBlockCommand) wc).setPageGenerator(pg);
                break;
            case "printtitle":
                wc = new PrintTitle();
                ((WikiBlockCommand) wc).setPageGenerator(pg);
                break;
            case "replace":
                wc = new Replace();
                ((WikiBlockCommand) wc).setPageGenerator(pg);
                break;
            case "replaceifnotcontains":
                wc = new ReplaceIfNotContains();
                ((WikiBlockCommand) wc).setPageGenerator(pg);
                break;
            case "removeifinsubcat":
                wc = new RemoveIfInSubcat();
                break;
        }
        if (wc == null) {
            throw new ParseException("Unknown command: " + command.image + " at line " + command.beginLine + " column " + command.beginColumn);
        }
        wc.setParameter(parameters);
        cl.add(wc);
    }

    /**
     * Create a generator.
     *
     * @param command
     * @param parameters
     * @throws ParseException
     */
    private void createGenerator(Token command, Deque parameters) throws ParseException {
        PageGenerator pg = null;
        switch (command.image) {
            case "recent":
                pg = new RecentChanges();
                break;
            case "pagelist":
                pg = new PageList();
                break;
            case "search":
                pg = new Search();
                break;
            case "searchns":
                pg = new SearchNS();
                break;
            case "linksto":
                pg = new LinksTo();
                break;
        }

        if (pg == null) {
            throw new ParseException("Unknown generator: " + command.image + " at line " + command.beginLine + " column " + command.beginColumn);
        }
        //Link the PageGenerators
        if (!generatorstack.isEmpty()) {
            pg.setParentPageGenerator(generatorstack.peekFirst());
        }
        generatorstack.addFirst(pg);

        pg.setParameter(parameters);
    }

    public class Node {

    }

}
