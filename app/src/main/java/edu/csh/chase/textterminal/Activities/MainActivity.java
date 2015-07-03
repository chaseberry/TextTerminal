package edu.csh.chase.textterminal.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import chase.csh.edu.textterminal.R;
import edu.csh.chase.textterminal.Adapters.MainActivityListLoader;
import edu.csh.chase.textterminal.Functions;
import edu.csh.chase.textterminal.Managers.SharedPrefManager;

public class MainActivity extends TextTerminalActivity {

    public static final String FIRSTRUN = "FIRST_RUN";
    private ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.main_activity_layout);
        final ArrayList<String> classNames = Functions.getCommandClassNames(this);

        //TODO figure out what to do here
        if (classNames.isEmpty()) {
            //failed;
        }
        Collections.sort(classNames);
        MainActivityListLoader loader = new MainActivityListLoader(this, classNames);
        mainListView = (ListView) findViewById(R.id.main_activity_list_view);
        mainListView.setAdapter(loader);
        if (SharedPrefManager.loadBoolean(FIRSTRUN, true)) {
            Functions.displayAdminDialog(this);
            SharedPrefManager.saveBoolean(FIRSTRUN, false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        return true;
    }

}
