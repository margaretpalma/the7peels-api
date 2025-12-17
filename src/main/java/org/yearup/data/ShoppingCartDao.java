package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart geByUserId(int userId);

    void addItem(int userId, int productId, int quantity);
    void updateQuantity(int userId, int productId, int quantity);
    void clearCart(int userId);
}

