package edu.csh.chase.textterminal.Adapters;

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

import chase.csh.edu.textterminal.R;
import edu.csh.chase.textterminal.Command.Command;
import edu.csh.chase.textterminal.Functions;


public class MainActivityListLoader extends BaseAdapter {

    ArrayList<Command> commands = new ArrayList<Command>();
    LayoutInflater inflater;
    Context parent;

    public MainActivityListLoader(Context c, ArrayList<String> classNames) {
        inflater = ((Activity) c).getLayoutInflater();
        parent = c;
        for (String s : classNames) {
            Command command = Functions.loadCommand(s, c, null, null);
            if (command != null) {
                commands.add(command);
            }
        }

    }

    @Override
    public int getCount() {
        return commands.size();
    }

    @Override
    public Object getItem(int i) {
        return commands.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        final Command command = commands.get(i);
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.main_activity_list_item_layout, null);
            holder.commandLayout = (RelativeLayout) view.findViewById(R.id.command);
            holder.commandChildLayout = (LinearLayout) view.findViewById(R.id.command_child);
            holder.name = (TextView) view.findViewById(R.id.main_activity_list_view_item_name);
            holder.enabled = (Switch) view.findViewById(R.id.main_activity_list_view_item_switch);
            holder.description = (TextView) view.findViewById(R.id.main_activity_list_view_item_description);
            holder.icon = (ImageView) view.findViewById(R.id.main_activity_list_view_item_icon);
            holder.parameters = (LinearLayout) view.findViewById(R.id.command_child_parameters);
            holder.flags = (LinearLayout) view.findViewById(R.id.command_child_flags);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.commandLayout.setOnClickListener(null);
        holder.description.setText(command.getHelpMessage());
        holder.name.setText(command.getName());
        holder.icon.setImageResource(command.getIconId());
        holder.enabled.setOnCheckedChangeListener(null);
        holder.enabled.setChecked(command.isEnabled());
        holder.enabled.setTag(command);
        holder.enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Command c = ((Command) compoundButton.getTag());
                c.setEnabled(b);
                c.save();
            }
        });
        if (holder.commandChildLayout.getVisibility() == View.VISIBLE) {

            String[] params = command.getParams();
            if (params != null && params.length > 0) {
                holder.parameters.removeAllViews();
                holder.parameters.setVisibility(View.VISIBLE);
                CommandParameterAdapter parameterLoader = new CommandParameterAdapter(params, parent);
                for (int z = 0; z < parameterLoader.getCount(); z++) {
                    View v = parameterLoader.getView(z, null, null);
                    if (v != null) {
                        holder.parameters.addView(v);
                    }
                }
            } else {
                holder.parameters.setVisibility(View.GONE);
            }

            if (command.getCommandFlags() != null && command.getCommandFlags().size() > 0) {
                holder.flags.removeAllViews();
                holder.flags.setVisibility(View.VISIBLE);
                CommandFlagAdapter flagLoader = new CommandFlagAdapter(command, parent);
                for (int z = 0; z < flagLoader.getCount(); z++) {
                    View v = flagLoader.getView(z, null, null);
                    if (v != null) {
                        holder.flags.addView(v);
                    }
                }
            } else {
                holder.flags.setVisibility(View.GONE);
            }

        }
        return view;
    }

    static class Holder {
        TextView name;
        Switch enabled;
        TextView description;
        ImageView icon;
        LinearLayout parameters;
        RelativeLayout commandLayout;
        LinearLayout commandChildLayout;
        LinearLayout flags;
    }

}
