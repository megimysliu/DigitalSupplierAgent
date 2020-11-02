package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelOrdersResponse(@Expose val error:Boolean, @Expose val message: String, @Expose val orders: List<ModelOrders>?,
                               @SerializedName("today_balance") @Expose val todayBalance: Int,
                               @SerializedName("total_balance") @Expose val totalBalance: Int) {

    companion object{
        @JvmStatic
        fun modelError(throwable: Throwable): ModelOrdersResponse{
            return ModelOrdersResponse(true, throwable.toString(),null,0,0)
        }
    }
}