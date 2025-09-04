package com.munity.sheetsautomator.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.munity.sheetsautomator.core.data.local.database.dao.QueueDao
import com.munity.sheetsautomator.core.data.local.database.entity.QueueEntry

@Database(
    entities = [QueueEntry::class],
    version = 1
)
abstract class SheetsAutomatorDatabase : RoomDatabase() {
    abstract val queueDao: QueueDao

    companion object {
        @Volatile
        private var Instance: SheetsAutomatorDatabase? = null

        fun getDatabase(context: Context): SheetsAutomatorDatabase =
            Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = SheetsAutomatorDatabase::class.java,
                    name = "sheets_automator_database"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { Instance = it }
            }
    }
}
