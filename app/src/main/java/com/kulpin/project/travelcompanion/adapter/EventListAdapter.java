package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.EventContentActivity;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.EventDTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    static private List<EventDTO> list;
    private Activity activity;

    public EventListAdapter(List<EventDTO> data, Activity activity) {
        this.list = data;
        this.activity = activity;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventDTO item = list.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(item.getEventDate()));
        holder.textPlace.setText(item.getPlace());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private CardView cardView;
        private TextView textTitle;
        private TextView textDate;
        private TextView textPlace;
        private Activity activity;

        public EventViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textTitle = (TextView)itemView.findViewById(R.id.textTitle);
            textDate = (TextView)itemView.findViewById(R.id.textDate);
            textPlace = (TextView)itemView.findViewById(R.id.textPlace);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventContentActivity.start(activity, list.get(getAdapterPosition()).getId());
        }

    }
}
