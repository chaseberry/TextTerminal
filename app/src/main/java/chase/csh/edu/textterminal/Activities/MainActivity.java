package chase.csh.edu.textterminal.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Adapters.MainActivityListLoader;
import chase.csh.edu.textterminal.R;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;
import dalvik.system.DexFile;

public class MainActivity extends TextTerminalActivity {

    public static final String FIRSTRUN = "FIRST_RUN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.main_activity_layout);
        ArrayList<String> classNames = new ArrayList<>();
        //new Lookup(this, new String[]{"Lookup", "Mike Keegan"}, "5188361344").execute();
        try {
            DexFile df = new DexFile(getPackageCodePath());
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String s = iter.nextElement();
                if (s.contains(".Commands") && !s.contains("$") && !s.contains(".Commands.Command")) {
                    classNames.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classNames.isEmpty()) {
            //failed;
        }
        Collections.sort(classNames);
        MainActivityListLoader loader = new MainActivityListLoader(this, classNames);
        ((ListView) findViewById(R.id.main_activity_list_view)).setAdapter(loader);

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
