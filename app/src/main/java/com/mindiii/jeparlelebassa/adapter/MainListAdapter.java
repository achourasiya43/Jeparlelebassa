package com.mindiii.jeparlelebassa.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindiii.jeparlelebassa.EducationActivity;
import com.mindiii.jeparlelebassa.R;
import com.mindiii.jeparlelebassa.model.MainListInfo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by mindiii on 14/4/17.
 */


public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    ArrayList<MainListInfo> mainListInfoArrayList;
    Activity activity;

    public MainListAdapter(Activity activity, ArrayList<MainListInfo> mainListInfoArrayList) {
        this.mainListInfoArrayList = mainListInfoArrayList;
        this.activity = activity;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MainListAdapter.ViewHolder vh = new MainListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainListAdapter.ViewHolder holder, final int position) {


        holder.MainListItemName.setText(mainListInfoArrayList.get(position).MainListItemName);
        if (mainListInfoArrayList.get(position).MainListItemImage.isEmpty()) {
            holder.MainListItemImage.setImageResource(R.drawable.profileimg);
        } else{
            Picasso.with(activity).load(mainListInfoArrayList.get(position).MainListItemImage).into(holder.MainListItemImage);
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Item click of list
               /* if(mainListInfoArrayList != null && !mainListInfoArrayList.equals("")){
                    Intent intent = new Intent(activity , EducationActivity.class);
                    intent.putExtra("ID",mainListInfoArrayList.get(position).MainListItemId);
                    activity.startActivity(intent);
                }
*/
                if(mainListInfoArrayList.get(position).MainListItemId != null){
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", String.valueOf(mainListInfoArrayList.get(position).MainListItemId));
                    Intent intent = new Intent(activity, EducationActivity.class);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);


                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return mainListInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each storyData item is just a string in this case
        public ImageView MainListItemImage;
        public TextView MainListItemName;

        public ViewHolder(View v) {
            super(v);
            this.MainListItemName = (TextView) v.findViewById(R.id.tv_title);
            this.MainListItemImage = (ImageView) v.findViewById(R.id.iv_logo);



        }
    }

}
