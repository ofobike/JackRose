package bookstore.dao.Impl;

import bookstore.dao.BookDao;
import bookstore.domain.Book;
import bookstore.domain.Category;
import bookstore.utils.JDCBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;


public class BookDaoImpl implements BookDao {
    QueryRunner queryRunner = new QueryRunner(JDCBUtils.getDataSource());

    @Override
    public String addBook(Book book) {
        try {
            int update = queryRunner.update("insert into books(id,name,author,price,path,filename,des,categoryId) values (?,?,?,?,?,?,?,?)",
                    book.getId(), book.getName(), book.getAuthor(), book.getPrice(), book.getPath(),
                    book.getFilename(), book.getDes(), book.getCategory() == null ? null : book.getCategory().getId());
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "添加图书失败";
    }

    /**
     * 查询总的记录条数
     *
     * @return
     */
    @Override
    public int getTotalRecordsNum() throws SQLException {
        String sql = "select count(id) from categorys";
        Long query = (Long) queryRunner.query(sql, new ScalarHandler(1));
        return query.intValue();
    }

    /**
     * 分业查询
     *
     * @param startIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<Book> findPageBooks(int startIndex, int pageSize) {
        try {
            String sql = "select * from books limit ?,?";
            List<Book> books = queryRunner.query(sql, new BeanListHandler<Book>(Book.class), startIndex, pageSize);
            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    //查询除分类的信息
                    sql = "select * from categorys where id=(select categoryId from books where id=?)";
                    Category category = queryRunner.query(sql, new BeanHandler<Category>(Category.class), books.get(i).getId());
                    //获取book
                    books.get(i).setCategory(category);
                }
            }
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book findBookById(String bookId) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = queryRunner.query(sql, new BeanHandler<Book>(Book.class), bookId);
        return book;
    }

    @Override
    public String updateBookByAdmin(Book book) {
        try {
            String sql = "UPDATE books SET NAME =?,author = ?,price = ?,path=?,filename=?,des=?,categoryId=? WHERE id =?";
            Object[] params = {book.getName(), book.getAuthor(), book.getPrice(), book.getPath(), book.getFilename(), book.getDes(), book.getCategory().getId(), book.getId()};
            queryRunner.update(sql, params);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "更新失败";
    }

    /**
     * 删除图书
     *
     * @param deleteBookId
     * @return
     */
    @Override
    public String deleteBookById(String deleteBookId) {
        try {
            String sql = "delete from books where id = ?";
            queryRunner.update(sql, deleteBookId);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "删除失败";
    }

    /**
     * 根据书籍id查询分类的书籍下面有几本书
     *
     * @param categoryId
     * @return
     */
    @Override
    public int getTotalRecordsNum(String categoryId) {
        try {
            String sql = "select count(id) from books where categoryId=?";
            Long num = (Long) queryRunner.query(sql, new ScalarHandler(1), categoryId);
            return num.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分页查询数据信息
     *
     * @param startIndex
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public List<Book> findPageBooks(int startIndex, int pageSize, String categoryId) {
        List<Book> list = null;
        String sql = "select * from books  where categoryId=?  limit  ?,?";
        try {
            list = queryRunner.query(sql, new BeanListHandler<Book>(Book.class), categoryId, startIndex, pageSize);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    sql = "select * from categorys where id=?";
                    Category Category = queryRunner.query(sql, new BeanHandler<Category>(Category.class),categoryId);
                    list.get(i).setCategory(Category);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
