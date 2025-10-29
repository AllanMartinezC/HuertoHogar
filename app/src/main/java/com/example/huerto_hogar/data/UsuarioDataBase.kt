package com.example.huerto_hogar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huerto_hogar.model.Usuario

@Database(entities = [Usuario::class], version = 2)
abstract class UsuarioDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Usuario ADD COLUMN fotoUri TEXT")
    }
}
