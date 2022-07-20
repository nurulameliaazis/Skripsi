package com.example.userriletion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userriletion.databinding.FragmentHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsArrayList: ArrayList<News>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val myAdapter = MyAdapter(requireContext(), newsArrayList)
            adapter = myAdapter

        }
        fetchData()
    }

    private fun fetchData() {
        FirebaseFirestore.getInstance().collection("riletion_collection")
            .get()
            .addOnSuccessListener { documents ->
                val realData = ArrayList<News>()
                for (document in documents) {
                    val news = documents.toObjects(News::class.java)

                    binding.list.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                        val myAdapter = MyAdapter(requireContext(),
                            news.toMutableList() as ArrayList<News>
                        )
                        adapter = myAdapter

                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), "${it.localizedMessage}.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun dataInitialize() {
        newsArrayList = arrayListOf()
        val news1 = News(R.drawable.brown_spot, "Ini item 1")
        val news2 = News(R.drawable.bacterial_leaf_blight, "Ini item 2")

        newsArrayList.add(news1)
        newsArrayList.add(news2)


    }
}