package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Command.CommandFlag;
import chase.csh.edu.textterminal.R;

public class CommandFlagAdapter extends BaseAdapter {

    private ArrayList<CommandFlag> flags;
    private Context parent;
    private LayoutInflater inflater;
    private Command command;

    public CommandFlagAdapter(Command command, Context c) {
        command = command;
        parent = c;
        flags = command.getCommandFlags();
        inflater = ((Activity) c).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return flags.size();
    }

    @Override
    public Object getItem(int position) {
        return flags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Holder holder;
        final CommandFlag flag = flags.get(position);
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.command_flag_layout, null);
            holder.name = (TextView) view.findViewById(R.id.command_flag_name);
            holder.enabled = (Switch) view.findViewById(R.id.command_flag_enabled);
            holder.description = (TextView) view.findViewById(R.id.command_flag_descriptions);
            holder.icon = (TextView) view.findViewById(R.id.command_flag_image);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.enabled.setOnCheckedChangeListener(null);
        holder.name.setText(flag.getFlagName());
        holder.description.setText(flag.getFlagDescription());
        holder.enabled.setChecked(flag.isFlagEnabled());
        holder.icon.setText(flag.getFlag());
        holder.enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag.setFlagEnabled(b);
                command.save();
            }
        });
        return view;
    }


    static class Holder {
        TextView name;
        Switch enabled;
        TextView description;
        TextView icon;
    }

}
