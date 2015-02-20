package chase.csh.edu.textterminal.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import chase.csh.edu.textterminal.Adapters.CommandActivityFlagAdapter;
import chase.csh.edu.textterminal.Adapters.CommandActivityParameterAdapter;
import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.R;

public class CommandActivity extends TextTerminalActivity {

    private LinearLayout flagListView, parameterListView, extraListView;
    private Command command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_activity_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flagListView = (LinearLayout) findViewById(R.id.command_activity_flag_list_view);
        parameterListView = (LinearLayout) findViewById(R.id.command_activity_parameters_list_view);
        extraListView = (LinearLayout) findViewById(R.id.command_activity_extras_list_view);
        command = Functions.loadCommand(getIntent().getStringExtra("command"), this, null, null);

        if (command == null) {
            //Uh.. I don't want to know how this happened
            Functions.createToastMessage("Failed to load command.", this, true).show();
            finish();
        }
        loadFlags();
        loadParameters();
    }

    @Override
    protected void onPause() {
        super.onPause();
        command.save();
    }

    private void loadFlags() {
        if (command.getCommandFlags() == null || command.getCommandFlags().size() == 0) {
            return;
        }
        CommandActivityFlagAdapter flagAdapter = new CommandActivityFlagAdapter(CommandActivity.this, command.getCommandFlags());
        flagListView.removeAllViews();
        for (int z = 0; z < flagAdapter.getCount(); z++) {
            View v = flagAdapter.getView(z, null, null);
            if (v != null) {
                flagListView.addView(v);
            }
        }
    }

    private void loadParameters() {
        if (command.getParams() == null || command.getParams().length == 0) {
            return;
        }
        CommandActivityParameterAdapter parameterAdapter = new CommandActivityParameterAdapter(CommandActivity.this, command.getParams());
        parameterListView.removeAllViews();
        for (int z = 0; z < parameterAdapter.getCount(); z++) {
            View v = parameterAdapter.getView(z, null, null);
            if (v != null) {
                parameterListView.addView(v);
            }
        }
    }

}
