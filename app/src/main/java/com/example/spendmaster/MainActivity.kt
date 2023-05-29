package com.example.spendmaster

import GastoDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.spendmaster.components.IngresoDialog
import com.example.spendmaster.components.miSQLiteHelper
import com.example.spendmaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ingreso = 0f
        var gasto = 0f

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = miSQLiteHelper(this)

        //Creamos funcionalidad para el boton de ingreso
        binding.addIngreso.setOnClickListener {
            IngresoDialog(
                onSubmitClickListener = { cantidadI ->
                    ingreso = cantidadI
                    Toast.makeText(this, "Se ha añadido un ingreso de: $cantidadI €", Toast.LENGTH_SHORT).show()
                }
            ).show(supportFragmentManager, "dialog")
        }

        //Creamos funcionalidad para el boton de gasto
        binding.addGasto.setOnClickListener {
            GastoDialog(
                onSubmitClickListener = { cantidadG ->
                    gasto = cantidadG
                    Toast.makeText(this, "Se ha añadido un gasto de: $cantidadG €", Toast.LENGTH_SHORT).show()
                }
            ).show(supportFragmentManager, "dialog")
        }
    }
}