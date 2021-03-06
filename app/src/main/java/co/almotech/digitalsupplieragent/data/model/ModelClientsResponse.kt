package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelClientsResponse (@Expose val error: Boolean, @Expose val message: String,
                                 @Expose val data: List<ModelClients>?){
    companion object{
        @JvmStatic
        fun modelError(error: Throwable): ModelClientsResponse{

            return ModelClientsResponse(true,"Error loading data",null)
        }
    }
}