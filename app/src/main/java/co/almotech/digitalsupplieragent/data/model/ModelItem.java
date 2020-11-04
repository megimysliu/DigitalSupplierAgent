package co.almotech.digitalsupplieragent.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cart", indices = @Index(value = "id", unique = true))
public class ModelItem {

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    private  int genId;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("is_package")
    @Expose
    private String itsPackage;

    @SerializedName("note")
    @Expose
    private String note;

    public int getTotalPrice(){
        return price * quantity;
    }
    public boolean isPackage(){
        return itsPackage.equals("yes");
    }

    public ModelItem() {
    }

    @Ignore
    public ModelItem(ModelProducts product){
        this.id = product.getId();
        this.price = product.getPrice();
        this.itsPackage = "no";
        this.note = "";
        this.quantity = 1;
    }


    @Ignore
    public ModelItem(ModelProducts product, int quantity){

        this.id = product.getId();
        this.price = product.getPrice();
        this.itsPackage = "no";
        this.note = "";
        this.quantity = quantity;

    }

    public int getGenId() {
        return genId;
    }

    public void setGenId(int genId) {
        this.genId = genId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItsPackage() {
        return itsPackage;
    }

    public void setItsPackage(String itsPackage) {
        this.itsPackage = itsPackage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
