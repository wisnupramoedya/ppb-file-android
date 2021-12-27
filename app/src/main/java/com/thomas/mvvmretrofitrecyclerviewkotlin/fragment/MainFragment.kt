package com.thomas.mvvmretrofitrecyclerviewkotlin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainActivity
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainAdapter
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainViewModel
import com.thomas.mvvmretrofitrecyclerviewkotlin.R
import com.thomas.mvvmretrofitrecyclerviewkotlin.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    val TAG = "MainFragment"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MainAdapter()
        viewModel = (activity as MainActivity).viewModel

        binding.recyclerview.adapter = adapter

        viewModel.presenceList.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreate: $it")
            adapter.setPresenceList(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {

        })

        viewModel.getAllPresences()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sendFragment)
        }
    }
}