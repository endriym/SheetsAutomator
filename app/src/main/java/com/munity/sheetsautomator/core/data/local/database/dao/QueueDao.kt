package com.munity.sheetsautomator.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.munity.sheetsautomator.core.data.local.database.entity.QueueEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueEntries(vararg queueEntries: QueueEntry)

    @Update
    suspend fun updateQueueEntries(vararg queueEntries: QueueEntry)

    @Delete
    suspend fun deleteQueueEntries(vararg queueEntries: QueueEntry)

    @Query("SELECT * FROM queue_entry")
    fun getQueueEntries(): Flow<List<QueueEntry>>

    @Query("SELECT * FROM queue_entry WHERE id = :entryId")
    fun getQueueEntryById(entryId: Int)
}
