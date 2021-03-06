package com.example.userriletion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.userriletion.databinding.FragmentSolusiBinding
import com.example.userriletion.model.History
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SolusiFragment : Fragment() {

    private var _binding: FragmentSolusiBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseFirestore
    private val args: SolusiFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolusiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromFirebase()

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_solusiFragment_to_homeFragment)
        }
    }

    private fun getDataFromFirebase() {
        database = FirebaseFirestore.getInstance()
        val fileName = args.fileName
        val firebaseStorage =
            FirebaseStorage.getInstance().getReference("Image/$fileName").downloadUrl

        firebaseStorage.addOnSuccessListener { imageUrl ->
            val user = Firebase.auth.currentUser?.uid //Mendapatkan ID User
            val docRef = database.collection("riletion_collection").document("bacterial-leaf")
            docRef.get().addOnSuccessListener { document ->
                val gejala = document.data?.get("gejala").toString()
                val jenis_gangguan = document.data?.get("jenis-gangguan").toString()
                val solusi = document.data?.get("solusi").toString()

                val history = History(
                    gejala = gejala,
                    jenis_gangguan = jenis_gangguan,
                    solusi = solusi,
                    imageUrl = imageUrl.toString()
                )

                val addHistory =
                    database.collection("history-collection").document("$user")
                        .collection("histories").document(fileName).set(history)

                addHistory.addOnSuccessListener {
                    binding.textViewjenis.text = jenis_gangguan
                    binding.textViewsolusi.text = solusi
                    binding.textViewgangguan.text = gejala
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Gagal menambahkan data history",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireActivity(), "error $exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
