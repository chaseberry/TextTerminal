package edu.csh.chase.textterminal.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Functions;
import edu.csh.chase.textterminal.Managers.PhoneListManager;
import edu.csh.chase.textterminal.Managers.SharedPrefManager;
import edu.csh.chase.textterminal.R;


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
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
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
                    return true;
                }
            });
        }

        Preference whiteListPref = findPreference("Whitelist");
        if (whiteListPref != null) {
            whiteListPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startPhoneListActivity("Whitelist");
                    return true;
                }
            });
        }

        Preference blackListPref = findPreference("Blacklist");
        if (blackListPref != null) {
            blackListPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startPhoneListActivity("Blacklist");
                    return true;
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
        super.onResume();
        SharedPrefManager.loadSharedPrefs(this);
        Preference pinPref = findPreference("preference_pin");
        if (pinPref != null) {
            pinPref.setSummary(
                    ((SharedPrefManager.loadString(Command.SECURITY_CODE_KEY, "").length() > 0)
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

    private void startPhoneListActivity(String list) {
        Intent i = new Intent(SettingsActivity.this, PhoneListActivity.class);
        i.putExtra("PHONE_LIST_TYPE", list);
        startActivity(i);
    }

}
