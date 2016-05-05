package com.kulpin.project.travelcompanion.adapter;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.Document;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.io.File;
import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder>{
    static private ArrayList<Document> documentList;
    private Activity activity;
    private int selectedPosition;

    public DocumentListAdapter(ArrayList<Document> documentList, Activity activity) {
        this.documentList = documentList;
        this.activity = activity;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_list_item, parent, false);
        return new DocumentViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(final DocumentViewHolder holder, int position) {
        Document item = documentList.get(position);
        if (holder.textTitle == null) Log.d("tclog", "textTitle is null");
        holder.textTitle.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = documentList.get(holder.getAdapterPosition()).getFilePath();
                if (filePath == null) return;
                File file = new File(filePath);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.fromFile(file));
                activity.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setSelectedPosition(holder.getAdapterPosition());
                return false;
            }
        });
        activity.registerForContextMenu(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder{
        private Activity activity;
        private TextView textTitle;

        public DocumentViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            textTitle = (TextView) itemView.findViewById(R.id.document_title);
        }
    }
}
