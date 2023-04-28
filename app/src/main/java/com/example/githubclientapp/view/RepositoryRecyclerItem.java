package com.example.githubclientapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubclientapp.R;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;

import java.util.List;

public class RepositoryRecyclerItem extends RecyclerView.Adapter<RepositoryRecyclerItem.MyViewHolder>{

    private final List<RepositoryInfo> mainList;
    private Context mainContext;

    private onItemClickListener mainListener;
    interface  onItemClickListener{
        void onItemClick(int position);
        void uploadNextPage();
    }
    public RepositoryRecyclerItem(List<RepositoryInfo> repositoriesList, Context context,
                                  onItemClickListener onClickListener) {
        mainList = repositoriesList;
        mainContext = context;
        mainListener = onClickListener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ownerNickname = itemView.findViewById(R.id.owner_nickname_repository_card);
        TextView repositoryName = itemView.findViewById(R.id.repository_name_repository_card);
        ImageView ownerAvatar = itemView.findViewById(R.id.repository_card_owner_image);
        TextView forksCount = itemView.findViewById(R.id.forks_count_repository_card);
        TextView watchersCount = itemView.findViewById(R.id.watchers_count_repository_card);

        public MyViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            itemView.findViewById(R.id.repository_card_button).setOnClickListener(view -> {
                listener.onItemClick(getAdapterPosition());
            });
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repository_card, parent, false);
        return new MyViewHolder(itemView, mainListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == mainList.size()-1) {
            mainListener.uploadNextPage();
        }

        holder.repositoryName.setText(mainList.get(position).name);
        holder.ownerNickname.setText(mainList.get(position).owner.login);
        holder.watchersCount.setText("Watchers: " + mainList.get(position).watchers_count);
        holder.forksCount.setText("Forks: " + mainList.get(position).forks_count);

        Glide.with(mainContext).load(mainList.get(position).owner.avatar_url)
                .placeholder(R.drawable.placeholder)
                .into(holder.ownerAvatar);
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }
}
