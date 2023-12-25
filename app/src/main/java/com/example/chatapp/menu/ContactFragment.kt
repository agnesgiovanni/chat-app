package com.example.chatapp.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.adapter.AdapterTab
import com.example.chatapp.databinding.FragmentTabBinding
import com.example.chatapp.model.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ContactFragment : Fragment() {

    private lateinit var binding: FragmentTabBinding
    private val list = ArrayList<Tab>()
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()

        if (firebaseUser != null) {
            bacaKontak()
        }

        return binding.root;

    }

    private fun bacaKontak() {
        firebaseFirestore.collection("Akun").get().addOnSuccessListener { queryDocumentSnapshots ->
            for (snapshot in queryDocumentSnapshots) {
                val ID = snapshot.getString("id")

                val akun = Tab(
                    ID ?: "",
                    snapshot.getString("noTelp") ?: "",
                    snapshot.getString("nama") ?: "",
                    snapshot.getString("keterangan") ?: "",
                    snapshot.getString("tanggal") ?: ""
                )

                if (ID != null && ID != firebaseUser.uid) {
                    list.add(akun)
                }
            }
            binding.recyclerView.adapter = AdapterTab(list, requireContext())
        }
    }


}