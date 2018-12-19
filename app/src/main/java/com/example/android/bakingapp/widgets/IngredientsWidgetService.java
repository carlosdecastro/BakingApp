package com.example.android.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;


public class IngredientsWidgetService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS = "com.example.android.mygarden.action.update_ingredients";
    private static String TAG = "IngredientsWidgetService";

    public IngredientsWidgetService() {
        super("BakingApp");
    }

    public static void startActionUpdateIngredients(Context context, Recipe recipe) {

        Bundle bundle= new Bundle();
        bundle.putParcelable("recipe", recipe);

        Intent intent = new Intent(context, IngredientsWidgetService.class);
        Log.d(TAG, "Current recipe (startActionUpdateIngredients): " + recipe.getName());

        intent.putExtras(bundle);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                handleActionUpdateIngredientsWidget(bundle.getParcelable("recipe"));
            }
        }
    }

    private void handleActionUpdateIngredientsWidget(Recipe recipe) {

        Log.d(TAG, "Current recipe (handleActionUpdateIngredientsWidget): " + recipe.getName());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, appWidgetIds, recipe);

    }


}
