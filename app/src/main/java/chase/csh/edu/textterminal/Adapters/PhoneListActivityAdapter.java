package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import chase.csh.edu.textterminal.Managers.PhoneListManager;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/21/14.
 */
public class PhoneListActivityAdapter extends RecyclerView.Adapter {

    private PhoneListManager.ListType type;
    private Context context;


    public PhoneListActivityAdapter(PhoneListManager.ListType items, Context context) {
        type = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.phone_list_activity_item_layout, viewGroup, false);
        return new Holder((TextView) v, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((Holder) viewHolder).setText(PhoneListManager.getNumberManager(type).getNumber(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return PhoneListManager.getNumberManager(type).size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView number;
        GestureDetector detector;
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                System.out.println(velocityX + ":::" + velocityY);
                return false;
            }
        };

        public Holder(TextView itemView, Context context) {
            super(itemView);
            number = itemView;
            detector = new GestureDetector(context, gestureListener);
            number.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    detector.onTouchEvent(event);
                    return false;
                }
            });
        }

        public void setText(String text) {
            number.setText(text);
        }
    }
}
