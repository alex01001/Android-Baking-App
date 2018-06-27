package com.example.user.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.Model.Ingredient;
import com.example.user.bakingapp.Model.Step;
import com.example.user.bakingapp.R;

import java.util.Collections;
import java.util.List;

public class IngredientsAdapter  extends RecyclerView.Adapter <IngredientsAdapter.MyViewHolder>{

//    final private StepsAdapter.StepClickListener onClickListener;
    private LayoutInflater inflater;
    List<Ingredient> data = Collections.emptyList();

    private Context context;

//    public interface StepClickListener {
//        void onStepItemClick(int ClickedItemIndex,TextView recipeName);
//    }

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
//            TextView tvIngredient = (TextView) convertView.findViewById(R.id.tv_ingredient_item);
//            TextView tvMeasure = (TextView) convertView.findViewById(R.id.tv_ingredient_measure);
//            TextView tvQuant = (TextView) convertView.findViewById(R.id.tv_ingredient_quantity);

//            tvIngredient.setText((CharSequence) recipe.getIngredients().get(position).getIngredient());
//            tvMeasure.setText((CharSequence) recipe.getIngredients().get(position).getMeasure());
//            tvQuant.setText(String.valueOf(recipe.getIngredients().get(position).getQuantity()));

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

        //            TextView tvIngredient = (TextView) convertView.findViewById(R.id.tv_ingredient_item);
//            TextView tvMeasure = (TextView) convertView.findViewById(R.id.tv_ingredient_measure);
//            TextView tvQuant = (TextView) convertView.findViewById(R.id.tv_ingredient_quantity);
//            tvIngredient.setText((CharSequence) recipe.getIngredients().get(position).getIngredient());
//            tvMeasure.setText((CharSequence) recipe.getIngredients().get(position).getMeasure());
//            tvQuant.setText(String.valueOf(recipe.getIngredients().get(position).getQuantity()));


        public MyViewHolder(View itemView) {
            super(itemView);

            tvIngredient = (TextView) itemView.findViewById(R.id.tv_ingredient_item);
            tvMeasure = (TextView) itemView.findViewById(R.id.tv_ingredient_measure);
            tvQuant = (TextView) itemView.findViewById(R.id.tv_ingredient_quantity);

            //            stepDescription = (TextView) itemView.findViewById(R.id.tv_step_description);
//            stepShortDescription = (TextView) itemView.findViewById(R.id.tv_step_short_description);
//            stepNumber = (TextView) itemView.findViewById(R.id.tv_step_id);
        }
    }
}
