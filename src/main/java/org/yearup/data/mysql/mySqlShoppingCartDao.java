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
        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM shopping_cart
                    JOIN products ON products.product_id = shoppingCart.product_id
                    WHERE user_id = ?
                    """);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShoppingCartItem item = new ShoppingCartItem();
                Product product = new Product();

                resultSet.getInt("shopping_cart_product_id");
                resultSet.getString("name");
                resultSet.getBigDecimal("price");
                resultSet.getInt("category_id");
                resultSet.getString("description");
                resultSet.getString("color");
                resultSet.getString("image_url");
                resultSet.getInt("stock");
                int uId = resultSet.getInt("user_id");
                int id = resultSet.getInt("id");
                int quantity = resultSet.getInt("quantity");

                item.setProduct(product);
                item.setQuantity(resultSet.getInt(quantity));

                shoppingCart.add(item);

            }
            return new ShoppingCart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ShoppingCart addItemsToCart(int userId, Product product) {
        ShoppingCart shoppingCart = getByUserId(userId);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO shopping_cart (user_id, product)
                    VALUE (?, ?);
                    """, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, userId);
            statement.setInt(2, product.getProductId());
            ResultSet resultSet = statement.executeQuery();

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            shoppingCart.add(item);

            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCart(int userId, ShoppingCartItem item, int productId) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ? AND product_id =?
                    """);
            statement.setInt(1, item.getQuantity());
            statement.setInt(1, userId);
            statement.setInt(1, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteCart(int userId) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM shopping_cart
                    WHERE user_id = ?
                    """);
            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
