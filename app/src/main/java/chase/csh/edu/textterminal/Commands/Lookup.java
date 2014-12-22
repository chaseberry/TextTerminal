package chase.csh.edu.textterminal.Commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.JSONObject;

import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/17/14.
 */
public class Lookup extends Command {

    public Lookup(Context c, String[] parts, String phone) {
        super(c, "Lookup", R.drawable.ic_contacts_white_48dp, parts, phone);
    }

    @Override
    protected boolean executeCommand() {
        System.out.println(lookup(params.get(0)));
        return false;
    }

    @Override
    public String getHelpMessage() {
        return "Looks up a number or a name depending on what was provided.";
    }

    @Override
    public String[] getParams() {
        return new String[0];
    }

    @Override
    public String[] getFlags() {
        return new String[0];
    }

    @Override
    protected JSONObject addExtras(JSONObject obj) {
        return obj;
    }

    private String lookup(String num) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(num));
        System.out.println(uri);
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = parent.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            System.out.println("No result");
            return "";
        }
        return cursor.getString(0);
    }


    private boolean isNumber(String in) {
        return false;
    }

}
