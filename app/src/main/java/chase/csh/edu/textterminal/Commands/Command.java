package chase.csh.edu.textterminal.Commands;

import android.content.Context;
import android.telephony.SmsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONObjectWrapper;

public abstract class Command implements JSONable {

    public static final String SECURITYCODEKEY = "securityKey";
    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_ALLOWED_FLAGS = "allowedFlags";


    private final String name;//Display purposes
    private final int iconId;
    protected Context parent;
    protected ArrayList<String> params;
    protected ArrayList<String> flags;
    protected String fromNumber;

    //Command data custom holders
    private boolean enabled = false;
    private ArrayList<String> allowedFlags;
    //End command data custom holders

    public Command(Context c, String name, int iconId, String[] values, String phone) {
        this.name = name;
        parent = c;
        this.iconId = iconId;
        fromNumber = phone;
        SharedPrefManager.loadSharedPrefs(c);
        params = new ArrayList<>();
        flags = new ArrayList<>();
        if (values != null) {
            for (String string : values) {
                if (string.charAt(0) == '-') {
                    flags.add(string.toLowerCase());
                } else if (!string.equals(name)) {
                    params.add(string);
                }
            }
        }
        try {
            JSONObjectWrapper obj = new JSONObjectWrapper(SharedPrefManager.loadString(getName(), null));
            enabled = obj.checkAndGetBoolean(false, KEY_ENABLED);
            allowedFlags = new ArrayList<String>(Arrays.asList(obj.checkAndGetStringArray(new String[0], KEY_ALLOWED_FLAGS)));
        } catch (Exception e) {
            enabled = false;//Make it not able to fun.
            //Something couldn't load
        }

    }

    public final boolean execute() {
        if (!canExecute()) {
            return false;
        }

        if (flags.contains("-h")) {
            sendMessage(getHelpMessage(), fromNumber);
            return true;
        }
        return executeCommand();
    }

    protected abstract boolean executeCommand();

    public abstract String getHelpMessage();

    public abstract String[] getParams();

    public abstract CommandFlag[] getFlags();

    private boolean canExecute() {
        return enabled;
    }

    protected void sendMessage(String message, String to) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendMultipartTextMessage(to, null, sms.divideMessage(message), null, null);

        //Log attempted to send message in response - ToNum
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    protected abstract JSONObject addExtras(JSONObject obj);

    public JSONObject getJSONClass() {
        JSONObject obj = new JSONObject();
        obj = addExtras(obj);
        try {
            obj.put(KEY_ENABLED, enabled);
        } catch (JSONException e) {
            //won't happen
        }
        return obj;
    }

    public void save() {
        SharedPrefManager.saveString(getName(), getJSONClass().toString());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
    }

}
