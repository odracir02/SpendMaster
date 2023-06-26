package com.example.spendmaster

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.spendmaster.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("usuarios")


        binding.bregistrar.setOnClickListener {
            val email = binding.tcorreo.text.toString()
            val password = binding.tcontra.text.toString()
            val confirmPassword = binding.tcontra2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    if (isValidPassword(password)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Usuario creado correctamente
                                    Toast.makeText(
                                        this,
                                        "Usuario creado correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val correo = binding.tcorreo.text.toString()
                                    val documentID = collection.document().id
                                    val nombre = binding.tNombreCreartarea.text.toString()
                                    val user = hashMapOf(
                                        "nombre" to nombre,
                                        "id" to documentID,
                                        "Correo" to correo
                                    )

                                    firestore.collection("usuarios").add(user)
                                        .addOnSuccessListener {
                                            val intent = Intent(this, login2::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                "Error al agregar usuario a la base de datos: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        showAlertDialog("La contraseña no cumple con los requisitos:\n" +
                                "Entre 9 y 20 caracteres\n" +
                                "Una mayúscula, una minúscula, un número y un carácter especial")
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bvolver.setOnClickListener {
            val loginIntent =   Intent(this, login2::class.java)
            startActivity(loginIntent)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[+=!@#\$%^=&.])(?=\\S+$).{9,20}$"
        )
        return passwordPattern.matcher(password).matches()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}