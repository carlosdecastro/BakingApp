package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakingapp.fragments.StepDetailFragment;
import com.example.android.bakingapp.fragments.StepListFragment;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

// This activity is the list of steps on a phone or de list + Fragment (Detail)
// in a tablet

public class StepActivity extends AppCompatActivity implements StepListFragment.ListItemClickListener {

    private String TAG = "StepActivity";

    private Recipe mRecipe;

    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();
            mRecipe = bundle.getParcelable("recipe");
            setTitle(mRecipe.getName());

        }

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", mRecipe);

            FragmentManager fragmentManager = getSupportFragmentManager();

            StepListFragment stepListFragment = new StepListFragment();

            stepListFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.step_list_container, stepListFragment)
                    .commit();
        }
    }

    @Override
    public void onListItemClick(Step step, int clickedPosition) {

        if (mTwoPane) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            stepDetailFragment.setCurrentStep(step);

            Log.v(TAG, "setCurrentStep(" + step.getShortDescription() + ")");


            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        } else {

            Bundle bundle= new Bundle();
            bundle.putInt("step_id", clickedPosition);

            bundle.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mRecipe.getSteps());

            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", mRecipe);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
