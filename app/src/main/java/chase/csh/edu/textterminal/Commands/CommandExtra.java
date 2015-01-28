package chase.csh.edu.textterminal.Commands;

import org.json.JSONException;
import org.json.JSONObject;

import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

/**
 * Created by chase on 1/26/15.
 */
public class CommandExtra implements JSONable {
    //boolean, ringtone, int(number),

    public enum Type {
        BOOLEAN(0, 0, Boolean.class),
        STRING(1, 0, String.class),
        INT(2, 0, Integer.class);

        private final int layout;
        private final Class classType;
        private final int typeId;

        Type(int typeId, int layout, Class classType) {
            this.typeId = typeId;
            this.layout = layout;
            this.classType = classType;
        }


        public int getLayout() {
            return layout;
        }

        public Class getClassType() {
            return classType;
        }

        public int getTypeId() {
            return typeId;
        }
    }


    private static Type convertTypeIdToType(int typeId) {
        switch (typeId) {
            case 0:
                return Type.BOOLEAN;
            case 1:
                return Type.STRING;
            case 2:
                return Type.INT;
            default:
                return null;
        }
    }

    public static final String NAME_KEY = "name";
    public static final String KEY_KEY = "key";
    public static final String DESCRIPTION_KEY = "description";
    public static final String VALUE_KEY = "value";
    public static final String TYPE_KEY = "type";

    private String name;
    private Object value;
    private Type type;
    private String description;
    private String key;


    public CommandExtra(JSONWrapper jsonObj) {
        name = jsonObj.checkAndGetString(null, NAME_KEY);
        value = jsonObj.checkAndGetObject(null, VALUE_KEY);
        type = convertTypeIdToType(jsonObj.checkAndGetInt(-1, TYPE_KEY));
        description = jsonObj.checkAndGetString(null, description);
        key = jsonObj.checkAndGetString(null, key);
    }

    public CommandExtra(String key, String name, String description, Type type) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public JSONObject getJSONClass() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(NAME_KEY, name);
            obj.put(VALUE_KEY, value);
            obj.put(TYPE_KEY, type.getTypeId());
            obj.put(DESCRIPTION_KEY, description);
            obj.put(KEY_KEY, key);
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
