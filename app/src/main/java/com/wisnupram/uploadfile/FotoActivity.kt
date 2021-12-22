package com.wisnupram.uploadfile

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.wisnupram.uploadfile.model.UploadedImageResponse
import com.wisnupram.uploadfile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

class FotoActivity : AppCompatActivity() {
    private var mImageView: ImageView? = null
    private var mUri: Uri? = null

    private lateinit var btnCapture: Button
    private lateinit var btnChoose: Button
    private lateinit var txtResult: TextView

    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foto_activity)

        initializeWidgets()

        btnCapture.setOnClickListener{
            capturePhoto()
        }
        btnChoose.setOnClickListener{
            openGallery()
        }
    }

    private fun imageToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageByte = baos.toByteArray()
        return Base64.encodeToString(imageByte, Base64.DEFAULT)
    }

    private fun uploadImageAPI(bitmap: Bitmap) {
        val img: String = imageToString(bitmap)
        RetrofitClient.instance.postImage(img).enqueue(object : Callback<UploadedImageResponse> {
            override fun onResponse(
                call: Call<UploadedImageResponse>,
                response: Response<UploadedImageResponse>
            ) {
                val responseText = "${response.body()?.msg}"
                txtResult.text = responseText
            }

            override fun onFailure(call: Call<UploadedImageResponse>, t: Throwable) {
                txtResult.text = t.message
            }

        })

        val toast = Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT)
        toast.show()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>
                                            , grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when(requestCode){
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(mUri!!))
                    mImageView!!.setImageBitmap(bitmap)
                    uploadImageAPI(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                    }
                }
        }
    }

    private fun initializeWidgets() {
        btnCapture = findViewById(R.id.btnCapture)
        btnChoose = findViewById(R.id.btnChoose)
        mImageView = findViewById(R.id.mImageView)
        txtResult = findViewById(R.id.txtResult)
    }

    private fun show(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun getStringDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return simpleDateFormat.format(Date())
    }

    private fun capturePhoto(){
        val currentDate = getStringDate()

        val capturedImage = File(externalCacheDir, currentDate + "_photo.jpg")
        if(capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "com.wisnupram.uploadfile.fileprovider", capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            mImageView?.setImageBitmap(bitmap)
            uploadImageAPI(bitmap)
        }
        else {
            show("ImagePath is null")
        }
    }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        var images_data = arrayOf(MediaStore.Images.Media.DATA);
        val cursor = contentResolver.query(uri!!, images_data, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(this, uri)){
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
}