package com.munity.sheetsautomator

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.munity.sheetsautomator.core.data.local.database.SheetsAutomatorDatabase
import com.munity.sheetsautomator.core.data.local.datastore.SheetsPreferencesDataSource
import com.munity.sheetsautomator.core.data.remote.SheetsApi
import com.munity.sheetsautomator.core.data.repository.SheetsRepository

private const val DATA_STORE_PREFERENCES_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_PREFERENCES_NAME
)

class SheetsAutomatorApplication : Application() {
    private lateinit var sheetsDatabase: SheetsAutomatorDatabase
    private lateinit var sheetsPrefsDataSource: SheetsPreferencesDataSource
    private lateinit var sheetsApi: SheetsApi
    lateinit var sheetsRepository: SheetsRepository

    override fun onCreate() {
        super.onCreate()

        // Manual dependency injection
        sheetsPrefsDataSource = SheetsPreferencesDataSource(dataStore = this.dataStore)
        sheetsDatabase = SheetsAutomatorDatabase.getDatabase(context = applicationContext)
        sheetsApi = SheetsApi(sheetsPrefsDataSource)
        sheetsRepository = SheetsRepository(sheetsPrefsDataSource, sheetsDatabase, sheetsApi)
    }

    override fun onTerminate() {
        super.onTerminate()
        sheetsApi.closeClient()
    }
}
