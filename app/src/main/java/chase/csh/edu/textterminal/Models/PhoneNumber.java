package chase.csh.edu.textterminal.Models;

import org.json.JSONException;
import org.json.JSONObject;

import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

/**
 * Created by chase on 1/24/15.
 */
public class PhoneNumber {

    public static final String PHONENUMBERKEY = "phoneNumber";
    public static final String TAGKEY = "tag";

    private String number;
    private String tag;

    public PhoneNumber(String number, String tag) {
        this.number = number;
        this.tag = tag;
    }

    public PhoneNumber(JSONWrapper json) {
        number = json.checkAndGetString(null, PHONENUMBERKEY);
        tag = json.checkAndGetString(null, TAGKEY);
    }

    public JSONObject getNumberAsJson() {
        try {
            return new JSONObject().put(PHONENUMBERKEY, number).put(TAGKEY, tag);
        } catch (JSONException e) {

        }
        return null;
    }

    public String formatNumber() {
        String temp = number.replace("+1", "");
        return "(" + temp.substring(0, 3) + ") " + temp.substring(3, 6) + "-" + temp.substring(6, 10);
    }

    public String getNumber() {
        return number;
    }

    public String getTag() {
        return tag;
    }

    public boolean equals(Object object) {
        return object instanceof PhoneNumber && equals((PhoneNumber) object);
    }

    public boolean equals(PhoneNumber number) {
        return getNumber().equals(number.getNumber());
    }

}
