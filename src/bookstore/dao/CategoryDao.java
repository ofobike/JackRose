package bookstore.dao;

import bookstore.domain.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    /**
     * 根据名字查询分类是否可以使用
     */
    Category findByName(String name) throws SQLException;

    /**
     * 添加书籍的分类
     * @return null代表成功
     */
    String addCategory(Category category);

    /**
     * 查询所有的分类
     * @return
     */
    List<Category> findAllResult() throws SQLException;

    /**
     * 删除制定的数据
     * @param cid
     * @return
     */
    String deleteCategoryById(String cid) throws SQLException;

    /**
     * 根据分类查询数据
     * @param cid
     * @return
     */
    Category findCategoryById(String cid) throws SQLException;

    /**
     * 修改分类
     * @param category
     * @return
     */
    String updateCategory(Category category);
}
