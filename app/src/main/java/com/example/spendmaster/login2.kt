package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import com.example.spendmaster.databinding.ActivityLogin2Binding
import com.google.firebase.auth.FirebaseAuth

class login2 : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val Google_Sign_In = 100
    private lateinit var binding: ActivityLogin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()



        binding.biniciarsesion.setOnClickListener {
            val email = binding.usuario.text.toString()
            val password = binding.contra.text.toString()

            val usuario = binding.usuario.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, primerapantalla::class.java)
                            intent.putExtra("usuario", usuario)
                            intent.putExtra("user", usuario)
                            startActivity(intent)
                            finish() // Cierra la actividad actual para que no se pueda volver atrás
                        } else {
                            val toast = Toast.makeText(this, "Usuario o contraseña no válidos", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                    }
            } else {
                Toast.makeText(this, "Rellena los campos", Toast.LENGTH_LONG).show()
            }
        }

        binding.bregistrarse.setOnClickListener {
            val loginIntent = Intent(this, register::class.java)
            startActivity(loginIntent)
        }

        binding.textView.setOnClickListener {
            val email = binding.usuario.text.toString()

            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se ha enviado un enlace de recuperación a tu correo electrónico", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "No se pudo enviar el enlace de recuperación. Verifica tu correo electrónico", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ingresa tu correo electrónico", Toast.LENGTH_LONG).show()
            }
        }
    }
}