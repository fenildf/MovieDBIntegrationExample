package edu.nyu.ds.moviedbgatherer.rest.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramandeep Singh on 04-11-2015.
 */
public class DiscoverMovieResult {

    private List<SearchedMovie> results;

    public List<SearchedMovie> getResults() {
        return results;
    }

    public void setResults(List<SearchedMovie> results) {
        this.results = results;
    }
    public void initDiscoverMovieResult(){
        results=new ArrayList<SearchedMovie>();
    }
}
