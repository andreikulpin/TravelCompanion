package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.MainActivity;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.JourneyViewHolder>{

    private List<JourneyDTO> list;
    private Activity activity;
    private int selectedPosition;

    public JourneyListAdapter(List<JourneyDTO> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_list_item, parent, false);
        return new JourneyViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(final JourneyViewHolder holder, final int position) {
        JourneyDTO item = list.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textStartDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(item.getStartDate()) + " - ");
        holder.textEndDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(item.getEndDate()));


        /*This implemented not in JourneyViewHolder because
        * JourneyViewHolder after deleting cant hold onLongClick.
        * */
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("myLOG", "adapter position = " + holder.getAdapterPosition());
                setSelectedPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void onViewRecycled(JourneyViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public class JourneyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView cardView;
        private TextView textTitle;
        private TextView textStartDate;
        private TextView textEndDate;
        private Activity activity;

        public JourneyViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textTitle = (TextView)itemView.findViewById(R.id.textTitle);
            textStartDate = (TextView)itemView.findViewById(R.id.textStartDate);
            textEndDate = (TextView)itemView.findViewById(R.id.textEndDate);
            itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
            activity.registerForContextMenu(itemView);
        }

        @Override
        public void onClick(View v) {
            //((MainActivity)activity).onFragmentReplace(list.get(getAdapterPosition()).getId());
            ((MainActivity)activity).onReplaceFragment(getAdapterPosition(), list.get(getAdapterPosition()).getId());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

    }


}
