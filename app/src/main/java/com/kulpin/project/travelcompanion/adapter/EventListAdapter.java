package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.EventContentActivity;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.Constants;
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

        /*holder.textTitle.setText(item.getTitle());
        holder.textDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(item.getStartDate()));
        holder.textPlace.setText(item.getPlace());*/
        setContent(holder.cardView, item);
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

    private void setContent(View view, EventDTO event){
        switch (event.getType()){
            case Constants.EventType.TYPE_PLANE:
                setContentMove(view, event);
                break;
            case Constants.EventType.TYPE_TRAIN:
                setContentMove(view, event);
                break;
            case Constants.EventType.TYPE_BUS:
                setContentMove(view, event);
                break;
            case Constants.EventType.TYPE_MUSEUM:
                setContentLocal(view, event);
                break;
            case Constants.EventType.TYPE_CINEMA:
                setContentLocal(view, event);
                break;
        }
    }

    private void setContentMove(View view, EventDTO event){
        View viewContent = LayoutInflater.from(activity).inflate(R.layout.list_item_content_move, (RelativeLayout)view.findViewById(R.id.list_content_container), true);
        ((TextView) viewContent.findViewById(R.id.list_content_text_title)).setText(event.getTitle());
        ((TextView) viewContent.findViewById(R.id.list_content_text_place_departure)).setText(event.getDeparturePlace());
        ((TextView) viewContent.findViewById(R.id.list_content_text_time_departure)).setText((new SimpleDateFormat("HH:mm")).format(event.getStartTime().getTime()));
        ((TextView) viewContent.findViewById(R.id.list_content_text_date_departure)).setText((new SimpleDateFormat("dd.MM.yy")).format(event.getStartDate().getTime()));
        ((TextView) viewContent.findViewById(R.id.list_content_text_place_destination)).setText(event.getDestinationPlace());
        ((TextView) viewContent.findViewById(R.id.list_content_text_time_destination)).setText((new SimpleDateFormat("HH:mm")).format(event.getEndDate().getTime()));
        ((TextView) viewContent.findViewById(R.id.list_content_text_date_destination)).setText((new SimpleDateFormat("dd.MM.yy")).format(event.getEndDate().getTime()));
        setContentIcon(viewContent, event);
    }

    private void setContentLocal(View view, EventDTO event){
        View viewContent = LayoutInflater.from(activity).inflate(R.layout.list_item_content_local, (RelativeLayout)view.findViewById(R.id.list_content_container), true);
        ((TextView) viewContent.findViewById(R.id.list_content_text_title)).setText(event.getTitle());
        ((TextView) viewContent.findViewById(R.id.list_content_text_place)).setText(event.getPlace());
        ((TextView) viewContent.findViewById(R.id.list_content_text_time_start)).setText((new SimpleDateFormat("HH:mm")).format(event.getStartTime().getTime()));
        ((TextView) viewContent.findViewById(R.id.list_content_text_time_end)).setText((new SimpleDateFormat("HH:mm")).format(event.getEndTime().getTime()));
        ((TextView) viewContent.findViewById(R.id.list_content_text_date)).setText((new SimpleDateFormat("dd.MM.yy")).format(event.getStartDate().getTime()));
        setContentIcon(viewContent, event);
    }

    private void setContentIcon(View viewContent, EventDTO event){
        switch (event.getType()){
            case Constants.EventType.TYPE_PLANE:
                ((ImageView) viewContent.findViewById(R.id.list_content_icon)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_airplane_black_24dp));
                break;
            case Constants.EventType.TYPE_TRAIN:
                ((ImageView) viewContent.findViewById(R.id.list_content_icon)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_train_black_24dp));
                break;
            case Constants.EventType.TYPE_BUS:
                ((ImageView) viewContent.findViewById(R.id.list_content_icon)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_bus_black_24dp));
                break;
            case Constants.EventType.TYPE_MUSEUM:
                ((ImageView) viewContent.findViewById(R.id.list_content_icon)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_hospital_building_black_24dp));
                break;
            case Constants.EventType.TYPE_CINEMA:
                ((ImageView) viewContent.findViewById(R.id.list_content_icon)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_filmstrip_black_24dp));
                break;
        }
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
            //textTitle = (TextView)itemView.findViewById(R.id.textTitle);
            //textDate = (TextView)itemView.findViewById(R.id.textDate);
            //textPlace = (TextView)itemView.findViewById(R.id.textPlace);
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
            Bitmap bitmap = GalleryUtilities.decodeBitmapFromResource(photoList.get(0).getPhotoPath(), galleryUtilities.getScreenWidth(), galleryUtilities.getScreenWidth());
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            EventContentActivity.start(activity, list.get(getAdapterPosition()).getId(), 0);
        }

    }
}
