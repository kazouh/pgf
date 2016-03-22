package net.bagot.pgf.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static FileLogger instance = null;
    private static File logFolder;

    private FileLogger() {
        if (canWriteOnExternalStorage()) {
            logFolder = new File(Environment.getExternalStorageDirectory() + "/pgf");
        } else {
            logFolder = new File(Environment.getDataDirectory() + "/pgf");
        }

        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }
    }

    private boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static FileLogger getInstance() {
        if (instance == null) {
            instance = new FileLogger();
        }
        return instance;
    }

    public void log(String tag, String msg) {
        try {
            File outputFile = new File(logFolder, "pgf.log");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter writer = new FileWriter(outputFile, true);
            writer.write(dateFormat.format(new Date()) + "," + tag + "," + msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
