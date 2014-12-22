package chase.csh.edu.textterminal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by chase on 12/21/14.
 */
public class PhoneListActivityAdapter extends BaseAdapter {

    private JSONArray numbers;
    private Context context;

    public PhoneListActivityAdapter(JSONArray items, Context context) {
        numbers = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numbers.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return numbers.get(position);
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;
        String number;
        try {
            number = numbers.getString(position);
        } catch (JSONException e) {
            number = "Failed";
        }
        if (view == null) {
            holder = new Holder();
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.phone_list_activity_item_layout, null);
            holder.number = (TextView) view.findViewById(R.id.phone_list_activity_list_item_number);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.number.setText(number);
        return view;
    }

    static class Holder {
        TextView number;
    }
}
