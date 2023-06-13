package com.example.spendmaster

import GastoDialog
import IngresoDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.spendmaster.components.miSQLiteHelper
import com.example.spendmaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: miSQLiteHelper

    @RequiresApi(Build.VERSION_CODES.R)
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

        //Creamos el cursor para ver los datos de la BD
        binding.btConsultar.setOnClickListener {
            binding.tvConsulta.text = ""
            val db : SQLiteDatabase = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM operation", null)

            if (cursor.moveToFirst()) {
                do {
                    binding.tvConsulta.append("ID: " + cursor.getString(0).toString() + ", ")
                    binding.tvConsulta.append("Titulo: " + cursor.getString(1).toString() + ", ")

                    val isIncome = cursor.getInt(2)
                    val isIncomeText = if (isIncome == 0) {
                        "Gasto"
                    } else {
                        "Ingreso"
                    }
                    binding.tvConsulta.append("IsIncome: $isIncomeText, ")

                    binding.tvConsulta.append("Categoria: " + cursor.getString(3).toString() + ", ")
                    binding.tvConsulta.append("Descripción: " + cursor.getString(4).toString() + ", ")

                    val amount = cursor.getDouble(5)
                    val formattedAmount = if (amount < 0) {
                        "-${-amount}"
                    } else {
                        "$amount"
                    }
                    binding.tvConsulta.append("Cantidad: $formattedAmount\n")
                } while (cursor.moveToNext())

                cursor.close()
                db.close()
            }
        }

        binding.btDelete.setOnClickListener {
            val idText = binding.etId.text.toString().trim()
            if (idText.isNotBlank()) {
                val id = idText.toInt()
                val cantidad = dbHelper.borrarDatos(id)
                binding.etId.text.clear()

                if (cantidad > 0) {
                    Toast.makeText(this, "Datos borrados: $cantidad", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se encontró ningún registro con el ID: $id", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "El campo del ID está en blanco", Toast.LENGTH_SHORT).show()
            }
        }




    }
}