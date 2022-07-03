package com.example.userriletion.login
import com.example.userriletion.R
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        email = view.findViewById(R.id.email)
        fAuth = Firebase.auth


        binding.buttondaftar.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            validateEmptyForm()
        }
        binding.buttonmasuk.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun validateEmptyForm() {
        val icon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_warning
        )

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim()) -> {
                username.setError("Masukkan Nama", icon)
            }
            TextUtils.isEmpty(email.text.toString().trim()) -> {
                email.setError("Masukkan Nomor Telephone", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim()) ->{
                password.setError("Masukkan Kata Sandi", icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    email.text.toString().isNotEmpty() -> {
                if (username.text.toString().matches(
                        Regex(
                            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"
                        )
                    )
                )
                    if (password.text.toString().length >= 10) {
                        firebaseSignUp()
                        Toast.makeText(context, "Daftar Akun Berhasil", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        password.setError("Kata Sandi Minamal 10 Angka", icon)
                    }

            }
        }


    }

    private fun firebaseSignUp() {
        fAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Daftar Akun Berhasil", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }

            }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

