package org.yearup.data.mysql;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource){
        super(dataSource);
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
                       SELECT
                            sc.product_id,
                            sc.quantity,
                            p.name,
                            p.price,
                            p.category_id,
                            p.description,
                            p.subcategory,
                            p.image_url,
                            p.stock,
                            p.featured
                        FROM shopping_cart sc
                        JOIN products p ON sc.product_id = p.product_id
                        WHERE sc.user_id = ?
                
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();
            while (row.next()) {

                Product product = new Product(
                        row.getInt("product_id"),
                        row.getString("name"),
                        row.getBigDecimal("price"),
                        row.getInt("category_id"),
                        row.getString("description"),
                        row.getString("subcategory"),
                        row.getInt("stock"),
                        row.getBoolean("featured"),
                        row.getString("image_url"));

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.getQuantity(row.getInt("quantity"));

                cart.add(item);
            }
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting shopping cart", e);
        }
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

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

    }



}
