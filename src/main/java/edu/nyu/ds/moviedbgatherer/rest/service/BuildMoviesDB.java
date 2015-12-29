package edu.nyu.ds.moviedbgatherer.rest.service;

import edu.nyu.ds.moviedbgatherer.rest.pojo.DiscoverMovieResult;
import edu.nyu.ds.moviedbgatherer.rest.pojo.Movie;
import edu.nyu.ds.moviedbgatherer.rest.pojo.SearchedMovie;
import edu.nyu.ds.moviedbgatherer.rest.pojo.SearchedMovieResults;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileSystem;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ramandeep Singh on 04-11-2015.
 */
public class BuildMoviesDB {
    private static final MovieDBUtil movieUtil;
    private static final Logger logger= LoggerFactory.getLogger(BuildMoviesDB.class);
    private static final Scanner scanner;
    private static CSVPrinter csvPrinter=null;
    private static final CSVFormat csvFormat;
    static {
        movieUtil=new MovieDBUtil();
        scanner=new Scanner(System.in);
        csvFormat=CSVFormat.DEFAULT.withRecordSeparator("\r\n");
    }


    public static SearchedMovieResults handleSearchMovieByName(){
        String movieName = scanner.nextLine();
        logger.info("Searching for movie :{}", movieName);
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("query", movieName);
        SearchedMovieResults searchedMovieResults = movieUtil.searchMovies(requestMap);
        return searchedMovieResults;

    }
    public static DiscoverMovieResult handleDiscoverMovieResult(){
        Map<String, Object> requestMap = new HashMap<>();
        logger.info("Enter Year []: ");

        String yearValue = scanner.nextLine();
        if(yearValue!=null&&!yearValue.isEmpty()){
            requestMap.put("primary_release_year",Integer.parseInt(yearValue));
        }
        logger.info("Enter Vote Count threshold: this is the lower limit [0]:");
        String voteCountValue = scanner.nextLine();
        if(voteCountValue!=null&&!voteCountValue.isEmpty()){
            requestMap.put("vote_count.gte", Integer.parseInt(voteCountValue));
        }
        logger.info("Enter average rating threshold: lower limit [0]:");
        String lowRatingLimitValue= scanner.nextLine();
        if(lowRatingLimitValue!=null&&!lowRatingLimitValue.isEmpty()){
            requestMap.put("vote_average.gte", Integer.parseInt(lowRatingLimitValue));
        }

        logger.info("Enter average rating threshold: Higher limit [10]:");
        String highLimitValue=scanner.nextLine();
        if(highLimitValue!=null&&!highLimitValue.isEmpty()){
            requestMap.put("vote_average.lte", Integer.parseInt(highLimitValue));
        }
        logger.info("Enter pages to retrieve: 20 movies per page[5]:");
        String pageSize=scanner.nextLine();
        int pageSizeValue=5;
        if(pageSize!=null&&!pageSize.isEmpty()){
            pageSizeValue=Integer.parseInt(pageSize);
        }


//                logger.info("Enter Keywords separate them by comma (,):[]:");
//                String keywords=scanner.nextLine();
//                if(keywords!=null&&!keywords.isEmpty()){
//                    requestMap.put("keywords", keywords);
//                }
           DiscoverMovieResult discoverMovieResult=new DiscoverMovieResult();
           discoverMovieResult.initDiscoverMovieResult();
           for(int i=1;i<=pageSizeValue;i++){
               requestMap.put("page",i);
               DiscoverMovieResult  tempDiscoverMovieResult = movieUtil.discoverMovies(requestMap);
               discoverMovieResult.getResults().addAll(tempDiscoverMovieResult.getResults());
           }
           return discoverMovieResult;

    }

    /**
     * This method does rate limited fetch of MovieDB API
     * It uses RXJava for achieving rate limiting
     * @param searchedMovies
     * @param saveFile
     * @param file
     * @return
     */
    public static List<Movie> invokeRateLimitedFetch(List<SearchedMovie> searchedMovies,boolean saveFile, File file){
        List<Movie> movies=new ArrayList<>();

           Observable.zip(Observable.from(searchedMovies),
                   Observable.interval(2, TimeUnit.SECONDS), (obs,timer)->obs)
                   .doOnNext(item -> {
                       Movie movie = movieUtil.getMovieDetails(item.getId());
                       movies.add(movie);
                       if(saveFile) {
                           writeRecordToFile(file, movie);
                       }
                   }




    ).toList().toBlocking().first();
     if(csvPrinter!=null){
         try {
             csvPrinter.close();
         } catch (IOException e) {

         }
     }
        return movies;
    }
    private static void writeRecordToFile(File file , Movie movie) {
        try {
            if (csvPrinter == null) {
                csvPrinter = new CSVPrinter(new FileWriter(file), csvFormat);
                printHeaderRecord(csvPrinter);

            }
            printValueRecord(csvPrinter, movie);
        }
        catch (IOException e) {
            logger.error("Unable to create csv printer or write record {}", movie, e);
            return;
        }



    }

    private static void printValueRecord(CSVPrinter csvPrinter, Movie movie) throws IOException{
        Field[] fields= Movie.class.getDeclaredFields();
        Object[] fieldValues=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            try {
                fields[i].setAccessible(true);
                fieldValues[i]=fields[i].get(movie).toString();
                fields[i].setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
        csvPrinter.printRecord(fieldValues);
    }

    private static void printHeaderRecord(CSVPrinter csvPrinter) throws IOException {
       Field[] fields= Movie.class.getDeclaredFields();
       String[] fieldNames=new String[fields.length];
       for(int i=0;i<fields.length;i++){
           fieldNames[i]=fields[i].getName();
       }
      csvPrinter.printRecord(fieldNames);
    }

    public static void main(String args[]){
        logger.info("Choose the option: \n" +
                "1.Search Movie By Name \n"+
                "2.Discover Movie By Criterion \n"+
                "3.Get Movie Detail \n"+
                "4.Discover and get Movie Detail");

        Scanner scanner=new Scanner(System.in);
        int option=Integer.parseInt(scanner.nextLine());
        switch (option){
            case 1: {
                logger.info("You chose option 1: \n Now Enter the movie name");
                SearchedMovieResults searchedMovieResults=handleSearchMovieByName();
                if (searchedMovieResults != null) {
                    List<SearchedMovie> movies = searchedMovieResults.getResults();
                    if (movies != null) {
                        for (SearchedMovie movie : movies) {
                            logger.info("Got result {}", movie);
                        }
                    }
                }
                break;
            }
            case 2: {
                logger.info("You chose option 2:");
                DiscoverMovieResult discoverMovieResult=handleDiscoverMovieResult();
                if (discoverMovieResult != null) {
                    List<SearchedMovie> movies = discoverMovieResult.getResults();
                    if (movies != null) {
                        for (SearchedMovie movie : movies) {
                            logger.info("Got result {}", movie);
                        }
                    }
                }
                break;
            }
            case 3:{
                logger.info("You chose option 3:");
                logger.info("Now Enter the movie Id:");
                String movieIdValue= scanner.nextLine();
                if(movieIdValue!=null){
                   Movie movie= movieUtil.getMovieDetails(Long.parseLong(movieIdValue));
                   logger.info("Got Movie {}",movie);
                }
                break;
            }
            case 4:{
                logger.info("You chose option 4:");
                logger.info("Would You like to save the csv file [Y/N] [Y]?");
                String saveFile=scanner.nextLine();
                File file=null;
                boolean shouldFileBeSaved=false;
                if(saveFile!=null&&saveFile.equalsIgnoreCase("Y")){
                    shouldFileBeSaved=true;
                    logger.info("Enter the complete file name including path [.//movie_corpus.csv]?");

                    String fileLocation=scanner.nextLine();
                    if(fileLocation==null||fileLocation.isEmpty()){
                        file =new File(".\\movie_corpus.csv");
                    }
                    else{
                        file=new File(fileLocation);
                    }

                }
                DiscoverMovieResult discoverMovieResult=handleDiscoverMovieResult();
                if(discoverMovieResult!=null){
                  List<Movie> movies=  invokeRateLimitedFetch(discoverMovieResult.getResults(),shouldFileBeSaved,file);
                    for(Movie movie:movies){
                      logger.info("Got Movie : {}",movie);
                  }
                }

            }

        }

    }

}
