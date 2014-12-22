package chase.csh.edu.textterminal;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import chase.csh.edu.textterminal.BCrypt.BCrypt;
import chase.csh.edu.textterminal.Receivers.DeviceAdminReceiver;

/**
 * Created by chase on 12/4/14.
 */
public class Functions {


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String checked) {
        return BCrypt.checkpw(checked, password);
    }

    public static AlertDialog createMessageWithButton(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public static Toast createToastMessage(String message, Context context, boolean longToast) {
        return Toast.makeText(context, message,
                longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);

    }

    public static boolean isAdmin(Context c) {
        return ((DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE))
                .isAdminActive(new ComponentName(c, DeviceAdminReceiver.class));
    }

    public static void displayAdminDialog(Context c) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "All features this can use are triggered by a text to the device.");
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(c, DeviceAdminReceiver.class));
        c.startActivity(intent);
    }

}
