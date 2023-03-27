package com.biswa1045.alumininetwork

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class AluminiAdapter(var itemList: List<aluminidata>, private val context: Context) :
    RecyclerView.Adapter<AluminiAdapter.ViewHolder>() {
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var storageReference = FirebaseStorage.getInstance().reference
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user_img = itemList[position].IMAGE
        val user_batch= itemList[position].BATCH
        val user_branch= itemList[position].BRANCH
        val user_name= itemList[position].NAME

        holder.name.text=user_name
        holder.details.text=user_batch+" . "+user_branch
        Glide.with(context).load(user_img)
            .centerCrop()
            .error(R.color.black)
            .into(holder.img)
        holder.itemView.setOnClickListener(){
           // if (firebaseUser!!.uid != itemList[position].ID) {
                val i = Intent(context, UserActivity::class.java)
                i.putExtra("id_user", itemList[position].ID)
                context.startActivity(i)
          //  }
        }


    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var img: ImageView
         var name: TextView
         var details: TextView

        init {
            img = itemView.findViewById(R.id.img_myposts)
            name = itemView.findViewById(R.id.name_user)
            details = itemView.findViewById(R.id.datails_user)


        }
    }
}