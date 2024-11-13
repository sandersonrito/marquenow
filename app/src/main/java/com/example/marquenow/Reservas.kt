package com.example.marquenow


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import com.example.marquenow.util.ReservaDatabaseHelper
import java.util.Calendar

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
        val editTextData: EditText = findViewById(R.id.reservaData)
        val editTextHora: EditText = findViewById(R.id.reservaHora)

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

        // Obtém o ID da reserva para edição (se houver)
        reservaId = intent.getLongExtra("reservaId", -1L).takeIf { it != -1L }

        reservaId?.let {
            val data = intent.getStringExtra("data")
            val hora = intent.getStringExtra("hora")
            val especialidade = intent.getStringExtra("especialidade")
            val medico = intent.getStringExtra("medico")

            editTextData.setText(data)
            editTextHora.setText(hora)
            mySpinner.setSelection(adapter.getPosition(especialidade))
            scSpinner.setSelection(values.getPosition(medico))
        }

        // Configuração do DatePickerDialog para o campo de data
        editTextData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val dataFormatada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    editTextData.setText(dataFormatada)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Configuração do TimePickerDialog para o campo de hora
        editTextHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val horaFormatada = String.format("%02d:%02d", hourOfDay, minute)
                    editTextHora.setText(horaFormatada)
                },
                hour,
                minute,
                true // Define se é formato 24 horas
            )
            timePickerDialog.show()
        }

        findViewById<Button>(R.id.btnReservas).setOnClickListener {
            reservar() // Chama reservar() para criar ou editar a reserva
        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish() // Chama finish() para voltar para a Home
        }
    }

    private fun reservar() {
        val data = findViewById<EditText>(R.id.reservaData).text.toString()
        val hora = findViewById<EditText>(R.id.reservaHora).text.toString()
        val especialidade = findViewById<Spinner>(R.id.especialidade).selectedItem.toString()
        val medico = findViewById<Spinner>(R.id.medicos).selectedItem.toString()

        // 1. Verificar se todos os campos foram preenchidos
        if (data.isEmpty() || hora.isEmpty() || especialidade == "Especialidade" || medico == "Médico") {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return // Sai da função sem criar a reserva
        }

        // 2. Verificar se a data e hora são anteriores à data e hora atuais
        val calendarSelecionado = Calendar.getInstance()
        calendarSelecionado.set(Calendar.YEAR, getYearFromString(data))
        calendarSelecionado.set(Calendar.MONTH, getMonthFromString(data) - 1) // Mês começa em 0
        calendarSelecionado.set(Calendar.DAY_OF_MONTH, getDayFromString(data))
        calendarSelecionado.set(Calendar.HOUR_OF_DAY, getHourFromString(hora))
        calendarSelecionado.set(Calendar.MINUTE, getMinuteFromString(hora))

        val calendarAtual = Calendar.getInstance()

        if (calendarSelecionado.before(calendarAtual)) {
            Toast.makeText(this, "Data ou hora inválidas! Selecione uma data e hora futuras.", Toast.LENGTH_SHORT).show()
            return // Sai da função sem criar a reserva
        }

        // Seu código original para inserir ou atualizar a reserva no banco de dados
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
                // Finaliza a Activity após criar a reserva
                finish()
            } else {
                Toast.makeText(this, "Erro ao criar reserva.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Atualiza a reserva existente
            val rowsAffected = db.update(
                ReservaDatabaseHelper.TABLE_NAME,
                values,
                "${ReservaDatabaseHelper.COLUMN_ID} = ?",
                arrayOf(reservaId.toString())
            )
            if (rowsAffected > 0) {
                Toast.makeText(this, "Reserva atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                // Finaliza a Activity após atualizar a reserva
                finish()
            } else {
                Toast.makeText(this, "Erro ao atualizar reserva.", Toast.LENGTH_SHORT).show()
            }
        }
        db.close()

        // Define o resultado como RESULT_OK para indicar sucesso
        setResult(Activity.RESULT_OK)
    }

    // Funções auxiliares para extrair ano, mês, dia, hora e minuto da string de data e hora
    private fun getYearFromString(data: String): Int {
        return data.substringAfterLast("/").toInt()
    }

    private fun getMonthFromString(data: String): Int {
        return data.substringAfter("/").substringBefore("/").toInt()
    }

    private fun getDayFromString(data: String): Int {
        return data.substringBefore("/").toInt()
    }

    private fun getHourFromString(hora: String): Int {
        return hora.substringBefore(":").toInt()
    }

    private fun getMinuteFromString(hora: String): Int {
        return hora.substringAfter(":").toInt()
    }
}
