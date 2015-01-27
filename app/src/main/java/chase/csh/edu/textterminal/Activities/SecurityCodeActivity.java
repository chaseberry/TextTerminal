package chase.csh.edu.textterminal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import chase.csh.edu.textterminal.Commands.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.R;
import chase.csh.edu.textterminal.Managers.SharedPrefManager;

/**
 * Created by chase on 12/3/14.
 */
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
            Functions.createMessageWithButton("Please fill all out fields", this).show();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            Functions.createMessageWithButton("Codes don't match!", this).show();
            return;
        }

        SharedPrefManager.saveString(Command.SECURITY_CODE_KEY, Functions.hashPassword(newPass));
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.security_code_activity_code_confirm).getWindowToken(), 0);
            imm.hideSoftInputFromWindow(findViewById(R.id.security_code_activity_code_new).getWindowToken(), 0);
        }
        Functions.createToastMessage("Code set", this, false).show();
    }

    public void clearSecurityCode(View view) {
        SharedPrefManager.saveString(Command.SECURITY_CODE_KEY, "");
        Functions.createToastMessage("Code Removed", this, false).show();
    }
}
