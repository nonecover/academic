package com.jakomulski.datacollector;

import android.os.Environment;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class AndroidLogger {
    private final static LogConfigurator logConfigurator = new LogConfigurator();

    public static final String LOG_FILE_NAME = Environment.getExternalStorageDirectory().toString() + File.separator + "log/file.log";
    static {
        logConfigurator.setFileName(LOG_FILE_NAME);
        logConfigurator.setRootLevel(Level.ALL);
        logConfigurator.setLevel("org.apache", Level.ALL);
        logConfigurator.setUseFileAppender(true);
        logConfigurator.setFilePattern("%d{HH:mm:ss} %-5p %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    public static Logger getLogger(Class<?> clazz){
        return Logger.getLogger(clazz);
    }
}
