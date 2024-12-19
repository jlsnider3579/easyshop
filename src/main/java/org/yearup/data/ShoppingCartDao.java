package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);

    // add additional method signatures here

    // Method to add a specific product to the user's cart.
    // The userId identifies the user, and the product object contains the details of the product to be added to the cart.
    ShoppingCart addItemsToCart(int userId, Product product);

    // Method to update a specific product in the user's cart.
    // The userId identifies the user whose cart is being updated.
    // The shoppingCartItem contains the updated details (e.g., quantity) of the product to be modified.
    // The productId identifies the specific product in the cart to be updated.
    void updateCart(int userId, ShoppingCartItem item, int productId);

    // Method to delete the entire shopping cart for the specified user.
    // The userId identifies the user whose cart is to be deleted.
    void deleteCart(int userId);
}
