package com.example.spendmaster

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.spendmaster.databinding.ActivityCrearGrupoBinding
import com.example.spendmaster.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class crearGrupo : AppCompatActivity() {
    private lateinit var binding: ActivityCrearGrupoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("grupo")
        val usuario = intent.getStringExtra("usuario") ?: ""
        val categoria = "Administrador"
        binding.TvUser.text = "Usuario: " + usuario
        binding.btCrearGrupo.setOnClickListener {
            val nombre = binding.etNombreGrupo.text.toString()
            val documentID = collection.document().id

            val grupo = hashMapOf(
                "Nombre" to nombre,
                "id" to documentID,
                "Categoria" to categoria,
                "Usuario" to usuario
            )

            val db = Firebase.firestore
            val grupoRef = db.collection("grupo")

            grupoRef.add(grupo)
                .addOnSuccessListener {
                    Toast.makeText(this, "Grupo creado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, primerapantalla::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al crear el grupo", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "Error al crear el grupo", exception)
                }

        }


        binding.btVolver.setOnClickListener {
            val loginIntent =   Intent(this, primerapantalla::class.java)
            startActivity(loginIntent)
        }
    }
}