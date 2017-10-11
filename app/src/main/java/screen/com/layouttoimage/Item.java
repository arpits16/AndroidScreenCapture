package screen.com.layouttoimage;

/**
 * Created by Arpit Singhal on 10/6/2017.
 */

/**
 * This class represents one Item purchased by the customer
 */
public class Item {

    public static int grandTotal=0;
    private int quantity;
    private String description;
    private int amount;

    private Item(int quantity, String description, int amount) {
        this.quantity = quantity;
        this.description = description;
        this.amount = amount;
        grandTotal+=amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public static Item getItem(String description, int quantity, int price) {
        if (quantity != 0) {
            price = price * quantity;
        }

        Item item = new Item(quantity, description, price);
        return item;
    }
}
