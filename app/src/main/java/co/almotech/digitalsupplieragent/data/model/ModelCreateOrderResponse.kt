package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelCreateOrderResponse(@Expose val Error: Boolean, @Expose val message: String) {
    companion object{
        @JvmStatic
        fun modelError(throwable: Throwable):ModelCreateOrderResponse{
            return ModelCreateOrderResponse(true,throwable.toString())
        }
    }
}