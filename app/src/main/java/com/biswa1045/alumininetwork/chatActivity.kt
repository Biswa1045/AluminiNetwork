package com.biswa1045.alumininetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class chatActivity : AppCompatActivity() {
    private lateinit var chat_rec: RecyclerView
    private lateinit var msg: EditText
    private lateinit var send: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mref: DatabaseReference
    var receiverRoom: String? = null
    var senderRoom: String? = null
    var senderuid:String?=null
    var receiveruid:String?=null
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val bundle:Bundle?=intent.extras
        val name = bundle?.getString("name").toString()
        val details = bundle?.getString("details").toString()
            receiveruid =  bundle?.getString("uid").toString()
            senderuid = FirebaseAuth.getInstance().currentUser?.uid
        mref = FirebaseDatabase.getInstance().getReference()
        senderRoom = receiveruid+  senderuid
        receiverRoom = senderuid+  receiveruid
        findViewById<TextView>(R.id.name_chat).text=name
        findViewById<TextView>(R.id.details_chat).text=details
        chat_rec = findViewById(R.id.chat_rec)
        msg = findViewById(R.id.chat_msg)
        send = findViewById(R.id.send_msg_img)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        chat_rec.layoutManager = LinearLayoutManager(this)
        chat_rec.adapter = messageAdapter
        //add data to recycler view
        mref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postsnapshot in snapshot.children){
                        val message = postsnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        send.setOnClickListener{

            val message = msg.text.toString()
            if(message != ""){
                val Chat_User_Object_send = Chat_User(receiveruid.toString(),"false")
                mref.child("chats_users").child(senderuid.toString()).child(receiveruid.toString())
                    .setValue(Chat_User_Object_send).addOnSuccessListener {
                        val Chat_User_Object_rec = Chat_User(senderuid.toString(),"true")
                        mref.child("chats_users").child(receiveruid.toString()).child(senderuid.toString()).setValue(Chat_User_Object_rec)
                    }
                val messageObject = Message(message,senderuid)
                mref.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                     //   mref.child("chats").child(senderRoom!!).child("direction").setValue("$senderuid to $receiveruid")
                        mref.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)
                    }
                msg.setText("")
            }else{

            }

        }

    }
    private fun profile_data(uid:String){
        val docRef: DocumentReference? = db?.collection("User")?.document(uid)
        docRef!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val NAME = document.getString("NAME").toString()
                    val BRANCH = document.getString("BRANCH").toString()
                    val BATCH = document.getString("PASSOUT BATCH").toString()

                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception
                )
            }
        }
    }

    fun back_chat(view: View) {
        startActivity(Intent(applicationContext, ChatListActivity::class.java))
    }
}