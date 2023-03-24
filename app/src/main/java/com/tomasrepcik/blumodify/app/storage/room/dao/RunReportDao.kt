package com.tomasrepcik.blumodify.app.storage.room.dao

import androidx.room.*
import com.tomasrepcik.blumodify.app.storage.room.entities.RunReport

@Dao
interface RunReportDao {
    @Query("SELECT * FROM runreport ORDER BY start_time ASC")
    fun getAll(): List<RunReport>
}