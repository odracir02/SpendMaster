package com.example.spendmaster

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.spendmaster.databinding.DialogGastoBinding
import android.text.InputFilter
import android.text.Spanned
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException


class GastoDialog(
    private val onSubmitClickListener: () -> Unit
): DialogFragment() {

    private lateinit var dialogBinding: DialogGastoBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var usuario:String



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        dialogBinding = DialogGastoBinding.inflate(inflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.bAddGasto.setOnClickListener {
            val title = dialogBinding.etTitulo.text.toString()
            val description = dialogBinding.etDescription.text.toString()
            val inputText = dialogBinding.etAmountG.text.toString()
            val value = if (inputText.isNotBlank()) {
                inputText.toFloatOrNull() ?: 0f
            } else {
                0f
            }

            val gastoId = UUID.randomUUID().toString()
            val nombreGrupo = obtenerNombreGrupo()
            val usuario = obtenerUsuario()

            val gastoData = hashMapOf(
                "Id" to gastoId,
                "title" to title,
                "description" to description,
                "value" to value,
                "nombreGrupo" to nombreGrupo,
                "usuario" to usuario
            )

            guardarGastoFirestore(gastoId, gastoData)

            dialog.dismiss()
        }

        dialogBinding.etAmountG.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())

        return dialog
    }

    private fun obtenerNombreGrupo(): String {
        var nombreGrupo = ""

        val grupoRef = db.collection("grupo").document("TU_DOCUMENTO_DE_GRUPO")

        grupoRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document != null && document.exists()) {
                        nombreGrupo = document.getString("nombreGrupo") ?: ""
                    }
                } else {
                    // Manejo de error al obtener el valor de nombreGrupo
                }
            }

        return nombreGrupo
    }


    private fun obtenerUsuario(): String {
        // Realiza la consulta a Firestore para obtener el valor de usuario
        // y devuélvelo como un String
        // ...

        return "" // Reemplaza esta línea con el código para obtener el valor real
    }

    private fun guardarGastoFirestore(gastoId: String, gastoData: Map<String, Any>) {
        val gastoRef = db.collection("gasto").document(gastoId)
        gastoRef.set(gastoData, SetOptions.merge())
            .addOnSuccessListener {
                onSubmitClickListener.invoke()
            }
            .addOnFailureListener { exception ->
                // Manejo de error al guardar los datos
            }
    }

    private class DecimalDigitsInputFilter : InputFilter {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val regex = Regex("\\d*\\.?\\d{0,2}")
            val input = dest?.subSequence(0, dstart).toString() + source?.subSequence(start, end).toString() + dest?.subSequence(dend, dest.length).toString()
            return if (regex.matches(input)) {
                null
            } else {
                ""
            }
        }
    }
}
