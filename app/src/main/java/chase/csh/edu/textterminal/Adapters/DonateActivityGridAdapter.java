package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chase.csh.edu.textterminal.Activities.DonateActivity;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 4/16/15.
 */
public class DonateActivityGridAdapter extends BaseAdapter {

    DonateActivity.Element[] elements;

    Context parent;

    public DonateActivityGridAdapter(Context c, DonateActivity.Element[] ele) {
        parent = c;
        elements = ele;
    }

    @Override
    public int getCount() {
        return elements.length;
    }

    @Override
    public Object getItem(int position) {
        return elements[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            LayoutInflater inflator = ((Activity) this.parent).getLayoutInflater();
            view = inflator.inflate(R.layout.activity_donate_grid_element, null);
            holder.display = (TextView) view.findViewById(R.id.activity_donate_grid_element_textview);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.display.setText(elements[position].display);

        return view;
    }

    static class Holder {
        TextView display;
    }


}
