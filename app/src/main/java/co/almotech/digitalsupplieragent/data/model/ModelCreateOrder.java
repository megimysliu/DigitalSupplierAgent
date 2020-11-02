package co.almotech.digitalsupplieragent.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCreateOrder {
    @Expose
    private String note;
    @SerializedName("client_id")
    @Expose
    private int clientId;
    @Expose
    private String type;
    @SerializedName("order_items")
    @Expose
    private List<ModelItem> orderItems;

    public ModelCreateOrder(String note, int clientId, String type, List<ModelItem> orderItems) {
        this.note = note;
        this.clientId = clientId;
        this.type = type;
        this.orderItems = orderItems;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ModelItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ModelItem> orderItems) {
        this.orderItems = orderItems;
    }
}
