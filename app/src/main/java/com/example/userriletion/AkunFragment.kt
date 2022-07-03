package com.example.userriletion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.databinding.FragmentAkunBinding


class AkunFragment : Fragment() {


    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editprofile1Fragment)
        }
        binding.editText.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editprofile1Fragment)
        }
        binding.editText2.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_katasandibaruFragment)
        }
        binding.email.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editprofile1Fragment)
        }
        binding.buttonlogout.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_loginFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}