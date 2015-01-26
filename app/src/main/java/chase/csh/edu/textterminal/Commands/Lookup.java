package chase.csh.edu.textterminal.Commands;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String lookup = getLookup();
        if (lookup == null) {
            sendMessage(fromNumber, "Must provide something to lookup with.");
            return true;
        }
        ArrayList<String> vCards = lookup(lookup);
        if (vCards == null || vCards.size() == 0) {
            sendMessage(fromNumber, "No results found for " + lookup);
            return true;
        }
        if (flags.contains("-a")) {
            String message = "";
            for (String vCard : vCards) {
                message += parseVCard(vCard).toString() + "\n";
            }
            sendMessage(message, fromNumber);//TODO Format output differently?
        } else {
            System.out.println(fromNumber);
            sendMessage(parseVCard(vCards.get(0)).toString(), fromNumber);//Format
        }
        return true;
    }

    private String getLookup() {
        StringBuilder builder = new StringBuilder(params.size() * 5);
        if (params.size() == 0) {
            return null;
        }
        builder.append(params.get(0));
        for (int z = 1; z < params.size(); z++) {
            builder.append(params.get(z) + " ");
        }
        return builder.toString();
    }

    @Override
    public String getHelpMessage() {
        return "Looks up a number or a name depending on what was provided.";
    }

    @Override
    public String[] getParams() {
        return new String[]{"The key to lookup."};
    }

    @Override
    public String[] getFlags() {
        return new String[]{"[-a] will send all values found instead of just the first"};
    }

    @Override
    protected JSONObject addExtras(JSONObject obj) {
        try {
            return obj.put(KEY_ALLOWED_FLAGS, new JSONArray().put("-a"));
        } catch (JSONException e) {
        }
        return obj;
    }

    private ArrayList<String> lookup(String num) {

        Uri uri;
        if (isNumber(num)) {
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(num));//Given a phone number
        } else {
            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(num));
        }
        String[] projection = new String[]{ContactsContract.Data.LOOKUP_KEY,
                ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = parent.getContentResolver().query(uri, projection, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }

        ArrayList<String> vCards = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String lookupKey = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));


            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
            AssetFileDescriptor fd;
            try {
                fd = parent.getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] b = new byte[(int) fd.getDeclaredLength()];
                fis.read(b);
                vCards.add(new String(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        return vCards;
    }


    private boolean isNumber(String in) {
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        Pattern phonePattern = Pattern.compile(expression);
        Matcher match = phonePattern.matcher(in);
        return match.matches();
    }

    private HashMap<String, String> parseVCard(String vCard) {
        String[] cardParts = vCard.split("\n");
        HashMap<String, String> map = new HashMap<String, String>();
        for (int z = 0; z < cardParts.length; z++) {
            if (!cardParts[z].contains(":") || cardParts[z].contains("PHOTO")
                    || cardParts[z].contains("VCARD")) {//Parse out the Photo if it exists and VCARD stuff
                cardParts[z] = null;
            } else {
                String[] split = cardParts[z].split(":");
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

}
