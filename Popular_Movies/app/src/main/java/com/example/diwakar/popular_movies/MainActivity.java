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
import android.widget.Toast;

/**
 * Created by diwakar on 7/7/16.
 */
public class MainActivity extends AppCompatActivity {

    FragmentPosterGrid fragmentPosterGrid;
    FragmentDetails fragmentDetails;
    Menu menu;
    MovieInfo movieBeingDisplayed = null;
    private final int MODE_FAVORITE = 1;
    private final int MODE_NORMAL = 2;
    private int MODE_CURRENT = MODE_NORMAL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentPosterGrid = new FragmentPosterGrid();//create the fragment instance for the top fragment
        FragmentManager manager = getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction
        transaction.replace(R.id.My_Container_1_ID, fragmentPosterGrid);
        transaction.commit();

        MODE_CURRENT = getIntent().getIntExtra("mode", MODE_NORMAL);

        ActionBar actionBar = getSupportActionBar();
        if (MODE_CURRENT == MODE_FAVORITE) {
            actionBar.setTitle("Your Favorites");
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String sortBy = prefs.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_popular));
            if (sortBy.equals(getString(R.string.pref_popular)))
                actionBar.setSubtitle("by popularity");
            else
                actionBar.setSubtitle("by rating");
        }

        if (savedInstanceState != null) {
            movieBeingDisplayed = savedInstanceState.getParcelable("movieBeingDisplayed");
            if (movieBeingDisplayed != null) {
                Toast.makeText(MainActivity.this, "restored", Toast.LENGTH_SHORT).show();
                movieFromPosterGridClicked(movieBeingDisplayed);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieBeingDisplayed != null) {
            outState.putParcelable("movieBeingDisplayed", movieBeingDisplayed);
            Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        if (MODE_CURRENT == MODE_FAVORITE) {
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
                intent.putExtra("mode", MODE_FAVORITE);
                startActivity(intent);
                return true;
        }
        return false;
    }

    //this is called every time user clicks on menu button, and one when onCreate() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(MODE_CURRENT == MODE_NORMAL) {
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
        bundle.putParcelable("movie_info", movieInfo);

        if (fragmentDetails.getArguments() == null) {
            fragmentDetails.setArguments(bundle);
        } else {
            //Consider explicitly clearing arguments here
            fragmentDetails.getArguments().putAll(bundle);
        }

        manager.beginTransaction()
                .replace(R.id.My_Container_2_ID, fragmentDetails)
                .addToBackStack(null)
                .commit();
    }

    public void fragmentDetailsDetached() {
        FrameLayout fragmentPosterGridLayout = (FrameLayout) findViewById(R.id.My_Container_1_ID);
        fragmentPosterGridLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        movieBeingDisplayed = null;
    }
}
