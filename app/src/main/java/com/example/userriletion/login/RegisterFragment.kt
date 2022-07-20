package com.example.userriletion.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.R
import com.example.userriletion.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.textViewmasuk.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.buttondaftar.setOnClickListener {
            val email = binding.edittextemail.text.toString()
            val password = binding.edittextpass.text.toString()
            val confimPass = binding.edittextpassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confimPass.isNotEmpty()) {
                if (password == confimPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireActivity(), "Sukses", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "${it.exception}.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                } else {
                    Toast.makeText(requireActivity(), "Kata Sandi Tidak Sama", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireActivity(), "Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
