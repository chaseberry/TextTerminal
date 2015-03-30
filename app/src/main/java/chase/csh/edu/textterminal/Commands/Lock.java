package chase.csh.edu.textterminal.Commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Receivers.DeviceAdminReceiver;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/4/14.
 */
public class Lock extends Command {

    public Lock(Context c, String[] values, String phone) {
        super(c, "Lock", R.drawable.ic_lock_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        DevicePolicyManager dpm = (DevicePolicyManager) parent.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm.isAdminActive(new ComponentName(parent, DeviceAdminReceiver.class))) {
            dpm.lockNow();
            sendMessage("Device locked.", fromNumber);
            return true;
        }
        return false;
    }

    @Override
    public String getHelpMessage() {
        return "Locks this device.";
    }

}
