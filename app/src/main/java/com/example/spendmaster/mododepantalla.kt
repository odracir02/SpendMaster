package com.example.spendmaster

import android.hardware.lights.Light
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.spendmaster.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class mododepantalla: BottomSheetDialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.modopantalla,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dark = view.findViewById<View>(R.id.darkBoton)
        val System = view.findViewById<View>(R.id.SystemBoton)
        val Light = view.findViewById<View>(R.id.LightBoton)

        dark.setOnClickListener {
            enableDarkmode()
            Toast.makeText(context,"Modo Oscuro",Toast.LENGTH_SHORT).show()
            dismiss()
        }
        Light.setOnClickListener{
            disableDarkmode()
            Toast.makeText(context,"Modo Claro",Toast.LENGTH_SHORT).show()
            dismiss()
        }
        System.setOnClickListener {
            systemMode()
            Toast.makeText(context,"Modo del Sistema",Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    fun enableDarkmode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }
    fun disableDarkmode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
    fun systemMode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
    }
}