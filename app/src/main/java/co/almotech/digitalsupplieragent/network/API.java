package co.almotech.digitalsupplieragent.network;

import java.util.List;

import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import co.almotech.digitalsupplieragent.data.model.ModelChangePasswordResponse;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateClientResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrder;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrderResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItemsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrdersResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelResponse;
import co.almotech.digitalsupplieragent.data.model.ModelUserResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {

    String NO_AUTHENTICATION_TRUE = "No-Authentication: true";

@POST("login")
@FormUrlEncoded
@Headers(NO_AUTHENTICATION_TRUE)
Single<ModelUserResponse> userLogin(@Field("email") String email, @Field("password") String password);

@GET("clients")
Single<ModelClientsResponse> getAllClients();

@GET("categories")
Single<ModelCategoriesResponse> getAllCategories();

@GET("products_by_category/{id}")
Single<ModelProductsResponse> getProductsByCategory(@Path("id") int id);

@GET("weekly_orders")
Single<ModelOrdersResponse> getWeeklyOrders();

@GET("all_products")
Single<ModelProductsResponse> getAllProducts();

@GET("order_items/{id}")
Single<ModelOrderItemsResponse> getOrderItemsByOrder(@Path("id") int id);

@GET("my_clients")
Single<ModelClientsResponse> getMyClients();

@POST("make_order")
@Headers("Content-Type: application/json")
Single<ModelCreateOrderResponse> createOrder(@Body ModelCreateOrder createOrder);

@FormUrlEncoded
@POST("change_password")
Single<ModelChangePasswordResponse> changePassword(@Field("old_password") String oldPassword,
                                                   @Field("password") String password,
                                                   @Field("password_confirmation") String passwordConfirmation);


@FormUrlEncoded
@POST("create_client")
    Single<ModelCreateClientResponse> createClient(@Field("name") String name, @Field("email") String email,
                                                   @Field("phone_number") String phoneNumber,
                                                   @Field("account_type") int accountType,
                                                   @Field("nuis") String nuis,
                                                   @Field("lat") String lat, @Field("lng") String lng,
                                                   @Field("address") String address);
}
