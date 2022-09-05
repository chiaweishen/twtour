package com.scw.twtour.db

import com.scw.twtour.db.dao.NoteDao
import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.entity.NoteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.java.KoinJavaComponent.get

@ExperimentalCoroutinesApi
class BasicDatabaseTest {

    private lateinit var database: BasicDatabase
    private lateinit var scenicSpotDao: ScenicSpotDao
    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        database = get(BasicDatabase::class.java)
        scenicSpotDao = database.scenicSpotDao()
        noteDao = database.noteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testNote() = runTest {
        noteDao.deleteAll()
        noteDao.insert(NoteEntity("test0001", true, false))
        noteDao.insert(NoteEntity("test0002", false, true))
        noteDao.insert(NoteEntity("test0003", true, true))

        val size = noteDao.queryAllNoteScenicSpots().first().size
        Assert.assertTrue(size == 3)
    }
}