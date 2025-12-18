package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    //get shopping cart
    ShoppingCart getByUserId(int userId);
    //add a product into the cart
    void addItem(int userId, int productId, int quantity);

    //update for things already in the cart
    void updateQuantity(int userId, int productId, int quantity);

    //remove all items
    void clearCart(int userId);
}

