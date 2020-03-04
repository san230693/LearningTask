package com.learnprogramming.learningtasks

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    //Our variables
    private var mImageView: ImageView? = null
    private lateinit var btnCapture: Button
    private lateinit var btnChoose: Button

    private var Image_Uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val loginbtn = findViewById(R.id.login) as Button
//        loginbtn.setOnClickListener{
//            login()
//        }

        initializeWidgets()

        btnChoose.setOnClickListener{
            Log.d(TAG, "btnChoose clicked")
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

        btnCapture.setOnClickListener{
            Log.d(TAG, "btnCapture clicked")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, CAMERACODE)
                }
                else{
                    openCamera()
                }
            }
            else{
                openCamera()
            }
        }
        //Calender
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //button Click to show dialogues
        datePickerBtn.setOnClickListener{
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view : DatePicker,mYear:Int,mMonth:Int,mDay :Int ->
                displayDateTxt.setText(""+mDay+"/"+mMonth+"/"+mYear)
            },year,month,day)
            dpd.show()
        }
    }

    private fun openCamera() {
       val  values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera")
        Image_Uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Image_Uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    private fun initializeWidgets() {
        btnCapture = findViewById(R.id.CaptureImage)
        btnChoose = findViewById(R.id.pickImage)
        mImageView = findViewById(R.id.Profile)
    }

    private fun pickImageFromGallery() {
        Log.d(TAG, "PickImage Func")
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
        private val CAMERACODE = 1002;
        private val IMAGE_CAPTURE_CODE = 1
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermission func")
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Gallery Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            CAMERACODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
                else
                {
                    Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //handle result of picked image
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult func")
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            mImageView!!.setImageURI(data?.data)
        }

        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            mImageView!!.setImageURI(Image_Uri)
        }
    }

//    private fun dispatchTakePictureIntent() {
//        Log.d(TAG, "dispatchTakePictureIntent clicked")
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }


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

}
