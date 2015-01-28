package chase.csh.edu.textterminal.Commands;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.R;

public class Help extends Command {

    public Help(Context c, String[] values, String phone) {
        super(c, "Help", R.drawable.ic_help_white_48dp, values, phone);
    }

    public String getCommands() {
        ArrayList<String> classNames = Functions.getCommandClassNames(parent);
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
