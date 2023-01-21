package com.biswa1045.alumininetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

lateinit var NAME:String
lateinit var EMAIL:String
lateinit var GENDER:String
lateinit var CURRENT_POSITION:String
lateinit var ADDRESS:String
lateinit var BATCH:String
lateinit var BRANCH:String
var mlist: ArrayList<post> = ArrayList()
lateinit var bottomNavigationView:BottomNavigationView
lateinit var ex:TextView
private var firebaseUser: FirebaseUser? = null
private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
var storageReference: StorageReference? = null
var mRef: DatabaseReference? = null
lateinit var homeFragment:Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment=HomeFragment()
        val profileFragment=ProfileFragment()
        val networkFragment=NetworkFragment()
        val notificationFragment=NotificationFragment()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        post()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.network->setCurrentFragment(networkFragment)
                R.id.add->addMailDialog()
                R.id.notification->setCurrentFragment(notificationFragment)
                R.id.profile->setCurrentFragment(profileFragment)
            }
            true
        }
        profile_data()
    }
    private fun addMailDialog(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_post, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
        dialog.findViewById<CardView>(R.id.post_image)?.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(applicationContext, UploadImageActivity::class.java))
            finish()

        }
        dialog.findViewById<CardView>(R.id.post_video)?.setOnClickListener {
            startActivity(Intent(applicationContext, addVideo::class.java))
            finish()
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    private fun profile_data(){
        val docRef: DocumentReference? = db?.collection("User")?.document(firebaseUser!!.uid)
        docRef!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    NAME = document.getString("NAME").toString()
                    EMAIL = document.getString("EMAIL").toString()
                    BRANCH = document.getString("BRANCH").toString()
                    BATCH = document.getString("PASSOUT BATCH").toString()
                    ADDRESS = document.getString("ADDRESS").toString()
                    GENDER = document.getString("GENDER").toString()
                    CURRENT_POSITION = document.getString("CURRENT_POSITION").toString()
                   // data(NAME,EMAIL,BATCH,BRANCH, ADDRESS, GENDER)
                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception
                )
            }
        }
    }
    override fun onBackPressed() {
        finishAffinity()
    }
    private fun post(){
        //database
        mlist.clear()
        var url:String=""
        var caption:String=""
        var profile_img:String=""
        var profile_name:String=""
        var uploader_uid:String=""
        var post_id:String=""
        var post_time:String=""
        var post_type:String=""
        mRef = FirebaseDatabase.getInstance().getReference("Post")
        mRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                     url= ds.child("Post_url").getValue(String::class.java).toString()
                     caption= ds.child("Caption").getValue(String::class.java).toString()
                     profile_img= ds.child("Uploader_img").getValue(String::class.java).toString()
                     profile_name= ds.child("Uploader_name").getValue(String::class.java).toString()
                     uploader_uid= ds.child("Uploader_uid").getValue(String::class.java).toString()
                     post_id= ds.child("Post_id").getValue(String::class.java).toString()
                     post_time= ds.child("Post_time").getValue(String::class.java).toString()
                    post_type= ds.child("Post_type").getValue(String::class.java).toString()
                    mlist.add(post(url,caption,profile_img,profile_name,uploader_uid,post_id,post_time,post_type))
                }


                setCurrentFragment(homeFragment)
             //   Toast.makeText(this@MainActivity, mlist.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }
}