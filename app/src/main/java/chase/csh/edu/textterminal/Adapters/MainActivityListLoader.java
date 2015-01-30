package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.R;
import chase.csh.edu.textterminal.Receivers.SmsReceiver;


public class MainActivityListLoader extends BaseAdapter {

    ArrayList<Command> commands = new ArrayList<Command>();
    LayoutInflater inflater;

    public MainActivityListLoader(Context c, ArrayList<String> classNames) {
        inflater = ((Activity) c).getLayoutInflater();
        for (String s : classNames) {
            try {
                Class<?> commandClass = SmsReceiver.class.getClassLoader().loadClass(s);
                Constructor constructor = commandClass.getDeclaredConstructor(Context.class, String[].class, String.class);
                constructor.setAccessible(true);
                Command command = (Command) constructor.newInstance(c, null, null);
                commands.add(command);
            } catch (Exception e) {
                e.printStackTrace();
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
        Holder holder = new Holder();
        final Command command = commands.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.main_activity_list_item_layout, null);
            holder.name = (TextView) view.findViewById(R.id.main_activity_list_view_item_name);
            holder.enabled = (Switch) view.findViewById(R.id.main_activity_list_view_item_switch);
            holder.description = (TextView) view.findViewById(R.id.main_activity_list_view_item_description);
            holder.icon = (ImageView) view.findViewById(R.id.main_activity_list_view_item_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(command.getClass());
            }
        });
        return view;
    }

    static class Holder {
        TextView name;
        Switch enabled;
        TextView description;
        ImageView icon;
    }

}
