package com.munity.sheetsautomator

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.munity.sheetsautomator.core.data.local.datastore.SheetsPreferencesDataSource

private const val DATA_STORE_PREFERENCES_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_PREFERENCES_NAME
)

class SheetsAutomatorApplication : Application() {
    lateinit var sheetsPreferencesDataSource: SheetsPreferencesDataSource

    override fun onCreate() {
        super.onCreate()

        // Manual dependency injection
        sheetsPreferencesDataSource = SheetsPreferencesDataSource(dataStore = this.dataStore)
    }
}