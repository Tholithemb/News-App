package com.southaficannewsapp.navfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.southaficannewsapp.R;
import com.southaficannewsapp.adapter.MainNews;
import com.southaficannewsapp.model.ArticleModel;
import com.southaficannewsapp.model.ResponseModel;
import com.southaficannewsapp.rests.APIInterface;
import com.southaficannewsapp.rests.ApiClient;
import com.southaficannewsapp.util.NetworkConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String API_KEY ="0104a52950a84df093a27b4ed40c190d";
    private ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout refreshLayout;
    private Button business,entertain,health,science,sport,tech;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        shimmerFrameLayout =rootView.findViewById(R.id.shimmer_layout);
        refreshLayout =rootView.findViewById(R.id.swipe_refresh);
        recyclerView = rootView.findViewById(R.id.recyclerMain);

        business =rootView.findViewById(R.id.business);
        entertain =rootView.findViewById(R.id.entertain);
        health =rootView.findViewById(R.id.health);
        science =rootView.findViewById(R.id.science);
        sport = rootView.findViewById(R.id.sport);
        tech =rootView.findViewById(R.id.tech);
        ImageView imageView = rootView.findViewById(R.id.developer);

        recyclerView.setHasFixedSize(true);

        final NavController navController = Navigation.findNavController(rootView);


        NetworkConnection connection =new NetworkConnection(getContext());

        loadRetrofit();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRetrofit();
                refreshLayout.setRefreshing(false);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Developed by Mngomezulu Siyanda", Toast.LENGTH_SHORT).show();
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_businessFragment);
            }
        });
        entertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_entertainmentFragment);
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_healthFragment);
            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_scienceFragment);
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_sportsFragment);
            }
        });
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newsFeedFragment_to_technologyFragment);
            }
        });


    }

    private void loadRetrofit() {
        try {

            final APIInterface apiService = ApiClient.getClient(getContext()).create(APIInterface.class);
            Call<ResponseModel> call = apiService.getSAHeadlines("za", API_KEY);

            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("ok")) {
                        List<ArticleModel> trendingModel = response.body().getArticles();
                        if (trendingModel.size() > 0) {

                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            final MainNews mainNews= new MainNews(trendingModel, getContext());
                            recyclerView.setAdapter(mainNews);
                            mainNews.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("out", t.toString());
                }
            });
        } catch (Exception exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();


    }

}