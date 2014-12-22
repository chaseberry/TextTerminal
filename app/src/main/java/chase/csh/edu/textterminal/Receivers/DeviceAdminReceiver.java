package chase.csh.edu.textterminal.Receivers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by chase on 12/4/14.
 */
public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }
}
