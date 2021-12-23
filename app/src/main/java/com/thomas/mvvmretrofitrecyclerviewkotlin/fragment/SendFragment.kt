package com.thomas.mvvmretrofitrecyclerviewkotlin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.thomas.mvvmretrofitrecyclerviewkotlin.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendFragment : Fragment(R.layout.fragment_send) {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false)
    }

    private fun show(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getStringDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return simpleDateFormat.format(Date())
    }

    private fun capturePhoto(){
        val currentDate = getStringDate()

        val capturedImage = File(requireContext().externalCacheDir, currentDate + "_photo.jpg")
        if (capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(requireContext(), "com.thomas.mvvmretrofitrecyclerviewkotlin.fileprovider", capturedImage)
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