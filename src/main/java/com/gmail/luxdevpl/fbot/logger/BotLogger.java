package com.gmail.luxdevpl.fbot.logger;

import com.gmail.luxdevpl.fbot.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.PrintStream;


public class BotLogger {

    private final Logger logger = (Logger) LogManager.getLogger("BotWrapperImpl");

    public PrintStream getOrigin() {
        return (PrintStream) Main.OUTPUT_STREAM;
    }

    public Logger getLogger() {
        return logger;
    }

}
