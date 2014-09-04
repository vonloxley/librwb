Compiling Rwb
=============

Rwb needs the following libraries to compile:

1. [Wiki-java](https://github.com/vonloxley/wiki-java)
2. Diffutils-1.2.1.jar
3. Jansi-1.11.jar
4. Jgrapht-core-0.9.0.jar
5. Jgrapht-ext-0.9.0.jar
6. Jgraphx-2.0.0.1.jar

Put all those into the directory ```libs```.
The target-jdk is 1.7.

To rebuild the parser Bot.jj you’ll need a fairly recent JavaCC (6.1_2 as of now).
After changing Bot.jj run `make` in the `src/Rwb/BotParser`-folder.

Call ```ant jar``` in the main directory or use Netbeans.

