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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Chatting : AppCompatActivity() {

    private lateinit var binding: ActivityChattingBinding

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var IDPenerima: String
    private lateinit var adapter: AdapterPesan
    private lateinit var list: List<Pesan>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting);

        val intent = intent
        val nama = intent.getStringExtra("nama")
        val IDPenerima = intent.getStringExtra("id")

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
        val jam = pukul.format(date)

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
    }

    private void bacaPesan(){
        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Pesan").addValueEventListener(new ValueEventListener(){
                @Override
                public voidonDataChange(@NonNull DataSnapshot dataSnapshot){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Pesan pesan = snapshot.getValue(Pesan.class);
                        if(pesan != null
                            && pesan.getPengirim().equals(firebaseUser.getUid())
                            && pesan.getPenerima().equals(IDPenerima)
                            || pesan.getPenerima().equals(firebaseUser.getUid())
                            && pesan.getPengirim().equals(IDPenerima)){
                            list.add(pesan)
                        }
                    }
                    if(adapter != null){
                        adapter.notifyDataSetChanged()
                    }else {
                        adapter = new AdapterPesan(list, Chatting.this)
                        binding.recyclerView.setAdapter(adapter)
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error){

                }
            })
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}