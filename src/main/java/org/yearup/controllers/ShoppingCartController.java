package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

//rest controller - http request
//requestmapping - base url for endpoints
//preauthorize - requires user to be logged in
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")

public class ShoppingCartController
{
    //access to shopping cart
    private ShoppingCartDao shoppingCartDao;
    //access to lookup users
    private UserDao userDao;
    //access to product info
    private ProductDao productDao;

    //injecting dao
    @Autowired
    public ShoppingCartController(
    //assigns to controller
            ShoppingCartDao shoppingCartDao,
            UserDao userDao,
            ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // Username of authenticated user
            String userName = principal.getName();

            // find database user by userId
            User user = userDao.getByUserName(userName);

            //gets shopping cart associated with user
            return shoppingCartDao.getByUserId(user.getUserId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
