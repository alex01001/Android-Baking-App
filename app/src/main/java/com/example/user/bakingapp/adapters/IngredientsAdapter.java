package com.example.user.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.model.Ingredient;
import com.example.user.bakingapp.R;

import java.util.Collections;
import java.util.List;

public class IngredientsAdapter  extends RecyclerView.Adapter <IngredientsAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<Ingredient> data = Collections.emptyList();

    private Context context;

    public IngredientsAdapter (Context tContext){
        context = tContext;
        inflater = LayoutInflater.from(tContext);
    }

    @Override
    public IngredientsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ingredients_item,parent,false);
        IngredientsAdapter.MyViewHolder holder = new IngredientsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.MyViewHolder holder, int position) {
        Ingredient current = data.get(position);
        holder.tvIngredient.setText(current.getIngredient());
        holder.tvMeasure.setText(current.getMeasure());
        holder.tvQuant.setText(String.valueOf(current.getQuantity()));
    }
    public void setStepsData (List<Ingredient> mData){
        data = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(data==null) return 0;
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIngredient;
        private TextView tvMeasure;
        private TextView tvQuant;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvIngredient = (TextView) itemView.findViewById(R.id.tv_ingredient_item);
            tvMeasure = (TextView) itemView.findViewById(R.id.tv_ingredient_measure);
            tvQuant = (TextView) itemView.findViewById(R.id.tv_ingredient_quantity);
        }
    }
}
