package com.example.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.news.activities.WebViewActivity;
import com.example.news.models.NewsModel.Article;
import com.example.news.interfaces.OnItemClickListener;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    List<Article> articleList;
    Context context;
    OnItemClickListener onItemClickListener;

    public Adapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, onItemClickListener);
        view.setOnClickListener(viewHolder);
        return viewHolder;
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Article model = articleList.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getSource().getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(model.getSource().getTitle());
        holder.description.setText(model.getSource().getDescription());
        holder.source.setText(model.getSource().getSourceSite().getName());
        holder.time.setText("\u2022" + Utils.DateToTimeFormat(model.getSource().getPublishedAt()));
        holder.author.setText(model.getSource().getAuthor());
        holder.title.setTag(model.getSource().getUrl());


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, publishedAt, source, time, author;
        ImageView imageView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.titleItem);
            description = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.timeItem);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progressLoadPhoto);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            TextView title = v.findViewById(R.id.titleItem);
            String url = title.getTag().toString();
            //onItemClickListener.onItemClick(v, getBindingAdapterPosition());
            Intent intent = new Intent(v.getContext(), WebViewActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);

        }
    }
}
