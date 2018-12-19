package com.example.android.bakingapp.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.StepActivity;
import com.example.android.bakingapp.StepDetailActivity;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    private String TAG = "StepDetailFragment";
    private StepDetailActivity mParentActivity;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    private Step currentStep;
    private List<Step> steps;

    private int stepsSize;
    private boolean mPlayer = false;

    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getParcelable("step");
            steps = savedInstanceState.getParcelableArrayList("steps");
            if (steps != null)
                stepsSize = steps.size();
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        TextView stepId = rootView.findViewById(R.id.step_id);
        View backgroundView = rootView.findViewById(R.id.button_background_view);
        Button nextButton = rootView.findViewById(R.id.step_next_button);
        Button backButton = rootView.findViewById(R.id.step_previous_button);
        ImageView stepImage = rootView.findViewById(R.id.step_image);

        mPlayerView = rootView.findViewById(R.id.playerView);

        if (!currentStep.getVideoURL().isEmpty()) {
            mPlayer = true;
            stepImage.setVisibility(View.GONE);
            initializePlayer(Uri.parse(currentStep.getVideoURL()));

            if (!StepActivity.mTwoPane) {

                if (getActivity().getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    mPlayerView.setLayoutParams(params);

                    ((StepDetailActivity) getActivity()).getSupportActionBar().hide();
                    nextButton.setVisibility(View.GONE);
                    backButton.setVisibility(View.GONE);
                }
            }

        } else if (currentStep.getThumbnailURL() != null) {
            mPlayer =false;
            mPlayerView.setVisibility(View.GONE);
            stepImage.setImageResource(R.drawable.ic_chef);
        }

        stepId.setText(currentStep.getDescription());


        if (!StepActivity.mTwoPane) {
            mParentActivity = (StepDetailActivity) getActivity();

            stepsSize = stepsSize - 1;
            Log.v(TAG, "Current step ID: " + currentStep.getId() + "Size: " + stepsSize);

            if (currentStep.getId() >= stepsSize) {
                nextButton.setEnabled(false);

            } else if (currentStep.getId() == 0) {
                backButton.setEnabled(false);
            }

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int nextStepId = (currentStep.getId() + 1);
                    doTransaction(nextStepId);
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int previousStepId = (currentStep.getId() - 1);
                    doTransaction(previousStepId);
                    Log.v(TAG, "Current step ID: " + currentStep.getId() + "Size: " + stepsSize + "Previous: " + previousStepId);
                }
            });

        } else {
            backgroundView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void setCurrentStep(Step step) {
        currentStep = step;
    }

    public void setStepsSize(int size) {
        stepsSize = size;
    }

    public void setSteps(List<Step> step) {
        steps = step;
        setStepsSize(steps.size());
    }

    private void doTransaction(int id) {


            currentStep = steps.get(id);

            FragmentManager fragmentManager = mParentActivity.getSupportFragmentManager();

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(steps.get(id));
            stepDetailFragment.setSteps(steps);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment_container, stepDetailFragment)
                    .commit();


    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingApp"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            //mExoPlayer.setPlayWhenReady(false);
            mPlayerView.setPlayer(mExoPlayer);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer)
            releasePlayer();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", currentStep);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);
    }
}
