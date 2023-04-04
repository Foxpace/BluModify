package com.tomasrepcik.blumodify.app.storage.room.dao

import androidx.room.*
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport

@Dao
interface LogsDao {
    @Query("SELECT * FROM logreport ORDER BY start_time DESC")
    fun getAll(): List<LogReport>

    @Query("SELECT * FROM logreport WHERE id = :pickedId")
    fun getLogById(pickedId: Int): LogReport?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(report: LogReport)

    @Query("DELETE FROM logreport WHERE start_time < :pickedTime")
    fun deleteOlderItemsThan(pickedTime: Long)
}