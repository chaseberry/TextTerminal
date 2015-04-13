package chase.csh.edu.textterminal.Commands;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.R;

public class Help extends Command {

    public Help(Context c, String[] values, String phone) {
        super(c, c.getString(R.string.command_help_title), R.drawable.ic_help_white_48dp, values, phone);
    }

    public String getCommands() {
        ArrayList<String> classNames = Functions.getCommandClassNames(parent);
        if (classNames.isEmpty()) {
            //failed;
        }
        Collections.sort(classNames);
        for (int z = 0; z < classNames.size(); z++) {
            classNames.set(z, classNames.get(z).split(Pattern.quote("."))[5]);
        }
        return Arrays.toString(classNames.toArray());
    }

    @Override
    protected boolean executeCommand() {
        sendMessage(parent.getString(R.string.command_help_response_1) +
                parent.getString(R.string.command_help_response_2) + getCommands() + ". " +
                parent.getString(R.string.command_help_response_3), fromNumber);
        return true;
    }

    @Override
    public String getHelpMessage() {
        return parent.getString(R.string.command_help_help);
    }

}
