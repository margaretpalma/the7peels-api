package org.yearup.data.mysql;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
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

    @Override
    public ShoppingCart geByUserId(int userId) {
        return null;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }

//    //get the cart for the user, use database
//
//    @Override
//    public ShoppingCart getByUserId(int userId) {
//        ShoppingCart cart = new ShoppingCart();
//
//        //sql query fpr shopping cart update
//        String sql = """
//                      SELECT
//                                sc.product_id,
//                                sc.quantity,
//                                p.product_id,
//                                p.name,
//                                p.price,
//                                p.category_id,
//                                p.description,
//                                p.subcategory,
//                                p.image_url,
//                                p.stock,
//                                p.featured
//                            FROM shopping_cart sc
//                            JOIN products p ON sc.product_id = p.product_id
//                            WHERE sc.user_id = ?
//
//                """;
//
//        //connection to databse
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, userId);
//            ResultSet row = statement.executeQuery();
//            while (row.next()) {
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Error updating cart quantity", e);
//        }
//
//        return null;
//
//    }

}
