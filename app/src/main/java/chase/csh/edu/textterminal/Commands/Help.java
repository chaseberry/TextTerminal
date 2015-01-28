package chase.csh.edu.textterminal.Commands;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import chase.csh.edu.textterminal.R;
import dalvik.system.DexFile;

public class Help extends Command {

    public Help(Context c, String[] values, String phone) {
        super(c, "Help", R.drawable.ic_help_white_48dp, values, phone);
    }

    public String getCommands() {
        ArrayList<String> classNames = new ArrayList<String>();
        try {
            DexFile df = new DexFile(parent.getPackageCodePath());
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String s = iter.nextElement();
                if (s.contains(".Commands") && !s.contains("$") && !s.contains(".Commands.Command") && !s.contains("Help")) {
                    classNames.add(s.replace("chase.csh.edu.textterminal.Commands.", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classNames.isEmpty()) {
            //failed;
        }
        Collections.sort(classNames);
        return Arrays.toString(classNames.toArray());
    }

    @Override
    protected boolean executeCommand() {
        sendMessage("Send a command to this phone and see what happens. " +
                "Valid commands are " + getCommands() + ". " +
                "You can append -h in each specific command for help", fromNumber);
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "Displays the help message";
    }

}
