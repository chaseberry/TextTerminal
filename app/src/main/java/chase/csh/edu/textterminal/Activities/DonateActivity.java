package chase.csh.edu.textterminal.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import chase.csh.edu.textterminal.Adapters.DonateActivityGridAdapter;
import chase.csh.edu.textterminal.R;

public class DonateActivity extends TextTerminalActivity {

    BillingProcessor billingProcessor;

    BillingProcessor.IBillingHandler billingHandler = new BillingProcessor.IBillingHandler() {
        @Override
        public void onProductPurchased(String s, TransactionDetails transactionDetails) {

        }

        @Override
        public void onPurchaseHistoryRestored() {

        }

        @Override
        public void onBillingError(int i, Throwable throwable) {

        }

        @Override
        public void onBillingInitialized() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.donate_activity_progress_bar).setVisibility(View.GONE);
                    findViewById(R.id.donate_activity_grid_view).setVisibility(View.VISIBLE);
                }
            });
        }
    };

    GridView grid;

    Element[] elements = {new Element("one", "$1"), new Element("ten", "$10"),
            new Element("five", "$5"), new Element("twenty", "$20")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_activity_layout);
        grid = ((GridView) findViewById(R.id.donate_activity_grid_view));
        billingProcessor = new BillingProcessor(this, getString(R.string.billing_api_key), billingHandler);
        grid.setAdapter(new DonateActivityGridAdapter(this, elements));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(elements[position].id);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingProcessor.release();
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

    public static class Element {
        public String id;
        public String display;

        public Element(String i, String d) {
            id = i;
            display = d;
        }
    }

}
