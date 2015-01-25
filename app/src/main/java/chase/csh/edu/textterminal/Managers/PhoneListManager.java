package chase.csh.edu.textterminal.Managers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import chase.csh.edu.textterminal.Models.PhoneNumber;
import edu.csh.chase.RestfulAPIConnector.JSONWrapper.JSONWrapper;

/**
 * Created by chase on 12/22/14.
 */
public abstract class PhoneListManager {

    public enum ListType {
        BLACKLIST("Black List"), WHITELIST("White List");

        String val;

        ListType(String val) {
            this.val = val;
        }

        public String toString() {
            return val;
        }

    }

    private ArrayList<PhoneNumber> numbers;
    private ListType listType;
    private static WhiteListManager whiteListManager;
    private static BlackListManager blackListManager;

    public static ListType stringToListType(String in) {
        if (in.equals("Black List")) {
            return ListType.BLACKLIST;
        }
        if (in.equals("White List")) {
            return ListType.WHITELIST;
        }
        return null;
    }

    public static PhoneListManager getNumberManager(ListType listType) {
        if (listType == ListType.BLACKLIST) {
            if (blackListManager == null) {
                blackListManager = new BlackListManager();
            }
            return blackListManager;
        } else if (listType == ListType.WHITELIST) {
            if (whiteListManager == null) {
                whiteListManager = new WhiteListManager();
            }
            return whiteListManager;
        }
        return null;
    }

    public PhoneListManager(ListType listType) {
        this.listType = listType;
        numbers = new ArrayList<>();
        try {
            JSONArray numbers = new JSONArray(SharedPrefManager.loadString(listType.toString(), "[]"));
            for (int z = 0; z < numbers.length(); z++) {
                this.numbers.add(new PhoneNumber(JSONWrapper.parseJSON(numbers.getString(z))));
            }
        } catch (JSONException e) {

        }
    }

    public boolean contains(PhoneNumber number) {
        return numbers.contains(number);
    }

    public boolean contains(String number) {
        for (PhoneNumber phoneNumber : numbers) {
            if (number != null && number.equals(phoneNumber.getNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean addNumber(String num, String tag) {
        num = num.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
        if (!num.startsWith("+1")) {
            num = "+1" + num;
        }
        PhoneNumber number = new PhoneNumber(num, tag);
        if (!contains(number)) {
            numbers.add(0, number);
            return true;
        }
        return false;
    }

    public boolean removeNumber(String num) {
        return numbers.remove(num);
    }

    public boolean removeNumber(int index) {
        return numbers.remove(index) != null;
    }

    public int size() {
        return numbers.size();
    }

    public PhoneNumber getNumber(int position) {
        //add fancy stuff like () and - ie (555) 444-4444 for readability
        return numbers.get(position);
    }

    public void save() {
        JSONArray array = new JSONArray();
        for (PhoneNumber number : numbers) {
            array.put(number.getNumberAsJson());
        }
        SharedPrefManager.saveString(listType.toString(), array.toString());
    }

}
