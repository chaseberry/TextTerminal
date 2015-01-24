package chase.csh.edu.textterminal.Activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import chase.csh.edu.textterminal.Activities.SettingsActivity;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/3/14.
 */
public class TextTerminalActivity extends Activity {

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
