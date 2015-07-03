package edu.csh.chase.textterminal.Commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.R;
import edu.csh.chase.textterminal.Receivers.DeviceAdminReceiver;

public class Lock extends Command {

    public Lock(Context c, String[] values, String phone) {
        super(c, c.getString(R.string.command_lock_title), R.drawable.ic_lock_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        DevicePolicyManager dpm = (DevicePolicyManager) parent.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm.isAdminActive(new ComponentName(parent, DeviceAdminReceiver.class))) {
            dpm.lockNow();
            sendMessage(parent.getString(R.string.command_lock_success), fromNumber);
            return true;
        }
        return false;
    }

    @Override
    public String getHelpMessage() {
        return parent.getString(R.string.command_lock_help);
    }

}
