package chase.csh.edu.textterminal.Managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import chase.csh.edu.textterminal.Models.Log;

/**
 * Created by chase on 12/25/14.
 */
public class LogManager {

    public static final String LOGKEY = "logging";

    private static LogManager logManager;

    public static LogManager getLogManager() {
        if (logManager == null) {
            logManager = new LogManager();
        }
        return logManager;
    }

    public void log(String msg) {
        if (SharedPrefManager.loadBoolean(LOGKEY, false)) {
            Log log = new Log(msg);
            log.save();
        }
    }

    public Iterator<Log> loadLogs() {
        return Log.findAll(Log.class);
    }


}
