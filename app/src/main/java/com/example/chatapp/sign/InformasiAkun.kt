package com.example.chatapp.sign

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityInformasiAkunBinding
import com.example.chatapp.model.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InformasiAkun : AppCompatActivity() {

    private lateinit var binding: ActivityInformasiAkunBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_informasi_akun)

        progressDialog = ProgressDialog(this)
        klikTombol()
    }

    private fun klikTombol() {
        binding.btnLanjut.setOnClickListener {
            if (binding.edNama.text.isEmpty()) {
                Toast.makeText(applicationContext, "Silahkan mengisi nama akun anda", Toast.LENGTH_SHORT).show()
            } else {
                simpan()
            }
        }
    }

    private fun simpan() {
        progressDialog.setMessage("Sedang Memproses ...")
        progressDialog.show()

        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val ID = firebaseUser.uid
            val akun = Tab(ID,
                firebaseUser.phoneNumber ?: "",
                binding.edNama.text.toString(),
                "", "")

            firebaseFirestore.collection("Akun")
                .document(firebaseUser.uid)
                .collection(ID)
                .add(akun)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss();
                    Toast.makeText(applicationContext, "Berhasil Menyimpan akun", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            progressDialog.dismiss();
            Toast.makeText(applicationContext, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()

        }

    }
}