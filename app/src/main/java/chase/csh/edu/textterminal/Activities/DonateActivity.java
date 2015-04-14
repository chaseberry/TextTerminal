package chase.csh.edu.textterminal.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import chase.csh.edu.textterminal.R;

public class DonateActivity extends TextTerminalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_activity_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.action_bar_menu, menu);
        menu.findItem(R.id.action_bar_settings).setVisible(false);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

}
