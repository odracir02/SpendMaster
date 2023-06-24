package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spendmaster.databinding.ActivityGestionarelperfilBinding
import com.example.spendmaster.databinding.ActivityPrimerapantallaBinding

class gestionarelperfil : AppCompatActivity() {

    private lateinit var binding: ActivityGestionarelperfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarelperfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""

        binding.btPerfil.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }


    }
}