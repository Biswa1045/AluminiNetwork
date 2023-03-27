package com.biswa1045.alumininetwork

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class ManagePostsActivity : AppCompatActivity() {
    private var postslist: ArrayList<myposts> = ArrayList()
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private lateinit var adapter: MyPostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_posts)
        val recyclerView: RecyclerView = findViewById(R.id.user_posts)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(applicationContext)
        adapter= MyPostAdapter(postslist, applicationContext)
        recyclerView.adapter = adapter
       /* val a=myposts()
        a.post_id="-NLvYjLhzeZMMYePNVXQ"
        a.caption="sd"
        a.post_url="https://firebasestorage.googleapis.com/v0/b/alumini2-71dda.appspot.com/o/posts%2Fposts_video%2FIHtX2QfsuTggV0kNHQniEvqqMwJ3_20230116_223856.mp4?alt=media&token=550924c7-fe03-4ba8-a040-dee955d99323"
        postslist.add(a)
        adapter.notifyDataSetChanged()*/
        db!!.collection("User").document(firebaseUser!!.uid).collection("uploads")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val firestore_id = document.id
                    val caption = document.data["Caption"]
                    val Post_id = document.data["Post_id"]
                    val Post_url = document.data["Post_url"]
                    val a=myposts()
                    a.post_id= Post_id.toString()
                    a.post_url= Post_url.toString()
                    a.caption= caption.toString()
                    a.firestore_id= firestore_id.toString()

                    postslist.add(a)
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG_data", "Error getting documents.", exception)
            }
        adapter.notifyDataSetChanged()
       /* db!!.collection("User").document(firebaseUser!!.uid).collection("uploads")
            .addSnapshotListener { value, error ->
                if(error!=null){
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            postslist.add(dc.document.toObject(myposts::class.java))

                        }
                    }
                    adapter.notifyDataSetChanged()

                }else{
                Log.e("activity",error+"")
                }
            }*/
    }

    fun back_activity(view: View) {
        onBackPressed()
    }
}