package edu.csh.chase.textterminal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import chase.csh.edu.textterminal.R;
import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Functions;
import edu.csh.chase.textterminal.Managers.SharedPrefManager;

public class SecurityCodeActivity extends TextTerminalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_code_activity_layout);
    }

    public void saveSecurityCode(View view) {
        String newPass = ((EditText) findViewById(R.id.security_code_activity_code_new)).getText().toString();
        String confirmPass = ((EditText) findViewById(R.id.security_code_activity_code_confirm)).getText().toString();
        if (null == newPass || null == confirmPass || "".equals(newPass) || "".equals(confirmPass)) {
            Functions.createMessageWithButton(getString(R.string.fill_all_fields_help_text), this).show();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            Functions.createMessageWithButton(getString(R.string.code_match_fail_help_text), this).show();
            return;
        }

        SharedPrefManager.saveString(Command.SECURITY_CODE_KEY, Functions.hashPassword(newPass));
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.security_code_activity_code_confirm).getWindowToken(), 0);
            imm.hideSoftInputFromWindow(findViewById(R.id.security_code_activity_code_new).getWindowToken(), 0);
        }
        Functions.createToastMessage(getString(R.string.code_set_help_text), this, false).show();
    }

    public void clearSecurityCode(View view) {
        SharedPrefManager.saveString(Command.SECURITY_CODE_KEY, "");
        Functions.createToastMessage(getString(R.string.code_removed_help_text), this, false).show();
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
