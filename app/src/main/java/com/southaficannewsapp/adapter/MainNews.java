package com.southaficannewsapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.southaficannewsapp.R;
import com.southaficannewsapp.activities.WebViewActivity;
import com.southaficannewsapp.model.ArticleModel;
import com.southaficannewsapp.util.DateUtils;
import com.southaficannewsapp.util.OnRecyclerViewClickListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainNews extends RecyclerView.Adapter<MainNews.NewsSliderViewHolder> {

    private List<ArticleModel> articleModel;
    private Context context;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;

    public MainNews(List<ArticleModel> articleModel,Context context) {
        this.articleModel = articleModel;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsSliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_item_container, parent,
                        false
                )
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsSliderViewHolder holder, int position) {
        ArticleModel model =articleModel.get(position);

        if (!TextUtils.isEmpty(model.getPublishedAt())) {
            holder.time.setText(" \u2022 " + DateUtils.DateToTimeFormat(model.getPublishedAt()));
        }
        if (!TextUtils.isEmpty(model.getTitle())){
            holder.title.setText(model.getTitle());
        }
        if (!TextUtils.isEmpty(model.getSource().getName())){
            holder.source.setText(model.getSource().getName());
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.rootLayout:
                        if (!TextUtils.isEmpty(model.getUrl())) {
                            Log.e("clicked url", model.getUrl());
                            Intent webActivity = new Intent(context, WebViewActivity.class);
                            webActivity.putExtra("url", model.getUrl());
                            context.startActivity(webActivity);
                        }
                        break;
                }
            }
        });

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .error(R.drawable.ic_error)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return articleModel.size();
    }

     class NewsSliderViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView title, source, time;
        LinearLayout rootLayout;

        public NewsSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageUrl);
            title = itemView.findViewById(R.id.title);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.dateTime);
            rootLayout =itemView.findViewById(R.id.rootLayout);



        }
    }

}
