package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged-in users should have access to these actions
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

// each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {

        try {
            // Get the currently logged-in username from the Principal object
            String userName = principal.getName();

            // Find the corresponding User object from the database using the username
            User user = userDao.getByUserName(userName);

            // Retrieve the user's ID to fetch their cart
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            // Fetch and return the shopping cart associated with the user
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            return cart;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

     // add a POST method to add a product to the cart - the url should be
     // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("products/{productId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal){

        // Get the logged-in user's username
        String userName = principal.getName();

        // Fetch the User object associated with the username
        User user = userDao.getByUserName(userName);

        // Retrieve the user's ID to associate the cart with the correct user
        int userId = user.getId();

        // Add the product to the user's cart and return the updated cart
        return shoppingCartDao.addItemsToCart(userId, productDao.getById(productId));
    }



    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{productId}")
    public void updateShoppingCart(Principal principal, @RequestBody ShoppingCartItem item, @PathVariable int productId){

        // Get the logged-in user's username
        String userName = principal.getName();

        // Fetch the User object associated with the username
        User user = userDao.getByUserName(userName);

        // Retrieve the user's ID to update the correct cart
        int userId = user.getId();

        // Update the shopping cart with the new quantity of the product
        shoppingCartDao.updateCart(userId, item, productId);

    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
   // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ShoppingCart deleteShoppingCart(Principal principal){

        // Get the logged-in user's username
        String userName = principal.getName();

        // Fetch the User object associated with the username
        User user = userDao.getByUserName(userName);

        // Retrieve the user's ID to delete the cart associated with the correct user
        int userId = user.getId();

        // Delete the shopping cart for the user
        shoppingCartDao.deleteCart(userId);

        return shoppingCartDao.getByUserId(userId);
    }

}
