package com.example.spendmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.spendmaster.components.AdapterGasto
import com.example.spendmaster.components.Gasto
import com.example.spendmaster.databinding.ActivityEconomiaBinding
import com.example.spendmaster.databinding.DialogIngresoBinding
import com.example.spendmaster.databinding.DialogGastoBinding
import com.google.firebase.firestore.FirebaseFirestore

class Economia : AppCompatActivity() {

    private lateinit var binding: ActivityEconomiaBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: AdapterGasto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEconomiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("usuario") ?: ""
        val operacion = db.collection("operacion")
        val gasto = "Gasto"

        db.collection("operacion")
            .whereEqualTo("usuario", usuario)
            .whereEqualTo("isIncome", gasto)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val gastos = querySnapshot.toObjects(Gasto::class.java)

                adapter = AdapterGasto(gastos)
                binding.rvGastos.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error ocurrido: ${e.localizedMessage}")
            }

        binding.btnAddIncome.setOnClickListener {
            showIncomeDialog()
        }

        binding.btnAddExpense.setOnClickListener {
            showExpenseDialog()
        }
    }

    private fun showIncomeDialog() {
        val dialogBinding = DialogIngresoBinding.inflate(layoutInflater)
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
            val inputText = dialogBinding.etAmountI.text.toString()
            val value = if (inputText.isNotBlank()) {
                inputText.toDoubleOrNull() ?: 0.0
            } else {
                0.0
            }

            if (title.isNotBlank() && category.isNotEmpty()) {
                val operacionData = hashMapOf<String, Any>(
                    "title" to title,
                    "isIncome" to isIncome,
                    "category" to category,
                    "description" to description,
                    "value" to value
                )

                saveOperacion(operacionData)
                dialog.dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }
    }

    private fun showExpenseDialog() {
        val dialogBinding = DialogGastoBinding.inflate(layoutInflater)
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
            val inputText = dialogBinding.etAmountG.text.toString()
            val value = if (inputText.isNotBlank()) {
                inputText.toDoubleOrNull() ?: 0.0
            } else {
                0.0
            }

            if (title.isNotBlank() && category.isNotEmpty()) {
                val operacionData = hashMapOf<String, Any>(
                    "title" to title,
                    "isIncome" to isIncome,
                    "category" to category,
                    "description" to description,
                    "value" to value
                )

                saveOperacion(operacionData)
                dialog.dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }
    }

    private fun saveOperacion(operacionData: HashMap<String, Any>) {
        db.collection("operacion")
            .add(operacionData)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "Operación guardada con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Error al guardar la operación: ${e.localizedMessage}")
            }
    }
}