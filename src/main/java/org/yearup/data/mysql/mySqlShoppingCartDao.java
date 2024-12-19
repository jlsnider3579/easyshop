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
public class mySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {


    public mySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        try (Connection connection = getConnection() ){
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM shopping_cart
                    WHERE user_id = ?
                    """);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int uId = resultSet.getInt("user_id");
                int id = resultSet.getInt("id");
                int quantity = resultSet.getInt("quantity");
            }

            return new ShoppingCart();




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //return null;
    }

    @Override
    public ShoppingCart addItemsToCart(int userId, Product product) {
        return null;
    }

    @Override
    public void updateCart(int userId, ShoppingCartItem item, int productId) {

    }

    @Override
    public void deleteCart(int userId) {

    }
}
