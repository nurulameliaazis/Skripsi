package com.example.userriletion.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.R
import com.example.userriletion.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.masuk.setOnClickListener {
            val email = binding.edittextemail.text.toString()
            val password = binding.edittextpassw.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireActivity(), "Sukses", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Email dan Kata sandi Tidak Sesuai",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            } else {
                Toast.makeText(requireActivity(), "Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                    .show()

            }
        }
        binding.regist.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
//        binding.lupasandi.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_lupakatasandiFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
