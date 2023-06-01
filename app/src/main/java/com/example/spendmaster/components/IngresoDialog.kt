package com.example.spendmaster.components



import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.spendmaster.databinding.DialogIngresoBinding

class IngresoDialog(
    private val onSubmitClickListener: (Float) -> Unit
): DialogFragment() {

    lateinit var binding : DialogIngresoBinding
    lateinit var dBHelper: miSQLiteHelper

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogIngresoBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        dBHelper = miSQLiteHelper(requireContext())

        binding.bAddIngreso.setOnClickListener {
            val title = binding.etTitulo.text.toString()
            val isIncome = 1
            val category = binding.spCategory.selectedItem.toString()
            val description = binding.etDescription.text.toString()
            val value = binding.etAmountI.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && category.isNotEmpty()) {
                dBHelper.addDatos(title, isIncome, category, description, value)
                dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }

        binding.bAddIngreso.setOnClickListener {
            onSubmitClickListener.invoke(binding.etAmountI.text.toString().toFloat())
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}