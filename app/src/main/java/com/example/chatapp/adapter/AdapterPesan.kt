package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.Pesan
import com.example.chatapp.model.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdapterPesan(private val list: List<Pesan>, private val context: Context) : RecyclerView.Adapter<AdapterPesan.ViewHolder>() {

    private lateinit var firebaseUser: FirebaseUser

    companion object {
        const val pesanKirim = 0
        const val pesanTerima = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == pesanKirim) {
            val view = LayoutInflater.from(context).inflate(R.layout.pesan_kirim_layout, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.pesan_terima_layout, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.gabung(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val isiPesan: TextView = itemView.findViewById(R.id.tv_isi_pesan)

        fun gabung(pesan: Pesan) {
            isiPesan.text = pesan.getIsiPesan()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (list[position].getPengirim() == firebaseUser?.uid) {
            return pesanKirim
        } else {
            return pesanTerima
        }
        return super.getItemViewType(position)
    }


}
