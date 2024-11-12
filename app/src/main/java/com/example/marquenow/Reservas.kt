package com.example.marquenow

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.marquenow.util.ReservaDatabaseHelper

class Reservas : AppCompatActivity() {
    private lateinit var dbHelper: ReservaDatabaseHelper
    private var reservaId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservas)

        dbHelper = ReservaDatabaseHelper(this)

        val mySpinner: Spinner = findViewById(R.id.especialidade)
        val scSpinner: Spinner = findViewById(R.id.medicos)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter

        val values = ArrayAdapter.createFromResource(
            this,
            R.array.second_spinner_options,
            android.R.layout.simple_spinner_item
        )
        values.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        scSpinner.adapter = values

        reservaId = intent.getLongExtra("reservaId", -1L).takeIf { it != -1L }

        reservaId?.let {
            val data = intent.getStringExtra("data")
            val hora = intent.getStringExtra("hora")
            val especialidade = intent.getStringExtra("especialidade")
            val medico = intent.getStringExtra("medico")

            findViewById<EditText>(R.id.reservaData).setText(data)
            findViewById<EditText>(R.id.reservaHora).setText(hora)
            mySpinner.setSelection(adapter.getPosition(especialidade))
            scSpinner.setSelection(values.getPosition(medico))
        }

        findViewById<Button>(R.id.btnReservas).setOnClickListener {
            if(reservaId != null){
                reservar()
            } else {
                reservar()
            }

        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }
    }

    private fun reservar() {
        val data = findViewById<EditText>(R.id.reservaData).text.toString()
        val hora = findViewById<EditText>(R.id.reservaHora).text.toString()
        val especialidade = findViewById<Spinner>(R.id.especialidade).selectedItem.toString()
        val medico = findViewById<Spinner>(R.id.medicos).selectedItem.toString()

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("data", data)
            put("hora", hora)
            put("especialidade", especialidade)
            put("medico", medico)
        }

        if (reservaId == null) {
            val newRowId = db.insert(ReservaDatabaseHelper.TABLE_NAME, null, values)
            if (newRowId != -1L) {
                Toast.makeText(this, "Reserva criada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao criar reserva.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val rowsAffected = db.update(
                ReservaDatabaseHelper.TABLE_NAME,
                values,
                "${ReservaDatabaseHelper.COLUMN_ID} = ?",
                arrayOf(reservaId.toString())
            )
            if (rowsAffected > 0) {
                Toast.makeText(this, "Reserva atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this, "Erro ao atualizar reserva.", Toast.LENGTH_SHORT).show()
            }
        }
        db.close()

    }

}
