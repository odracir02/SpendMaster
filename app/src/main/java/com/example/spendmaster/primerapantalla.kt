package com.example.spendmaster


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.spendmaster.databinding.ActivityPrimerapantallaBinding

private const val CHAT_FRAGMENT = "chat_fragment"

class primerapantalla : AppCompatActivity() {

    private lateinit var binding: ActivityPrimerapantallaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimerapantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""

        binding.btGestionCasa.setOnClickListener {
            val intent = Intent(this, gestionarCasa::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btEmail.setOnClickListener {
            val intent = Intent(this, email::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btCrearGrupo.setOnClickListener {
            val intent = Intent(this, crearGrupo::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btGestionPerfil.setOnClickListener {
            val intent = Intent(this, gestionarelperfil::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.botonchat.setOnClickListener {
            //val chatFragment = ListOfChatsFragment()
            val bundle = Bundle()
            bundle.putString("usuario", usuario)
            //chatFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                //.replace(android.R.id.content, chatFragment, CHAT_FRAGMENT)
                .commit()
        }



    }
}