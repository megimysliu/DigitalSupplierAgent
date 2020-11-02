package co.almotech.digitalsupplieragent.data.model

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import co.almotech.digitalsupplieragent.R
import com.bumptech.glide.Glide
import com.google.gson.annotations.Expose

data class ModelCategoriesResponse(@Expose val error:Boolean,
                                   @Expose val message: String,
                                   @Expose val data: List<ModelCategories>? ){
    companion object{
        @JvmStatic
        public fun modelError(error: Throwable) :ModelCategoriesResponse {
            return ModelCategoriesResponse(true, error.toString(), null)
        }

        @JvmStatic
        @BindingAdapter("image","placeholder")
        fun setImage(image: ImageView, url: String?, placeHolder: Drawable) {

            if (!url.isNullOrEmpty()){

                Glide.with(image.context).load(url).centerCrop()
                        .placeholder(R.drawable.ic_noimg)
                        .into(image)
            }
            else{
                image.setImageDrawable(placeHolder)
            }


        }
    }
}