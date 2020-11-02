package co.almotech.digitalsupplieragent.repo;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import co.almotech.digitalsupplieragent.data.db.dao.CartDAO;
import co.almotech.digitalsupplieragent.data.model.ModelItem;
import io.reactivex.Completable;
import io.reactivex.Single;

public class CartRepository {

private  CartDAO mCartDAO;

@Inject
public CartRepository(CartDAO cartDAO){

    mCartDAO = cartDAO;
}

public Single<List<ModelItem>> getAllItems(){

    return mCartDAO.getAllItems();
}

    public  Completable insert(ModelItem item){
    return  mCartDAO.insert(item);
}

    public Completable insertAll(List<ModelItem> items){

    return mCartDAO.insertAll(items);
}

    public Completable delete(ModelItem item){
    return mCartDAO.delete(item);
}

    public Completable deleteAll(){
    return mCartDAO.deleteAll();
}

    public  Completable updateQuantity(int id){
    return mCartDAO.updateQuantity(id);
}

    public  LiveData<Integer> getCartCount(){
    return mCartDAO.getCartCount();
}

    public Completable addCount(int id, int quantity){
    return mCartDAO.addCount(id,quantity);

}


}
