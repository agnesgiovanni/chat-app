package com.example.chatapp.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.AdapterTab
import com.example.chatapp.model.Tab

class ChatFragment : Fragment() {

    private val list: MutableList<Tab> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        bacaChat()
        return view
    }

    private fun bacaChat() {
//        list.add(Chat("001", "Abc", "daring", "20/12/2020"))
//        list.add(Chat("002", "Def", "luring", "20/12/2020"))
//        list.add(Chat("003", "Ghi", "boring", "20/12/2020"))

        recyclerView.adapter = AdapterTab(list, requireContext())
    }
}
