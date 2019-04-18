package com.gmail.luxdevpl.fbot;

import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.Validate;

public final class BotShutdownHook extends Thread {

    private final IBotWrapper bot;

    BotShutdownHook(IBotWrapper bot) {
        super("ShutdownHook");

        Validate.notNull(bot);

        this.bot = bot;
    }

    @Override
    public void run() {
        bot.stop();
    }

}

