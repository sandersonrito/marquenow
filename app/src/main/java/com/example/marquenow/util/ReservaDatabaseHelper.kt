package com.example.marquenow.util

import Reserva
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ReservaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "reservas.db"
        const val TABLE_NAME = "reservas"
        const val COLUMN_ID = "_id"
        const val COLUMN_DATA = "data"
        const val COLUMN_HORA = "hora"
        const val COLUMN_ESPECIALIDADE = "especialidade"
        const val COLUMN_MEDICO = "medico"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATA TEXT,
                $COLUMN_HORA TEXT,
                $COLUMN_ESPECIALIDADE TEXT,
                $COLUMN_MEDICO TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertReserva(data: String, hora: String, especialidade: String, medico: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_DATA, data)
            put(COLUMN_HORA, hora)
            put(COLUMN_ESPECIALIDADE, especialidade)
            put(COLUMN_MEDICO, medico)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L
    }

    fun getAllReservas(): List<Reserva> {
        val reservas = mutableListOf<Reserva>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
                val especialidade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESPECIALIDADE))
                val medico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO))

                reservas.add(Reserva(id, data, hora, especialidade, medico))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return reservas
    }

    fun updateReserva(reserva: Reserva): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_DATA, reserva.data)
            put(COLUMN_HORA, reserva.hora)
            put(COLUMN_ESPECIALIDADE, reserva.especialidade)
            put(COLUMN_MEDICO, reserva.medico)
        }
        // Correção: Usando COLUMN_ID na condição de atualização
        val result = db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(reserva.id.toString()))
        db.close()
        return result > 0
    }

    fun deleteReserva(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
