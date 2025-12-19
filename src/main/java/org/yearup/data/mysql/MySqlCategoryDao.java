package org.yearup.data.mysql;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }
    // get all categories
    //hold categories from database
    @Override
    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        //query to pull categories

        String sql = "select category_id, name, description from categories";

        //open database connection
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet row = statement.executeQuery()) {
            //loop through row returned by query
            while (row.next()) {
                Category category = mapRow(row);

                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all categories", e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "Select category_id, name, description from categories where category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            try (ResultSet row = statement.executeQuery()) {
                if (!row.next())
                {
                    return null;
                }
                return mapRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting category by id", e);
        }
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql = "Insert into categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys())
            {
                if (keys.next())
                {
                    category.setCategoryId(keys.getInt(1));
                }
            }
            return category;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error creating category", e);
        }
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        // update category

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.setInt(3, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error update category", e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String sql = "Delete From categories where category_id = ?";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        Category category = new Category();
        category.setCategoryId(row.getInt("category_id"));
        category.setName(row.getString("name"));
        category.setDescription(row.getString("description"));
        return category;
    }

}
