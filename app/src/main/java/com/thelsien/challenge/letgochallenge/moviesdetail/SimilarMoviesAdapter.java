package com.thelsien.challenge.letgochallenge.moviesdetail;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.models.MovieDetailModel;
import com.thelsien.challenge.letgochallenge.models.MovieRowModel;

import java.util.ArrayList;
import java.util.List;

class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.ViewHolder> {

    private OnSimilarMovieClickedListener mListener;
    private List<MovieRowModel> mMovies = new ArrayList<>();

    public SimilarMoviesAdapter(OnSimilarMovieClickedListener listener) {
        this.mListener = listener;
    }

    public void addCurrentMovie(MovieDetailModel currentMovieWithDetails) {
        MovieRowModel currentMovie = new MovieRowModel();

        currentMovie.id = currentMovieWithDetails.id;
        currentMovie.poster_path = currentMovieWithDetails.poster_path;
        currentMovie.title = currentMovieWithDetails.title;
        currentMovie.vote_average = currentMovieWithDetails.vote_average;

        mMovies.add(currentMovie);
        notifyDataSetChanged();
    }


    public void addMovies(List<MovieRowModel> movies) {
        int startPos = mMovies.size();
        mMovies.addAll(movies);

        notifyItemRangeChanged(startPos, mMovies.size() - startPos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_similar_movie, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieRowModel model = mMovies.get(position);

        Glide.with(holder.posterView)
                .load(MovieDbApiService.IMAGE_BASE_URL + model.poster_path)
                .apply(new RequestOptions()
                        .error(R.drawable.default_poster)
                        .placeholder(R.drawable.default_poster))
                .into(holder.posterView);

        ViewCompat.setTransitionName(holder.posterView, "similar_list_" + model.id);

        holder.posterView.setOnClickListener(view -> mListener.onSimilarMovieClicked(model, holder.posterView));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView posterView;

        public ViewHolder(View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }

    public interface OnSimilarMovieClickedListener {
        void onSimilarMovieClicked(MovieRowModel movie, View sharedView);
    }
}
