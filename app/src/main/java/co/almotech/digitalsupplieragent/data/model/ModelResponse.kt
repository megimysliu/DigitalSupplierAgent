package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

open class ModelResponse<T>( error: Boolean, message: String, @Expose val data: T?):BaseResponse(error, message) {

    companion object{
        @JvmStatic
        fun error(throwable: Throwable): ModelResponse<String>{
            return ModelResponse(true, throwable.toString(),null)
        }
    }
}