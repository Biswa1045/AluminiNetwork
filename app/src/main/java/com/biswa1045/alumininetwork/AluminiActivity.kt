package com.biswa1045.alumininetwork

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class AluminiActivity : AppCompatActivity() {
    private lateinit var aluminiRecyclerAdapter: AluminiAdapter
    private lateinit var recyclerView:RecyclerView
    private var aluminidatalist: ArrayList<aluminidata> = ArrayList()
   // private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private lateinit var search_edittext:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumini)
        search_edittext = findViewById(R.id.search)
        recyclerView = findViewById(R.id.recycle_alumini)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(applicationContext)
        aluminiRecyclerAdapter= AluminiAdapter(aluminidatalist, applicationContext)
        recyclerView.adapter = aluminiRecyclerAdapter
        aluminidatalist.clear()
        searchUser("all")
        search_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.length != 0) {
                    aluminidatalist.clear()
                    searchUser(s.toString())
                }else{
                    aluminidatalist.clear()
                    searchUser("all")
                }

            }
        })



    }
private fun searchUser(s:String){
    db!!.collection("User")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val firestore_id = document.id
                val name = document.data["NAME"]
                val image = document.data["PHOTO"]
                val branch = document.data["BRANCH"]
                val batch = document.data["PASSOUT BATCH"]
                val a=aluminidata()
                a.NAME= name.toString()
                a.IMAGE= image.toString()
                a.BRANCH= branch.toString()
                a.BATCH= batch.toString()
                a.ID=firestore_id.toString()
                if(s=="all" || s==""){
                    aluminidatalist.add(a)
                }
                else if(a.NAME!!.toLowerCase()==s.toLowerCase(Locale.ROOT)){
                    aluminidatalist.add(a)
                }else{
                //    Toast.makeText(this@AluminiActivity, "No user found in this name", Toast.LENGTH_SHORT).show()

                }

                aluminiRecyclerAdapter.notifyDataSetChanged()

            }
        }
        .addOnFailureListener { exception ->
            Log.w("TAG_data", "Error getting documents.", exception)
        }
    aluminiRecyclerAdapter.notifyDataSetChanged()
}

    fun back_alumini(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}