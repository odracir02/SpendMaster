package com.example.spendmaster.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendmaster.R


class AdapterGasto (private val listaGasto:List<Gasto>): RecyclerView.Adapter<AdapterGasto.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterGasto.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.cardviewgasto,parent,false)
        val holder = ViewHolder(v)
        return holder
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var itemNombre: TextView
        var itemDescripcion: TextView
        var itemCantidad: TextView
        var itemTipo: TextView


        init {
            itemNombre=itemView.findViewById(R.id.textViewNombre)
            itemDescripcion=itemView.findViewById(R.id.textViewDescripcion)
            itemCantidad=itemView.findViewById(R.id.textViewCantidad)
            itemTipo=itemView.findViewById(R.id.textViewTipo)
        }
    }


    override fun onBindViewHolder(holder: AdapterGasto.ViewHolder, position: Int) {
        holder.itemNombre.setText(listaGasto[position].title)
        holder.itemDescripcion.setText(listaGasto[position].description)
        holder.itemCantidad.setText(listaGasto[position].value)
        holder.itemTipo.setText(listaGasto[position].isIncome)
    }


    override fun getItemCount(): Int {
        return listaGasto.size
    }

}