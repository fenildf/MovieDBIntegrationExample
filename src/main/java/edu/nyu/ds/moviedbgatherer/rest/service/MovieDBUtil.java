package edu.nyu.ds.moviedbgatherer.rest.service;

import edu.nyu.ds.moviedbgatherer.rest.pojo.DiscoverMovieResult;
import edu.nyu.ds.moviedbgatherer.rest.pojo.Movie;
import edu.nyu.ds.moviedbgatherer.rest.pojo.SearchedMovieResults;
import edu.nyu.ds.moviedbgatherer.rest.interfaces.MovieDBService;
import retrofit.RestAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Ramandeep Singh on 04-11-2015.
 */
public class MovieDBUtil {
    private static final ResourceBundle rb;
    private static final String api_key;
    private static final String endPoint;
    private static final RestAdapter restAdapter;
    private static final MovieDBService movieDBService;
    static {
        rb=ResourceBundle.getBundle("app");
        api_key=rb.getString("api_key");
        endPoint=rb.getString("service_endpoint_moviedb");
        String logLevel=rb.getString("log_level_retrofit");
        restAdapter=new RestAdapter.Builder().setEndpoint(endPoint).setLogLevel(RestAdapter.LogLevel.valueOf(logLevel)).build();
        movieDBService =restAdapter.create(MovieDBService.class);
    }
    public DiscoverMovieResult discoverMovies(Map requestMap){
        requestMap.put("api_key",api_key);
       return movieDBService.discoverMovies(requestMap);

    }
    public SearchedMovieResults searchMovies(Map requestMap){
        requestMap.put("api_key",api_key);
        return movieDBService.searchMovies(requestMap);

    }
    public Movie getMovieDetails(long movieId){
        Map requestMap=new HashMap<>();
        requestMap.put("api_key",api_key);
        return movieDBService.getMovieDetails(movieId,requestMap);

    }




}
