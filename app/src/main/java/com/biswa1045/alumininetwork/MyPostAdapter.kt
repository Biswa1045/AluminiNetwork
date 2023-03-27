package com.biswa1045.alumininetwork

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MyPostAdapter(var itemList: List<myposts>, private val context: Context) :
    RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var storageReference = FirebaseStorage.getInstance().reference
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_posts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post_url_s = itemList[position].post_url
        val post_id_s = itemList[position].post_id
        val post_caption_s = itemList[position].caption
       likes(post_id_s,holder.like)
        Glide.with(context).load(post_url_s)
            .centerCrop()
            .error(R.color.black)
            .into(holder.img)
        if(post_caption_s.length>100){
            holder.caption.text=post_caption_s.substring(0,100)+"..."

        }else{
            holder.caption.text=post_caption_s
        }
        holder.edit.setOnClickListener {

            val dialog = BottomSheetDialog(holder.itemView.context)
            val view =  LayoutInflater.from(context).inflate(R.layout.caption_edit, null)
            dialog.setContentView(view)
            dialog.setCancelable(true)

            val caption_edit = dialog.findViewById<EditText>(R.id.caption_edit)
            caption_edit!!.setText(itemList[position].caption)
            dialog.findViewById<Button>(R.id.post_new_caption)!!.setOnClickListener {
                if(caption_edit?.text.toString()!=""){

                    val data: MutableMap<String, Any> = HashMap()
                    data["Caption"] = caption_edit?.text.toString()
                    FirebaseFirestore.getInstance().collection("User")
                        .document(firebaseUser!!.uid)
                        .collection("uploads")
                        .document(itemList[position].firestore_id)
                        .update("Caption",caption_edit?.text.toString())
                        .addOnSuccessListener {
                            val databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(post_id_s).child("Caption")
                            databaseReference.setValue(caption_edit?.text.toString())
                            Toast.makeText(context, "Caption Uploaded!!", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener {
                            Toast.makeText(context, "Caption couldn't update", Toast.LENGTH_SHORT).show()
                        }
                }

            }
            dialog.show()
            }
        holder.delete.setOnClickListener{

            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.delete_dailog)
            dialog.findViewById<TextView>(R.id.delete_dailog).setOnClickListener {
                dialog.findViewById<RelativeLayout>(R.id.delete_visibility).visibility=View.INVISIBLE
                dialog.findViewById<ProgressBar>(R.id.progressbar_delete).visibility=View.VISIBLE
                dialog.setCancelable(false)
                FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser!!.uid)
                    .collection("uploads")
                    .document(itemList[position].firestore_id)
                    .delete()
                    .addOnSuccessListener {
                        val databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(post_id_s)
                        databaseReference.removeValue().addOnCompleteListener {
                            val ref = storageReference!!.child("posts").child(firebaseUser!!.uid + "_" + post_id_s + ".jpg")
                            ref.delete().addOnCompleteListener {
                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }.addOnFailureListener{
                            Toast.makeText(context, "Post Couldn't Delete", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }

                    }.addOnFailureListener {
                        Toast.makeText(context, "Post Couldn't Delete", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

            }
            dialog.findViewById<TextView>(R.id.cancel_post_dailog).setOnClickListener{
                dialog.dismiss()
            }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()


        }

    }

    private fun likes(postIdS: String?,likes_textview:TextView) {

            val databaseReference =
                FirebaseDatabase.getInstance().getReference("Post_likes").child(postIdS.toString())
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    likes_textview.text =  snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    likes_textview.text =  "---"
                }
            })

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var img: ImageView
         var caption:TextView
         var like:TextView
         var edit:LinearLayout
         var delete:LinearLayout

        init {
            delete = itemView.findViewById(R.id.delete_mypost)
            img = itemView.findViewById(R.id.img_myposts)
            caption = itemView.findViewById(R.id.caption_myposts)
            like = itemView.findViewById(R.id.like_myposts)
            edit = itemView.findViewById(R.id.edit_mypost)
        }
    }
}