package com.test.cameraandvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.model.ModelImages;

import java.util.List;

public class GalleryAdapter  extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    Context context;
    List<ModelImages> modelImages;
    OnItemClick onItemClick;
    boolean isActivated = false;
    ModelImages selectedModelImage = null;

    public interface OnItemClick {
        public void itemClick(int position);
    }

    public GalleryAdapter(Context context, List<ModelImages> modelImages, OnItemClick onItemClick) {
        this.modelImages =  modelImages;
        this.context = context;
        this.onItemClick = onItemClick;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.adapter_photosfolder, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public void setActivated(boolean activated, int position) {
        isActivated = activated;
        for (int i=0; i<modelImages.size(); i++){
            modelImages.get(i).setSelected(false);
        }
        modelImages.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvFolder.setText(modelImages.get(position).getFolder());

        Glide.with(context).load("file://" + modelImages.get(position).getPathItem().get(0).getPath())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v->{
            onItemClick.itemClick(position);
        });

        if (modelImages.get(position).isSelected()){
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
        }
        if (!modelImages.get(position).isSelected()){
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }

        holder.linearLayout.setOnClickListener(v->{
            onItemClick.itemClick(position);
        });
    }


    @Override
    public int getItemCount() {
        return modelImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFolder, tvFolderSize;
        private ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvFolder = (TextView) itemView.findViewById(R.id.tv_folder);
           // this.tvFolderSize = (TextView) itemView.findViewById(R.id.tv_folder2);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_image);
        }
    }
}