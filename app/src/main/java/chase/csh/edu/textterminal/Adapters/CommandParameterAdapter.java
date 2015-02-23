package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by chase on 2/23/15.
 */
public class CommandParameterAdapter extends BaseAdapter {

    private String[] parameters;
    private Context parent;
    private LayoutInflater inflater;

    public CommandParameterAdapter(String[] params, Context c) {
        parent = c;
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

        return null;
    }
}
