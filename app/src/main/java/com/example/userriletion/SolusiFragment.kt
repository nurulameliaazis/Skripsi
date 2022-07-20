package com.example.userriletion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.userriletion.databinding.FragmentSolusiBinding
import com.google.firebase.firestore.FirebaseFirestore

class SolusiFragment : Fragment() {

    private var _binding: FragmentSolusiBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolusiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("riletion_collection").document("bacterial-leaf")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.textViewjenis.text = document.data?.get("gejala").toString()
                    binding.textViewgangguan.text = document.data?.get("jenis-gangguan").toString()
                    binding.textViewsolusi.text = document.data?.get("solusi").toString()

                    Toast.makeText(requireActivity(),"DocumentSnapshot data",  Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(),"No Such Document",  Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(),"error",  Toast.LENGTH_SHORT).show()
            }
      val docR = db.collection("riletion_collection").document("brown-spot")
        docR.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.textViewjenis.text = document.data?.get("gejala").toString()
                    binding.textViewgangguan.text = document.data?.get("jenis-gangguan").toString()
                    binding.textViewsolusi.text = document.data?.get("solusi").toString()

                    Toast.makeText(requireActivity(),"DocumentSnapshot data",  Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(),"No Such Document",  Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(),"error",  Toast.LENGTH_SHORT).show()
            }
        val docRe = db.collection("riletion_collection").document("leaf-blast")
        docRe.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.textViewjenis.text = document.data?.get("gejala").toString()
                    binding.textViewgangguan.text = document.data?.get("jenis-gangguan").toString()
                    binding.textViewsolusi.text = document.data?.get("solusi").toString()

                    Toast.makeText(requireActivity(),"DocumentSnapshot data",  Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(),"No Such Document",  Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(),"error",  Toast.LENGTH_SHORT).show()
            }
    }
}
