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
import com.example.spendmaster.components.miSQLiteHelper
import com.example.spendmaster.databinding.DialogIngresoBinding
import android.text.InputFilter
import android.text.Spanned

class IngresoDialog(
    private val onSubmitClickListener: (Float) -> Unit
): DialogFragment() {

    lateinit var binding: DialogIngresoBinding
    lateinit var dbHelper: miSQLiteHelper

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogIngresoBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        dbHelper = miSQLiteHelper(requireContext())

        // Aplicar el filtro de entrada al campo etAmountI
        binding.etAmountI.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())

        binding.bAddIngreso.setOnClickListener {
            val title = binding.etTitulo.text.toString()
            val isIncome = 1
            val category = binding.spCategory.selectedItem.toString()
            val description = binding.etDescription.text.toString()
            val inputText = binding.etAmountI.text.toString()
            val value = if (inputText.isNotBlank()) {
                inputText.toDoubleOrNull() ?: 0.0
            } else {
                0.0
            }

            if (title.isNotBlank() && category.isNotEmpty()) {
                dbHelper.addDatos(title, isIncome, category, description, value)
                dismiss()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    // Clase DecimalDigitsInputFilter
    class DecimalDigitsInputFilter : InputFilter {
        private val decimalPattern = "\\d+(\\.\\d{0,2})?"

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val input = dest.toString() + source.toString()
            if (input.matches(decimalPattern.toRegex())) {
                return null
            }
            return ""
        }
    }
}
