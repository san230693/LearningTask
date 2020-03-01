package com.learnprogramming.learningtasks

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    //Our variables
    private var mImageView: ImageView? = null
    private var mUri: Uri? = null
    //Our widgets
    private lateinit var btnCapture: Button
    private lateinit var btnChoose: Button
    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1000
    private val OPERATION_CHOOSE_PHOTO = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginbtn = findViewById(R.id.login) as Button
        loginbtn.setOnClickListener{
            login()
        }

        initializeWidgets()

        btnChoose.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
//        btnCapture.setOnClickListener { capturePhoto() }
//        btnChoose.setOnClickListener {
//            //check permission at runtime
//            val checkSelfPermission = ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
//                //Requests permissions to be granted to this application at runtime
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
//                )
//            } else {
//                openGallery()
//            }
//        }
    }

    fun login() {
            Log.d(TAG, "loginbtn Clicked")
            hideKeyboard()
            var textViewUsername = findViewById<TextInputEditText>(R.id.usernameInputBox)
            var textViewPassword = findViewById<TextInputEditText>(R.id.passwordInputBox)
            var userName = textViewUsername.text.toString()
            var password = textViewPassword.text.toString()

            if (userName != "" && password != "") {
                if (userName == "Testing" && password == "123456789")
                {
                    Log.d(TAG, "userName : $userName password $password")
                    Toast.makeText(
                        this,
                        "userName : $userName password $password",
                        Toast.LENGTH_SHORT
                    ).show()
                    setContentView(R.layout.activity_main)
                }
                else {
                    Toast.makeText(this, "Username and password mismatch", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else
            {
                Toast.makeText(this, "Username and password Cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.SHOW_FORCED
        )
    }

    private fun initializeWidgets() {
        btnCapture = findViewById(R.id.CaptureImage)
        btnChoose = findViewById(R.id.pickImage)
        mImageView = findViewById(R.id.Profile)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            mImageView!!.setImageURI(data?.data)
        }
    }

//    private fun show(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun capturePhoto() {
//        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
//        if (capturedImage.exists()) {
//            capturedImage.delete()
//        }
//        capturedImage.createNewFile()
//        mUri = if (Build.VERSION.SDK_INT >= 24) {
//            FileProvider.getUriForFile(
//                this, "info.camposha.kimagepicker.fileprovider",
//                capturedImage
//            )
//        } else {
//            Uri.fromFile(capturedImage)
//        }
//
//        val intent = Intent("android.media.action.IMAGE_CAPTURE")
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
//        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
//    }
//
//    private fun openGallery() {
//        val intent = Intent("android.intent.action.GET_CONTENT")
//        intent.type = "image/*"
//        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
//    }
//
//    private fun renderImage(imagePath: String?) {
//        if (imagePath != null) {
//            val bitmap = BitmapFactory.decodeFile(imagePath)
//            mImageView?.setImageBitmap(bitmap)
//        } else {
//            show("ImagePath is null")
//        }
//    }
//
//    private fun getImagePath(uri: Uri?, selection: String?): String {
//        var path: String? = null
//        val cursor = contentResolver.query(uri!!, null, selection, null, null)
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
//            }
//            cursor.close()
//        }
//        return path!!
//    }
//
//    @TargetApi(19)
//    private fun handleImageOnKitkat(data: Intent?) {
//        var imagePath: String? = null
//        val uri = data!!.data
//        //DocumentsContract defines the contract between a documents provider and the platform.
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            val docId = DocumentsContract.getDocumentId(uri)
//            if ("com.android.providers.media.documents" == uri!!.authority) {
//                val id = docId.split(":")[1]
//                val selsetion = MediaStore.Images.Media._ID + "=" + id
//                imagePath = getImagePath(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    selsetion
//                )
//            } else if ("com.android.providers.downloads.documents" == uri.authority) {
//                val contentUri = ContentUris.withAppendedId(
//                    Uri.parse(
//                        "content://downloads/public_downloads"
//                    ), java.lang.Long.valueOf(docId)
//                )
//                imagePath = getImagePath(contentUri, null)
//            }
//        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
//            imagePath = getImagePath(uri, null)
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            imagePath = uri.path
//        }
//        renderImage(imagePath)
//    }




}
