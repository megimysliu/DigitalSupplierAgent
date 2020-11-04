package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelChangePasswordResponse(@Expose val error:  Boolean, @Expose val message: String ) {

    companion object{
        @JvmStatic
        fun modelError(error: Throwable):ModelChangePasswordResponse{

            return ModelChangePasswordResponse(true, error.toString())
        }
    }
}