package com.example.spendmaster

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.example.spendmaster.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class perfil : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val usuario = intent.getStringExtra("usuario") ?: ""
        binding.TvEmail.text = usuario

        binding.profileImage.setOnClickListener {
            openGallery()
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(contentResolver, imageUri)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            val inputStream = contentResolver.openInputStream(imageUri)
                            BitmapFactory.decodeStream(inputStream)
                        }

                        binding.profileImage.setImageBitmap(bitmap)

                        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                        roundedBitmapDrawable.isCircular = true
                        binding.profileImage.setImageDrawable(roundedBitmapDrawable)
                    }
                }
            }
        }

        firestore.collection("usuarios")
            .whereEqualTo("Correo", usuario)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nombre = document.getString("nombre")
                    binding.TvUsername.text = nombre
                }
            }
            .addOnFailureListener { exception ->
                // Manejo de errores en caso de fallo
            }

        binding.bvolver.setOnClickListener {
            val loginIntent =   Intent(this, gestionarelperfil::class.java)
            startActivity(loginIntent)
        }
        binding.btLogout.setOnClickListener {
            val loginIntent =   Intent(this, login2::class.java)
            startActivity(loginIntent)
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }
}


