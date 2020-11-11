package co.almotech.digitalsupplieragent.ui.orders;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItemsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrders;
import co.almotech.digitalsupplieragent.data.model.ModelOrdersResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class OrdersViewModel extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MainRepository mRepository;
    @ViewModelInject
    public OrdersViewModel(MainRepository repository){
        mRepository = repository;


    }

    private final MutableLiveData<ModelOrdersResponse> orders = new MutableLiveData<>();
    private final MutableLiveData<ModelOrderItemsResponse> orderItems = new MutableLiveData<>();
    private final MutableLiveData<ModelOrders> order = new MutableLiveData<>();


    public void getOrders(){
        mDisposable.add(mRepository.getWeeklyOrders()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(orders::setValue,
                throwable -> orders.setValue(ModelOrdersResponse.modelError(throwable))
        ));
    }

    public void getOrderItems(int id){
        mDisposable.add(mRepository.getOrderItemsByOrder(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(orderItems::setValue,
                throwable -> orderItems.setValue(ModelOrderItemsResponse.modelError(throwable))));
    }

    public LiveData<ModelOrdersResponse> orders(){
        return orders;
    }

    public LiveData<ModelOrderItemsResponse> orderItems(){

        return orderItems;
    }



    public void setOrder(ModelOrders order){
        this.order.setValue(order);
    }

    public LiveData<ModelOrders> order(){
        return order;
    }


    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}
