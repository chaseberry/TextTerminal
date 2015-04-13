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

public class Contact extends Command {

    public final String CUSTOM_FLAG = parent.getString(R.string.command_contact_flag_custom);

    public Contact(Context context, String[] values, String phone) {
        super(context, context.getString(R.string.command_contact_title), R.drawable.ic_person_add_white_48dp, values, phone);
    }

    @Override
    protected boolean executeCommand() {
        boolean usingCustom = canUseFlag(CUSTOM_FLAG);
        if (params.size() < 2) {
            sendMessage(parent.getString(R.string.command_contact_response_invalid_parameters), fromNumber);
            return false;
        }
        if (usingCustom && params.size() < 3) {
            sendMessage(parent.getString(R.string.command_contact_response_invalid_custom_parameters), fromNumber);
        }
        String firstName = params.get(0), lastName = params.get(1);
        if (addContact(firstName, lastName, usingCustom ? params.get(2) : fromNumber)) {
            sendMessage(parent.getString(R.string.command_contact_response_success), fromNumber);
            return true;
        } else {
            sendMessage(parent.getString(R.string.command_contact_response_failure), fromNumber);
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
        return new String[]{"first name last name"};
    }

    @Override
    public String getHelpMessage() {
        return parent.getString(R.string.command_contact_help);
    }

    @Override
    protected ArrayList<CommandFlag> getFlags() {
        ArrayList<CommandFlag> flags = new ArrayList<>(1);
        if (commandFlags.get(CUSTOM_FLAG) != null) {
            flags.add(commandFlags.get(CUSTOM_FLAG));//Flag exists and was loaded in from memory
        } else {
            //Flag was not loaded in from memory (IE first run, so it must be created)
            flags.add(new CommandFlag(CUSTOM_FLAG, parent.getString(R.string.command_contact_flag_title),
                    parent.getString(R.string.command_contact_flag_description)));
        }
        return flags;
    }
}
