package chase.csh.edu.textterminal.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.lang.reflect.Constructor;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Managers.BlackListManager;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import chase.csh.edu.textterminal.Managers.WhiteListManager;


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

                        String phoneNumber = "+1" + currentMessage.getDisplayOriginatingAddress();//TODO confirm the +1 works

                        String message = currentMessage.getDisplayMessageBody();

                        //Parse message

                        WhiteListManager whiteList = new WhiteListManager();//Create a reference to the whiteList

                        BlackListManager blackList = new BlackListManager();//create a reference to the blackList

                        if (whiteList.size() > 0 && !whiteList.contains(phoneNumber)) {
                            continue;
                        }

                        if (blackList.contains(phoneNumber)) {
                            continue;
                        }

                        //TODO generate a list of commands, check if it exists to help incoming messages

                        String[] bodyParts = message.split(" ");
                        String code = SharedPrefManager.loadString(Command.SECURITY_CODE_KEY, "");
                        if (!code.equals("")) {
                            if (!Functions.checkPassword(code, bodyParts[0])) {
                                //Security-code failed.. May be invalid, may be text, unsure, no way to know
                                return;
                            }
                            message = message.substring(message.indexOf(' ') + 1);
                        }
                        bodyParts = message.split(" ");
                        String comm = bodyParts[0];
                        if (null == comm || comm.isEmpty()) {
                            //No way to know what happens
                            break;
                        }
                        //Parse message
                        comm = Character.toUpperCase(comm.charAt(0)) + comm.substring(1);
                        String className = "chase.csh.edu.textterminal.Commands." + comm;
                        System.out.println(className);
                        Command command = Functions.loadCommand(className, context, bodyParts, phoneNumber);
                        if (command != null) {
                            command.execute();//TODO -- log the value of this -- fromNum
                        }
                        //Log - command found - executing - fromNum
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
