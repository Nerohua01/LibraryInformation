package com.libraryinformation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libraryinformation.dao.BookDao;
import com.libraryinformation.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书控制器
 *
 * @author Nerohua
 */
@RestController
@Transactional
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookDao zh3317_bookDao;

    /**
     * 保存图书
     *
     * @param book
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public Result save(@RequestBody Book book) {
        int zh3317_flag = zh3317_bookDao.insert(book);
        return new Result(zh3317_flag == 1 ? Code.zh3317_SAVE_OK : Code.zh3317_SAVE_ERR, zh3317_flag);
    }

    /**
     * 更新图书
     *
     * @param book
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public Result update(@RequestBody Book book) {
        int zh3317_flag = zh3317_bookDao.updateById(book);
        return new Result(zh3317_flag == 1 ? Code.zh3317_UPDATE_OK : Code.zh3317_UPDATE_ERR, zh3317_flag);
    }

    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public Result delete(@PathVariable String id) {
        int zh3317_flag = zh3317_bookDao.deleteById(id);
        return new Result(zh3317_flag == 1 ? Code.zh3317_DELETE_OK : Code.zh3317_DELETE_ERR, zh3317_flag);
    }

    /**
     * 根据id查询图书
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public Result getById(@PathVariable String id) {
        Book zh3317_book = zh3317_bookDao.selectById(id);
        Integer zh3317_code = zh3317_book != null ? Code.zh3317_GET_OK : Code.zh3317_GET_ERR;
        String zh3317_msg = zh3317_book != null ? "" : "数据查询失败，请重试！";
        return new Result(zh3317_code, zh3317_book, zh3317_msg);
    }

    /**
     * 根据条件查询图书，并分页
     *
     * @param page
     * @param size
     * @param bname
     * @param bauthor
     * @param bstatus
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public IPage<Book> getByPage(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 @RequestParam(required = false) String bid,
                                 @RequestParam(required = false) String bname,
                                 @RequestParam(required = false) String bauthor,
                                 @RequestParam(required = false) String bstatus
    ) {
        IPage<Book> userPage = new Page<>(page, size);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (bid != null && !"".equals(bid)) {
            wrapper.like("bid", bid);
        }
        if (bname != null && !"".equals(bname)) {
            wrapper.like("bname", bname);
        }
        if (bauthor != null && !"".equals(bauthor)) {
            wrapper.like("bauthor", bauthor);
        }
        if (bstatus != null && !"".equals(bstatus)) {
            wrapper.like("bstatus", bstatus);
        }
        zh3317_bookDao.selectPage(userPage, wrapper);
        return userPage;
    }

    /**
     * 查询所有图书
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public Result getAll() {
        List<Book> bookList = zh3317_bookDao.getAll();
        Integer zh3317_code = bookList != null ? Code.zh3317_GET_OK : Code.zh3317_GET_ERR;
        String zh3317_msg = bookList != null ? "" : "数据查询失败，请重试！";
        return new Result(zh3317_code, bookList, zh3317_msg);
    }

    /**
     * 批量删除图书
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/ids")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteByIds(@RequestBody List<String> ids) {
        int zh3317_flag = zh3317_bookDao.deleteBatchIds(ids);
        return new Result(zh3317_flag == ids.size() ? Code.zh3317_DELETE_OK : Code.zh3317_DELETE_ERR, zh3317_flag);
    }
}
