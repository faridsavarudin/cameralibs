package com.test.cameraandvideo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.cameraandvideo.R;
import com.test.cameraandvideo.options.AspectRatioItem;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    List<AspectRatioItem> containers;
    private final OnItemClickListener listener;


    public SizeAdapter(List<AspectRatioItem> listdata, OnItemClickListener listener) {
        containers = listdata;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_size, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(containers.get(position).getText());
        holder.imgIcon.setImageResource(containers.get(position).getImg());

        holder.imgIcon.setOnClickListener(v->{
            listener.onItemClick(containers.get(position), position);
        });
    }


    @Override
    public int getItemCount() {
        return containers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.txt_name);
            this.imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AspectRatioItem item, int position);
    }
}
