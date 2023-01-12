package com.biswa1045.alumininetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

lateinit var bottomNavigationView:BottomNavigationView
lateinit var ex:TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment=HomeFragment()
        val profileFragment=ProfileFragment()
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
            startActivity(Intent(applicationContext, UploadVideo::class.java))
            finish()
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onBackPressed() {
        finishAffinity()
    }
}