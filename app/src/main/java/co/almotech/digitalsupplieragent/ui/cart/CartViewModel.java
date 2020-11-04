package co.almotech.digitalsupplieragent.ui.cart;

import androidx.databinding.ObservableInt;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import co.almotech.digitalsupplieragent.data.model.ModelCreateOrder;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrderResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import java8.util.stream.StreamSupport;

import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.repo.CartRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CartViewModel  extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private CartRepository mRepository ;
    private MainRepository mMainRepository;

    private final MutableLiveData<List<ModelItem>> items = new MutableLiveData<>();
    public final ObservableInt totalSum = new ObservableInt();
    private final MutableLiveData<List<ModelProducts>> products = new MutableLiveData<>();
    private final MutableLiveData<ModelCreateOrderResponse> newOrder = new MutableLiveData<>();

    @ViewModelInject
    public CartViewModel(CartRepository repository, MainRepository mainRepository){
        mRepository = repository;
        mMainRepository = mainRepository;
        getAllItems();
        getAllProducts();
    }

    public void getAllItems(){

        mDisposable.add(mRepository.getAllItems()
                .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
       .subscribe(modelItems -> {
           items.setValue(modelItems);
           int total;
           if(modelItems.isEmpty()){
               total = 0;
           }else{
               total = StreamSupport.stream(modelItems)
                       .mapToInt(ModelItem::getTotalPrice)
                       .sum();
           }

           totalSum.set(total);
       }, Timber::e));
    }

    public void incrementQuantity(ModelItem item){

        mDisposable.add(mRepository.addCount(item.getId(), 1)
        .subscribeOn(Schedulers.io())
        .subscribe(this::getAllItems));
    }

    public void decrementQuantity(ModelItem item){

        if(item.getQuantity() > 1){

            mDisposable.add(mRepository.addCount(item.getId(), -1)
            .subscribeOn(Schedulers.io())
            .subscribe(this::getAllItems));
        }

    }
    public void deleteItem(ModelItem item){
        mDisposable.add(mRepository.delete(item)
        .subscribeOn(Schedulers.io())
        .subscribe(this::getAllItems));
    }

    public void addToCart(ModelProducts product){
        if(product != null){
            ModelItem item = new ModelItem(product);
            mDisposable.add(mRepository.insert(item)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
            .subscribe(this::getAllItems,
                    throwable -> mDisposable.add(mRepository.updateQuantity(item.getId())
                            .subscribeOn(Schedulers.io())
                    .subscribe(this::getAllItems))));

        }



    }

    public void addToCartWithQuantity(ModelProducts product,int quantity){
        if(product != null){
            ModelItem item = new ModelItem(product,quantity);
            mDisposable.add(mRepository.insert(item)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::getAllItems,
                            throwable -> mDisposable.add(mRepository.updateQuantity(item.getId())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(this::getAllItems))));

        }



    }


    public void clearCart(){

        mDisposable.add(mRepository.deleteAll()
                .subscribeOn(Schedulers.io())
                .subscribe(this::getAllItems));
    }

    public void getAllProducts(){
        mDisposable.add(mMainRepository.getAllProducts()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe( response -> products.setValue(response.getData()),
                throwable -> Timber.e(throwable)));
    }

    public void createOrder(ModelCreateOrder createOrder){
        mDisposable.add(mMainRepository.createOrder(createOrder)
                .doAfterSuccess(response -> clearCart())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> newOrder.setValue(response),
                        throwable -> newOrder.setValue(ModelCreateOrderResponse.modelError(throwable))
                ));
    }

    public LiveData<ModelCreateOrderResponse> getNewOrder(){
        return  newOrder;
    }


    public LiveData<List<ModelItem>> getItems(){
        return items;
    }

    public LiveData<List<ModelProducts>> getProducts(){
        return products;
    }


    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}
