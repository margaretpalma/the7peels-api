package org.yearup.data.mysql;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    //get the cart for the user, use database
//    @Override
//    public ShoppingCart geByUserId(int userId) {
//        return null;
//    }

    @Override
    public ShoppingCart geByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
        SELECT product_id, quantity
        FROM shopping_cart
        WHERE user_id = ?
    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            while (row.next()) {

                int productId = row.getInt("product_id");
                int quantity = row.getInt("quantity");

                var product = productDao.getById(productId);

                var item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);

                cart.add(item);
            }
        }
        catch (SQLException e) {
        throw new RuntimeException("Error getting shopping cart", e);
    }
            return cart;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

            String sql = """
                    SELECT quantity
                    FROM shopping_cart
                    WHERE user_id = ? AND product_id = ? 
                    """;
        try(Connection connection = new Connection();
            PreparedStatement statement = connection.prepareStatement(sql)){

        }
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {

       String sql = """
               UPDATE shopping_cart
               SET quantity = ?
               WHERE user_id = ? AND product_id = ?
               """;


       try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
       {
           statement.setInt(1, quantity);
           statement.setInt(2, userId);
           statement.setInt(3, productId);
           statement.executeUpdate();
       }
       catch (SQLException e)
       {
           throw new RuntimeException("Error updating cart quantity", e);

       }
    }

    @Override
    public void clearCart(int userId) {

        String sql = """
                DELETE FROM 
                shopping_cart
                WHERE 
                user_id = ?
                """;

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
}
