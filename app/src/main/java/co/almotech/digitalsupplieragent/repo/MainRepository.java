package co.almotech.digitalsupplieragent.repo;

import java.util.List;

import javax.inject.Inject;

import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateClientResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrder;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrderResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItemsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrdersResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelResponse;
import co.almotech.digitalsupplieragent.network.API;
import io.reactivex.Single;
import retrofit2.http.Field;

public class MainRepository {

    private API api;

    @Inject
    public MainRepository(API api){
        this.api = api;

    }

    public Single<ModelClientsResponse> getAllClients(){

        return api.getAllClients();
    }

    public Single<ModelCategoriesResponse> getAllCategories(){
        return api.getAllCategories();
    }

    public Single<ModelProductsResponse> getProductsByCategory(int id){
        return api.getProductsByCategory(id);
    }

    public Single<ModelOrdersResponse> getWeeklyOrders(){
        return api.getWeeklyOrders();
    }

    public Single<ModelProductsResponse> getAllProducts(){
        return api.getAllProducts();
    }

    public Single<ModelOrderItemsResponse> getOrderItemsByOrder(int id){
        return api.getOrderItemsByOrder(id);
    }

    public Single<ModelClientsResponse> getMyClients(){
        return  api.getMyClients();
    }

    public Single<ModelCreateOrderResponse> createOrder(ModelCreateOrder createOrder){
        return api.createOrder(createOrder);
    }

    public Single<ModelCreateClientResponse> createClient(String name,String email, String phoneNumber, int accountType,
                                                         String nuis, String lat,String lng, String address){
        return api.createClient(name, email, phoneNumber, accountType, nuis, lat, lng, address);
    }
}
