package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelClients(@Expose val id: Int, @Expose val name: String, @Expose val email: String,
                        @SerializedName("phone_number") @Expose val phoneNumber: String,
                        @Expose val address: String, @Expose val lat:String, @Expose val lng: String)