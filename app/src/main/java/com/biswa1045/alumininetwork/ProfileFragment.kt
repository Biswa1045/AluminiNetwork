package com.biswa1045.alumininetwork

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment:Fragment(R.layout.fragment_profile) {
    private lateinit var name:TextView
    private lateinit var email:TextView
    private lateinit var batch:TextView
    private lateinit var branch:TextView
    private lateinit var address:TextView
    private lateinit var current_position:TextView
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.fragment_profile, container, false)
        }

        override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
            super.onViewCreated(itemView, savedInstanceState)
            view?.findViewById<LinearLayout>(R.id.logout_l)?.setOnClickListener{
                showpopup_logout()
            }
            view?.findViewById<LinearLayout>(R.id.activity_profile)?.setOnClickListener{
                startActivity(Intent(context, UploadsActivity::class.java))
                // finish()
            }
            view?.findViewById<LinearLayout>(R.id.spam_activity)?.setOnClickListener{
                //   startActivity(Intent(applicationContext, UploadsActivity::class.java))
                //  finish()
            }
            view?.findViewById<LinearLayout>(R.id.privacy_profile)?.setOnClickListener{
                startActivity(Intent(context, PrivacyActivity::class.java))
                // finish()
            }
            name = view?.findViewById(R.id.name_p)!!
            email = view?.findViewById(R.id.email_p)!!
            batch = view?.findViewById(R.id.batch_p)!!
            branch = view?.findViewById(R.id.branch_p)!!
            address = view?.findViewById(R.id.address_p)!!
            current_position = view?.findViewById(R.id.current_position)!!
            name.text=NAME
            email.text=com.biswa1045.alumininetwork.EMAIL
            batch.text=com.biswa1045.alumininetwork.BATCH+" - Batch"
            branch.text=com.biswa1045.alumininetwork.BRANCH
            address.text=com.biswa1045.alumininetwork.ADDRESS
            current_position.text=com.biswa1045.alumininetwork.CURRENT_POSITION
        }


    private fun showpopup_logout(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.logout_dailog)
        dialog.findViewById<TextView>(R.id.logout_dailog).setOnClickListener {
            dialog.dismiss()
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(context, login::class.java)
            startActivity(intent)
        }
        dialog.findViewById<TextView>(R.id.cancel_dailog).setOnClickListener{
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}