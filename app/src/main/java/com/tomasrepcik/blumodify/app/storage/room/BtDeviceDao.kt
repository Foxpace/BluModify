package com.tomasrepcik.blumodify.app.storage.room

import androidx.room.*

@Dao
interface BtDeviceDao {
    @Query("SELECT * FROM btdevice ORDER BY deviceName ASC")
    fun getAll(): List<BtDevice>

    @Query("SELECT macAddress FROM btdevice")
    fun getMacAddresses(): List<String>

    @Query("SELECT * FROM btdevice WHERE wasConnected = 1")
    fun getRecentlyConnectedDevices(): List<BtDevice>

    @Query("UPDATE btdevice SET wasConnected = 0 WHERE wasConnected = 1")
    fun resetDevices()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBtDevice(device: BtDevice)

    @Query("DELETE FROM btdevice WHERE macAddress = :macAddress")
    fun deleteByMacAddress(macAddress: String)
}