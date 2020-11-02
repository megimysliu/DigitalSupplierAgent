package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelOrderItems(@Expose val id: Int, @Expose val image: String, @Expose val name: String,
                           @Expose val quantity: Int, @Expose val price: Int,
                           @SerializedName("price_total") @Expose val priceTotal: Int )