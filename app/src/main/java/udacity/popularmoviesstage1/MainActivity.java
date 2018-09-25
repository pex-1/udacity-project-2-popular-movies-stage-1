package udacity.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import udacity.popularmoviesstage1.model.Movie;
import udacity.popularmoviesstage1.utils.JsonUtils;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ProgressBar progressBar;


    private static String apiKey = BuildConfig.ApiKey;
    private Context context;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.popularity_sort:
                runAsyncTask(getString(R.string.popularity_sort));
                break;
            case R.id.rating_sort:
                runAsyncTask(getString(R.string.rating_sort));
                break;
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //not working for some reason

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridView.setNumColumns(2);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(4);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.movies_toolbar);
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.grid);
        progressBar = findViewById(R.id.progressBar);


        runAsyncTask(getString(R.string.popularity_sort));


        gridView.setOnItemClickListener(detailViewListener);
    }

    private void runAsyncTask(String sort){
        if(isNetworkConnected()){
            LoadMoviesAsyncTask task = new LoadMoviesAsyncTask(this);
            task.execute(sort);
        }
        else{
            Toast.makeText(this, "No network connection!", Toast.LENGTH_SHORT).show();
        }
    }



    private final GridView.OnItemClickListener detailViewListener = new GridView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Movie movie = (Movie) adapterView.getItemAtPosition(i);

            //Toast.makeText(MainActivity.this, movie.getOverview(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            //TODO: parcelable
            intent.putExtra(getString(R.string.movieObject), movie);

            startActivity(intent);
        }
    };



    public static class LoadMoviesAsyncTask extends AsyncTask<String, Void, Movie[]>{
        private WeakReference<MainActivity> activityWeakReference;

        LoadMoviesAsyncTask(MainActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);

            MainActivity activity = activityWeakReference.get();
            activity.progressBar.setVisibility(View.GONE);
            activity.gridView.setAdapter(new GridAdapter(activity.getApplicationContext(), movies));
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String moviesJsonString = null;

            try{
                URL url = getApiUrl(strings);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();

                if(inputStream == null){
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while( (line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line).append("\n");
                }

                moviesJsonString = stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection != null) httpURLConnection.disconnect();
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try{
                MainActivity activity = activityWeakReference.get();
                return JsonUtils.parseMovieJson(moviesJsonString, activity.context);
            }
            catch (JSONException e){
                return null;
            }
        }
    }



    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private static URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String BASE = "http://api.themoviedb.org/3" + parameters[0] + "?api_key=";

        Uri builtUri = Uri.parse(BASE + apiKey).buildUpon().build();

        return new URL(builtUri.toString());
    }
}
