package com.munity.sheetsautomator.core.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queue_entry")
data class QueueEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val amount: Float,
    val date: String,
    val category: String,
    val description: String,
    val isSynced: Boolean,
)
