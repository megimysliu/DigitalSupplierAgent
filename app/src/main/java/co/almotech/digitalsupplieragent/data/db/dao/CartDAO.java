package co.almotech.digitalsupplieragent.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import co.almotech.digitalsupplieragent.data.model.ModelItem;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM cart")
    Single<List<ModelItem>> getAllItems();

    @Insert
    Completable insert(ModelItem item);

    @Insert
    Completable insertAll(List<ModelItem> items);

    @Delete
    Completable delete(ModelItem item);

    @Query("DELETE FROM cart")
    Completable deleteAll();

    @Query("UPDATE cart set quantity = quantity + 1 WHERE id= :id")
    Completable updateQuantity(int id);

    @Query("SELECT COUNT(genId) FROM cart")
    LiveData<Integer> getCartCount();

    @Query("UPDATE cart SET quantity = quantity + :quantity WHERE id = :id")
    Completable addCount(int id, int quantity);




}
