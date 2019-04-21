package com.saidibuss.test.githubapi.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saidibuss.test.githubapi.Object.GithubUser;
import com.saidibuss.test.githubapi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GithubUserAdapter extends RecyclerView.Adapter<GithubUserAdapter.ViewHolder> {

    private List<GithubUser> githubUserList;
    private Context context;

    public GithubUserAdapter(List<GithubUser> githubUserList, Context context) {
        this.githubUserList = githubUserList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTv;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.imageAvatar);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final GithubUser githubUser = githubUserList.get(i);

        viewHolder.usernameTv.setText(githubUser.getUsername());
        Picasso.get().load(githubUser.getImage()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return githubUserList.size();
    }
}
