package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 2/20/15.
 */
public class CommandActivityParameterAdapter extends BaseAdapter {

    private String[] parameters;
    private LayoutInflater inflater;

    public CommandActivityParameterAdapter(Context c, String[] param) {
        parameters = param;
        inflater = ((Activity) c).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return parameters.length;
    }

    @Override
    public Object getItem(int position) {
        return parameters[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String parameter = parameters[position];
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.command_activity_parameter_textview_layout, parent);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.command_activity_parameters_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(parameter);

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

}
