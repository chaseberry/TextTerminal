package chase.csh.edu.textterminal.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import chase.csh.edu.textterminal.Adapters.PhoneListActivityAdapter;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Managers.PhoneListManager;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/7/14.
 */
public class PhoneListActivity extends TextTerminalActivity {

    private PhoneListManager.ListType listType;
    private Dialog addNumberDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.phone_list_activity_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listType = PhoneListManager.stringToListType(getIntent().getStringExtra(getResources().getString(R.string.phone_list_type)));
        PhoneListActivityAdapter adatper = new PhoneListActivityAdapter(listType, this);
        ((android.support.v7.widget.RecyclerView) findViewById(R.id.phone_list_activity_list_view)).setAdapter(adatper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ((android.support.v7.widget.RecyclerView) findViewById(R.id.phone_list_activity_list_view)).setLayoutManager(layoutManager);
        setTitle(listType.toString());
        //((ListView) findViewById(R.id.phone_lit_activity_list_view)).setEmptyView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void addNumber(View view) {
        addNumberDialog = new AlertDialog.Builder(this)
                .setTitle(listType.toString() + " a number")
                .setView(R.layout.add_number_dialog_layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = ((EditText) addNumberDialog.findViewById(R.id.add_number_edit_text)).getText().toString();
                        String tag = ((EditText) addNumberDialog.findViewById(R.id.add_tag_edit_text)).getText().toString();
                        if (PhoneListManager.getNumberManager(listType).addNumber(number, tag)) {
                            itemAdded();
                        } else {
                            Functions.createToastMessage("Number already in " + listType.toString(), PhoneListActivity.this, false);
                        }
                    }
                })
                .setNegativeButton("Cancel", null).create();
        addNumberDialog.show();
    }

    public void itemAdded() {
        ((RecyclerView) findViewById(R.id.phone_list_activity_list_view)).getAdapter().notifyItemInserted(0);
        PhoneListManager.getNumberManager(listType).save();
    }
}
