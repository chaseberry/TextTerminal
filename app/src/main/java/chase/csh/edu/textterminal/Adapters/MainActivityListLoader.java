package chase.csh.edu.textterminal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import chase.csh.edu.textterminal.Command.Command;
import chase.csh.edu.textterminal.Functions;
import chase.csh.edu.textterminal.R;


public class MainActivityListLoader implements ExpandableListAdapter {

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

    public int getCount() {
        return commands.size();
    }

    public Object getItem(int i) {
        return commands.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

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
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return getCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getItem(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getItemId(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getView(groupPosition, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        System.out.println("Expanded: " + commands.get(groupPosition).getName());
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        System.out.println("Collopsed: " + commands.get(groupPosition).getName());
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return childId;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

    static class Holder {
        TextView name;
        Switch enabled;
        TextView description;
        ImageView icon;
    }

}
