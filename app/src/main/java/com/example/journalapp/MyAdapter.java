package com.example.journalapp;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Journals> journalsList;

    public MyAdapter(Context context, List<Journals> journalsList) {
        this.context = context;
        this.journalsList = journalsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.journal_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         Journals currentJournal=journalsList.get(position);
         holder.title.setText(currentJournal.getTitle());
         holder.thoughts.setText(currentJournal.getThoughts());
         holder.name.setText(currentJournal.getUserName());

         String imageUrl=currentJournal.getImageUrl();
         String timeAge=(String) DateUtils.getRelativeTimeSpanString(
                 currentJournal.getTimeAdded().getSeconds()*1000
         );
         holder.dateAdded.setText(timeAge);

         //glide library to display the image
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return journalsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,thoughts,dateAdded,name;
        public ImageView image,shareButton;
        public String userId,username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.joural_title_list);
            thoughts=itemView.findViewById(R.id.joural_thought_list);
            dateAdded=itemView.findViewById(R.id.journal_timestamp_list);

            image=itemView.findViewById(R.id.joural_image_list);
            name=itemView.findViewById(R.id.journal_row_username);

            shareButton=itemView.findViewById(R.id.journal_row_image);
            shareButton.setOnClickListener(v -> {
                //onclick

            });
        }
    }
}
