package co.almotech.digitalsupplieragent.ui.categories;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class CategoriesViewModel extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private final MutableLiveData<ModelCategoriesResponse> categories = new MutableLiveData<>();
    private final MutableLiveData<ModelProductsResponse> productsByCategory = new MutableLiveData<>();
    private final HashMap<Integer, ModelProductsResponse> prodCategory = new HashMap<>();
    private final MutableLiveData<ModelProductsResponse> allProducts = new MutableLiveData<>();


    public int selectedCategory = -1;
    private MainRepository mRepository;

    @ViewModelInject
    public CategoriesViewModel(MainRepository repository){
        this.mRepository = repository;
        getCategories();
    }

    private void getCategories(){

        mDisposable.add(mRepository.getAllCategories()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(categories::setValue,
                throwable -> categories.setValue(ModelCategoriesResponse.modelError(throwable))));
    }

    public void getProductsByCategory(int categoryId){
        if(prodCategory.get(categoryId) != null){
            productsByCategory.setValue(prodCategory.get(categoryId));
        }else{

            mDisposable.add(mRepository.getProductsByCategory(categoryId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> setCategoryProducts(categoryId,response),
                            throwable -> setCategoryProducts(categoryId,ModelProductsResponse.modelError(throwable))
                    ));
        }

    }

    public void getCategoriesAndProducts(int categoryId){

        
    }

    public void setCategoryProducts(Integer categoryId, ModelProductsResponse response){
        productsByCategory.setValue(response);
        if(prodCategory.get(categoryId) == null){
            prodCategory.put(categoryId,response);
        }

    }

    public void getAllProducts(){

        mDisposable.add(mRepository.getAllProducts()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(allProducts::setValue,
                throwable -> allProducts.setValue(ModelProductsResponse.modelError(throwable))));

    }


    public LiveData<ModelCategoriesResponse> categories(){

        return categories;
    }

    public LiveData<ModelProductsResponse> categoryProducts(){
        return productsByCategory;
    }

    public LiveData<ModelProductsResponse> products(){
        return allProducts;
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}
