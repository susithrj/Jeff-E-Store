package sample;

public class OrderItem {
    private Item item;
    private int quantity;
    private String size;
    private double price;

    public OrderItem(Item item, int quantity, String size) {
        this.item = item;
        this.quantity = quantity;
        this.size = size;
    }

    public OrderItem(Item item, int quantity, String size, double price) {
        this.item = item;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }
    public double calculateItemPrice(OrderItem item){
        String size = item.size;           //calculating the price according to size and quantity
        double sizeEffect;
        String type =  item.getItem().getSizeType();
       if (type.equals("stadard")){
           if(size.equals("L")){
               sizeEffect = 1.5;
           } else if (size.equals("M")) {
               sizeEffect = 1.2;
           }else {
               sizeEffect = 1;
           }
       }else if(type.equals("number")){
           sizeEffect = Double.parseDouble(size) * 0.5;
       }else if(type.equals("weight")){
          String arr[]= size.split("g");
          sizeEffect = Double.parseDouble(arr[0]);
       }else {
           String arr[]= size.split("m");
           sizeEffect = Double.parseDouble(arr[0]);
       }
       return item.getItem().getCost() * item.getQuantity() * sizeEffect;
    }
}
