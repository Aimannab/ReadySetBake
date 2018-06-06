package com.example.android.readysetbake;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiman Nabeel on 01/06/2018.
 */

//Ref: http://mateoj.com/2015/10/06/creating-movies-app-retrofit-picasso-android/
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeCardHolder> {

    private ArrayList<Recipe> recipeList;
    private Context rContext;
    final private RecipeListItemClickListener rListener;

    //Interface for MainActivity for clicking on Recipes List
    public interface RecipeListItemClickListener {
        void onRecipeListItemClick(Recipe selectedItemIndex);
    }

    public RecipesAdapter (RecipeListItemClickListener rOnClickListener) {
        rListener = rOnClickListener;
    }

    public void setRecipeList(ArrayList<Recipe> recipesIn, Context context) {
        recipeList = recipesIn;
        rContext=context;
        notifyDataSetChanged();
    }

    @Override
    public RecipesAdapter.RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_card_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        RecipesAdapter.RecipeCardHolder viewHolder = new RecipesAdapter.RecipeCardHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipeCardHolder holder, int position) {

        holder.textView.setText(recipeList.get(position).getName());
        String imageUrl=recipeList.get(position).getImage();

        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(rContext).load(builtUri).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {

        return (recipeList == null) ? 0 : recipeList.size();
    }


    class RecipeCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView imageView;
        public TextView textView;

        public RecipeCardHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedRecipe = getAdapterPosition();
            rListener.onRecipeListItemClick(recipeList.get(clickedRecipe));
        }
    }
}
