package com.tomasrepcik.blumodify.unittests.app.storage.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tomasrepcik.blumodify.app.storage.room.AppDatabase
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class BtDaoInstrumentedTest {

    private lateinit var db: AppDatabase
    private lateinit var sut: BtDeviceDao

    private val exampleDevice = BtDevice("0", "device", false, lastConnection = 0L)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        sut = db.btDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addAndGetToDb() {
        // ACTION
        sut.insertBtDevice(exampleDevice)

        // CHECK
        val results = sut.getAll()
        assertEquals(results.first(), exampleDevice)
    }

    @Test
    fun addAndDeleteDb() {
        // ARRANGE
        sut.insertBtDevice(exampleDevice)

        // ACTION
        sut.deleteByMacAddress("0")

        // CHECK
        val result = sut.getAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun gettingMacAddressesDb() {
        // ARRANGE
        sut.insertBtDevice(exampleDevice)
        sut.insertBtDevice(exampleDevice.copy(macAddress = "1"))

        // ACTION
        val results = sut.getMacAddresses()

        // CHECK
        assertTrue(results.containsAll(arrayListOf("0", "1")))
    }

    @Test
    fun resetDevicesDb() {
        // ARRANGE
        sut.insertBtDevice(exampleDevice.copy(wasConnected = true))
        sut.insertBtDevice(exampleDevice.copy(macAddress = "1", wasConnected = false))

        // ACTION
        sut.resetDevices()

        // CHECK
        val results = sut.getAll()
        assertTrue(results.all { !it.wasConnected })
    }

    @Test
    fun recentlyConnectedDb() {
        // ARRANGE
        sut.insertBtDevice(exampleDevice.copy(wasConnected = true))
        sut.insertBtDevice(exampleDevice.copy(macAddress = "1", wasConnected = false))

        // ACTION
        val devices = sut.getRecentlyConnectedDevices()

        // CHECK
        assertEquals(devices.size, 1)
    }
}