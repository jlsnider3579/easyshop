package org.yearup.data.mysql;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    //Change I made here was declaring the datasource, so I can use it to make a connection
    //private DataSource dataSource;

    //Constructor
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    //Method to retrieve all categories
    public List<Category> getAllCategories() {
        ArrayList<Category> findCategories = new ArrayList<>();



        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM categories;
                    """);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Category c = mapRow(resultSet);
                findCategories.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findCategories;
    }

    @Override
    public Category getById(int categoryId) {



        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM categories WHERE category_id = ?;
                    """);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
              int id = resultSet.getInt("category_id");
              String name = resultSet.getString("name");
              String description = resultSet.getString("description");
              return new Category(id, name, description);


            }

            // get category by id
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category create(Category category) {

        ArrayList<Category> createCategory = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO categories(name, description) VALUE (?, ?);
                    """, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            resultSet.next();
            int rows = resultSet.getInt(1);


            // create a new category
            return new Category(rows, category.getName(), category.getDescription());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // create a new category
    }

    @Override
    public void update(int categoryId, Category category) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE categories
                    SET category_id = ?, name = ?, description = ?
                    WHERE category_id = ?
                    """);
            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());
            statement.setInt(4, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // update category
    }

    @Override
    public void delete(int categoryId) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM categories
                    WHERE category_id = ?;
                    """);
            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // delete category
        //DONE
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {
            {
                setCategoryId(categoryId);
                setName(name);
                setDescription(description);
            }
        };

        return category;
    }

}
