package chase.csh.edu.textterminal.Commands;

import org.json.JSONException;
import org.json.JSONObject;

import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

/**
 * Created by chase on 1/26/15.
 */
public class CommandExtra implements JSONable {

    public static final String NAME_KEY = "name";
    public static final String KEY_KEY = "key";
    public static final String DESCRIPTION_KEY = "description";
    public static final String VALUE_KEY = "value";
    public static final String TYPE_KEY = "type";

    private String name;
    private Object value;
    private Class type;
    private String description;
    private String key;

    public CommandExtra(JSONWrapper jsonObj) {
        name = jsonObj.checkAndGetString(null, NAME_KEY);
        value = jsonObj.checkAndGetObject(null, VALUE_KEY);
        type = (Class) jsonObj.checkAndGetObject(null, TYPE_KEY);
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


    @Override
    public JSONObject getJSONClass() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(NAME_KEY, name);
            obj.put(VALUE_KEY, value);
            obj.put(TYPE_KEY, type);
        } catch (JSONException e) {

        }
        return obj;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }
}
