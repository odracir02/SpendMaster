package com.example.spendmaster.components



import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.spendmaster.databinding.DialogIngresoBinding

class IngresoDialog(
    private val onSubmitClickListener: (Float) -> Unit
): DialogFragment() {

    private lateinit var binding : DialogIngresoBinding
    lateinit var DBHelper: miSQLiteHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogIngresoBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)





        binding.bAddIngreso.setOnClickListener {
            onSubmitClickListener.invoke(binding.etAmountG.text.toString().toFloat())
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}