package udacity.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.popularmoviesstage1.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_OBJECT = "movieObject";
    ImageView posterImageView;
    TextView title;
    TextView voteAverage;
    TextView overview;
    TextView releaseDate;


    //back functionality
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //adding back arrow
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Movie movie = getIntent().getParcelableExtra(MOVIE_OBJECT);
        posterImageView = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        voteAverage = findViewById(R.id.vote_average);
        overview = findViewById(R.id.overview);
        releaseDate = findViewById(R.id.releaseDate);

        setupUI(movie);
    }

    public void setupUI(Movie movie){
        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(185, 278)
                .into(posterImageView);


        title.setText(movie.getTitle());
        voteAverage.setText(movie.getVoteAverage().toString() + "/10");
        releaseDate.setText(movie.getReleaseDate().substring(0,4));
        overview.setText(movie.getOverview());
    }


}
