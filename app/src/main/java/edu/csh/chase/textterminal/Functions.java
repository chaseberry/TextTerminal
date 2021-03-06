package edu.csh.chase.textterminal;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;

import dalvik.system.DexFile;
import edu.csh.chase.textterminal.BCrypt.BCrypt;
import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Receivers.DeviceAdminReceiver;
import edu.csh.chase.textterminal.Receivers.SmsReceiver;

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

    public static String formatNumber(String num) {
        num = num.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
        if (!num.startsWith("+1")) {
            num = "+1" + num;
        }
        if (num.length() != 12) {
            return null;
        }
        return num;
    }

    public static ArrayList<String> getCommandClassNames(Context context) {
        ArrayList<String> classNames = new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String s = iter.nextElement();
                if (s.contains(".Commands") && !s.contains("$") && !s.contains(".Commands.Command")) {
                    classNames.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public static Command loadCommand(String commandName, Context c, String[] parts, String fromNum) {
        try {
            Class<?> commandClass = SmsReceiver.class.getClassLoader().loadClass(commandName);
            Constructor constructor = commandClass.getDeclaredConstructor(Context.class, String[].class, String.class);
            constructor.setAccessible(true);
            return (Command) constructor.newInstance(c, parts, fromNum);
        } catch (ClassNotFoundException e) {

        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
        }
        return null;
    }

}
