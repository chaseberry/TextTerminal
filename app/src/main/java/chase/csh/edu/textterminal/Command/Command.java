package chase.csh.edu.textterminal.Command;

import android.content.Context;
import android.telephony.SmsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONObjectWrapper;
import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

public abstract class Command implements JSONable {

    public static final String SECURITY_CODE_KEY = "securityKey";
    public static final String KEY_ENABLED = "commandEnabled";
    public static final String KEY_FLAGS = "commandFlags";
    public static final String KEY_EXTRAS = "commandExtras";

    private final String name;//Display/loading purposes
    private final int iconId;
    protected Context parent;
    protected ArrayList<String> params;
    protected ArrayList<String> flags;
    protected String fromNumber;

    //Command data custom holders
    private boolean enabled = false;
    protected HashMap<String, CommandFlag> commandFlags;//Hash-map for use when command runs. Maps the incoming flag to the data
    protected HashMap<String, CommandExtra> commandExtras;//Hash-map for use when command runs. Maps the extra data to the command
    //End command data custom holders

    public Command(Context context, String name, int iconId, String[] values, String phone) {
        //Define vars on startup
        this.name = name;
        this.iconId = iconId;
        parent = context;
        fromNumber = phone;
        params = new ArrayList<>();
        flags = new ArrayList<>();
        commandFlags = new HashMap<>();
        commandExtras = new HashMap<>();

        SharedPrefManager.loadSharedPrefs(context);
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
            JSONArray commandFlagArray = obj.checkAndGetJSONArray(null, KEY_FLAGS);
            if (commandFlagArray != null) {
                for (int z = 0; z < commandFlagArray.length(); z++) {
                    //Load the flags from the JSON, includes updated values.
                    CommandFlag flag = new CommandFlag(JSONWrapper.parseJSON(commandFlagArray.getString(z)));
                    commandFlags.put(flag.getFlag(), flag);
                }
            }
            JSONArray commandExtraArray = obj.checkAndGetJSONArray(null, KEY_EXTRAS);
            if (commandExtraArray != null) {
                for (int z = 0; z < commandExtraArray.length(); z++) {
                    CommandExtra extra = new CommandExtra(JSONWrapper.parseJSON(commandExtraArray.getString(z)));
                    commandExtras.put(extra.getKey(), extra);
                }
            }
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

    public String[] getParams() {
        return null;
    }

    public ArrayList<CommandFlag> getFlags() {
        return null;
    }

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

    protected JSONArray getExtras() {
        return null;
    }

    public JSONObject getJSONRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_ENABLED, enabled);
            ArrayList<CommandFlag> flags = getFlags();
            if (flags != null) {
                JSONArray flagArray = new JSONArray();
                for (CommandFlag flag : flags) {
                    flagArray.put(flag.getJSONRepresentation());
                }
                obj.put(KEY_FLAGS, flagArray);
            }
            JSONArray extras = getExtras();
            if (extras != null) {
                obj.put(KEY_EXTRAS, extras);
            }
        } catch (JSONException e) {
            //won't happen
        }
        return obj;
    }

    public void save() {
        SharedPrefManager.saveString(getName(), getJSONRepresentation().toString());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
    }

    /**
     * Checks the flag to make sure the flag was passed to this request.
     * This also validates the flag as a valid flag registered for this command
     * and has not been disabled by the user.
     *
     * @param flag The flag to check
     * @return Whether the flag is correctly used
     */
    protected boolean canUseFlag(String flag) {
        return flags.contains(flag) && commandFlags.get(flag) != null && commandFlags.get(flag).isFlagEnabled();
    }

}
