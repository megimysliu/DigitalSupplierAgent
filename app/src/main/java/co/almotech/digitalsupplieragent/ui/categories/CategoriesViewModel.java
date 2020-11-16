package co.almotech.digitalsupplieragent.ui.categories;

import android.util.Log;

import androidx.databinding.ObservableInt;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.List;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import co.almotech.digitalsupplieragent.utils.CategoriesAndProducts;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class CategoriesViewModel extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private final MutableLiveData<ModelCategoriesResponse> categories = new MutableLiveData<>();
    private final MutableLiveData<ModelProductsResponse> productsByCategory = new MutableLiveData<>();
    private final HashMap<Integer, ModelProductsResponse> prodCategory = new HashMap<>();
    private final MutableLiveData<ModelProductsResponse> allProducts = new MutableLiveData<>();
    private final MutableLiveData<Integer> firstCategory =  new MutableLiveData<>();
    public final ObservableInt firstCat = new ObservableInt();
    private final SavedStateHandle savedStateHandle;



    public int selectedCategory = -1;
    private MainRepository mRepository;

    @ViewModelInject
    public CategoriesViewModel(MainRepository repository, @Assisted SavedStateHandle savedStateHandle){
        this.mRepository = repository;
        this.savedStateHandle = savedStateHandle;
        getCategories();

        Log.d(" First categor",String.valueOf(firstCategory.getValue()));
    }

    public void getCategories(){

        mDisposable.add(mRepository.getAllCategories()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response ->{
            categories.setValue(response);
            List<ModelCategories> categories = response.getData();
            if(categories != null || !categories.isEmpty()){

                firstCategory.setValue(categories.get(0).getId());
                System.out.println("Category id viewmodel check: " + firstCategory);

            }else{
                firstCategory.setValue(-1);
            }
                } ,
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

    public void getCategoriesAndProducts(int firstCategory){


        Log.d("Category id control: ", String.valueOf(firstCategory));
       if(prodCategory.get(firstCategory) != null) {
         productsByCategory.setValue(prodCategory.get(firstCategory));
       }

        Single<ModelCategoriesResponse> categoriesCall  = mRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
                Single<ModelProductsResponse> productsCall = mRepository.getProductsByCategory(firstCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());




       mDisposable.add(Single.zip(categoriesCall,productsCall, CategoriesAndProducts::new)
               .subscribe(categoriesAndProducts -> {
           categories.setValue(categoriesAndProducts.getCategories());
           setCategoryProducts(firstCategory,categoriesAndProducts.getProducts());

       },throwable -> {categories.setValue(ModelCategoriesResponse.modelError(throwable));
           setCategoryProducts(firstCategory,ModelProductsResponse.modelError(throwable));
       }));


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
    public LiveData<Integer> getFirstCategory(){
        return  firstCategory;
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}
