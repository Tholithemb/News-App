package com.southaficannewsapp.categoryfragments;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.southaficannewsapp.R;
import com.southaficannewsapp.adapter.HeadlinesNewsAdapter;
import com.southaficannewsapp.model.ResponseModel;
import com.southaficannewsapp.model.ArticleModel;
import com.southaficannewsapp.rests.APIInterface;
import com.southaficannewsapp.rests.ApiClient;
import com.southaficannewsapp.util.NetworkConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessFragment extends Fragment{

    private RecyclerView recyclerView;
    private static final String API_KEY ="0104a52950a84df093a27b4ed40c190d";
    private ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //final  NavController navController = Navigation.findNavController(view);

        shimmerFrameLayout =view.findViewById(R.id.shimmer_layout);
        refreshLayout =view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.businessRecyclerView);
        ImageView back  = view.findViewById(R.id.back);



        final NavController navController = Navigation.findNavController(view);


        recyclerView.setHasFixedSize(true);
        NetworkConnection connection =new NetworkConnection(getContext());
        loadRetrofit();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRetrofit();
                refreshLayout.setRefreshing(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_businessFragment_to_newsFeedFragment);
            }
        });

    }

    private void loadRetrofit(){
        try {

            final APIInterface apiService = ApiClient.getClient(getContext()).create(APIInterface.class);
            Call<ResponseModel> call = apiService.getLatestNews("za", "business", API_KEY);

            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getStatus().equals("ok")) {
                        List<ArticleModel> trendingModel = response.body().getArticles();
                        if (trendingModel.size() > 0) {


                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);


                            final HeadlinesNewsAdapter trendingNewsAdapter = new HeadlinesNewsAdapter(trendingModel, getContext());
                            recyclerView.setAdapter(trendingNewsAdapter);
                            trendingNewsAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("out", t.toString());
                }
            });
        }catch (Exception exception){
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