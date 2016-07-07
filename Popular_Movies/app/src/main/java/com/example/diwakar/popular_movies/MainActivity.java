package com.example.diwakar.popular_movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

/**
 * Created by diwakar on 7/7/16.
 */
public class MainActivity extends FragmentActivity {

    FragmentPosterGrid fragmentPosterGrid;
    FragmentDetails fragmentDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentPosterGrid = new FragmentPosterGrid();//create the fragment instance for the top fragment
        FragmentManager manager = getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction
        transaction.add(R.id.My_Container_1_ID, fragmentPosterGrid, "Frag_Top_tag");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    void movieFromPosterGridClicked(MovieInfo movieInfo) {
        Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
        FragmentManager manager = getSupportFragmentManager();//create an instance of fragment manager
        if(fragmentDetails != null)
            manager.beginTransaction().remove(fragmentDetails).commit();
        fragmentDetails = new FragmentDetails();//create the fragment instance for the top fragment

        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie_info", movieInfo);

        if (fragmentDetails.getArguments() == null) {
            fragmentDetails.setArguments(bundle);
        } else {
            //Consider explicitly clearing arguments here
            fragmentDetails.getArguments().putAll(bundle);
        }

        transaction.add(R.id.My_Container_2_ID, fragmentDetails);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
