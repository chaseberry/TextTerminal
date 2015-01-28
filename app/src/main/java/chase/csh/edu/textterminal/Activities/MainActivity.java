package chase.csh.edu.textterminal.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import chase.csh.edu.textterminal.Adapters.MainActivityListLoader;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import chase.csh.edu.textterminal.R;
import dalvik.system.DexFile;

public class MainActivity extends TextTerminalActivity {

    public static final String FIRSTRUN = "FIRST_RUN";
    private ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.main_activity_layout);
        final ArrayList<String> classNames = Functions.getCommandClassNames(this);
        if (classNames.isEmpty()) {
            //failed;
        }
        Collections.sort(classNames);
        MainActivityListLoader loader = new MainActivityListLoader(this, classNames);
        mainListView = ((ListView) findViewById(R.id.main_activity_list_view));
        mainListView.setAdapter(loader);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String className = classNames.get(position);
                System.out.println(className);
            }
        });

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
