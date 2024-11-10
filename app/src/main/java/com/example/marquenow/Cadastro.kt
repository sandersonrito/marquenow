package com.example.marquenow

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.marquenow.util.UserDatabaseHelper

class Cadastro : AppCompatActivity() {
    private lateinit var dbHelper: UserDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        dbHelper = UserDatabaseHelper(this)

        val btnCadastrar = findViewById<Button>(R.id.cadastrar)
        btnCadastrar.setOnClickListener{
            cadastrarUsuario()
        }
    }

    private fun cadastrarUsuario() {
        val db = dbHelper.writableDatabase

        val email = findViewById<EditText>(R.id.userEmail).text.toString()
        val senha = findViewById<EditText>(R.id.userSenha).text.toString()

        val values = ContentValues().apply {
            put("email", email)
            put("password", senha)
        }

        val newRowId = db.insert(UserDatabaseHelper.TABLE_NAME, null, values)
        if (newRowId != -1L) {
            Toast.makeText(this, "Usuário cadastrado com sucesso!!!", Toast.LENGTH_SHORT).show()
            retornar()
        } else {
            Toast.makeText(this, "Erro ao criar o usuário.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retornar() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}