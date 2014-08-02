Compiling Rwb
=============

Rwb needs the following libraries to compile:

1. Wiki-Java: http://code.google.com/p/wiki-java/ +Patches
2. Diffutils-1.2.1.jar
3. jansi-1.11.jar

The target-jdk is 1.7.

To rebuild the parser Bot.jj you’ll need a fairly recent JavaCC (6.1_2 as of now).
After changing Bot.jj run `make` in the `src/Rwb/BotParser`-folder.

Call ```ant jar``` in the main directory or use Netbeans.

