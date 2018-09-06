package com.vincentbrison.roomdeferreddemo

import android.arch.persistence.room.RoomDatabase

fun RoomDatabase.runInTransactionWithDeferredForeignKeys(transaction: () -> Unit) {
    runInTransaction {
        setDeferredForeignKeys(true)
        transaction.invoke()
    }
}

fun RoomDatabase.runInTransactionWithDeferredForeignKeysHack(transaction: () -> Unit) {
    runInTransaction {
        setDeferredForeignKeys(true)
        transaction.invoke()
        setDeferredForeignKeys(false)
    }
}

private fun RoomDatabase.setDeferredForeignKeys(deferred: Boolean) {
    query("PRAGMA defer_foreign_keys = $deferred", null)
}