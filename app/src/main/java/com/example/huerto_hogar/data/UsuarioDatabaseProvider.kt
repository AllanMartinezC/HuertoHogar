package com.example.huerto_hogar.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// ✅ nombre distinto, sin conflicto
val MIGRATION_USUARIO_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Usuario ADD COLUMN fotoUri TEXT")
    }
}

object UsuarioDatabaseProvider {
    @Volatile
    private var INSTANCE: UsuarioDatabase? = null

    fun getDatabase(context: Context): UsuarioDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                UsuarioDatabase::class.java,
                "usuario_db"
            )
                .addMigrations(MIGRATION_USUARIO_1_2)  // usa el nuevo nombre
                //.fallbackToDestructiveMigration()   // opcional en desarrollo
                .build()
                .also { INSTANCE = it }
        }
}
