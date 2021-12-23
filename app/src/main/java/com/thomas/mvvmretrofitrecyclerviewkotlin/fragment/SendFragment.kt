package com.thomas.mvvmretrofitrecyclerviewkotlin.fragment

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.thomas.mvvmretrofitrecyclerviewkotlin.BuildConfig
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainActivity
import com.thomas.mvvmretrofitrecyclerviewkotlin.MainViewModel
import com.thomas.mvvmretrofitrecyclerviewkotlin.R
import com.thomas.mvvmretrofitrecyclerviewkotlin.databinding.FragmentSendBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.lang.Long

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SendFragment : Fragment(R.layout.fragment_send) {
    lateinit var viewModel: MainViewModel

    //private var mImageView: ImageView? = null
    private var mUri: Uri? = null
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2

    private var _binding: FragmentSendBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    val TAG = "SendFragment"

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
        _binding = FragmentSendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.presence.observe(viewLifecycleOwner, {
            binding.resultTextView.text = it.name
        })

        binding.btnCapture.setOnClickListener {
            capturePhoto()
        }

        binding.btnChoose.setOnClickListener {
            openGallery()
        }

        binding.btnUpload.setOnClickListener {
            val name = binding.inputNama.text.toString()
            val nik = binding.inputNik.text.toString()
            val image_mask = uploadImageAPI()

            viewModel.postPresence(name, nik, image_mask)
        }
    }

    private fun show(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getStringDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return simpleDateFormat.format(Date())
    }

    fun capturePhoto(){
        val currentDate = getStringDate()

        val capturedImage = File(requireContext().externalCacheDir, currentDate + "_photo.jpg")
        if (capturedImage.exists()) {
            capturedImage.delete()
        }

        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    fun openGallery() {
        val checkSelfPermission = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        activity?.contentResolver?.openInputStream(mUri!!))
                    binding.mImageView.setImageBitmap(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                    }
                }
        }
    }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        var images_data = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.contentResolver?.query(uri!!, images_data, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.mImageView.setImageBitmap(bitmap)
        }
        else {
            show("ImagePath is null")
        }
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(requireContext(), uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority){
                val id = docId.split(":")[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selection)
            }
            else if ("com.android.providers.downloads.documents" == uri?.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse(
                    "content://downloads/public_downloads"), Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
            else if ("com.android.externalstorage.documents" == uri?.authority) {
                imagePath = Environment.getExternalStorageDirectory().toString() + "/" + docId.split(":")[1]
            }
        }
        else if ("content".equals(uri?.scheme, ignoreCase = true)){
            imagePath = getImagePath(uri, null)
        }
        else if ("file".equals(uri?.scheme, ignoreCase = true)){
            imagePath = uri?.path
        }
        renderImage(imagePath)
    }

    private fun imageToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageByte = baos.toByteArray()
        return Base64.encodeToString(imageByte, Base64.DEFAULT)
    }

    private fun uploadImageAPI(): String {
        val drawable = binding.mImageView.drawable as BitmapDrawable
        val bitmap: Bitmap = drawable.bitmap

        return imageToString(bitmap)
    }
}