package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component  // Marks this class as a Spring component, which will be automatically detected by the Spring container for dependency injection
public class mySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    // Constructor that accepts a DataSource and passes it to the superclass (MySqlDaoBase)
    public mySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    // Retrieves the shopping cart for a given user based on userId
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart(); // Create a new ShoppingCart object

        // Get a database connection
        // Prepare SQL query to select products and their associated details for the given userId
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM shopping_cart
                    JOIN products ON products.product_id = shopping_cart.product_id
                    WHERE user_id = ?
                    """);
            statement.setInt(1, userId); // Set the userId parameter in the query
            ResultSet resultSet = statement.executeQuery(); // Execute the query and get results

            while (resultSet.next()) {
                ShoppingCartItem item = new ShoppingCartItem();

                // Extract product details from the ResultSet
                int shoppingCartProductId = resultSet.getInt("shopping_cart.product_id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                int categoryId = resultSet.getInt("category_id");
                String description = resultSet.getString("description");
                String color = resultSet.getString("color");
                Boolean featured = resultSet.getBoolean("featured");
                String imageUrl = resultSet.getString("image_url");
                int stock = resultSet.getInt("stock");

                // Create a Product object using the extracted details
                Product product = new Product(shoppingCartProductId, name, price, categoryId, description, color, stock, featured, imageUrl);

                // Get the quantity of the product in the user's cart
                int quantity = resultSet.getInt("quantity");

                // Set the product and quantity on the ShoppingCartItem
                item.setProduct(product);
                item.setQuantity(resultSet.getInt(quantity));

                // Add the item to the shopping cart
                shoppingCart.add(item);

            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    // Adds an item (product) to the shopping cart for a given user
    public ShoppingCart addItemsToCart(int userId, Product product) {
        ShoppingCart shoppingCart = getByUserId(userId); // Retrieve the user's existing shopping cart

        // Get a database connection
        // Prepare SQL query to insert a new item into the shopping cart
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO shopping_cart (user_id, product_id)
                    VALUE (?, ?);
                    """, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, userId); // Set the userId parameter
            statement.setInt(2, product.getProductId());  // Set the productId parameter
            statement.executeUpdate();  // Execute the insert query

            // Create a new ShoppingCartItem and add the product to the shopping cart
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            shoppingCart.add(item);   // Add the item to the cart


            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // Method to update the quantity of a specific product in the user's shopping cart
    public void updateCart(int userId, ShoppingCartItem item, int productId) {

        // Get a database connection
        // SQL query to update the quantity of a product in the shopping_cart table for a specific user and product.
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ? AND product_id =?
                    """);

            // Set the quantity parameter from the ShoppingCartItem object.
            statement.setInt(1, item.getQuantity());

            statement.setInt(1, userId);
            statement.setInt(1, productId);
            statement.executeUpdate();   // Execute the update statement to apply the change to the database.

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    // Method to delete all items from a user's shopping cart.
    public void deleteCart(int userId) {

        // Get a database connection
        // SQL query to delete all rows in the shopping_cart table that match the userId.
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM shopping_cart
                    WHERE user_id = ?
                    """);
            // Set the userId parameter to identify the user whose cart will be deleted.
            statement.setInt(1, userId);
            statement.executeUpdate(); // Execute the delete statement to remove all items from the cart.


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
