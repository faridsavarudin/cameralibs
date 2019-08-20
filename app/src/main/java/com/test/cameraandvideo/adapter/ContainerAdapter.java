package com.test.cameraandvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.cameraandvideo.model.Container;
import com.test.cameraandvideo.R;

import java.util.List;

public class ContainerAdapter  extends RecyclerView.Adapter<ContainerAdapter.ViewHolder>{
    List<Container> containers;
    private boolean isGray;
    private Context context;


    public ContainerAdapter(List<Container> listdata, Context context) {
        containers = listdata;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(containers.get(position).getName());
        if (isGray)
        holder.textView.setTextColor(context.getResources().getColor(R.color.colorGray2));
        else
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
    }

    public void setColorGray(boolean activated) {
        isGray = activated;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return containers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}