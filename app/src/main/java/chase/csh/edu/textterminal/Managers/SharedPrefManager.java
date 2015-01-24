package chase.csh.edu.textterminal.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class SharedPrefManager {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    public static void loadSharedPrefs(Context c) {
        pref = PreferenceManager.getDefaultSharedPreferences(c);
        editor = pref.edit();
    }

    public static void saveBoolean(String name, boolean val) {
        editor.putBoolean(name, val);
        editor.commit();
    }

    public static boolean loadBoolean(String name, boolean def) {
        return pref.getBoolean(name, def);
    }

    public static void saveString(String name, String val) {
        editor.putString(name, val);
        editor.commit();
    }

    public static String loadString(String name, String def) {
        return pref.getString(name, def);
    }

    public static void saveStringSet(String name, Set<String> strings) {
        editor.putStringSet(name, strings);
        editor.commit();
    }

    public static Set<String> loadStringSet(String name, Set<String> strings) {
        return pref.getStringSet(name, strings);
    }

}
