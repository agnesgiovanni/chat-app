package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.menu.ChatFragment
import com.example.chatapp.menu.ContactFragment
import com.example.chatapp.sign.SignIn
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        aturViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.btnKeluar.setOnClickListener {
            dialogKeluar()
        }

    }

    private fun dialogKeluar() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Keluar Dari akun?")
        builder.setPositiveButton("Keluar") { dialogInterface, _ ->
            dialogInterface.dismiss()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        builder.setNegativeButton("Batal") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun aturViewPager(viewPager: ViewPager) {
        val adapter = aturPagerAdapter(supportFragmentManager)
        adapter.tambahFragment(ChatFragment(), "Chat")
        adapter.tambahFragment(ContactFragment(), "Contact")

        viewPager.adapter = adapter
    }

    inner class aturPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()
        private val stringList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return stringList[position]
        }

        fun tambahFragment(fragment: Fragment, judul: String) {
            fragmentList.add(fragment)
            stringList.add(judul)
        }
    }
}
