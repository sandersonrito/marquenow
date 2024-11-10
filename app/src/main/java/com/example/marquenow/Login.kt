package com.example.marquenow

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

class Login : AppCompatActivity() {
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        dbHelper = UserDatabaseHelper(this)

        val btnLogin = findViewById<Button>(R.id.login)
        btnLogin.setOnClickListener{
            login()
        }

        val btnCadastro = findViewById<Button>(R.id.cadastrar)
        btnCadastro.setOnClickListener{
            cadastrar()
        }
    }

    private fun login(){
        val db = dbHelper.readableDatabase

        val email = findViewById<EditText>(R.id.userEmail).text.toString()
        val senha = findViewById<EditText>(R.id.userSenha).text.toString()

        val cursor = db.rawQuery(
            "SELECT * FROM ${UserDatabaseHelper.TABLE_NAME} WHERE ${UserDatabaseHelper.COLUMN_EMAIL} = ? AND ${UserDatabaseHelper.COLUMN_PASSWORD} = ?",
            arrayOf(email, senha)
        )

        if(cursor.moveToFirst()) {
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Email ou senha incorretos.", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
    }

    private fun cadastrar() {
        val intent = Intent(this, Cadastro::class.java)
        startActivity(intent)
    }
}