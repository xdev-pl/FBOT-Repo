package com.gmail.luxdevpl.fbot.storage.util;

import com.gmail.luxdevpl.fbot.configuration.DefaultConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.Validate;

public abstract class AbstractStorage implements IDatabase {

    private final IBotWrapper bot;

    public AbstractStorage(IBotWrapper bot) {
        Validate.notNull(bot);

        this.bot = bot;
    }

    @Override
    public void open() {
        this.create(bot.getBotConfiguration());
    }

    @Override
    public void close() {
        this.destroy();
    }

    protected abstract void create(DefaultConfiguration configuration);

    protected abstract void destroy();

}
