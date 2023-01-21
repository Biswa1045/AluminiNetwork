package com.biswa1045.alumininetwork

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private  var NAME_adp: String? =null
private  var BRANCH_adp: String? =null
private  var BATCH_adp: String? =null
class UserAdapter(val context: Context, val userList: ArrayList<Chat_User>):

    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.user_item,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val currentUser = userList[position]
        var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
        val docRef: DocumentReference? = db?.collection("User")?.document(currentUser.uid.toString())
        docRef!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    NAME_adp = document.getString("NAME").toString()
                    BRANCH_adp = document.getString("BRANCH").toString()
                    BATCH_adp = document.getString("PASSOUT BATCH").toString()
                    holder.textname.text = NAME_adp
                    holder.details.text= BATCH_adp +" . "+ BRANCH_adp
                    //  CURRENT_POSITION = document.getString("CURRENT_POSITION").toString()

                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception
                )
            }
        }


        if(currentUser.starting=="true"){
            holder.new_msg.visibility=View.VISIBLE
        }else{
            holder.new_msg.visibility=View.INVISIBLE
        }

        holder.itemView.setOnClickListener{
            val intent5 = Intent(context,chatActivity::class.java)
            intent5.putExtra("name",NAME_adp)
            intent5.putExtra("details", BATCH_adp +" . "+ BRANCH_adp)
            intent5.putExtra("uid",currentUser.uid)
            context.startActivity(intent5)
        }
    }
    private fun  profile_data(uid:String){

    }
    override fun getItemCount(): Int {
        return userList.size
    }
    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.name_chat_item)
        val details = itemView.findViewById<TextView>(R.id.details_chat_item)
        val image = itemView.findViewById<ImageView>(R.id.user_item)
        val new_msg = itemView.findViewById<ImageView>(R.id.new_msg)
    }
}


