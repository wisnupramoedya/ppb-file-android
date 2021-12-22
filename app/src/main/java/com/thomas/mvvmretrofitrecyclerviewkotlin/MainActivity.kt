package com.thomas.mvvmretrofitrecyclerviewkotlin

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thomas.mvvmretrofitrecyclerviewkotlin.databinding.ActivityMainBinding
import com.thomas.mvvmretrofitrecyclerviewkotlin.network.PresenceResponseInterface
import com.thomas.mvvmretrofitrecyclerviewkotlin.network.RetrofitClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitClient.instance
    val adapter = MainAdapter()

    //private var mImageView: ImageView? = null
    private var mUri: Uri? = null
    private val OPERATION_CAPTURE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)

        binding.recyclerview.adapter = adapter

        viewModel.presenceList.observe(this, {
            Log.d(TAG, "onCreate: $it")
            adapter.setPresenceList(it)
        })

        viewModel.errorMessage.observe(this, {

        })

        viewModel.getAllMovies()

        val fab: View = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            capturePhoto()
        }
    }

    private fun show(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun getStringDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return simpleDateFormat.format(Date())
    }

    private fun capturePhoto(){
        val currentDate = getStringDate()

        val capturedImage = File(externalCacheDir, currentDate + "_photo.jpg")
        if (capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "com.thomas.mvvmretrofitrecyclerviewkotlin.fileprovider", capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

//    private fun renderImage(imagePath: String?){
//        if (imagePath != null) {
//            val bitmap = BitmapFactory.decodeFile(imagePath)
//            mImageView?.setImageBitmap(bitmap)
//            uploadImageAPI(bitmap)
//        }
//        else {
//            show("ImagePath is null")
//        }
//    }
}