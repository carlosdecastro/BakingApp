package com.example.android.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.IngredientAdapter;
import com.example.android.bakingapp.adapters.StepsAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment implements StepsAdapter.ListItemClickListener {

    private String TAG = "StepListFragment";

    private ListItemClickListener callback;

    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private Recipe mRecipe;


    public interface ListItemClickListener {
        void onListItemClick (Step step, int clickedPosition);
    }

    @Override
    public void onAttach(Context context) {

        try {
            callback = (ListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnListItemClick");
        }

        super.onAttach(context);
    }

    public StepListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable("recipe");

            RecyclerView stepsRv = rootView.findViewById(R.id.rvSteps);
            RecyclerView ingredientsRv = rootView.findViewById(R.id.rvIngredients);

            LinearLayoutManager stepslLm = new LinearLayoutManager(getContext());
            LinearLayoutManager ingredientsLlm = new LinearLayoutManager(getContext());
            stepsRv.setLayoutManager(stepslLm);
            ingredientsRv.setLayoutManager(ingredientsLlm);

                mIngredients = mRecipe.getIngredients();
                IngredientAdapter ingredientsAdapter = new IngredientAdapter(getContext(), mIngredients);
                ingredientsRv.setAdapter(ingredientsAdapter);

                mSteps = mRecipe.getSteps();
                StepsAdapter stepsAdapter = new StepsAdapter(getContext(), mSteps, this);
                stepsRv.setAdapter(stepsAdapter);

            Log.v(TAG, "Ingredientes: " + mRecipe.getIngredients().get(0).getIngredient());
        }

        return rootView;
    }

    @Override
    public void onListItemClick(Step step, int clickedPosition) {

        callback.onListItemClick(step, clickedPosition);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) mIngredients);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
        outState.putParcelable("recipe", mRecipe);
    }
}
