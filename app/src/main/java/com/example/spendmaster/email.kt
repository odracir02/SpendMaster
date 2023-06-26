package com.example.spendmaster

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spendmaster.databinding.ActivityEmailBinding

class email : AppCompatActivity() {
    private lateinit var binding: ActivityEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = intent.getStringExtra("usuario") ?: ""
        binding.botonemail.setOnClickListener {
            val email = binding.textousuarioO30.text.toString()
            val asunto = binding.textousuarioO17.text.toString()
            val mensaje = binding.editTextTextPersonName.text.toString()

            val addresses = email.split(",".toRegex()).toTypedArray()

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, addresses)
                putExtra(Intent.EXTRA_SUBJECT, asunto)
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }

            startActivity(intent)


            // Verificar si la aplicación de Gmail está instalada
            val manager = packageManager
            val activities = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (activities.isNotEmpty()) {
                // Obtener el nombre del paquete de la aplicación de Gmail
                val packageName = activities[0].activityInfo.packageName
                intent.`package` = packageName
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Aplicación de Gmail no instalada",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // MENU
        binding.menuEmail.setOnNavigationItemSelectedListener { menuItem ->
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
                    val mensaje = "Escribe y envía correos electrónicos fácilmente desde la aplicación."

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