package com.mrsalwater.kapteyn.ui.data;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log {

    private static final String INFO_PREFIX = "[Info] ";
    private static final String WARNING_PREFIX = "[Warning] ";
    private static final String ERROR_PREFIX = "[Error] ";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static Log create() {
        return new Log();
    }

    public static void initialize(Log log) {
        File file = AppData.getLogFile();
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
                if (!result) {
                    log.warning("Cannot create log file!");
                    throw new IOException();
                }
            } catch (IOException e) {
                log.warning("Cannot create log file:");
                e.printStackTrace();
            }
        }

        try {
            PrintWriter writer = new PrintWriter(file);
            log.setFileWriter(writer);
        } catch (FileNotFoundException e) {
            log.warning("Cannot create log file writer:");
            e.printStackTrace();
        }
    }

    private final PrintStream consoleOutput;
    private PrintWriter fileWriter;

    private Log() {
        consoleOutput = System.out;
    }

    public void setFileWriter(PrintWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void info(String message) {
        log(INFO_PREFIX.concat(message));
    }

    public void warning(String message) {
        log(WARNING_PREFIX.concat(message));
    }

    public void error(String message) {
        log(ERROR_PREFIX.concat(message));
    }

    public void log(String message) {
        StringBuilder builder = new StringBuilder();
        String timeStamp = DATE_FORMAT.format(new Date());

        builder.append("[").append(timeStamp).append("] ").append(message);
        String output = builder.toString();

        consoleOutput.println(output);
        if (fileWriter != null) {
            fileWriter.println(output);
            fileWriter.flush();
        }
    }

    public void close() {
        fileWriter.close();
    }

}
