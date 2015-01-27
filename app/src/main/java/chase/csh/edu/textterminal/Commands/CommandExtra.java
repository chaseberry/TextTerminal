package chase.csh.edu.textterminal.Commands;

import org.json.JSONException;
import org.json.JSONObject;

import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

/**
 * Created by chase on 1/26/15.
 */
public class CommandExtra {

    public static final String NAME_KEY = "name";
    public static final String VALUE_KEY = "value";
    public static final String TYPE_KEY = "type";

    private String name;
    private Object value;
    private Class type;

    public CommandExtra(JSONWrapper jsonObj) {
        name = jsonObj.checkAndGetString(null, NAME_KEY);
        value = jsonObj.checkAndGetObject(null, VALUE_KEY);
        type = (Class) jsonObj.checkAndGetObject(null, TYPE_KEY);
    }

    public JSONObject getSaveObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(NAME_KEY, name);
            obj.put(VALUE_KEY, value);
            obj.put(TYPE_KEY, type);
        } catch (JSONException e) {

        }
        return obj;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return type.cast(value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class getType() {
        return type;
    }


}
