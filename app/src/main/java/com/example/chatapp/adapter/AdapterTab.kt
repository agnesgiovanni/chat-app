package com.example.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.menu.Chatting
import com.example.chatapp.model.Tab

class AdapterTab(private val list: List<Tab>, private val context: Context) : RecyclerView.Adapter<AdapterTab.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tab_layout, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chatItem = list[position]
        holder.bind(chatItem)

        holder.tvNama.text = chatItem.getNama()
        holder.tvKet.text = chatItem.getKeterangan()
        holder.tvTgl.text = chatItem.getTanggal()

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Chatting::class.java).apply {
                putExtra("ID", chatItem.getID())
                putExtra("nama", chatItem.getNama())
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvKet: TextView = itemView.findViewById(R.id.tv_ket)
        val tvTgl: TextView = itemView.findViewById(R.id.tv_tgl)

        fun bind(chat: Tab) {
            tvNama.text = chat.getNama()
            tvKet.text = chat.getKeterangan()
            tvTgl.text = chat.getTanggal()
        }
    }
}
