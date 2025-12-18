package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Map;

// convert this class to a REST controller
// only logged-in users should have access to these actions

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
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
    public ShoppingCart getCart(Principal principal)
    {
      String username = principal.getName();
      User user = userDao.getByUserName(username);

      return shoppingCartDao.geByUserId(user.getId());
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added

    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(
            @PathVariable int productId,
            Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.addItem(user.getId(), productId, 1);
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated

    @PutMapping("/products/{productId}")
    public void updateProduct(
            @PathVariable int productId,
            @RequestBody Map<String, Integer> body,
            Principal principal)
    {
        try {
            int quantity = body.get("quantity");

            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            shoppingCartDao.updateQuantity(user.getId(), productId, quantity);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops, our bad");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            shoppingCartDao.clearCart(user.getId());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops.. our bad.");
        }
       }
}