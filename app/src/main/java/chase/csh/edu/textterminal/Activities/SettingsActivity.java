package chase.csh.edu.textterminal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import chase.csh.edu.textterminal.Commands.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.Managers.PhoneListManager;
import chase.csh.edu.textterminal.R;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;


public class SettingsActivity extends PreferenceActivity {


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.action_bar_menu, menu);
        menu.findItem(R.id.action_bar_settings).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_activity_layout);

        Preference pinPref = findPreference("preference_pin");
        if (pinPref != null) {
            pinPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getApplicationContext(), SecurityCodeActivity.class));
                    return false;
                }
            });
        }

        Preference deviceAdminPref = findPreference("preference_admin_features");
        if (deviceAdminPref != null) {
            if (!Functions.isAdmin(SettingsActivity.this)) {
                deviceAdminPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Functions.displayAdminDialog(SettingsActivity.this);
                        return true;
                    }
                });
            }
        }

    }

    @Override
    protected void onResume() {
        super.onRestart();
        SharedPrefManager.loadSharedPrefs(this);
        Preference pinPref = findPreference("preference_pin");
        if (pinPref != null) {
            pinPref.setSummary(
                    ((SharedPrefManager.loadString(Command.SECURITYCODEKEY, "").length() > 0)
                            ? R.string.enabled_string : R.string.disabled_string));
        }
        Preference whiteListPref = findPreference("Whitelist");
        if (whiteListPref != null) {
            whiteListPref.setSummary(PhoneListManager.getNumberManager(PhoneListManager.ListType.WHITELIST).size() > 0 ?
                    R.string.enabled_string : R.string.disabled_string);
        }
        Preference blackListPref = findPreference("Blacklist");
        if (blackListPref != null) {
            blackListPref.setSummary(PhoneListManager.getNumberManager(PhoneListManager.ListType.BLACKLIST).size() > 0 ?
                    R.string.enabled_string : R.string.disabled_string);
        }
        Preference deviceAdminPref = findPreference("preference_admin_features");
        if (deviceAdminPref != null) {
            if (Functions.isAdmin(SettingsActivity.this)) {
                deviceAdminPref.setEnabled(false);
            }
        }
    }
}
