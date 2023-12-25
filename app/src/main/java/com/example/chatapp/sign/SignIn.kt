package com.example.chatapp.sign

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivitySignInBinding
import com.example.chatapp.model.Tab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    private var mVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var progressDialog: ProgressDialog
    private var mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var mUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        mFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance()

        mUser = FirebaseAuth.getInstance().currentUser;
        if (mUser != null) {
            startActivity(Intent(this, InformasiAkun::class.java))
            finish();
        }

        progressDialog = ProgressDialog(this)

        binding.btnMasuk.setOnClickListener {
            if (binding.btnMasuk.text.toString() == "Masuk") {
                progressDialog.setMessage("Mengirim Kode Verifikasi")
                progressDialog.show()

                val nponsel = binding.edPonsel.text.toString()
                startPhoneNumberVerification(nponsel)
            } else {
                progressDialog.setMessage("Memferifikasi Kode")
                progressDialog.show()

                verifyPhoneNumberWithCode(mVerificationId, binding.edVerifikasi.text.toString())
            }
        }


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "Verifikasi Berhasil")
                signInWithPhoneAuthCredential(credential) // Pass the received 'credential' parameter
                progressDialog.dismiss()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "Verifikasi Gagal ${e.message}")
                progressDialog.dismiss()
            }


            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                resendToken = token

                binding.btnMasuk.text = "Verifikasi"
                progressDialog.dismiss();
            }

        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user

                    user?.let { currentUser ->
                        val ID = currentUser.uid
                        val akun = Tab(ID, currentUser.phoneNumber ?: "", "", "", "")

                        mFirestore.collection("Akun")
                            .document(currentUser.uid)
                            .collection(ID)
                            .add(akun)
                            .addOnSuccessListener { documentReference ->
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(applicationContext, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                                // Optionally log the error or handle the failure further
                            }
                    }


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }
}