package com.example.communityhubproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
   List<model> list = new ArrayList();
   Context context;
   FirebaseStorage storage = FirebaseStorage.getInstance();
    public ProductsListAdapter(List<model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent, false);
        return new ProductsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductsList vh = (ProductsList) holder;
        model item = list.get(position);
        vh.title.setText(item.getTitle());
        vh.description.setText(item.getDescription());
        vh.price.setText(item.getPrice());
        vh.title.setText(item.getTitle());
       // Glide.with(getContext()).load(item.getImageURL()).into(vh.img);
       Glide.with(context).load(item.getImageURL()).into(vh.img);
    }

    @Override
    public int getItemCount() {
        if(list != null && list.size()!= 0){
            return list.size();
        }
        return 0;
    }

    private class ProductsList extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,description,price;
        public ProductsList(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img1);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            price=itemView.findViewById(R.id.price);
        }
    }
}
