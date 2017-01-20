package com.example.romanrosiak.dietapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.romanrosiak.dietapp.ListViewHolder.IngredientHolder;
import com.example.romanrosiak.dietapp.ListViewHolder.WeekListHolder;
import com.example.romanrosiak.dietapp.R;

import java.util.List;

/**
 * Created by roman.rosiak on 18.01.2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private List<IngredientHolder> ingredientList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ingredientName, ingredientQuantity, quantityType ;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ingredientName = (TextView) view.findViewById(R.id.ingredientNameTV);
            ingredientQuantity = (TextView) view.findViewById(R.id.ingredientQuantityTV);
            quantityType = (TextView) view.findViewById(R.id.ingredientQuantityTypeTV);
        }

        @Override
        public void onClick(View view) {

        }
    }


    public IngredientsAdapter(List<IngredientHolder> ingredientList) {
        this.ingredientList = ingredientList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item_layout, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        IngredientHolder ingredient = ingredientList.get(position);
        holder.ingredientName.setText(ingredient.getIngredientName());
        holder.ingredientQuantity.setText(String.valueOf(ingredient.getIngredientQuantity()));
        holder.quantityType.setText(ingredient.getQuantityType());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}