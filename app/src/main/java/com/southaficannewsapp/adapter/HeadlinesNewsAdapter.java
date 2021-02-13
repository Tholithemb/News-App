package com.southaficannewsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.southaficannewsapp.R;
import com.southaficannewsapp.activities.WebViewActivity;
import com.southaficannewsapp.model.ArticleModel;
import com.southaficannewsapp.util.DateUtils;

import java.util.List;

public class HeadlinesNewsAdapter extends RecyclerView.Adapter<HeadlinesNewsAdapter.TrendingViewHolder>{

    private List<ArticleModel> trendingModel;

    private Context context;

    public HeadlinesNewsAdapter(List<ArticleModel> trendingModel, Context context) {
        this.trendingModel = trendingModel;
        this.context =context;
    }


    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrendingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.news_item_container, parent,
                        false
                )
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {

        ArticleModel model =trendingModel.get(position);
        if (!TextUtils.isEmpty(model.getTitle())){
            holder.title.setText(model.getTitle());
        }
        if (!TextUtils.isEmpty(model.getSource().getName())){
            holder.source.setText(model.getSource().getName());
        }
        if (!TextUtils.isEmpty(model.getPublishedAt())){
            holder.time.setText(" \u2022 " + DateUtils.DateToTimeFormat(model.getPublishedAt()));
        }

            Glide.with(context)
                    .load(model.getUrlToImage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .error(R.drawable.ic_error)
                    .into(holder.imageView);

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.rootContainer:
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
    }

    @Override
    public int getItemCount() {
        return trendingModel.size();
    }

    static class TrendingViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView title, source, time;
        ConstraintLayout rootLayout;


        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.urlImage);
            title =itemView.findViewById(R.id.title);
            source =itemView.findViewById(R.id.source);
            time =itemView.findViewById(R.id.time);
            rootLayout =itemView.findViewById(R.id.rootContainer);

        }
    }

}
