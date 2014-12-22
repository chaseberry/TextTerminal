package chase.csh.edu.textterminal.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.lang.reflect.Constructor;

import chase.csh.edu.textterminal.Commands.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.SharedPrefManager;


public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        SharedPrefManager.loadSharedPrefs(context);
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object aPdusObj : pdusObj) {
                    try {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();//format is 5188361344

                        String message = currentMessage.getDisplayMessageBody();

                        //Parse message

                        String[] bodyParts = message.split(" ");
                        String code = SharedPrefManager.loadString(Command.SECURITYCODEKEY, "");
                        if (!code.equals("")) {
                            if (!Functions.checkPassword(code, bodyParts[0])) {
                                //TODO log failed -- security code
                                return;
                            }
                            message = message.substring(message.indexOf(' ') + 1);
                        }
                        bodyParts = message.split(" ");
                        String comm = bodyParts[0];
                        if (null == comm || comm.isEmpty()) {
                            //TODO log failed -- no command
                            break;
                        }
                        //Parse message
                        comm = Character.toUpperCase(comm.charAt(0)) + comm.substring(1);
                        String className = "chase.csh.edu.textterminal.Commands." + comm;

                        System.out.println(className);

                        Class<?> commandClass = SmsReceiver.class.getClassLoader().loadClass(className);
                        Constructor c = commandClass.getDeclaredConstructor(Context.class, String[].class, String.class);
                        c.setAccessible(true);
                        Command command = (Command) c.newInstance(context, bodyParts, phoneNumber);
                        command.execute();//TODO -- log the value of this

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Command not found");
                        //Log that shit bro TODO
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
