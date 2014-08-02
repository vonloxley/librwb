GENFILES=Bot.java BotConstants.java BotTokenManager.java ParseException.java SimpleCharStream.java Token.java Provider.java StreamProvider.java StringProvider.java Token.java TokenMgrException.java CatBotConstants.java CatBot.java CatBotTokenManager.java

$(GENFILES): Bot.jj.intermediate

clean: 
	rm -f $(GENFILES)

.INTERMEDIATE: Bot.jj.intermediate
Bot.jj.intermediate: Bot.jj
	javacc Bot.jj

