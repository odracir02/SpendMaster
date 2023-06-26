package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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

        // MENU
        binding.menuGestionCasa.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_item1 -> {
                    val intent = Intent(this, primerapantalla::class.java)
                    intent.putExtra("usuario", usuario)
                    intent.putExtra("user", usuario)
                    startActivity(intent)
                    true
                }
                R.id.navigation_item2 -> {
                    val intent = Intent(this, gestionarelperfil::class.java)
                    intent.putExtra("usuario", usuario)
                    startActivity(intent)
                    true
                }
                R.id.navigation_item3 -> {
                    val mensaje = "Gestiona tus gastos e ingresos para mantener un registro de tus finanzas en casa."

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(mensaje)
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss() // Cierra la ventana emergente al pulsar el botón "Aceptar"
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                    true
                }

                R.id.navigation_item4 -> {
                    val intent = Intent(this, login2::class.java)

                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}