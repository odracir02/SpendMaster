package com.example.spendmaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.spendmaster.R
import com.example.spendmaster.LanguageChangeListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class idiomas: BottomSheetDialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.idiomas,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val espanol = view.findViewById<View>(R.id.espanol)
        val ingles = view.findViewById<View>(R.id.ingles)


        espanol.setOnClickListener {
            (activity as? LanguageChangeListener)?.onLanguageChanged("en-us")
            Toast.makeText(context, "Español", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        ingles.setOnClickListener {
            (activity as? LanguageChangeListener)?.onLanguageChanged("en-rUs")
            Toast.makeText(context, "Inglés", Toast.LENGTH_SHORT).show()
            dismiss()
        }


    }
}