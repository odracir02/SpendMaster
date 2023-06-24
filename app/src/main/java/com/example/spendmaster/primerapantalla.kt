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

        binding.button.setOnClickListener {
            //val intent = Intent(this, pantallaPrincipal::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.button7.setOnClickListener {
            //val intent = Intent(this, email::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.botondecrearproyecto.setOnClickListener {
            //val intent = Intent(this, crearproyecto::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.button9.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
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

        binding.button9.setOnClickListener{
            //val intent = Intent(this, gestionarelperfil::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }



    }
}