package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelOrders(@Expose val id: Int, @SerializedName("client_name") @Expose val clientName: String,
                      @SerializedName("client_email") @Expose val clientEmail: String,
                      @SerializedName("client_phone") @Expose val clientPhone: String,
                      @SerializedName("price_total") @Expose val priceTotal: Int,
                       @Expose val status: String, @SerializedName("created_at") @Expose val createdAt: String
                      )