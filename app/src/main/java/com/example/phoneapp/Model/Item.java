package com.example.phoneapp.Model;

import android.widget.ImageView;

public class Item {
    public String id;
    public String name;
    public String description;
    public String imageUrl;
    public double price;
    public String type;

    // Required no-argument constructor for Firebase
    public Item() {}

    public Item(String _id, String _name, String _description, double _price, String  _imageUrl, String _type) {
        id = _id;
        name = _name;
        description = _description;
        price = _price;
        imageUrl = _imageUrl;
        type = _type;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String  getImageUrl() { return imageUrl; }
    public void setImageUrl(String  imageUrl) { this.imageUrl = imageUrl; }

    public String  getType() { return type; }
    public void setType(String  type) { this.type = type; }
}
