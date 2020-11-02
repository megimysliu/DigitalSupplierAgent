package co.almotech.digitalsupplieragent.data.model

import com.google.gson.annotations.Expose

data class ModelUser(@Expose val id: Int, @Expose val name: String, @Expose val email: String)