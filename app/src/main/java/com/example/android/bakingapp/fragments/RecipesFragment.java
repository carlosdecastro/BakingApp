package com.example.android.bakingapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.StepActivity;
import com.example.android.bakingapp.adapters.RecipesAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.RecipeAPI;
import com.example.android.bakingapp.network.RecipeRestAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment implements RecipesAdapter.ListItemClickListener{

    private String TAG = "RecipesFragment";

    private RecyclerView recipesRv;
    private List<Recipe> recipes;
    private ProgressBar progressBar;
    private RelativeLayout messageLayout;
    private TextView emptyTitleText;
    private TextView emptySubtitleText;
    private ImageView emptyErrorImage;



    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);

        recipesRv = rootView.findViewById(R.id.rvRecipes);
        progressBar = rootView.findViewById(R.id.loading_spinner);
        emptyTitleText = rootView.findViewById(R.id.empty_title_text);
        emptyErrorImage = rootView.findViewById(R.id.empty_recipe_image);
        emptySubtitleText = rootView.findViewById(R.id.empty_subtitle_text);
        messageLayout = rootView.findViewById(R.id.empty_view);
        messageLayout.setVisibility(View.GONE);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recipesRv.setLayoutManager(llm);

        MainActivity mainActivity = (MainActivity) getActivity();

        mIdlingResource = (SimpleIdlingResource) mainActivity.getIdlingResource();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        getData();

        return rootView;
    }

    private void getData() {
        Retrofit retrofit = RecipeAPI.provideRetrofit();

        final RecipeRestAdapter recipeRestAdapter = retrofit.create(RecipeRestAdapter.class);

        Call<List<Recipe>> call = recipeRestAdapter.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();

                setAdapter();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                emptyTitleText.setText(getString(R.string.error_empty_title));
                emptySubtitleText.setText(R.string.error_empty_subtitle);
                progressBar.setVisibility(View.GONE);
                messageLayout.setVisibility(View.VISIBLE);
                //emptyErrorImage.setImageResource(R.drawable.ic_wifi);
            }
        });

    }

    private void setAdapter() {
        RecipesAdapter recipesAdapter = new RecipesAdapter(getContext(), recipes, this);
        recipesRv.setAdapter(recipesAdapter);
        mIdlingResource.setIdleState(true);
    }

    @Override
    public void onListItemClick(Recipe recipe) {

        Bundle bundle= new Bundle();
        bundle.putParcelable("recipe", recipe);

        Intent intent = new Intent(getContext(), StepActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }


}
