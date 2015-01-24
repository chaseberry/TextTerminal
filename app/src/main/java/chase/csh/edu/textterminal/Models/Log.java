package chase.csh.edu.textterminal.Models;

import com.orm.SugarRecord;

/**
 * Created by chase on 12/25/14.
 */
public class Log extends SugarRecord<Log> {

    private String logMessage;
    private String number;//Log a message to/from
    private String className;//Da name of da class
    private int time;

    public Log() {
    }

    public Log(String message) {
        this.logMessage = message;
        this.time = (int) (System.currentTimeMillis() / 1000);
    }

}
