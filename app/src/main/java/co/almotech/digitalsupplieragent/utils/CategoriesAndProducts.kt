package co.almotech.digitalsupplieragent.utils

import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse

data class CategoriesAndProducts(val categories: ModelCategoriesResponse, val products: ModelProductsResponse) {
}