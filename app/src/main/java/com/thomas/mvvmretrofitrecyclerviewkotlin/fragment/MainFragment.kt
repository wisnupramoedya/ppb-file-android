package com.thomas.mvvmretrofitrecyclerviewkotlin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainActivity
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainAdapter
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainViewModel
import com.thomas.mvvmretrofitrecyclerviewkotlin.R
import com.thomas.mvvmretrofitrecyclerviewkotlin.databinding.FragmentMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    val TAG = "BreakingNewsFragment"

    //private var mImageView: ImageView? = null
    private var mUri: Uri? = null
    private val OPERATION_CAPTURE_PHOTO = 1

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        viewModel.getAllMovies()

        binding.fab.setOnClickListener {
            capturePhoto()
        }
    }
}