package co.almotech.digitalsupplieragent.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelClients;

public class AutoCompleteAdapter extends ArrayAdapter<ModelClients> {

    private List<ModelClients> mAllClients = new ArrayList<>();
    private List<ModelClients> mFilteredClients = new ArrayList<>();

    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<ModelClients> clients) {
        super(context,0,  clients);
        mAllClients = new ArrayList<>(clients);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        TextView clientTextView = (TextView) convertView;
        ModelClients client = getItem(position);
        if(client !=null){
            clientTextView.setText(client.getName());
        }
        return convertView;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            mFilteredClients = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                mFilteredClients.addAll(mAllClients);
            }else{
                String filterPattern  = constraint.toString().toLowerCase().trim();
                for(ModelClients  client: mAllClients){
                    if(client.getName().toLowerCase().contains(filterPattern)){
                        mFilteredClients.add(client);
                    }
                }
            }

            results.values = mFilteredClients;
            results.count = mFilteredClients.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            clear();
            if(results.values !=null){
                addAll((List) results.values);
                notifyDataSetChanged();
            }

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ModelClients)resultValue).getName();
        }
    };
}
