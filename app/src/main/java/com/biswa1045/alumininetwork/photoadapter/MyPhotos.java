package com.biswa1045.alumininetwork.photoadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biswa1045.alumininetwork.PostAdapter;
import com.biswa1045.alumininetwork.R;
import com.biswa1045.alumininetwork.post;

import java.util.List;


public class MyPhotos extends RecyclerView.Adapter<MyPhotos.ViewHolder>{

    private Context context;
    private List<PostAdapter> post;

    public MyPhotos(Context context, List<post> postList) {
        this.context = context;
        this.post = post;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_manage_post,parent,false);

        return new MyPhotos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
PostAdapter postAdapter=post.get(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
