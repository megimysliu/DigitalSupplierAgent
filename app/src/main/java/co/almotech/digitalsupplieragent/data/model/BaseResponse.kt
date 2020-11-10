package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

open class BaseResponse( @Expose val error: Boolean, @Expose val message: String) {
}