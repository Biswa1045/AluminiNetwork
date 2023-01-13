package com.biswa1045.alumininetwork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

 lateinit var NAME:String
lateinit var EMAIL:String
lateinit var GENDER:String
lateinit var ADDRESS:String
lateinit var BATCH:String
lateinit var BRANCH:String
lateinit var bottomNavigationView:BottomNavigationView
lateinit var ex:TextView
private var firebaseUser: FirebaseUser? = null
private var db: FirebaseFirestore? = null
var storageReference: StorageReference? = null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment=HomeFragment()
        val profileFragment=ProfileFragment()
        db = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.add->addMailDialog()
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
            startActivity(Intent(applicationContext, UploadVideoActivity::class.java))
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
}