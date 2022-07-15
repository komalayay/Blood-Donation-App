package com.example.bloodred;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends FirebaseRecyclerAdapter<Model,MyAdapter.myviewholder> implements Filterable {

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Model model) {

        holder.textName.setText(model.getName());
        holder.textType.setText(model.getType());
        holder.textBloodGroup.setText(model.getBloodgroup());
        holder.textEmail.setText(model.getEmail());
        Glide.with(holder.imageView.getContext()).load(model.getProfilepicture()).into(holder.imageView);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_searchview,parent,false);
        return new myviewholder(view);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        CircleImageView imageView;
        TextView textType,textEmail,textName,textBloodGroup;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.imageView);
            textType=(TextView)itemView.findViewById(R.id.textType);
            textEmail=(TextView)itemView.findViewById(R.id.textEmail);
            textName=(TextView)itemView.findViewById(R.id.textName);
            textBloodGroup=(TextView)itemView.findViewById(R.id.textBloodGroup);
        }
    }
}
