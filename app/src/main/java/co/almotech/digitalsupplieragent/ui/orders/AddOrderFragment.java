package co.almotech.digitalsupplieragent.ui.orders;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrder;
import co.almotech.digitalsupplieragent.data.model.ModelCreateOrderResponse;
import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.FragmentAddBinding;
import co.almotech.digitalsupplieragent.ui.cart.CartViewModel;
import co.almotech.digitalsupplieragent.ui.clients.ClientsViewModel;
import static android.widget.LinearLayout.VERTICAL;


public class AddOrderFragment extends Fragment {

    private FragmentAddBinding mBinding;
    private CartViewModel mCartViewModel;
    private ClientsViewModel mClientsViewModel;
    private AddItemAdapter mItemAdapter;
    private  LoginViewModel mLoginViewModel;
    private List<ModelItem> mItems = new ArrayList<>();
    private List<ModelProducts> mProducts = new ArrayList<>();
    private List<ModelClients> mClients = new ArrayList<>();
    private AutoCompleteAdapter mClientsAdapter;
    private String typeChoice ="";
    private OrdersViewModel mOrdersViewModel;
    private NavController mNavController;


    public AddOrderFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        mClientsViewModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mOrdersViewModel = new ViewModelProvider(requireActivity()).get(OrdersViewModel.class);
        mBinding = FragmentAddBinding.inflate(inflater,container,false);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mCartViewModel.getItems().observe(getViewLifecycleOwner(),this::consumeItems);
        mCartViewModel.getProducts().observe(getViewLifecycleOwner(),this::consumeProducts);
        mBinding.setViewmodel(mCartViewModel);
        mClientsViewModel.getClients();
        mClientsViewModel.clients().observe(getViewLifecycleOwner(),this::consumeClients);
        mNavController = NavHostFragment.findNavController(this);
        setupRecyclerView();
        setupAutoComplete();
        mBinding.client.setOnItemClickListener(((parent, view, position, id) -> {
            ModelClients client = (ModelClients)parent.getItemAtPosition(position);
            mClientsViewModel.selectedClient.set(client.getId());
        }));

        setHasOptionsMenu(true);

        mBinding.type.setOnItemClickListener((parent, view, position, id)->{

            String choice = (String) parent.getItemAtPosition(position);
            if(choice.equals("To deliver")){
                typeChoice = choice.toLowerCase().replace(" ", "_");
            }else{
                typeChoice = choice.toLowerCase();
            }

        });

        mBinding.addOrder.setOnClickListener(v ->{
            makeNewOrder();

        });



        return mBinding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.agentFragment:
                mNavController.navigate(BottomNavGraphDirections.actionAgent());
                return true;
            case R.id.cartFragment:
                mNavController.navigate(BottomNavGraphDirections.actionCart());
                return true;
            case R.id.action_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_confirm_txt)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, ((dialog, which) -> logout())).show();
            default:
                return super.onOptionsItemSelected(item);


        }
    }


    private void logout(){

        mLoginViewModel.logout();
        mNavController.navigate(BottomNavGraphDirections.actionLogout());



    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = mBinding.itemsRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        mItemAdapter = new AddItemAdapter(mItems,mProducts);
        recyclerView.setAdapter(mItemAdapter);
    }

    private void setupAutoComplete(){
        List<String> choices = new ArrayList(Arrays.asList(new String[]{"To deliver", "Stock"}));
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.list_item,choices);
        mBinding.type.setAdapter(arrayAdapter);
    }


    private void consumeClients(ModelClientsResponse response){

        if(!response.getError()){
            List<ModelClients> clients = response.getData();
            mClients.clear();
            mClients.addAll(clients);
            mClientsAdapter = new AutoCompleteAdapter(getContext(), mClients);
            mBinding.client.setAdapter(mClientsAdapter);
        }


    }

    private void consumeItems(List<ModelItem> items){
        mItems.clear();
        mItems.addAll(items);
        if(mItems.isEmpty()){
            mBinding.emptyLinear.setVisibility(View.VISIBLE);
        }else{
            mBinding.emptyLinear.setVisibility(View.GONE);
        }
        mItemAdapter.notifyDataSetChanged();
    }

    private void consumeProducts(List<ModelProducts> products){
        mProducts.clear();
        mProducts.addAll(products);
        mItemAdapter.notifyDataSetChanged();
    }

    private void makeNewOrder(){
        String note = mBinding.notes.getText().toString();
        int clientId = mClientsViewModel.selectedClient.get();
        if(clientId == -1){
            mBinding.clientAutocomplete.setError("Please select client");
            mBinding.client.requestFocus();
            return;
        }else{
            mBinding.clientAutocomplete.setError(null);
        }

        if(mBinding.notes.getText().toString().equals("")){

            mBinding.noteLayout.setError("Field is required");
            mBinding.notes.requestFocus();
            return;
        }else{
            mBinding.noteLayout.setError(null);

        }
        String type = typeChoice;
        if(typeChoice.equals("")){
            mBinding.typeAutocomplete.setError("Please select type");
            mBinding.type.requestFocus();
            return;
        }else{
            mBinding.typeAutocomplete.setError(null);
        }



        List<ModelItem> items = mCartViewModel.getItems().getValue();
        if(items == null || items.isEmpty()) {
            Toast.makeText(getContext(), "Cart can't be empty", Toast.LENGTH_SHORT).show();
            return;

        }


        ModelCreateOrder order = new ModelCreateOrder(note, clientId,type, items);

        mCartViewModel.createOrder(order);
        mCartViewModel.getNewOrder().observe(getViewLifecycleOwner(),this::consumeOrderCreation);


    }

    private void consumeOrderCreation(ModelCreateOrderResponse response){
        if(!response.getError()){
            Toast.makeText(getContext(),"Order created", Toast.LENGTH_SHORT).show();
           mNavController.navigate(AddOrderFragmentDirections.actionOrders());


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
