package com.aodev.cmchat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aodev.cmchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

private const val TAG = "RegisterActivity"
private const val SELECT_PHOTO_CODE = 0

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var selectedPhotoUri: Uri? = null
    private lateinit var mUsername :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register_button_register.setOnClickListener {
            mUsername = username_edittext_register.text.toString()
            registerUser()
        }

        already_have_an_account_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PHOTO_CODE)
        }
    }

    private fun registerUser() {
        val mEmail = email_edittext_register.text.toString()
        val mPassword = password_edittext_register.text.toString()

        if (mEmail.isNullOrEmpty() || mPassword.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter your Email or Password", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
//                        val user = auth.currentUser
                    uploadToFirebaseStore()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                        updateUI(null)
                }
            }
    }

    private fun uploadToFirebaseStore() {
        if (selectedPhotoUri == null) return

        val fileName = UUID.randomUUID().toString()
        val db = FirebaseStorage.getInstance().getReference("/images/${fileName}")
        db.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d(TAG, "addOnSuccessListener: upload successfully")
            db.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "file location: ${it}")
                insertUserToDatabase(it.toString())
            }
        }.addOnFailureListener {
            Log.d(TAG, "addOnFailureListener: upload fail")
        }
    }

    private fun insertUserToDatabase(imageUrl:String) {
        val uuid = FirebaseAuth.getInstance().uid?:""
        val db = FirebaseDatabase.getInstance().getReference("/users/${uuid}")
        val user = User(uuid,mUsername,imageUrl)

        db.setValue(user).addOnSuccessListener {
            Log.d(TAG, "insert user to FirebaseDatabase: is Successfully")
            val intent = Intent(this,LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Log.e(TAG, "insert user to FirebaseDatabase: is fail : ${it.message.toString()}")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap: Bitmap
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            selectedPhotoUri
                        )
//                        imageView.setImageBitmap(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            this.contentResolver,
                            selectedPhotoUri!!
                        )
                        bitmap = ImageDecoder.decodeBitmap(source)
//                        imageView.setImageBitmap(bitmap)
                    }
                    photo_imageview_register.setImageBitmap(bitmap)
                    selectphoto_button_register.alpha = 0f
//                    val bitmapDrawable = BitmapDrawable(bitmap)
//                    selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}