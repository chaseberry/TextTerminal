package chase.csh.edu.textterminal.Activities;

import android.os.Bundle;
import android.widget.ListView;

import chase.csh.edu.textterminal.R;

public class CommandActivity extends TextTerminalActivity {

    private ListView flagListView, parameterListView, extraListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_activity_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
