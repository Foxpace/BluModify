package com.tomasrepcik.blumodify.app.storage.room

import androidx.room.*

@Dao
interface BtDeviceDao {
    @Query("SELECT * FROM btdevice ORDER BY deviceName ASC")
    fun getAll(): List<BtDevice>

    @Query("SELECT macAddress FROM btdevice")
    fun getMacAddresses(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBtDevice(device: BtDevice)

    @Query("DELETE FROM btdevice WHERE macAddress = :macAddress")
    fun deleteByMacAddress(macAddress: String)
}