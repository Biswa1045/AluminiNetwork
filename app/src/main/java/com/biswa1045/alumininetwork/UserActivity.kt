package com.biswa1045.alumininetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var branch:String
    private lateinit var batch:String
    private lateinit var address:String
    private lateinit var gender:String
    private lateinit var current_position:String
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        db = FirebaseFirestore.getInstance()
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("id_user")
            profile_data(value.toString())

        findViewById<CardView>(R.id.send_user).setOnClickListener {


            val intent = Intent(applicationContext,chatActivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("details",batch+" batch")
            intent.putExtra("uid",value.toString())
            startActivity(intent)
        }
        }
    }
    private fun profile_data(user_id:String){
        val docRef: DocumentReference? = db?.collection("User")?.document(user_id)
        docRef!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    name = document.getString("NAME").toString()
                    email = document.getString("EMAIL").toString()
                    branch = document.getString("BRANCH").toString()
                    batch = document.getString("PASSOUT BATCH").toString()
                    address = document.getString("ADDRESS").toString()
                    gender = document.getString("GENDER").toString()
                    current_position = document.getString("CURRENT_POSITION").toString()
                    findViewById<TextView>(R.id.name_user).text=name
                    findViewById<TextView>(R.id.email_user).text=email
                    findViewById<TextView>(R.id.batch_user).text=batch
                    findViewById<TextView>(R.id.branch_user).text=branch
                    findViewById<TextView>(R.id.address_user).text=address
                    findViewById<TextView>(R.id.current_position_user).text=current_position
                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception
                )
            }
        }
    }

    fun back_user(view: View) {
        val i = Intent(this@UserActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }


}