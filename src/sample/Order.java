package sample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class   Order {
    private int orderId;
    private Date orderDate;
    private Customer customer;
    private double totalAmount;
    private ArrayList<OrderItem> itemList;

    public Order(int orderId, Date orderDate, Customer customer, double totalAmount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalAmount = totalAmount;
    }

    public Order(Date orderDate, Customer customer, double totalAmount, ArrayList<OrderItem> itemlist) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.itemList = itemlist;
    }

    public Order(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public ArrayList<OrderItem> getItemlist() {
        return itemList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String generateOrderInvoice(Order customerOrder){
        String invoice;
        String items = "\n";          //generating the tax invoice of the order
        for (int j = 0 ;j < customerOrder.getItemlist().size();j++){
            items = items + String.format("%4s %12s %9s %10s %8s %4s %8s \n",String.valueOf(j+1),customerOrder.getItemlist().get(j).getItem().getItemName(),
                    customerOrder.getItemlist().get(j).getItem().getItemCode(), "#"+customerOrder.getItemlist().get(j).getSize(),
                    customerOrder.getItemlist().get(j).getItem().getCost(),customerOrder.getItemlist().get(j).getQuantity(),customerOrder.getItemlist().get(j).getPrice());
        }
        String head = String.format("%45s ","Jeff’s Fishing Shack\n\n" ) +String.format( "%39s","Tax Invoice\n\n") +"Jeff’s Fishing Shack\n" +
                "Trading as: IIT Pty Ltd\nShop 4, Trump Boat Harbour\nColombo, WA, 15191\n" +
                "T: 011 2833856\nE: Jeffstoresiit@gmail.com\n\n" + String.format("%1s %49s","Receipt#: " +String.valueOf(customerOrder.orderId),"Date:" +
                (new SimpleDateFormat("dd/MM/yyyy").format(customerOrder.orderDate))) +
                "\n\nCustomer: "+ customerOrder.customer.getName()+"\n" + customerOrder.customer.getAddress() +
                "\nCustomer#:" + String.valueOf(customerOrder.customer.getId()) +"\n" +
                "Customer email: " + customerOrder.customer.getEmail()+ "\n\n";
        String columns  = String.format("%5s %-15s %5s %10s %8s %4s %8s \n","NO.", "Desc.", "Code", "Size","Cost","Qty","Amount");
        String end = String.format("%65s","Total Paid-"+ String.valueOf(customerOrder.totalAmount)+"\n\n") + String.format("%65s","Thank you for your business.\n" )+
                String.format("%65s","Jeff’s - where the real fishermen shop.\n");
        invoice =head + columns + items + "\n" + end;
        System.out.println(invoice);
        return invoice;
    }
}
