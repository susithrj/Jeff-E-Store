package sample;

import javafx.scene.image.Image;

public class Item {
    private String itemCode;
    private String itemName;
    private double cost;
    private Image image;
    private String sizeType;

    public Item(String itemCode) {
        this.itemCode = itemCode;
    }

    public Item(String itemCode, String itemName, double cost, Image image, String sizeType) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.cost = cost;
        this.image = image;
        this.sizeType = sizeType;
    }

    public String getItemName() {
        return itemName;
    }

    public double getCost() {
        return cost;
    }

    public Image getImage() {
        return image;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getSizeType() {
        return sizeType;
    }
}
/*All the attributes are private getters and setters are used to get and set data*/