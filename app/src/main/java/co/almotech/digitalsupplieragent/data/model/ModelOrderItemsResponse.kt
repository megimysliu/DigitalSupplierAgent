package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelOrderItemsResponse(@Expose val error:  Boolean, @Expose val message:  String,
                                   @Expose val data: List<ModelOrderItems>?) {

    companion object{
        @JvmStatic
        fun modelError(throwable: Throwable):ModelOrderItemsResponse{
            return ModelOrderItemsResponse(true, throwable.toString(), null)
        }
    }
}