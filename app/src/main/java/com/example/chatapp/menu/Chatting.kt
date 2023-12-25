package com.example.chatapp.menu

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.AdapterPesan
import com.example.chatapp.databinding.ActivityChattingBinding
import com.example.chatapp.model.Pesan
import com.example.chatapp.model.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Chatting : AppCompatActivity() {

    private lateinit var binding: ActivityChattingBinding

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var IDPenerima: String
    private lateinit var adapter: AdapterPesan
    private var list: MutableList<Pesan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting);

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().reference

        val intent = intent
        val nama = intent.getStringExtra("nama")
        IDPenerima = intent.getStringExtra("ID").toString()

        if (IDPenerima != null) {
            binding.tvNamaKontak.text = nama
        }

        binding.btnKirim.setOnClickListener { view ->
            if (!binding.edTulisPesan.text.isNullOrEmpty()) {
                kirimPesan(binding.edTulisPesan.text.toString())
                binding.edTulisPesan.setText("")
            }
        }

        list = ArrayList()
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager

        bacaPesan()

    }

    private fun kirimPesan(text: String) {
        val date = Calendar.getInstance().time
        val tanggal = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        val hari = tanggal.format(date)

        val calendar = Calendar.getInstance()
        val pukul = SimpleDateFormat("hh mm", Locale.getDefault())
        val jam = pukul.format(calendar.time)

        val pesan = Pesan(
            text,
            firebaseUser.uid,
            IDPenerima,
            "$hari $jam"
        )

        reference.child("Pesan").push().setValue(pesan)
            .addOnSuccessListener {
                Log.d("Pengiriman", "Berhasil")
            }

        val reference1 = FirebaseDatabase.getInstance().getReference("Daftar Chat")
            .child(firebaseUser.uid)
            .child(IDPenerima)
        reference1.child("IDChat").setValue(IDPenerima)

        val reference2 = FirebaseDatabase.getInstance().getReference("Daftar Chat")
            .child(firebaseUser.uid)
            .child(IDPenerima)
        reference1.child("IDChat").setValue(IDPenerima)
    }

    private fun bacaPesan() {
        try {
            val reference = FirebaseDatabase.getInstance().getReference()
            reference.child("Pesan").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val pesan = snapshot.getValue(Pesan::class.java)
                        if (pesan != null &&
                            (pesan.pengirim == firebaseUser.uid && pesan.penerima == IDPenerima ||
                                    pesan.penerima == firebaseUser.uid && pesan.pengirim == IDPenerima)
                        ) {
                            list.add(pesan)
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter = AdapterPesan(list, this@Chatting)
                        binding.recyclerView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled if needed
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}