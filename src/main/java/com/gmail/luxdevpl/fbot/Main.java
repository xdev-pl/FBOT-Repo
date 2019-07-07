package com.gmail.luxdevpl.fbot;

import com.gmail.luxdevpl.fbot.impl.launcher.BotWrapperImpl;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.logger.BotLogger;
import com.gmail.luxdevpl.fbot.logger.Log4j2OutputStream;
import org.apache.logging.log4j.Level;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public final class Main {

    public static final InputStream INPUT_STREAM = System.in;
    public static final OutputStream OUTPUT_STREAM = System.out;

    public static void main(String[] args) {
        BotLogger logger = new BotLogger();

        System.setOut(new PrintStream(new Log4j2OutputStream(logger.getLogger(), Level.INFO)));
        System.setErr(new PrintStream(new Log4j2OutputStream(logger.getLogger(), Level.ERROR)));

        if(args.length == 0) {
            logger.getLogger().info("Sprobuj uruchomic bota z argumentem --start.");
            return;
        }

        if (args[0].equalsIgnoreCase("--start")) {
            logger.getOrigin().println(" ******** ******     *******   **********\n" +
                    "/**///// /*////**   **/////** /////**/// \n" +
                    "/**      /*   /**  **     //**    /**    \n" +
                    "/******* /******  /**      /**    /**    \n" +
                    "/**////  /*//// **/**      /**    /**    \n" +
                    "/**      /*    /**//**     **     /**    \n" +
                    "/**      /*******  //*******      /**    \n" +
                    "//       ///////    ///////       //     ");

            logger.getOrigin().println("Copyright (C) 2017-present by Paweł Dębiński (https://github.com/xdev-pl).");
            logger.getOrigin().println("All right reserved.");

            logger.getOrigin().println();
            logger.getLogger().info("Bootstrap has been enabled.");
            logger.getOrigin().println();

            logger.getLogger().info("Bot will now load everything, this may take a while.");
            try {
                IBotWrapper iBotWrapper = new BotWrapperImpl(logger);

                iBotWrapper.start();

                Runtime.getRuntime().addShutdownHook(new BotShutdownHook(iBotWrapper));
            } catch (Exception ex) {
                logger.getLogger().info("An exception while starting bot.", ex);
            }
        }
    }

}
