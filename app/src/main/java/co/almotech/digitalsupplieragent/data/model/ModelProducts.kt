package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelProducts(@Expose val id: Int, @Expose val name: String,@Expose val image: String,
                         @Expose val description:String, @Expose val price: Int, @Expose val stock: Int,
                         @Expose val unit: String, @Expose @SerializedName("minimum_order") val minimumOrder: Int,
                         @SerializedName("maximum_order") @Expose val maximumOrder: Int,
                         @Expose val discount: Int, @SerializedName("discounted_price") @Expose val discountedPrice: Float,
                         @Expose val prices: List<ModelPrices>)