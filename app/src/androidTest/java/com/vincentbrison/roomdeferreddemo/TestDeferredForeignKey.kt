package com.vincentbrison.roomdeferreddemo

import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDeferredForeignKey {

    private val BOB_ID = "1"
    private val BOB_NAME = "BOB"

    @Test
    fun testDeferredForeign() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val database = Room.inMemoryDatabaseBuilder(appContext, DemoDatabase::class.java).build()
        database.personDao().insert(makeBob())
        try {
            database.catDao().insert(makeCatWithUnknownForeignKey())
        } catch (ex: SQLiteConstraintException) {
            // no-op
        }
        database.catDao().insert(makeCatWithValidForeignKey())
    }

    private fun makeBob() = Person(BOB_ID, BOB_NAME)

    private fun makeCatWithUnknownForeignKey() = Cat("1", "wrong value", "Foo")

    private fun makeCatWithValidForeignKey() = Cat("1", BOB_ID, "Foo")
}