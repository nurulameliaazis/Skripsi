package com.example.userriletion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.userriletion.databinding.FragmentDetailHistoryBinding
import com.example.userriletion.databinding.FragmentHistoryBinding


class DetailHistoryFragment : Fragment() {

    private var _binding: FragmentDetailHistoryBinding? = null
    private val binding get() = _binding!!
    private val args: DetailHistoryFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val history = args.history

        Glide.with(requireContext()).load(history.imageUrl).into(binding.imageView4)
        binding.textView2.text = history.gejala
        binding.textView3.text = history.jenis_gangguan
        binding.textView11.text = history.solusi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}