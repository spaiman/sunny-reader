package com.setiawanpaiman.sunnyreader.data.db

import androidx.room.*

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "response") val response: String,
    @ColumnInfo(name = "last_update") val lastUpdate: Long = System.currentTimeMillis()
) {

}

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Query("SELECT * FROM item WHERE id = :id")
    fun findById(id: Long): Item?

    @Insert
    fun insertAll(vararg items: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT COUNT(*) FROM item")
    fun getCount(): Long

    @Query("DELETE FROM item WHERE id IN (SELECT id FROM item ORDER BY last_update ASC LIMIT :n)")
    fun removeOldestNData(n: Long)
}

@Database(entities = [Item::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
