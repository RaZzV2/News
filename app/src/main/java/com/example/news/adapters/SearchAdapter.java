package com.example.news.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.news.R;
import com.example.news.interfaces.OnItemClickListener;
import com.example.news.models.NewsModel.Article;
import com.example.news.utils.Utils;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    List<Article> articleList;
    OnItemClickListener onItemClickListener;

    Context context;


    public SearchAdapter(List<Article> articleList, Context context, OnItemClickListener onItemClickListener) {
        this.articleList = articleList;
        this.context = context;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        //view.setOnClickListener(viewHolder);
        return new MyViewHolder(view, onItemClickListener);
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
        holder.publishedAt.setText((int) (model.getScore() * 100) + "%");
        holder.title.setText(model.getSource().getTitle());
        holder.description.setText(model.getSource().getDescription());
        holder.source.setText(model.getSource().getSourceSite().getName());
        holder.time.setText("\u2022" + Utils.DateToTimeFormat(model.getSource().getPublishedAt()));
        holder.author.setText(model.getSource().getAuthor());
        holder.title.setTag(model.getSource().getUrl());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
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
    }
}
