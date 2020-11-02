package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelUserResponse(@Expose val error: Boolean, @Expose val message: String?,
                             @Expose val token: String? , @Expose val data: ModelUser?){

    companion object {
        @JvmStatic
        fun modelError(error: Throwable):ModelUserResponse{
            return ModelUserResponse(true, error.toString(),null,null)
        }
    }

}
