package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;


//The adapter for the recyclerView

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipe> mData;
    private LayoutInflater mInflater;
    private ListItemClickListener mOnClickListener;


    public interface ListItemClickListener {
        void onListItemClick (Recipe recipe);
    }

    public RecipesAdapter(Context context, List<Recipe> data, ListItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = mData.get(position).getName();
        holder.recipeName.setText(name);

        switch (name.toLowerCase()) {
            case "brownies":
                holder.imageView.setImageResource(R.drawable.ic_brownie);
                break;

            case "nutella pie":
                holder.imageView.setImageResource(R.drawable.ic_nutella);
                break;

            case "yellow cake":
                holder.imageView.setImageResource(R.drawable.ic_yellow);
                break;

            case "cheesecake":
                holder.imageView.setImageResource(R.drawable.ic_cheese);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView recipeName;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = mData.get(getAdapterPosition());

            mOnClickListener.onListItemClick(recipe);

        }
    }

}
