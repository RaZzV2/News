package com.example.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.classes.Notification;
import com.example.news.interfaces.OnItemClickListener;
import com.example.news.models.NewsModel.Article;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder2> {

    List<Notification> notificationList;
    OnItemClickListener onItemClickListener;

    Context context;


    public NotificationAdapter(List<Notification> notificationList, Context context, OnItemClickListener onItemClickListener) {
        this.notificationList = notificationList;
        this.context = context;
        this.onItemClickListener=onItemClickListener;
    }



    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder2(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holders, int position) {
        final MyViewHolder2 holder = holders;
        Notification model = notificationList.get(position);
        holder.publishedAt.setText(model.getDate());
        holder.description.setText(model.getBody());
        holder.title.setText(model.getTitle());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView description;
        TextView publishedAt;
        TextView title;
        OnItemClickListener onItemClickListener;


        public MyViewHolder2(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            description = itemView.findViewById(R.id.desc);
            title = itemView.findViewById(R.id.titleItem);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            this.onItemClickListener = onItemClickListener;
        }
    }

}
