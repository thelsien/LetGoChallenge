package com.thelsien.challenge.letgochallenge.movielist;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.models.MovieRowModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<MovieRowModel> movies = new ArrayList<>();
    private OnMovieClickListener mClickListener;

    public void addMovies(List<MovieRowModel> movies) {
        int startPos = this.movies.size();
        this.movies.addAll(movies);

        notifyItemRangeChanged(startPos, this.movies.size() - startPos);
    }

    public MovieListAdapter(OnMovieClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_movie, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieRowModel model = movies.get(position);

        Glide.with(holder.mPosterView)
                .load(MovieDbApiService.IMAGE_BASE_URL + model.poster_path)
                .apply(new RequestOptions()
                        .dontTransform()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.default_poster)
                        .error(R.drawable.default_poster)
                        .centerCrop())
                .into(holder.mPosterView);

        ViewCompat.setTransitionName(holder.mPosterView, "top_rated_" + model.id);

        holder.mRateView.setText(String.valueOf(model.vote_average));
        holder.mTitleView.setText(model.title);
        holder.itemView.setOnClickListener(view -> mClickListener.onMovieClicked(model, holder.mPosterView));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleView;
        public TextView mRateView;
        public ImageView mPosterView;

        ViewHolder(View itemView) {
            super(itemView);

            mTitleView = itemView.findViewById(R.id.tv_movie_title);
            mRateView = itemView.findViewById(R.id.tv_movie_rating);
            mPosterView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClicked(MovieRowModel movie, View sharedElement);
    }
}
