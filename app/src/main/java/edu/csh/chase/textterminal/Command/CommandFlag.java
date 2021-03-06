package edu.csh.chase.textterminal.Command;

import org.json.JSONException;
import org.json.JSONObject;

import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

public class CommandFlag implements JSONable {

    public static final String FLAG_KEY = "flag";
    public static final String FLAG_ENABLED_KEY = "flagEnabled";
    public static final String FLAG_NAME_KEY = "flagName";
    public static final String FLAG_DESCRIPTION_KEY = "flagDescription";

    private String flag;
    private boolean flagEnabled;
    private String flagName;
    private String flagDescription;

    public CommandFlag(JSONWrapper obj) {
        flag = obj.checkAndGetString(null, FLAG_KEY);
        flagEnabled = obj.checkAndGetBoolean(false, FLAG_ENABLED_KEY);
        flagName = obj.checkAndGetString(null, FLAG_NAME_KEY);
        flagDescription = obj.checkAndGetString(null, FLAG_DESCRIPTION_KEY);
    }

    //Constructor to create the flag for first run
    public CommandFlag(String flag, String flagName, String flagDescription) {
        this.flag = flag;
        this.flagName = flagName;
        this.flagDescription = flagDescription;
        this.flagEnabled = true;
    }

    @Override
    public JSONObject getJSONRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FLAG_KEY, flag);
            obj.put(FLAG_ENABLED_KEY, flagEnabled);
            obj.put(FLAG_NAME_KEY, flagName);
            obj.put(FLAG_DESCRIPTION_KEY, flagDescription);
        } catch (JSONException e) {
        }
        return obj;
    }

    public String getFlag() {
        return flag;
    }

    public String getFlagDescription() {
        return flagDescription;
    }

    public boolean isFlagEnabled() {
        return flagEnabled;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagEnabled(boolean enabled) {
        flagEnabled = enabled;
    }
}
