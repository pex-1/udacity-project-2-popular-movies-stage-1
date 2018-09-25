package udacity.popularmoviesstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * implementation of parcelable: https://www.techjini.com/blog/passing-objects-via-intent-in-android/
 * serialized - slowest approach
 */

public class Movie implements Parcelable {

    public static final String HTTPS_IMAGE_TMDB_ORG_T_P_W185 = "https://image.tmdb.org/t/p/w185";
    private String title;
    private String posterPath;
    private Double voteAverage;
    private String overview;
    private String releaseDate;

    public Movie(){ }

    public Movie(String title, String posterPath, Double voteAverage, String overview, String releaseDate){
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    // In constructor you will read the variables from Parcel. Make sure to read them in the same sequence in which you have written them in Parcel.
    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return HTTPS_IMAGE_TMDB_ORG_T_P_W185 + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeValue(voteAverage);

    }
}
