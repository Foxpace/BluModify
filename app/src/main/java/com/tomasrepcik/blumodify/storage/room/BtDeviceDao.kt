package com.tomasrepcik.blumodify.storage.room

import androidx.room.*

@Dao
interface BtDeviceDao {
    @Query("SELECT * FROM btdevice")
    fun getAll(): List<BtDevice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBtDevice(device: BtDevice)

    @Delete
    fun deleteBtDevice(device: BtDevice)
}