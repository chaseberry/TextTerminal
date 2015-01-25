package chase.csh.edu.textterminal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chase.csh.edu.textterminal.Managers.PhoneListManager;
import chase.csh.edu.textterminal.R;

/**
 * Created by chase on 12/21/14.
 */
public class PhoneListActivityAdapter extends RecyclerView.Adapter {

    private PhoneListManager.ListType type;
    private Context context;


    public PhoneListActivityAdapter(PhoneListManager.ListType items, Context context) {
        type = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_list_activity_item_layout, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((Holder) viewHolder).setNumberText(PhoneListManager.getNumberManager(type).getNumber(position).formatNumber());
        ((Holder) viewHolder).setTagViewText(PhoneListManager.getNumberManager(type).getNumber(position).getTag());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return PhoneListManager.getNumberManager(type).size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView number;
        TextView tagView;

        public Holder(View parentView) {
            super(parentView);
            number = (TextView) parentView.findViewById(R.id.phone_list_activity_list_item_number);
            tagView = (TextView) parentView.findViewById(R.id.phone_list_activity_list_item_tag);
        }

        public void setNumberText(String text) {
            number.setText(text);
        }

        public void setTagViewText(String text) {
            tagView.setText(text);
        }
    }
}
