package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.RecipesAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.RecipeAPI;
import com.example.android.bakingapp.network.RecipeRestAdapter;
import com.example.android.bakingapp.widgets.IngredientsWidgetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IngredientsWidgetActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener{

    private String TAG = "IngredientsWidgetActivity";

    private RecyclerView recipesRv;
    private List<Recipe> recipes;
    private int mAppWidgetId;
    private ProgressBar progressBar;
    private RelativeLayout messageLayout;
    private TextView emptyTitleText;
    private TextView emptySubtitleText;
    private ImageView emptyErrorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_widget);

        progressBar = findViewById(R.id.loading_spinner);
        emptyTitleText = findViewById(R.id.empty_title_text);
        emptyErrorImage = findViewById(R.id.empty_recipe_image);
        emptySubtitleText = findViewById(R.id.empty_subtitle_text);
        messageLayout = findViewById(R.id.empty_view);
        messageLayout.setVisibility(View.GONE);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            mAppWidgetId = intent.getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        recipesRv = findViewById(R.id.rvRecipesWidget);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recipesRv.setLayoutManager(llm);

        getData();

    }

    private void setAdapter() {
        RecipesAdapter recipesAdapter = new RecipesAdapter(this, recipes, this);
        recipesRv.setAdapter(recipesAdapter);
    }

    private void getData() {
        Retrofit retrofit = RecipeAPI.provideRetrofit();

        final RecipeRestAdapter recipeRestAdapter = retrofit.create(RecipeRestAdapter.class);

        Call<List<Recipe>> call = recipeRestAdapter.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                progressBar.setVisibility(View.GONE);
                setAdapter();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                emptyTitleText.setText(getString(R.string.error_empty_title));
                emptySubtitleText.setText(R.string.error_empty_subtitle);
                progressBar.setVisibility(View.GONE);
                messageLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onListItemClick(Recipe recipe) {

        IngredientsWidgetService.startActionUpdateIngredients(this, recipe);
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        Log.v(TAG, "Current recipe: " + recipe.getName() + "mAppWidgetId: " + mAppWidgetId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
