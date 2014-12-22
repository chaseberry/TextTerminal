package edu.csh.chase.RestfulAPIConnector.JSONWrapper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONWrapper {

    private JSONException noObjectOrArrayExccetion = new JSONException("No JSONObjecr or JSONArray");
    private JSONException noArrayExcetion = new JSONException("No JSONArray");

    private boolean debugMode = false;
    private final String TAG = "JSONWRAPPER";

    /**
     * Returns a new JSONObjectWrapper or JSONArrayWrapper from the given String
     *
     * @param string A valid JSONEncoded string of either a JSONObject or JSONArray
     * @return A JSONObjectWrapper if the string was a JSONObject or JSONArrayWrapper if the string was a JSONArray
     * @throws JSONException if the string could not be parsed into valid JSON
     */
    public static JSONWrapper parseJSON(String string) throws JSONException{
        try {
            return new JSONObjectWrapper(string);
        } catch (JSONException e) {
        }
        try{
            return new JSONArrayWrapper(string);
        }catch(JSONException e){

        }
        throw new JSONException("Couldn't parse" + string);
    }

    /**
     * Wraps the given JSONObject in a JSONObjectWrapper
     *
     * @param object The JSONObject to wrap
     * @return The wrapped JSONObject
     */
    public static JSONObjectWrapper wrapperFromObject(JSONObject object){
        return new JSONObjectWrapper(object);
    }

    /**
     * Wraps the given JSONArray into a JSONArray
     *
     * @param array The JSONArray to wrap
     * @return The wrapped JSONArray
     */
    public static JSONArrayWrapper wrapperFromArray(JSONArray array){
        return new JSONArrayWrapper(array);
    }

    //***************************Debug functions****************************************************

    /**
     * Sets the debug mode. Defaults to false.
     *
     * @param debug true to print debug messages, false to not.
     */
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    protected void debug(String message) {
        if (debugMode) {
            Log.d(TAG, message);
        }
    }

    //***************************Valid Stuff********************************************************

    public abstract String getValidKey(String... keys);

    /**
     * Checks to see if the given key exists in the currently wrapped JSONItem
     *
     * @param key The key to check, can be a multikey
     * @return true if the key exists, false otherwise
     */
    public boolean has(String key) {
        return getValidKey(key) != null;
    }

    //****************************PARSERS***********************************************************

    protected Object parseKey(JSONWrapperKeyset keySet, JSONArray array) throws JSONException {
        boolean hasNext = keySet.hasNext();
        String key = keySet.next();
        int arrayKey = -1;
        debug("Trying key: " + key + " : With JSONArray");
        try {
            arrayKey = Integer.parseInt(key);
        } catch (NumberFormatException ex) {
            debug(key + " is not a valid array key");
            throw new JSONException(key + " is not a valid key for array");
        }
        if (hasNext) {
            if (isObjectJSONObject(array.get(arrayKey))) {
                return parseKey(keySet, array.getJSONObject(arrayKey));
            } else {
                return parseKey(keySet, array.getJSONArray(arrayKey));
            }
        } else {
            return array.get(arrayKey);
        }
    }

    protected Object parseKey(JSONWrapperKeyset keySet, JSONObject object) throws JSONException {
        boolean hasNext = keySet.hasNext();
        String key = keySet.next();
        debug("Trying key: " + key + " : On JSONObject");
        if (hasNext) {
            if (isObjectJSONObject(object.get(key))) {
                return parseKey(keySet, object.getJSONObject(key));
            } else {
                return parseKey(keySet, object.getJSONArray(key));
            }
        } else {
            return object.get(key);
        }
    }

    protected boolean isObjectJSONObject(Object o) {
        return o instanceof JSONObject;
    }

    //****************************Getters***********************************************************

    /**
     * Gets a standard java Object with the given key
     *
     * @param key The key to check, can be a multikey
     * @return The Object found at the specific key
     * @throws JSONException if the key does not exist
     */
    public abstract Object getObject(String key) throws JSONException;

    /**
     * Gets the String at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return The String found at the key
     * @throws JSONException if the key does not exist or is not a String
     */
    public String getString(String key) throws JSONException {
        Object string = this.getObject(key);
        if (string == null) {
            throw new JSONException("Invalid key:" + key);
        }
        return String.valueOf(string);
    }

    /**
     * Gets the boolean at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return The boolean found at the key
     * @throws JSONException if the key does not exist, or is not a boolean
     */
    public boolean getBoolean(String key) throws JSONException {
        Object bool = this.getObject(key);
        if (bool == null) {
            throw new JSONException("Invalid key:" + key);
        }
        return Boolean.parseBoolean(String.valueOf(bool));
    }

    /**
     * Gets the int at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return the int found at the key
     * @throws JSONException if the key does not exist, or is not an int
     */
    public int getInt(String key) throws JSONException {
        Object integer = this.getObject(key);
        if (integer == null) {
            throw new JSONException("Invalid key:" + key);
        }
        try {
            return Integer.parseInt(String.valueOf(integer));
        } catch (NumberFormatException ex) {
            throw new JSONException(integer + " in an invalid int");
        }
    }

    /**
     * Gets the double at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return the double found at the key
     * @throws JSONException if the key does not exist, or is not a double
     */
    public double getDouble(String key) throws JSONException {
        Object doubleValue = this.getObject(key);
        if (doubleValue == null) {
            throw new JSONException("Invalid key:" + key);
        }
        try {
            return Double.parseDouble(String.valueOf(doubleValue));
        } catch (NumberFormatException ex) {
            throw new JSONException(doubleValue + " is an invalid double");
        }
    }

    /**
     * Gets the long found at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return the long found at the specific key
     * @throws JSONException if the key is not found or was not a long
     */
    public long getLong(String key) throws JSONException {
        Object longValue = this.getObject(key);
        if (longValue == null) {
            throw new JSONException("Invalid key:" + key);
        }
        try {
            return Long.parseLong(String.valueOf(longValue));
        } catch (NumberFormatException ex) {
            throw new JSONException(longValue.toString() + " is an invalid long");
        }
    }

    /**
     * Gets the JSONArray at the specific key
     *
     * @param key The key to check, can be a multikey
     * @return The JSONArray found at the specific key
     * @throws JSONException if the key is not found or not a JSONArray
     */
    public JSONArray getJSONArray(String key) throws JSONException {
        Object jsonArray = this.getObject(key);
        if (jsonArray == null) {
            throw new JSONException("Invalid key: " + key);
        }
        try {
            return (JSONArray) jsonArray;
        } catch (ClassCastException ex) {
            throw new JSONException(key + " does not link to a JSONArray");
        }
    }

    /**
     * Gets the JSONObject at the given key
     *
     * @param key The key to check, can be a multikey
     * @return The JSONObject found at the given key
     * @throws JSONException if the key was invalid, or not a JSONObject
     */
    public JSONObject getJSONObject(String key) throws JSONException {
        Object jsonObject = getObject(key);
        if (jsonObject == null) {
            throw new JSONException("Invalid key: " + key);
        }
        try {
            return (JSONObject) jsonObject;
        } catch (ClassCastException ex) {
            throw new JSONException(key + " does not link to a JSONArray");
        }
    }

    /**
     * Gets a JSONArray from a given key and converts it to a String Array for convience
     *
     * @param key The key to check, can be a multikey
     * @return A String Array from a JSONArray at the given key
     * @throws JSONException if the key was invalid or not a JSONArray
     */
    public String[] getStringArray(String key) throws JSONException {
        JSONArray jsonArray = this.getJSONArray(key);
        if (jsonArray == null) {
            throw new JSONException("Invalid key:" + key);
        }
        return JSONArrayToStringArray(jsonArray);
    }

    //**********getters with int

    public Object getObject(int i) throws JSONException{
        return null;
    }

    public JSONObject getJSONObject(int i) throws JSONException {
        Object json = getObject(i);
        if(json == null){
            throw new JSONException("Wrapper is not JSONArray");
        }
        try {
            return (JSONObject) json;
        } catch (ClassCastException ex) {
            throw new JSONException(i + " does not link to a JSONArray");
        }
    }

    public JSONArray getJSONArray(int i) throws JSONException {
        Object array = getJSONObject(i);
        if(array == null){
            throw new JSONException("Wrapper is not array");
        }
        try {
            return (JSONArray) array;
        } catch (ClassCastException ex) {
            throw new JSONException(i + " does not link to a JSONArray");
        }
    }

    public String[] getStringArray(int i) throws JSONException {
        return JSONArrayToStringArray(getJSONArray(i));
    }

    public String getString(int i) throws JSONException {
        Object string = getObject(i);
        if(string == null){
            throw new JSONException("Wrapper is not JSONArray");
        }
        return String.valueOf(string);
    }

    public int getInt(int i) throws JSONException {
        try{
            return Integer.parseInt(String.valueOf(getObject(i)));
        }catch(NumberFormatException ex){
            throw new JSONException("Couldn't convert to int");
        }
    }

    public double getDouble(int i) throws JSONException {
        try{
            return Double.parseDouble(String.valueOf(getObject(i)));
        }catch(NumberFormatException ex){
            throw new JSONException("Couldn't convert to double");
        }
    }

    public long getLong(int i) throws JSONException {
        try{
            return Long.parseLong(String.valueOf(getObject(i)));
        }catch(NumberFormatException ex){
            throw new JSONException("Couldn't convert to long");
        }
    }

    public boolean getBolean(int i) throws JSONException {
        return Boolean.parseBoolean(String.valueOf(getObject(i)));
    }

    //************************checkAndGetters*******************************************************
    public abstract Object checkAndGetObject(Object failed, String... keys);

    public JSONObject checkAndGetJSONObject(JSONObject failed, String... keys) {
        try{
            return new JSONObject(String.valueOf(checkAndGetObject(failed, keys)));
        }catch (JSONException e){
            debug("Couldn't convert to JSONObject");
        }
        return failed;
    }

    public long checkAndGetLong(long failed, String... keys) {
        try{
            return Long.parseLong(String.valueOf(checkAndGetObject(failed, keys)));
        }catch(NumberFormatException ex){
            debug("Couldn't convert to long");
        }
        return failed;
    }

    public String checkAndGetString(String failed, String... keys) {
        String returned = String.valueOf(checkAndGetObject(failed, keys));
        return "null".equals(returned) ? failed : returned;
    }

    public String[] checkAndGetStringArray(String[] failed, String... keys) {
        JSONArray array = checkAndGetJSONArray(null, keys);
        if(array == null){
            return failed;
        }
        return JSONArrayToStringArray(array);
    }

    public JSONArray checkAndGetJSONArray(JSONArray failed, String... keys) {
        try{
            return new JSONArray(String.valueOf(checkAndGetObject(failed, keys)));
        }catch(JSONException e){
            debug("Couldn't convernt to JSONArray");
        }
        return failed;
    }

    public double checkAndGetDouble(double failed, String... keys) {
        try{
            return Double.parseDouble(String.valueOf(checkAndGetObject(failed, keys)));
        }catch(NumberFormatException e){
            debug("Couldn't convert to double");
        }
        return failed;
    }

    public int checkAndGetInt(int failed, String... keys) {
        try {
            return Integer.parseInt(String.valueOf(checkAndGetObject(failed, keys)));
        }catch(NumberFormatException ex){
            debug("Item not an int");
        }
        return failed;
    }

    public boolean checkAndGetBoolean(boolean failed, String... keys) {
        return Boolean.parseBoolean(String.valueOf(checkAndGetObject(failed, keys)));
    }

    public int length() {
        return -1;
    }

    public boolean isValid() {
        return false;
    }

    private String[] JSONArrayToStringArray(JSONArray array) {
        String[] data = new String[array.length()];
        for (int z = 0; z < array.length(); z++) {
            try {
                data[z] = String.valueOf(array.get(z));
            } catch (JSONException e) {
            }
        }
        return data;
    }

    public String toString(){
        return "Invalid JSONWrapper";
    }

}