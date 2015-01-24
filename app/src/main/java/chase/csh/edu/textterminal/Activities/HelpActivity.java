package chase.csh.edu.textterminal.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 1/24/15.
 */
public class HelpActivity extends TextTerminalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.action_bar_menu, menu);
        menu.findItem(R.id.action_bar_help).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

}
