package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.EventContentActivity;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.DBHelper;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    static private List<EventDTO> list;
    private Activity activity;
    private int selectedPosition;

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
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        //Log.d("tclog", "EventListAdapter.onBindViewHolder");
        EventDTO item = list.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(item.getEventDate()));
        holder.textPlace.setText(item.getPlace());
        holder.setImage(item.getId());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Log.d("tclog", "adapter position = " + holder.getAdapterPosition());
                setSelectedPosition(holder.getAdapterPosition());
                return false;
            }
        });
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

    public static class EventViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private CardView cardView;
        private TextView textTitle;
        private TextView textDate;
        private TextView textPlace;
        private ImageView imageView;
        private Activity activity;
        private DBHelper dbHelper;
        private List<Photo> photoList;
        private GalleryUtilities galleryUtilities;

        public EventViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textTitle = (TextView)itemView.findViewById(R.id.textTitle);
            textDate = (TextView)itemView.findViewById(R.id.textDate);
            textPlace = (TextView)itemView.findViewById(R.id.textPlace);
            imageView = (ImageView)itemView.findViewById(R.id.event_list_image);
            //imageView.setImageResource(R.drawable.image_1);
            itemView.setOnClickListener(this);
            activity.registerForContextMenu(itemView);
        }

        private void setImage(long eventId){
            dbHelper = new DBHelper(activity);
            galleryUtilities = new GalleryUtilities(activity);
            photoList = dbHelper.getPhotosByEventId(eventId);
            if (photoList.isEmpty()) {
                ViewGroup.LayoutParams params= imageView.getLayoutParams();
                params.height = 0;
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(null);
                return;
            } else {
                ViewGroup.LayoutParams params= imageView.getLayoutParams();
                params.height = (int)galleryUtilities.convertDIPtoPXL(150);
                imageView.setLayoutParams(params);
            }
            Bitmap bitmap = GalleryUtilities.decodeBitmapFromResource(photoList.get(0).getFilePath(), galleryUtilities.getScreenWidth(), galleryUtilities.getScreenWidth());
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            EventContentActivity.start(activity, list.get(getAdapterPosition()).getId());
        }

    }
}
