package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelClientsResponse (@Expose val error: Boolean, @Expose val message: String,
                                 @Expose val data: List<ModelClients>?){
    companion object{
        fun modelError(error: Throwable): ModelClientsResponse{

            return ModelClientsResponse(true,error.toString(),null)
        }
    }
}