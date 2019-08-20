package com.test.cameraandvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.model.ModelImages;

import java.util.List;

public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.ViewHolder> {

    Context context;
    List<ModelImages> modelImages;
    OnItemClickItem onItemClick;
    private int getPosition;

    public interface OnItemClickItem {
        public void itemClickItem(int position);
    }

    public GalleryItemAdapter(Context context, List<ModelImages> modelImages, int getPosition, OnItemClickItem onItemClick) {
        this.modelImages = modelImages;
        this.context = context;
        this.getPosition = getPosition;
        this.onItemClick = onItemClick;

    }

    public void setActivated(int position) {
       // modelImages.get(getPosition).getPathItem().get(position).setSelected(false);

        modelImages.get(getPosition).getPathItem().get(position).setSelected(true);
        notifyDataSetChanged();
    }

    public void setClearSelected() {
        for (int i = 0; i < modelImages.size(); i++) {
            modelImages.get(i).setSelectedItem(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_photosfolderitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load("file://" + modelImages.get(getPosition).getPathItem().get(position).getPath())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v ->
                onItemClick.itemClickItem(position));

        if (!modelImages.get(getPosition).getPathItem().get(position).isSelected()) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
        if (modelImages.get(getPosition).getPathItem().get(position).isSelected()) {
            holder.linearLayout.setBackground(context.getResources().getDrawable(R.drawable.bg_selector));
        }
    }


    @Override
    public int getItemCount() {
        return modelImages.get(getPosition).getPathItem().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFolder, tvFolderSize;
        private ImageView imageView;
        private LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_image);
        }
    }
}