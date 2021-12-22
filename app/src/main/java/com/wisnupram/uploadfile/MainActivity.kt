package com.wisnupram.uploadfile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisnupram.uploadfile.adapter.AttendanceAdapter
import com.wisnupram.uploadfile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var aAdapter = AttendanceAdapter()
    lateinit var viewModel: MainViewModel
    private val presenceResponse = PresenceResponseInterface.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(MainRepository(presenceResponse))).get(MainViewModel::class.java)

        binding.recyclerview.apply {
            adapter = aAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        viewModel.presenceList.observe(this, {
            Log.d(TAG, "onCreate: $it")
            aAdapter.setPresenceList(it)
        })

        viewModel.errorMesssage.observe(this, { })

        viewModel.getAllPresence()

        //supportFragmentManager.beginTransaction().apply {
        //    replace(binding.frame.id, Homepage())
        //    addToBackStack(null)
        //    commit()
        //}
    }
}