package chase.csh.edu.textterminal.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.PhoneListActivityAdapter;
import chase.csh.edu.textterminal.R;
import chase.csh.edu.textterminal.SharedPrefManager;

/**
 * Created by chase on 12/7/14.
 */
public class PhoneListActivity extends TextTerminalActivity {

    private JSONArray list;
    private String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.loadSharedPrefs(this);
        setContentView(R.layout.phone_list_activity_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listType = getIntent().getStringExtra(getResources().getString(R.string.phone_list_type));
        try {
            list = new JSONArray(SharedPrefManager.loadString(listType, "[]"));
        } catch (JSONException e) {
            Functions.createToastMessage("Failed to load " + listType, this, true).show();
            list = new JSONArray();
        }
        PhoneListActivityAdapter adatper = new PhoneListActivityAdapter(list, this);
        ((ListView) findViewById(R.id.phone_lit_activity_list_view)).setAdapter(adatper);
        //((ListView) findViewById(R.id.phone_lit_activity_list_view)).setEmptyView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefManager.saveString(listType, list.toString());
    }

    public void addNumber(View view) {

    }
}
