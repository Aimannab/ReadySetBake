package com.example.android.readysetbake;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aiman Nabeel on 07/06/2018.
 */

public class RecipesDetailAdapter extends RecyclerView.Adapter<RecipesDetailAdapter.RecipeDetailViewHolder> {

    final private RecipeStepClickListener rListener;
    List<Step> recipeStepList;
    private String recipeName;
    List<Step> stepsList;

    //Interface for RecipeDetailActivity for clicking on Recipe Step Detail
    public interface RecipeStepClickListener {
        void onRecipeStepDetailItemClick(Recipe selectedStepItemIndex);
    }

    public RecipesDetailAdapter (RecipeStepClickListener stepOnClickListener) {
        rListener = stepOnClickListener;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int listItemLayout = R.layout.recipe_detail_card_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemLayout, parent, false);
        RecipeDetailViewHolder viewHolder = new RecipeDetailViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {

        holder.steptDescripCard.setText(stepsList.get(position).getId()
                + ". "
                + stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList !=null ? stepsList.size():0 ;
    }

    public void recipeStepData (List<Recipe> recipeList, Context context) {
        recipeStepList = recipeList.get(0).getSteps();
        recipeName = recipeList.get(0).getName();
        notifyDataSetChanged();
    }

    class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView steptDescripCard;

        public RecipeDetailViewHolder(View itemView) {

            super(itemView);
            steptDescripCard = (TextView) itemView.findViewById(R.id.stepDescriptionCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
