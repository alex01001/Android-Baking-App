package com.example.user.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.model.Recipe;
import com.example.user.bakingapp.R;

import java.util.Collections;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter <RecipeAdapter.MyViewHolder>{

    final private RecipeItemClickListener onClickListener;
    private LayoutInflater inflater;
    List<Recipe> data = Collections.emptyList();

    private Context context;

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int ClickedItemIndex,TextView recipeName);
    }

    public RecipeAdapter (Context tContext, RecipeItemClickListener listener){
        context = tContext;
        inflater = LayoutInflater.from(tContext);
        onClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recipe current = data.get(position);
        holder.recipeName.setText(current.getName());
    }
    public void setRecipeData (List<Recipe> mData){
        data = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(data==null) return 0;
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TextView recipeName;

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onRecipeItemClick(clickedPosition,recipeName);
        }


        public MyViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }
    }
}
