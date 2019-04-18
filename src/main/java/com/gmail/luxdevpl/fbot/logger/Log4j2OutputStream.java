package com.gmail.luxdevpl.fbot.logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import java.io.OutputStream;

public final class Log4j2OutputStream extends OutputStream {

    private final Logger logger;
    private final Level level;

    public Log4j2OutputStream(Logger logger, Level level) {
        Validate.notNull(logger);
        Validate.notNull(level);

        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(final int b) {
        print(String.valueOf((char) b));
    }

    @Override
    public void write(final byte[] b) {
        print(new String(b));
    }

    @Override
    public void write(final byte[] b, final int off, final int len) {
        print(new String(b, off, len));
    }

    private void print(String string) {
        string = StringUtils.trimToEmpty(string);
        if (string.isEmpty()) {
            return;
        }

        synchronized (logger) {
            logger.log(level, string);
        }
    }

}