package com.biswa1045.alumininetwork

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment:Fragment(R.layout.fragment_profile) {

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