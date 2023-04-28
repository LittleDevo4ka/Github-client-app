package com.example.githubclientapp.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubclientapp.R;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;

import org.w3c.dom.Text;

import java.util.List;

public class CommitRecyclerItem extends RecyclerView.Adapter<CommitRecyclerItem.MyViewHolder>{

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView committerNickname = itemView.findViewById(R.id.committer_nickname);
        TextView message = itemView.findViewById(R.id.commit_message);
        TextView sha = itemView.findViewById(R.id.commit_sha);
        TextView date = itemView.findViewById(R.id.commit_date);

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private final List<Commits> mainList;
    private RepositoryRecyclerItem.onItemClickListener mainListener;
    CommitRecyclerItem(List<Commits> list, RepositoryRecyclerItem.onItemClickListener onItemClickListener) {
        mainList = list;
        mainListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CommitRecyclerItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commit_card, parent, false);
        return new CommitRecyclerItem.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommitRecyclerItem.MyViewHolder holder, int position) {
        holder.committerNickname.setText("Committer: " + mainList.get(position).commit.committer.name);
        holder.message.setText(mainList.get(position).commit.message);
        holder.sha.setText("Hash: " + mainList.get(position).sha.substring(0, 6));
        holder.date.setText(mainList.get(position).commit.committer.date);

        if (position == mainList.size()-1) {
            mainListener.uploadNextPage();
        }

    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }
}
