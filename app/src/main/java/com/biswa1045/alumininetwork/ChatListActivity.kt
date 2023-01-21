package com.biswa1045.alumininetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatListActivity : AppCompatActivity() {
    private lateinit var user_recycle: RecyclerView
    private lateinit var userList: ArrayList<Chat_User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mref: DatabaseReference
    private lateinit var progress_circular_bar: ProgressBar
    private var firebaseUser: String = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        mAuth = FirebaseAuth.getInstance()
        mref = FirebaseDatabase.getInstance().reference
        userList = ArrayList()
        adapter = UserAdapter(this,userList)
        user_recycle = findViewById(R.id.recycle_chatlist)
        user_recycle.layoutManager = LinearLayoutManager(this)
        user_recycle.adapter = adapter
        progress_circular_bar = findViewById(R.id.progress_circular_bar)

        mref.child("chats_users").child(firebaseUser.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(ds in snapshot.children){
                   val uid= ds.child("uid").getValue(String::class.java).toString()
                   val starting= ds.child("starting").getValue(String::class.java).toString()

                        userList.add(Chat_User(uid,starting))

                }
                adapter.notifyDataSetChanged()
                progress_circular_bar.visibility= View.INVISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun back_chat_list(view: View) {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}