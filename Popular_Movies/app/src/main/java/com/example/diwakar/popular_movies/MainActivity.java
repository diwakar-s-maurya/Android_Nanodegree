package com.example.diwakar.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by diwakar on 7/7/16.
 */
public class MainActivity extends AppCompatActivity {

    FragmentPosterGrid fragmentPosterGrid;
    FragmentDetails fragmentDetails;
    Menu menu;
    MovieInfo movieBeingDisplayed = null;
    private int MODE_CURRENT = ProjectConstants.MODE_NORMAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentPosterGrid = new FragmentPosterGrid();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.My_Container_1_ID, fragmentPosterGrid);
        transaction.commit();

        MODE_CURRENT = getIntent().getIntExtra(ProjectConstants.BUNDLE_MODE, ProjectConstants.MODE_NORMAL);

        ActionBar actionBar = getSupportActionBar();
        if (MODE_CURRENT == ProjectConstants.MODE_FAVOURITE) {
            actionBar.setTitle(getString(R.string.title_your_favorite));
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
            if (sortBy.equals(getString(R.string.pref_popular)))
                actionBar.setSubtitle(getString(R.string.subtitle_by_popularity));
            else
                actionBar.setSubtitle(R.string.subtitle_by_rating);
        }

        if (savedInstanceState != null) {
            movieBeingDisplayed = savedInstanceState.getParcelable(ProjectConstants.BUNDLE_MOVIE_BEING_DISPLAYED);
            if (movieBeingDisplayed != null) {
                movieFromPosterGridClicked(movieBeingDisplayed);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieBeingDisplayed != null) {
            outState.putParcelable(ProjectConstants.BUNDLE_MOVIE_BEING_DISPLAYED, movieBeingDisplayed);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        if (MODE_CURRENT == ProjectConstants.MODE_FAVOURITE) {
            menu.findItem(R.id.show_saved_movies).setVisible(false);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_saved_movies:
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra(ProjectConstants.BUNDLE_MODE, ProjectConstants.MODE_FAVOURITE);
                startActivity(intent);
                return true;
        }
        return false;
    }

    //this is called every time user clicks on menu button, and one when onCreate() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (MODE_CURRENT == ProjectConstants.MODE_NORMAL) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
            if (sortBy.equals(getString(R.string.pref_popular))) {
                menu.findItem(R.id.sortBy_popular).setVisible(false);
                menu.findItem(R.id.sortBy_top_rated).setVisible(true);
            } else {
                menu.findItem(R.id.sortBy_top_rated).setVisible(false);
                menu.findItem(R.id.sortBy_popular).setVisible(true);
            }

            return true;
        }
        return false;
    }

    //poster clicked, show details
    void movieFromPosterGridClicked(MovieInfo movieInfo) {
        movieBeingDisplayed = movieInfo;
        FrameLayout fragmentPosterGridLayout = (FrameLayout) findViewById(R.id.My_Container_1_ID);

        int dp = 200;
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        fragmentPosterGridLayout.getLayoutParams().width = px;


        FragmentManager manager = getSupportFragmentManager();//create an instance of fragment manager
        if (fragmentDetails != null)
            manager.beginTransaction().remove(fragmentDetails).commit();
        fragmentDetails = new FragmentDetails();//create the fragment instance for the top fragment

        //// TODO: 7/7/16 shared element transition not working
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragmentDetails.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            fragmentDetails.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(ProjectConstants.BUNDLE_MOVIE_INFO, movieInfo);

        if (fragmentDetails.getArguments() == null) {
            fragmentDetails.setArguments(bundle);
        } else {
            fragmentDetails.getArguments().putAll(bundle);
        }

        manager.beginTransaction()
                .replace(R.id.My_Container_2_ID, fragmentDetails)
                .addToBackStack(null)
                .commit();
    }

    //favourite/unfavourite button clicked, refresh the grid by adding/removing the movie
    public void favUnFavButtonClicked() {
        if (MODE_CURRENT == ProjectConstants.MODE_FAVOURITE)
            fragmentPosterGrid.refreshGridInFavMode();
    }

    //details panel closed, expand movie gird
    public void fragmentDetailsDetached() {
        FrameLayout fragmentPosterGridLayout = (FrameLayout) findViewById(R.id.My_Container_1_ID);
        fragmentPosterGridLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        movieBeingDisplayed = null;
    }
}
