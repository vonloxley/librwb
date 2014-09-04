Extending Rwb
=============

Writing commands and generators
----------------
There are to kind of commands:

1. Commands implementing WikiCommand.
2. Commands implementing WikiBlockCommand.

The first ones can be used anywhere in a script, the second ones only within
generators. That means the commands that want to work on pages have to implement
WikiBlockCommand.

Generators need to implement the ```PageGenerator```-interface.

Commands and generators get their parameters passed as a stack (Deque). Blockcommands get a 
PageGenerator via their ```setPageGenerator```-method and must call its 
```generatePages```-method to get the pages they should work on.

All commands may raise CommandExceptions.

Generator and commands must be instantiated in BotAst.java.

Please see the javadoc for

* WikiCommand.java
* WikiBlockCommand.java
* PageGenerator.Java

