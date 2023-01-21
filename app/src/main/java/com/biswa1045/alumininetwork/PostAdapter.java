package com.biswa1045.alumininetwork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    List<post> itemList;
    private Context context;

    public PostAdapter(List<post> itemList,Context context) {

        this.itemList=itemList;
        this.context=context;

    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, final int position) {

        String post_url_s = itemList.get(position).getURL();
        String uploader_img_s = itemList.get(position).getUPLOADER_IMG();
        String post_type_s = itemList.get(position).getPOST_TYPE();
        String post_time_s = itemList.get(position).getPOST_TIME();
        String uploader_name_s = itemList.get(position).getUPLOADER_NAME();
        String post_id = itemList.get(position).getPOST_ID();
        holder.uploader_name.setText(uploader_name_s);
        holder.time.setText(post_time_s);
        holder.caption.setText(itemList.get(position).getCAPTION());

        Glide.with(context).load(itemList.get(position).getUPLOADER_IMG())
                .centerCrop()
                .error(R.drawable.person)
                .into(holder.profileimg);
        if(post_type_s.equals("video")){
            holder.post_video.setVisibility(View.VISIBLE);
            holder.post.setVisibility(View.GONE);
            MediaController mediaController = new MediaController(context);
            holder.post_video.setMediaController(mediaController);
            holder.post_video.setVideoPath(post_url_s);
            holder.post_video.start();

        }else{
            holder.post_video.setVisibility(View.GONE);
            holder.post.setVisibility(View.VISIBLE);
            Glide.with(context).load(post_url_s)
                    .centerCrop()
                    .into(holder.post);
        }

        islikes(post_id,holder.like_img,holder.likes_post);
        nrlikes(holder.likes,post_id);
        holder.uploader_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firebaseUser.getUid().equals(itemList.get(position).getUPLOADER_UID())) {
                    Intent i = new Intent(context, UserActivity.class);
                    i.putExtra("id_user", itemList.get(position).getUPLOADER_UID());
                    context.startActivity(i);
                }
            }
        });

        holder.like_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.like_img.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference("Post_likes").child(post_id).child(firebaseUser.getUid()).setValue(true);
                   // userlike(post_id);
                } else {
                    FirebaseDatabase.getInstance().getReference("Post_likes").child(post_id).child(firebaseUser.getUid()).removeValue();
                   // userdislike(post_id);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView post;
        ImageView profileimg;
        TextView likes_post;
        TextView uploader_name;
        ImageView like_img;
        ImageView share_img;
        TextView caption;
        TextView time;
        VideoView post_video;
        TextView likes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_image);
            profileimg=itemView.findViewById(R.id.profile_image);
            likes_post=itemView.findViewById(R.id.likes_post);
            uploader_name=itemView.findViewById(R.id.profile_name);
            like_img=itemView.findViewById(R.id.likes_image);
            share_img=itemView.findViewById(R.id.share_image);
            caption = itemView.findViewById(R.id.caption_post);
            time= itemView.findViewById(R.id.time);
            post_video= itemView.findViewById(R.id.post_video);
            likes= itemView.findViewById(R.id.likes);
        }
    }
    private void islikes(String factid,ImageView imageview,TextView likes_post){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Post_likes").child(factid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageview.setImageResource(R.drawable.like_clicked);
                    imageview.setTag("liked");
                    likes_post.setTextColor(Color.parseColor("#2085F8"));

                }else{
                    imageview.setImageResource(R.drawable.like);
                    imageview.setTag("like");
                    likes_post.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void nrlikes(final TextView likes, String postid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post_likes").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" Likes");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*private void userlike(String factid_ul,String url_us,String uploader_img_us,String uploader_name_us){

        Map<String, Object> us = new HashMap<>();
        us.put("like_img",url_us);
        us.put("uploader_img",uploader_img_us);
        us.put("uploader_name",uploader_name_us);
        FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid())
                .collection("likes")
                .document(factid_ul)
                .set(us)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "check network connection", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void userdislike(String factid_ul){


        FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid())
                .collection("likes")
                .document(factid_ul)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "check network connection", Toast.LENGTH_SHORT).show();
                    }
                });


    }*/
}
