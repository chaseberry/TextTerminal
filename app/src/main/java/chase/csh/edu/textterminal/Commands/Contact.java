package chase.csh.edu.textterminal.Commands;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Command.CommandFlag;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 3/30/15.
 */
public class Contact extends Command {

    public static final String CUSTOM_FLAG = "-c";

    public Contact(Context context, String[] values, String phone) {
        super(context, "Contact", R.drawable.ic_person_add_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        boolean usingCustom = canUseFlag(CUSTOM_FLAG);
        if (params.size() < 2) {
            sendMessage("Please provide both a first name and a last name.", fromNumber);
            return false;
        }
        if (usingCustom && params.size() < 3) {
            sendMessage("Please add a number for a custom contact.", fromNumber);
        }
        String firstName = params.get(0), lastName = params.get(1);
        if (addContact(firstName, lastName, usingCustom ? params.get(2) : fromNumber)) {
            sendMessage("New contact added", fromNumber);
            return true;
        } else {
            sendMessage("The contact failed to add", fromNumber);
        }
        return false;
    }

    public boolean addContact(String firstName, String lastName, String num) {
        int contactId = 0;
        ArrayList<ContentProviderOperation> insertList = new ArrayList<ContentProviderOperation>();
        insertList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // first and last names
        insertList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, contactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName)
                .build());

        insertList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, num)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            ContentProviderResult[] results = parent.getContentResolver().applyBatch(ContactsContract.AUTHORITY, insertList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public String[] getParams() {
        return new String[]{"The first name.", "The last name"};
    }

    @Override
    public String getHelpMessage() {
        return "Adds this number as a contact";
    }

    @Override
    protected ArrayList<CommandFlag> getFlags() {
        ArrayList<CommandFlag> flags = new ArrayList<>(1);
        if (commandFlags.get(CUSTOM_FLAG) != null) {
            flags.add(commandFlags.get(CUSTOM_FLAG));//Flag exists and was loaded in from memory
        } else {
            //Flag was not loaded in from memory (IE first run, so it must be created)
            flags.add(new CommandFlag(CUSTOM_FLAG, "Custom Number", "This will use the third parameter as a contact."));
        }
        return flags;
    }
}
