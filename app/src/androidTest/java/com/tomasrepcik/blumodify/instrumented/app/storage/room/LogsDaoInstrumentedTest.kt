package com.tomasrepcik.blumodify.instrumented.app.storage.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.tomasrepcik.blumodify.app.storage.room.AppDatabase
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LogsDaoInstrumentedTest {

    private lateinit var db: AppDatabase
    private lateinit var sut: LogsDao

    private val exampleDevice = BtItem("phone", "00:00:00")
    private val exampleReport = LogReport(
        id = 1,
        startTime = System.currentTimeMillis(), connectedDevices = arrayListOf(
            exampleDevice
        ), isSuccess = false
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        sut = db.logsDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addAndGetToDb() {
        // ACTION
        sut.insertReport(exampleReport)
        val results = sut.getAll()

        // CHECK
        assertEquals(results.first(), exampleReport)
    }

    @Test
    fun getByIdDb() {
        // ARRANGE
        sut.insertReport(exampleReport)
        sut.insertReport(exampleReport.copy(id = 2))
        sut.insertReport(exampleReport.copy(id = 3))
        sut.insertReport(exampleReport.copy(id = 4))

        // ACTION
        val results = sut.getLogById(1)

        // CHECK
        assertEquals(results, exampleReport)
    }

    @Test
    fun deleteOldLogsDb() {
        // ARRANGE
        val currentTime = System.currentTimeMillis()
        sut.insertReport(exampleReport.copy(startTime = currentTime))
        sut.insertReport(exampleReport.copy(id = 2, startTime = currentTime - hoursToMillis(24)))
        sut.insertReport(exampleReport.copy(id = 3, startTime = currentTime - hoursToMillis(48)))
        sut.insertReport(exampleReport.copy(id = 4, startTime = currentTime - hoursToMillis(70)))
        sut.insertReport(exampleReport.copy(id = 5, startTime = currentTime - hoursToMillis(80)))

        // ACTION
        sut.deleteOlderItemsThan(currentTime - hoursToMillis(72))

        // CHECK
        val results = sut.getAll()
        assertEquals(results.size, 4)
        assertFalse(results.any {it.id == 5})
    }

    private fun hoursToMillis(h: Int): Long = h.toLong() * 3600 * 1000


}