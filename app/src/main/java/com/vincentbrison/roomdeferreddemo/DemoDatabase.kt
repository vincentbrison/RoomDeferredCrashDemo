package com.vincentbrison.roomdeferreddemo

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Database
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Insert
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.RoomDatabase

@Database(
    entities = [Person::class, Cat::class, Dog::class],
    version = 1
)
abstract class DemoDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun catDao(): CatDao
    abstract fun dogDao(): DogDao
}

@Dao
abstract class PersonDao {
    @Insert
    abstract fun insert(person: Person)

    @Delete
    abstract fun delete(person: Person)
}

@Dao
abstract class CatDao {
    @Insert
    abstract fun insert(cat: Cat)
}

@Dao
abstract class DogDao {
    @Insert
    abstract fun insert(dog: Dog)
}

@Entity
data class Person(
    @PrimaryKey var id: String,
    var name: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("personId"),
            deferred = true
        )
    ]
)
data class Cat(
    @PrimaryKey var id: String,
    var personId: String,
    var name: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("personId"),
            deferred = false
        )
    ]
)
data class Dog(
    @PrimaryKey var id: String,
    var personId: String,
    var name: String
)