package edu.nyu.ds.moviedbgatherer.rest.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ramandeep Singh on 04-11-2015.
 */
public class Movie {
    private long id;
    private List<Genre> genres;
    private String budget;
    private String status;
    private String title;
    @SerializedName("vote_average")
    private double voteAvg;
    @SerializedName("vote_count")
    private int voteCount;
    private String revenue;
    @SerializedName("release_date")
    private String releaseDate;
    private String popularity;


    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", genres=" + genres +
                ", budget=" + budget +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", voteAvg=" + voteAvg +
                ", voteCount=" + voteCount +
                ", revenue=" + revenue +
                ", releaseDate=" + releaseDate +
                '}';
    }



    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(double voteAvg) {
        this.voteAvg = voteAvg;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }
}
