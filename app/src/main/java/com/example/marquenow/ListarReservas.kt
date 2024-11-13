package com.example.marquenow

import Reserva
import ReservaAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marquenow.util.ReservaDatabaseHelper

class ListarReservas : AppCompatActivity() {
    private lateinit var dbHelper: ReservaDatabaseHelper
    private lateinit var reservaAdapter: ReservaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_reservas)

        dbHelper = ReservaDatabaseHelper(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reservaAdapter = ReservaAdapter(
            this,
            mutableListOf(),
            onEditClick = { reserva -> editReserva(reserva) },
            onDeleteClick = { reserva -> deleteReserva(reserva) }
        )
        recyclerView.adapter = reservaAdapter

        loadReservas()
    }

    override fun onResume() {
        super.onResume()
        loadReservas() // Recarrega as reservas
    }

    private fun loadReservas() {
        val reservas = dbHelper.getAllReservas()
        reservaAdapter.updateData(reservas)
        reservaAdapter.notifyDataSetChanged() // Notifica o adapter sobre as mudanças
    }

    private fun editReserva(reserva: Reserva) {
        val intent = Intent(this, Reservas::class.java).apply {
            putExtra("reservaId", reserva.id)
            putExtra("data", reserva.data)
            putExtra("hora", reserva.hora)
            putExtra("especialidade", reserva.especialidade)
            putExtra("medico", reserva.medico)
        }
        startActivity(intent)
    }

    private fun deleteReserva(reserva: Reserva) {
        dbHelper.deleteReserva(reserva.id)
        loadReservas() // Recarrega a lista após a exclusão
        Toast.makeText(this, "Reserva deletada com sucesso!", Toast.LENGTH_SHORT).show()
    }
}
