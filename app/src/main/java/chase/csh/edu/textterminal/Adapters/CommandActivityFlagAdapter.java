package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import chase.csh.edu.textterminal.Command.CommandFlag;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 2/9/15.
 */
public class CommandActivityFlagAdapter extends BaseAdapter {

    ArrayList<CommandFlag> flags;
    Context parent;
    LayoutInflater inflater;

    public CommandActivityFlagAdapter(Context context, ArrayList<CommandFlag> flags) {
        this.flags = flags;
        parent = context;
        inflater = ((Activity) context).getLayoutInflater();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FlagViewHolder viewHolder;
        final CommandFlag flag = flags.get(position);
        if (convertView == null) {
            viewHolder = new FlagViewHolder();
            convertView = inflater.inflate(R.layout.command_activity_flag_item_layout, null);
            viewHolder.enabled = (Switch) convertView.findViewById(R.id.command_activity_flag_item_switch);
            viewHolder.title = (TextView) convertView.findViewById(R.id.command_activity_flag_item_title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.command_activity_flag_item_name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.command_activity_flag_item_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FlagViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(flag.getFlag());
        viewHolder.description.setText(flag.getFlagDescription());
        viewHolder.name.setText(flag.getFlagName());
        viewHolder.enabled.setOnCheckedChangeListener(null);
        viewHolder.enabled.setChecked(flag.isFlagEnabled());
        viewHolder.enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag.setFlagEnabled(isChecked);
            }
        });

        return null;
    }

    static class FlagViewHolder {
        Switch enabled;
        TextView title;
        TextView name;
        TextView description;
    }

}
