package com.example.spendmaster.components

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cursoradapter.widget.CursorAdapter
import com.example.spendmaster.R
import com.example.spendmaster.databinding.ActivityListaBinding
import com.example.spendmaster.databinding.ActivityMainBinding
import com.example.spendmaster.databinding.ItemListviewBinding

class ActivityLista : AppCompatActivity() {

    lateinit var binding: ActivityListaBinding
    lateinit var dbHelper: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = miSQLiteHelper(this)

        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM operation", null)

        val adaptador = CursorAdapterListView(this, cursor)
        binding.lvDatos.adapter = adaptador
        db.close()
    }

    inner class CursorAdapterListView(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER){
        override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
            val inflater = LayoutInflater.from(context)
            return  inflater.inflate(R.layout.item_listview, parent, false)
        }

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            val bindingItems = ItemListviewBinding.bind(view!!)
            bindingItems.textTitle.text = cursor!!.getString(1)
            bindingItems.textAmount.text = cursor!!.getDouble(5).toString()
            bindingItems.textSubtitle.text = cursor!!.getString(4)

            view.setOnClickListener {
                Toast.makeText(this@ActivityLista,
                    "${bindingItems.textTitle.text}, " +
                    "${bindingItems.textAmount.text}",
                Toast.LENGTH_SHORT).show()
            }
        }

    }
}