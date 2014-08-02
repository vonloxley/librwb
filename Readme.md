Rwb (RezepteWikiBot)
====================
Rwb shall empower non-programming but tech-savvy MediaWiki-users to write simple
bots without having to learn a complete programming-language.

It uses very simple configuration-files to describe bots. The configurations-files (.wb)
consist of only two components: Generators and Commands.

Generators
----------
Generators wrap MediaWikis generator-api. They consist of the generator-name, some 
parameters and the body with commands in curly braces. Generators may be nested.

The recent-changes generator:

```
recent(500){
}
```

Will work on the pages of the last 500 changes.

Commands
--------
Commands live within Generators body and do the work. They consist of the command-name, 
some parameters and a closing semi-colon.

The replace-command:

```
replace("Question", "Answer");
replace("Test(\d{2})", "Answer$1");
```

Will replace 

1. Question with Answer
2. QuestionXX wit AnswerXX, where XX are two digits.

Further reading
---------------
* [Building rwb](documentation/Compile.md).
* [Extending rwb](documentation/Extend.md).
* [Reference](documentation/Reference.md) (in German).


