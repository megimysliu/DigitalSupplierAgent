package co.almotech.digitalsupplieragent.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import co.almotech.digitalsupplieragent.data.db.dao.CartDAO;
import co.almotech.digitalsupplieragent.data.model.ModelItem;

@Database(entities = {ModelItem.class}, version = 1, exportSchema = false)
public  abstract class AppDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
}
