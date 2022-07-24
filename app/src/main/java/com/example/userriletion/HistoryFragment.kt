package com.example.userriletion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userriletion.databinding.FragmentHistoryBinding
import com.example.userriletion.model.History
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment(), MyAdapter.MyAdapterInterface {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
    }

    private fun fetchData() {
        val user = Firebase.auth.currentUser?.uid
        FirebaseFirestore.getInstance()
            .collection("history-collection")
            .document("$user")
            .collection("histories")
            .get()
            .addOnSuccessListener { documents ->
                val histories = ArrayList<History>()
                for (document in documents) {
                    val data = document.data
                    histories.add(
                        History(
                            gejala = data["gejala"].toString(),
                            imageUrl = data["imageUrl"].toString(),
                            jenis_gangguan = data["jenis_gangguan"].toString(),
                            solusi = data["solusi"].toString()
                        )
                    )
                }

                if (histories.size > 0) {
                    binding.list.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                        val myAdapter = MyAdapter(requireContext(), histories, this@HistoryFragment)
                        adapter = myAdapter

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "${it.localizedMessage}.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onItemClicked(history: History) {
        val action = HistoryFragmentDirections.actionHistoryFragmentToDetailHistoryFragment(history)
        findNavController().navigate(action)
    }
}