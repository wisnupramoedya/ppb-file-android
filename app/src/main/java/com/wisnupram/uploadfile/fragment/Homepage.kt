package com.wisnupram.uploadfile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wisnupram.uploadfile.databinding.FragmentHomepageBinding

class Homepage : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)

        binding.buttonStart.setOnClickListener {
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}