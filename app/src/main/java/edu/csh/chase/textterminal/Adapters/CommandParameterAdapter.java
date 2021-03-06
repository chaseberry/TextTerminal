package edu.csh.chase.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.csh.chase.textterminal.R;

public class CommandParameterAdapter extends BaseAdapter {

    private String[] parameters;
    private LayoutInflater inflater;

    public CommandParameterAdapter(String[] params, Context c) {
        parameters = params;
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
        String param = parameters[position];
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.command_parameter_text_view, parent);
        }
        ((TextView) convertView).setText(param);
        return convertView;
    }
}
