package com.example.android.bakingapp;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakingapp.fragments.StepDetailFragment;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity {

    private String TAG = "StepDetailActivity";

    private int mStepId;
    private List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {

            if (getIntent().getExtras() != null) {

                Bundle bundle = getIntent().getExtras();
                mSteps = bundle.getParcelableArrayList("steps");
                mStepId = bundle.getInt("step_id");
                Log.v(TAG, "Estoy en con los extras del intent" + mSteps.get(0).getShortDescription());
                addFragment();
                setTitle(R.string.steps);
            }
        }

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            Log.v(TAG, "Estoy en savedInstance" + mSteps.get(0).getShortDescription());
            mStepId = savedInstanceState.getInt("step_id");
        }
    }

    public void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        StepDetailFragment stepDetailFragment = new StepDetailFragment();

            stepDetailFragment.setCurrentStep(mSteps.get(mStepId));
            stepDetailFragment.setSteps(mSteps);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_fragment_container, stepDetailFragment)
                    .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("step_id", mStepId);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
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
