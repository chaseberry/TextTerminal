package chase.csh.edu.textterminal.Managers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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

    private ArrayList<String> numbers;
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
                this.numbers.add(numbers.getString(z));
            }
        } catch (JSONException e) {

        }
    }

    public boolean contains(String num) {
        return numbers.contains(num);
    }

    public boolean addNumber(String num) {
        num = num.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
        if (!num.startsWith("+1")) {
            num = "+1" + num;
        }
        if (!contains(num)) {
            numbers.add(0, num);
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

    public String getNumber(int position) {
        //add fancy stuff like () and - ie (555) 444-4444 for readability
        return numbers.get(position);
    }

    public void save() {
        JSONArray array = new JSONArray(numbers);
        SharedPrefManager.saveString(listType.toString(), array.toString());
    }

}
