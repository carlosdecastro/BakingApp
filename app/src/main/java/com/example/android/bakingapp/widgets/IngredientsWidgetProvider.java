package com.example.android.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.IngredientsWidgetActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static String TAG = "IngredientsWidgetProvider";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        Log.v(TAG, "Hola caracola ");

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        Intent intent = new Intent(context, IngredientsWidgetActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.ingredients_list, pendingIntent);


        views.setTextViewText(R.id.recipe_name_widget, "Choose a recipe");
        views.setTextViewText(R.id.ingredients_list, "Press here to choose a recipe");

        if (recipe != null) {
            views.setTextViewText(R.id.recipe_name_widget, recipe.getName());
            List<Ingredient> ingredientList = recipe.getIngredients();

            String ingredients = "";
            String name;

            for (Ingredient ingredient: ingredientList) {

                name = ingredient.getIngredient();
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                ingredients = ingredients + name + " " + "(" + ingredient.getQuantity() + " " + ingredient.getMeasure().toLowerCase() + ")" + "\n";

                views.setTextViewText(R.id.ingredients_list, ingredients);
            }

        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Recipe recipe = null;
        updateIngredientsWidgets(context, appWidgetManager, appWidgetIds, recipe);

    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

