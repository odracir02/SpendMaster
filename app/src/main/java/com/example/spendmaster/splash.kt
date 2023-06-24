package com.example.spendmaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Utilizamos un Handler para retrasar la navegación a la siguiente actividad
        Handler().postDelayed({
            // Creamos un Intent para pasar de la pantalla de presentación (splash screen) a la siguiente actividad
            val intent = Intent(this, login2::class.java)
            startActivity(intent)
            finish() // Cerramos la actividad de la pantalla de presentación (splash screen)
        }, splashTimeOut)
    }
}