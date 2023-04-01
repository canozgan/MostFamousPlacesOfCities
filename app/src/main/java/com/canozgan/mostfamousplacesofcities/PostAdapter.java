package com.canozgan.mostfamousplacesofcities;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.canozgan.mostfamousplacesofcities.databinding.RecyclerRowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    ArrayList<Post> postArrayList;

    public PostAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.binding.emailTextView.setText(postArrayList.get(position).email);
        holder.binding.placeNameTextView.setText(postArrayList.get(position).placeName);
        holder.binding.commentTextView.setText(postArrayList.get(position).comment);
        try{
            Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.binding.imageView);

        }catch (Exception e){
            e.printStackTrace();
        }
        holder.binding.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToMapsActivity=new Intent(v.getContext(),MapsActivity.class);
                intentToMapsActivity.putExtra("latitute",postArrayList.get(position).latitute);
                intentToMapsActivity.putExtra("longitute",postArrayList.get(position).longitute);
                intentToMapsActivity.putExtra("value","show");
                v.getContext().startActivity(intentToMapsActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;
        public PostHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}

