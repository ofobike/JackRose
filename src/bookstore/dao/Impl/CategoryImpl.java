package bookstore.dao.Impl;

import bookstore.dao.CategoryDao;
import bookstore.domain.Category;
import bookstore.utils.JDCBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class CategoryImpl implements CategoryDao {
    private QueryRunner queryRunner = new QueryRunner(JDCBUtils.getDataSource());

    /**
     * g根据分类查询是否存在
     *
     * @param name
     * @return
     * @throws SQLException
     */
    @Override
    public Category findByName(String name) throws SQLException {
        String sql = "SELECT * FROM categorys WHERE NAME = ?";
        Category query = queryRunner.query(sql, new BeanHandler<Category>(Category.class), name);
        return query;
    }

    @Override
    public String addCategory(Category category) {
        String sql = "insert into categorys values(?,?,?)";
        try {
            int update = queryRunner.update(sql, category.getId(), category.getName(), category.getDes());
            if (update > 0) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "添加失败";
    }

    /**
     * 查询所有的分类
     *
     * @return
     */
    @Override
    public List<Category> findAllResult() throws SQLException {
        String sql = "select * from categorys";
        return queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
    }

    /**
     * 根据Id删除
     *
     * @param cid
     * @return
     */
    @Override
    public String deleteCategoryById(String cid) {
        String sql = "DELETE FROM categorys WHERE id = ?";
        try {
            int update = queryRunner.update(sql, cid);
            if (update > 0) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "删除失败";
    }

    @Override
    public Category findCategoryById(String cid) throws SQLException {
        String sql = "select * from categorys where id =?";
        Category category = queryRunner.query(sql, new BeanHandler<Category>(Category.class), cid);
        return category;
    }

    @Override
    public String updateCategory(Category category) {
        try {
            String sql = "UPDATE categorys SET des=? WHERE id = ?";
            int update = queryRunner.update(sql, category.getDes(), category.getId());
            if (update>0){
                return  null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "更新失败";
    }
}
