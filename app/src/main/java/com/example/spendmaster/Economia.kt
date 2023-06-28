package com.example.spendmaster

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.spendmaster.databinding.ActivityEconomiaBinding
import com.example.spendmaster.databinding.DialogGastoBinding
import com.example.spendmaster.databinding.DialogIngresoBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat

class Economia : AppCompatActivity() {

    private lateinit var binding: ActivityEconomiaBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var usuario: String
    private var balance: Double = 0.0
    private var totalIngresos: Double = 0.0
    private var totalGastos: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEconomiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuario = intent.getStringExtra("usuario") ?: ""

        binding.btnAddIncome.setOnClickListener {
            showIncomeDialog()
        }

        binding.btnAddExpense.setOnClickListener {
            showExpenseDialog()
        }

        // MENU
        binding.menuEconomia.setOnNavigationItemSelectedListener { menuItem ->
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
                    val mensaje = "Controla tus gastos e ingresos, y visualiza una lista de transacciones para mantener tus finanzas en orden."

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

        loadIngresos(usuario)
        loadGastos(usuario)
        calculateBalance()
    }

    private fun showIncomeDialog() {
        val dialogBinding = DialogIngresoBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialogBinding.bAddIngreso.setOnClickListener {
            val title = dialogBinding.etTitulo.text.toString()
            val isIncome = 1
            val category = dialogBinding.spCategory.selectedItem.toString()
            val description = dialogBinding.etDescription.text.toString()
            val value = dialogBinding.etAmountI.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && category.isNotEmpty()) {
                val operacionData = hashMapOf<String, Any>(
                    "title" to title,
                    "isIncome" to isIncome,
                    "category" to category,
                    "description" to description,
                    "value" to value,
                    "usuario" to usuario
                )

                saveOperacion(operacionData)
                dialog.dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }
    }

    private fun showExpenseDialog() {
        val dialogBinding = DialogGastoBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialogBinding.bAddGasto.setOnClickListener {
            val title = dialogBinding.etTitulo.text.toString()
            val isIncome = 0
            val category = dialogBinding.spCategory.selectedItem.toString()
            val description = dialogBinding.etDescription.text.toString()
            val value = dialogBinding.etAmountG.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && category.isNotEmpty()) {
                val operacionData = hashMapOf<String, Any>(
                    "title" to title,
                    "isIncome" to isIncome,
                    "category" to category,
                    "description" to description,
                    "value" to value,
                    "usuario" to usuario
                )

                saveOperacion(operacionData)
                dialog.dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }
    }

    private fun updateBalanceTextView() {
        val balanceTextView = binding.balance
        balanceTextView.text = "Balance: $balance"
    }
    private fun calculateBalance() {
        db.collection("operacion")
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { documents ->
                var totalIngresos = 0.0
                var totalGastos = 0.0

                for (document in documents) {
                    val isIncome = document.getLong("isIncome") ?: 0
                    val value = document.getDouble("value") ?: 0.0

                    if (isIncome == 1L) {
                        totalIngresos += value
                    } else {
                        totalGastos += value
                    }

                    val decimalFormat = DecimalFormat("#.00")
                    val balance = decimalFormat.format(totalIngresos - totalGastos)
                    binding.balance.text = balance
                }

                balance = totalIngresos - totalGastos
                updateBalanceTextView()
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error al cargar las operaciones: ${e.localizedMessage}")
            }
    }
    private fun saveOperacion(operacionData: HashMap<String, Any>) {
        db.collection("operacion")
            .add(operacionData)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "Operación guardada con ID: ${documentReference.id}")
                loadIngresos(usuario)
                loadGastos(usuario)
                calculateBalance() // Calcular y actualizar el saldo
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error al guardar la operación: ${e.localizedMessage}")
            }
    }

    private fun loadIngresos(usuario: String) {
        db.collection("operacion")
            .whereEqualTo("isIncome", 1)
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { documents ->
                val ingresosLayout = binding.svIngresos.getChildAt(0) as LinearLayout
                ingresosLayout.removeAllViews()

                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val category = document.getString("category") ?: ""
                    val description = document.getString("description") ?: ""
                    val value = document.getDouble("value") ?: 0.0

                    val ingresoView = LayoutInflater.from(this).inflate(R.layout.item_ingreso, null)

                    val tvTitle = ingresoView.findViewById<TextView>(R.id.tvTitle)
                    val tvValue = ingresoView.findViewById<TextView>(R.id.tvValue)
                    val tvCategory = ingresoView.findViewById<TextView>(R.id.tvCategory)
                    val tvDescription = ingresoView.findViewById<TextView>(R.id.tvDescription)

                    tvTitle.text = title
                    tvValue.text = value.toString()
                    tvCategory.text = category
                    tvDescription.text = description

                    ingresosLayout.addView(ingresoView)
                }
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error al cargar los ingresos: ${e.localizedMessage}")
            }
    }


    private fun loadGastos(usuario: String) {
        db.collection("operacion")
            .whereEqualTo("isIncome", 0)
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { documents ->
                val gastosLayout = binding.svGastos.getChildAt(0) as LinearLayout
                gastosLayout.removeAllViews()

                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val category = document.getString("category") ?: ""
                    val description = document.getString("description") ?: ""
                    val value = document.getDouble("value") ?: 0.0

                    val gastoView = LayoutInflater.from(this).inflate(R.layout.item_gasto, null)

                    val tvTitle = gastoView.findViewById<TextView>(R.id.tvTitle)
                    val tvValue = gastoView.findViewById<TextView>(R.id.tvValue)
                    val tvCategory = gastoView.findViewById<TextView>(R.id.tvCategory)
                    val tvDescription = gastoView.findViewById<TextView>(R.id.tvDescription)

                    tvTitle.text = title
                    tvValue.text = value.toString()
                    tvCategory.text = category
                    tvDescription.text = description

                    gastosLayout.addView(gastoView)
                }
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error al cargar los gastos: ${e.localizedMessage}")
            }
    }
}
