package bookstore.dao;

import bookstore.domain.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    /**
     * 添加book
     * @param book
     * @return null 代表成功
     */
    String addBook(Book book);

    /**
     * 查询总记录条数
     * @return
     */
    int getTotalRecordsNum() throws SQLException;

    /**
     * 查询条数
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<Book> findPageBooks(int startIndex, int pageSize);

    /**
     * 根据id查询book的数据
     * @param bookId
     * @return
     */
    Book findBookById(String bookId) throws SQLException;

    /**
     * 更新书籍信息
     * @param book
     * @return
     */
    String updateBookByAdmin(Book book) throws SQLException;

    /**
     * 根据id删除书籍
     * @param deleteBookId
     * @return
     */
    String deleteBookById(String deleteBookId);

    /**
     * 根据书籍的id查询总记录数量
     * @param categoryId
     * @return
     */
    int getTotalRecordsNum(String categoryId);

    /**
     * 分类查询书籍
     * @param startIndex
     * @param pageSize
     * @param categoryId
     * @return
     */
    List<Book> findPageBooks(int startIndex, int pageSize, String categoryId);
}
