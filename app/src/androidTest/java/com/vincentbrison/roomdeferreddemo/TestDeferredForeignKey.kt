package com.vincentbrison.roomdeferreddemo

import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDeferredForeignKey {

    private val BOB_ID = "1"
    private val BOB_NAME = "BOB"

    private lateinit var database: DemoDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, DemoDatabase::class.java).build()
        database.personDao().insert(makeBob())
        database.dogDao().insert(makeDog())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testDeferredForeign() {
        database.catDao().insert(makeCatWithUnknownForeignKey())
        try {
            database.catDao().insert(makeCatWithUnknownForeignKey())
        } catch (ex: SQLiteConstraintException) {
            // no-op
        }
        database.catDao().insert(makeCatWithValidForeignKey())
    }

    @Test
    fun testDeferredWithPragmaHack() {
        database.runInTransactionWithDeferredForeignKeysHack {
            database.personDao().delete(makeBob())
            database.personDao().insert(makeBob())
        }
    }

    @Test
    fun testDeferredWithPragma() {
        database.runInTransactionWithDeferredForeignKeys {
            database.personDao().delete(makeBob())
            database.personDao().insert(makeBob())
        }
    }

    private fun makeBob() = Person(BOB_ID, BOB_NAME)

    private fun makeCatWithUnknownForeignKey() = Cat("1", "wrong value", "Foo")

    private fun makeCatWithValidForeignKey() = Cat("1", BOB_ID, "Foo")

    private fun makeDog() = Dog("1", BOB_ID, "Bar")
}