package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;


//The adapter for the recyclerView

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> mData;
    private LayoutInflater mInflater;


    public IngredientAdapter(Context context, List<Ingredient> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = holder.parseIngredient(mData.get(position).getIngredient());
        Double quantity = mData.get(position).getQuantity();
        String measure = mData.get(position).getMeasure();

        String result = name + " " + "(" + quantity + " " + measure.toLowerCase() + ")";

        holder.ingredientName.setText(result);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientName;

        ViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
        }

        private String parseIngredient(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            } else {
                return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
            }
        }

    }

}
