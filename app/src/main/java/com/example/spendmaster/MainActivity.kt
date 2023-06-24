package com.example.spendmaster

import GastoDialog
import IngresoDialog
import android.content.Intent
import android.content.res.Configuration
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.spendmaster.components.ActivityLista
import com.example.spendmaster.components.miSQLiteHelper
import com.example.spendmaster.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: miSQLiteHelper

    private var saldo: Double = 0.0
    private var nuevoSaldo: Double = 0.0
    private lateinit var tvSaldo: TextView

    fun actualizarSaldo() {
        tvSaldo.text = String.format("%.2f €", nuevoSaldo)
    }

    //Creamos el cursor para ver los datos de la BD
    private fun consultarDatos() {
        binding.tvConsulta.text = ""
        val db: SQLiteDatabase = dbHelper.readableDatabase
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
        }

        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ingreso = 0f
        var gasto = 0f

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = miSQLiteHelper(this)

        tvSaldo = binding.tvSaldo

        // Obtener el saldo inicial de la base de datos
        saldo = dbHelper.obtenerSaldo()

        actualizarSaldo()

        //Creamos funcionalidad para el boton de ingreso
        binding.addIngreso.setOnClickListener {
            IngresoDialog(
                onSubmitClickListener = { cantidadI ->
                    ingreso = cantidadI
                    saldo += cantidadI
                    Toast.makeText(this, "Se ha añadido un ingreso de: $cantidadI €", Toast.LENGTH_SHORT).show()
                    // Llamar a la función de consulta después de agregar un ingreso
                    consultarDatos()
                    actualizarSaldo()
                }
            ).show(supportFragmentManager, "dialog")
        }

        //Creamos funcionalidad para el boton de gasto
        binding.addGasto.setOnClickListener {
            GastoDialog(
                onSubmitClickListener = { cantidadG ->
                    gasto = cantidadG
                    Toast.makeText(this, "Se ha añadido un gasto de: $cantidadG €", Toast.LENGTH_SHORT).show()

                    // Llamar al método addDatos() sin incluir el argumento "saldo"
                    val cantidadG: Double = cantidadG.toDouble()
                    dbHelper.addDatos("Gasto", 0, "Categoría", "Descripción", cantidadG)

                    // Actualizar la UI en el subproceso principal
                    runOnUiThread {
                        consultarDatos()
                    }
                }
            ).show(supportFragmentManager, "dialog")
        }

        binding.btConsultar.setOnClickListener {
            consultarDatos()
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

                // Llamar a la función de consulta después de la eliminación
                consultarDatos()
            } else {
                Toast.makeText(this, "El campo del ID está en blanco", Toast.LENGTH_SHORT).show()
            }
        }

        //Configuración de la barra de herramientas, el cajón de navegación y el menú de navegación
       val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.my_drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val  navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> {
                Toast.makeText(this, "Resumen", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ActivityLista::class.java)
                startActivity(intent)
            }
            R.id.nav_item_two -> Toast.makeText(this, "Item 2", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
