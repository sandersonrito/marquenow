package com.example.marquenow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val btnReserva = findViewById<Button>(R.id.btnReserva)
        btnReserva.setOnClickListener{
            reserva()
        }

        val btnListar = findViewById<Button>(R.id.listarReservas)
        btnListar.setOnClickListener{
            listarReservas()
        }
    }

    private fun reserva() {
        val intent = Intent(this, Reservas::class.java)
        startActivity(intent)
    }

    private fun listarReservas() {
        val listar = Intent(this, ListarReservas::class.java)
        startActivity(listar)
    }

}