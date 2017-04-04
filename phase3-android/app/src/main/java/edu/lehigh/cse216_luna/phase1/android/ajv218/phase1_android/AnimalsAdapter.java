package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;

/**
 * Created by alex on 2/25/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.ViewHolder>{
    private List<String> mDataSet;
    private Context mContext;

    public AnimalsAdapter(Context context, List<String> list){
        mDataSet = list;
        mContext = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public Button mRemoveButton;
        public Button mLikeButton;
        public RelativeLayout mRelativeLayout;
        public ViewHolder(View v){
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv);
            mRemoveButton = (Button) v.findViewById(R.id.ib_remove);
            mLikeButton = (Button) v.findViewById(R.id.ib_like);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.rl);
        }
    }

    @Override
    public AnimalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_view,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //will eventually just be commented out to be replaced
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.mTextView.setText(mDataSet.get(position));

        // Set a click listener for TextView
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String animal = mDataSet.get(position);

                //should take you to message page instead of printing toast
                Toast.makeText(mContext,animal,Toast.LENGTH_SHORT).show();
            }
        });

        //not necessary if we include deleting as only a function on individual message pages
        //probably should just be commented out and replaced later on
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked item label
                String itemLabel = mDataSet.get(position);

                // Remove the item on remove/button click
                mDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mDataSet.size());

                // Show the removed item label
                Toast.makeText(mContext,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for item remove button
        //not necessary if we include liking as only a function on individual message pages
        //probably should just be commented out and replaced
        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * The code enclosed here is copy pasted from remove button's code.
                 *
                 * mDataSet.remove(position);
                 * notifyItemRemoved(position);
                 * notifyItemRangeChanged(position,mDataSet.size());
                */

                // Get the clicked item label
                String itemLabel = mDataSet.get(position);

                // For now, our like button will just display dialogue box that says "Liked : itemLabel"
                Toast.makeText(mContext,"Liked : " + itemLabel,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }
}