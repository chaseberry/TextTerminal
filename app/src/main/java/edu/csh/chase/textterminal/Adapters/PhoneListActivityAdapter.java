package edu.csh.chase.textterminal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.csh.chase.textterminal.Activities.PhoneListActivity;
import edu.csh.chase.textterminal.Managers.PhoneListManager;
import edu.csh.chase.textterminal.R;
import edu.csh.chase.textterminal.RecyclerViewSwipeListener;

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
        Holder holder = new Holder(view);
        RecyclerViewSwipeListener.bindListenerToView(view, holder, new RecyclerViewSwipeListener.Callback() {

            @Override
            public void viewSwiped(int viewPosition) {
                PhoneListManager.getNumberManager(type).removeNumber(viewPosition);
                notifyItemRemoved(viewPosition);
                PhoneListManager.getNumberManager(type).save();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(PhoneListActivity.UPDATEEMPTYVIEWINTENT));
            }
        });
        return holder;
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
