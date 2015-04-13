package chase.csh.edu.textterminal.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import chase.csh.edu.textterminal.Adapters.PhoneListActivityAdapter;
import chase.csh.edu.textterminal.DividerItemDecoration;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Managers.PhoneListManager;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/7/14.
 */
public class PhoneListActivity extends TextTerminalActivity {

    public static final String UPDATEEMPTYVIEWINTENT = "edu.csh.chase.TexTerminal.phoneListActivity.emptyView";

    private PhoneListManager.ListType listType;
    private Dialog addNumberDialog;
    private TextView emptyView;
    private BroadcastReceiver emptyViewUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.phone_list_activity_layout);

        emptyViewUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (PhoneListManager.getNumberManager(listType).size() == 0) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.VISIBLE);
                            //TODO fade in
                        }
                    });
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((emptyViewUpdateReceiver),
                new IntentFilter(UPDATEEMPTYVIEWINTENT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(emptyViewUpdateReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listType = PhoneListManager.stringToListType(getIntent().getStringExtra(getResources().getString(R.string.phone_list_type)));
        PhoneListActivityAdapter adatper = new PhoneListActivityAdapter(listType, this);
        RecyclerView listView = ((android.support.v7.widget.RecyclerView) findViewById(R.id.phone_list_activity_list_view));
        listView.setAdapter(adatper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        setTitle(listType.toString());
        listView.addItemDecoration(new DividerItemDecoration(PhoneListActivity.this, DividerItemDecoration.VERTICAL_LIST));

        emptyView = (TextView) findViewById(R.id.phone_list_activity_empty_view);
        emptyView.setText(listType.toString() + getString(R.string.a_number));
        if (PhoneListManager.getNumberManager(listType).size() != 0) {
            emptyView.setVisibility(View.GONE);
        }

        //((ListView) findViewById(R.id.phone_lit_activity_list_view)).setEmptyView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PhoneListManager.getNumberManager(listType).save();
    }

    public void addNumber(View view) {
        addNumberDialog = new AlertDialog.Builder(this)
                .setTitle(listType.toString() + getString(R.string.a_number))
                .setView(R.layout.add_number_dialog_layout)
                .setPositiveButton(getString(R.string.add_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = ((EditText) addNumberDialog.findViewById(R.id.add_number_edit_text)).getText().toString();
                        String tag = ((EditText) addNumberDialog.findViewById(R.id.add_tag_edit_text)).getText().toString();
                        final PhoneListManager.AddNumberResult result = PhoneListManager.getNumberManager(listType).addNumber(number, tag);
                        if (result == PhoneListManager.AddNumberResult.success) {
                            itemAdded();
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    switch (result) {
                                        case alreadyExists:
                                            Functions.createToastMessage(getString(R.string.number_already_present_help_text) + listType.toString(),
                                                    PhoneListActivity.this, false).show();
                                            break;

                                        case noTag:
                                            Functions.createToastMessage(getString(R.string.supply_tag_help_text),
                                                    PhoneListActivity.this, false).show();
                                            break;

                                        case invalidFormat:
                                            Functions.createToastMessage(getString(R.string.invalid_phone_number_help_text),
                                                    PhoneListActivity.this, false).show();
                                            break;
                                    }

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel_text), null).create();
        addNumberDialog.show();
    }

    public void itemAdded() {
        ((RecyclerView) findViewById(R.id.phone_list_activity_list_view)).getAdapter().notifyItemInserted(0);
        PhoneListManager.getNumberManager(listType).save();
        if (emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
            //TODO fade out
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.action_bar_menu, menu);
        menu.findItem(R.id.action_bar_settings).setVisible(false);
        return true;
    }

}
