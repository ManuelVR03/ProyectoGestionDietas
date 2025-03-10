package com.example.mygestiondietas.baseDatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mygestiondietas.modelo.IDaoCD
import com.example.mygestiondietas.modelo.IDaoIngrediente
import com.example.mygestiondietas.modelo.Ingrediente
import com.example.mygestiondietas.modelo.ComponenteDieta

@Database(
    entities = [ComponenteDieta::class, Ingrediente::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun componenteDietaDao(): IDaoCD
    abstract fun ingredienteDao(): IDaoIngrediente

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_dietas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}