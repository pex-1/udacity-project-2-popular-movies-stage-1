package udacity.popularmoviesstage1.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarEntry;

import udacity.popularmoviesstage1.MainActivity;
import udacity.popularmoviesstage1.model.Movie;

public class JsonUtils {

    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "original_title";

    public static Movie[] parseMovieJson(String moviesJsonStr, Context context) throws JSONException {

        JSONObject rootObject = new JSONObject(moviesJsonStr);
        JSONArray results = rootObject.getJSONArray("results");

        Movie[] movies = new Movie[results.length()];

        for (int i = 0; i < results.length(); i++) {
            movies[i] = new Movie();

            JSONObject movieInfo = results.getJSONObject(i);

            movies[i].setTitle(movieInfo.getString(TITLE));
            movies[i].setPosterPath(movieInfo.getString(POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
        }

        return movies;
    }

    /*
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return ( netInfo != null && netInfo.isConnected() );
    }
    */
}
