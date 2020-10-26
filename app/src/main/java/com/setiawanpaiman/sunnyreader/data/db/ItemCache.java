package com.setiawanpaiman.sunnyreader.data.db;

import androidx.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE)
public class ItemCache extends BaseModel {

    private static final long MAX_ITEMS = 5000;

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

    @Override
    public void save() {
        super.save();

        long count = SQLite.selectCountOf().from(ItemCache.class).count();
        if (count > MAX_ITEMS) {
            SQLite.delete()
                    .from(ItemCache.class)
                    .where(ItemCache_Table.id.in(
                            SQLite.select(ItemCache_Table.id)
                                    .from(ItemCache.class)
                                    .orderBy(ItemCache_Table.lastUpdate, true)
                                    .limit((int) (count - MAX_ITEMS))
                    ))
                    .query();
        }
    }

    public long getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }

    @Nullable
    public static ItemCache get(long id) {
        return SQLite.select()
                .from(ItemCache.class)
                .where(ItemCache_Table.id.is(id))
                .querySingle();
    }
}
