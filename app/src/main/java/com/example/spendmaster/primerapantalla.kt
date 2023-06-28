package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.example.spendmaster.databinding.ActivityPrimerapantallaBinding

private const val CHAT_FRAGMENT = "chat_fragment"

class primerapantalla : AppCompatActivity() {

    private lateinit var binding: ActivityPrimerapantallaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimerapantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""

        binding.root.setOnTouchListener { _, event ->
            hideKeyboard()
            false
        }

        binding.btGestionCasa.setOnClickListener {
            val intent = Intent(this, Economia::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btEmail.setOnClickListener {
            val intent = Intent(this, email::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.btGestionPerfil.setOnClickListener {
            val intent = Intent(this, gestionarelperfil::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }

        binding.botonchat.setOnClickListener {
            val chatFragment = ListOfChatsFragment()
            val bundle = Bundle()
            bundle.putString("usuario", usuario)
            chatFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, chatFragment, CHAT_FRAGMENT)
                .commit()
        }

        // MENU
        binding.menuPrimera.setOnNavigationItemSelectedListener { menuItem ->
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
                    val mensaje = "Pantalla principal con diferentes funciones para administrar tu casa y enviar correos electrónicos."

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

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }
}
