import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.spendmaster.components.miSQLiteHelper
import com.example.spendmaster.databinding.DialogGastoBinding
import android.text.InputFilter
import android.text.Spanned

class GastoDialog(
    private val onSubmitClickListener: (Float) -> Unit
): DialogFragment() {

    lateinit var binding : DialogGastoBinding
    lateinit var dBHelper: miSQLiteHelper

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogGastoBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        dBHelper = miSQLiteHelper(requireContext())

        // Aplicar el filtro de entrada al campo etAmountG
        binding.etAmountG.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())

        binding.bAddGasto.setOnClickListener {
            val title = binding.etTitulo.text.toString()
            val isIncome = 0
            val category = binding.spCategory.selectedItem.toString()
            val description = binding.etDescription.text.toString()
            val value = binding.etAmountG.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && category.isNotEmpty()) {
                dBHelper.addDatos(title, isIncome, category, description, value)
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