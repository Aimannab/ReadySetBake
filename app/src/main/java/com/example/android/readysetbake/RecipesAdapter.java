package com.example.android.readysetbake;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiman Nabeel on 01/06/2018.
 */

//Ref: http://mateoj.com/2015/10/06/creating-movies-app-retrofit-picasso-android/
public class RecipesAdapter extends RecyclerView.Adapter<MainActivity.RecipeCardHolder> {

    private ArrayList<Recipe> recipeList;
    private LayoutInflater rInflater;
    private Context rContext;

    //Interface for MainActivity for clicking on Recipes List
    public interface RecipeListItemClickListener {
        void onRecipeListItemClick(Recipe selectedItemIndex);
    }

    public RecipesAdapter (Context context) {
        this.rContext = context;
        this.rInflater = LayoutInflater.from(context);
        this.recipeList = new ArrayList<>();
    }

    @Override
    public MainActivity.RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_recipe;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        MainActivity.RecipeCardHolder viewHolder = new MainActivity.RecipeCardHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainActivity.RecipeCardHolder holder, int position) {

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

    public void setRecipeList(ArrayList<Recipe> recipesIn, Context context) {
        recipeList = recipesIn;
        rContext=context;
        notifyDataSetChanged();
    }
}
