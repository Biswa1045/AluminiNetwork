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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment


class NetworkFragment: Fragment(R.layout.fragment_network) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        itemView.findViewById<CardView>(R.id.clubs).setOnClickListener {
            startActivity(Intent(context, clubs::class.java))

        }
        itemView.findViewById<CardView>(R.id.alumini_network).setOnClickListener {
            startActivity(Intent(context, AluminiActivity::class.java))

        }


    }






}