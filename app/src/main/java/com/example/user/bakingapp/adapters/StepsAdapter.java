package com.example.user.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.Model.Recipe;
import com.example.user.bakingapp.Model.Step;
import com.example.user.bakingapp.R;

import java.util.Collections;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter <StepsAdapter.MyViewHolder>{

    final private StepsAdapter.StepClickListener onClickListener;
    private LayoutInflater inflater;
    List<Step> data = Collections.emptyList();

    private Context context;

    public interface StepClickListener {
        void onStepItemClick(int ClickedItemIndex,TextView recipeName);
    }

    public StepsAdapter (Context tContext, StepsAdapter.StepClickListener listener){
        context = tContext;
        inflater = LayoutInflater.from(tContext);
        onClickListener = listener;
    }

    @Override
    public StepsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.step_item,parent,false);
        StepsAdapter.MyViewHolder holder = new StepsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StepsAdapter.MyViewHolder holder, int position) {
        Step current = data.get(position);
        holder.stepDescription.setText(current.getDescription());


        //        URL posterURL = NetworkTools.buildPosterUrl(current.getPosterPath());
//        Picasso.with(context).load(posterURL.toString()).fit().into(holder.posterImg);
//        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//            ViewGroup.LayoutParams lp;
//            lp = holder.posterImg.getLayoutParams();
//            lp.height = 270* Resources.getSystem().getDisplayMetrics().widthPixels/(4*185)-16;
//
//        }
//        else{
//            ViewGroup.LayoutParams lp;
//            lp = holder.posterImg.getLayoutParams();
//            lp.height = 270*Resources.getSystem().getDisplayMetrics().widthPixels/(2*185)-16;
//
//        }

    }
    public void setStepsData (List<Step> mData){
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
        private TextView stepDescription;

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onStepItemClick(clickedPosition,stepDescription);
        }


        public MyViewHolder(View itemView) {
            super(itemView);
            stepDescription = (TextView) itemView.findViewById(R.id.tv_step_description);
            itemView.setOnClickListener(this);
        }
    }
}

