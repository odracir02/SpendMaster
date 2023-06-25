package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spendmaster.databinding.ActivityGestionarCasaBinding

class gestionarCasa : AppCompatActivity() {

    private lateinit var binding: ActivityGestionarCasaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarCasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""

        binding.btGestionar.setOnClickListener {
            //val intent = Intent(this, gestionarCasa::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btEconomia.setOnClickListener {
            val intent = Intent(this, Economia::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btHogar.setOnClickListener {
            //val intent = Intent(this, crearGrupo::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btEditarGrupo.setOnClickListener {
            //val intent = Intent(this, gestionarelperfil::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

    }
}