package edu.nyu.ds.moviedbgatherer.rest.interfaces;

import edu.nyu.ds.moviedbgatherer.rest.pojo.DiscoverMovieResult;
import edu.nyu.ds.moviedbgatherer.rest.pojo.Movie;
import edu.nyu.ds.moviedbgatherer.rest.pojo.SearchedMovieResults;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Created by Ramandeep Singh on 04-11-2015.
 */
public interface MovieDBService {
    @GET("/discover/movie")
    public DiscoverMovieResult discoverMovies(@QueryMap Map requestParams);

    @GET("/search/movie")
    public SearchedMovieResults searchMovies(@QueryMap Map requestParams);

    @GET("/movie/{id}")
    public Movie getMovieDetails(@Path("id") long id,@QueryMap Map requestParams);


}
