package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelProductsResponse(@Expose val error: Boolean, @Expose val message: String, @Expose val data: List<ModelProducts>? ) {

    companion object{

        @JvmStatic
        fun modelError(error: Throwable):ModelProductsResponse{

            return ModelProductsResponse(true, error.toString(),null)

        }
    }
}