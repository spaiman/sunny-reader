package com.setiawanpaiman.sunnyreader.data.db;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE)
public class ItemCache extends BaseModel {

    @PrimaryKey(autoincrement = false)
    @Column
    long id;

    @Column
    String response;

    @Column
    long lastUpdate;

    public ItemCache() {
    }

    public ItemCache(long id, String response) {
        this.id = id;
        this.response = response;
        this.lastUpdate = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    @Nullable
    public static ItemCache get(long id) {
        return SQLite.select()
                .from(ItemCache.class)
                .where(ItemCache_Table.id.is(id))
                .querySingle();
    }
}
