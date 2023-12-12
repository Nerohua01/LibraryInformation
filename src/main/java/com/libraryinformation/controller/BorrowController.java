package com.libraryinformation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libraryinformation.dao.BookDao;
import com.libraryinformation.dao.BorrowDao;
import com.libraryinformation.domain.Borrow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借阅信息控制器
 *
 * @author Nerohua
 */
@RestController
@Transactional
@Slf4j
@RequestMapping("/borrows")
public class BorrowController {
    @Autowired
    private BorrowDao borrowDao;
    @Autowired
    private BookDao bookDao;

    /**
     * 借书
     *
     * @param borrow
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public Result borrowBook(@RequestBody Borrow borrow) {
        int zh3317_flag = 0;
        try {
            if (borrowDao.borrowBook(borrow) == 1 && bookDao.updateBstatusT(borrow.getBid()) == 1)
                zh3317_flag = 1;
        } catch (Exception e) {
            log.error("出现异常:{}", e.getMessage());
            throw new RuntimeException("借书失败，请重试！");
        }
        Integer zh3317_code = zh3317_flag == 1 ? Code.zh3317_SAVE_OK : Code.zh3317_SAVE_ERR;
        String zh3317_msg = zh3317_flag == 1 ? "借书成功" : "借书失败，请重试！";
        return new Result(zh3317_code, zh3317_flag, zh3317_msg);
    }

    /**
     * 还书
     *
     * @param borrow
     * @return
     */
    @PutMapping("/return")
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public Result returnBook(@RequestBody Borrow borrow) {
        int zh3317_flag = 0;
        try {
            if (borrowDao.returnBook(borrow) == 1 && bookDao.updateBstatusF(borrow.getBid()) == 1)
                zh3317_flag = 1;
        } catch (Exception e) {
            log.error("出现异常:{}", e.getMessage());
            throw new RuntimeException("还书失败，请重试！");
        }
        Integer zh3317_code = zh3317_flag == 1 ? Code.zh3317_UPDATE_OK : Code.zh3317_UPDATE_ERR;
        String zh3317_msg = zh3317_flag == 1 ? "还书成功" : "还书失败，请重试！";
        return new Result(zh3317_code, zh3317_flag, zh3317_msg);
    }

    /**
     * 更新借阅信息
     *
     * @param borrow
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public Result update(@RequestBody Borrow borrow) {
        int zh3317_flag = borrowDao.updateById(borrow);
        return new Result(zh3317_flag == 1 ? Code.zh3317_UPDATE_OK : Code.zh3317_UPDATE_ERR, zh3317_flag);
    }

    /**
     * 删除借阅信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public Result delete(@PathVariable String id) {
        int zh3317_flag = borrowDao.deleteById(id);
        return new Result(zh3317_flag == 1 ? Code.zh3317_DELETE_OK : Code.zh3317_DELETE_ERR, zh3317_flag);
    }

    /**
     * 根据id查询借阅信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public Result getById(@PathVariable String id) {
        Borrow zh3317_borrow = borrowDao.selectById(id);
        Integer zh3317_code = zh3317_borrow != null ? Code.zh3317_GET_OK : Code.zh3317_GET_ERR;
        String zh3317_msg = zh3317_borrow != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(zh3317_code, null, zh3317_msg);
    }


    /**
     * 条件查询所有借阅记录，并分页
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin')")
    public IPage<Borrow> getByPage(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String id,
                                   @RequestParam(required = false) String uid,
                                   @RequestParam(required = false) String bid
    ) {
        IPage<Borrow> borrowPage = new Page<>(page, size);
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        if (id != null && !"".equals(id)) {
            queryWrapper.like("id", id);
        }
        if (uid != null && !"".equals(uid)) {
            queryWrapper.like("uid", uid);
        }
        if (bid != null && !"".equals(bid)) {
            queryWrapper.like("bid", bid);
        }
        borrowDao.selectPage(borrowPage, queryWrapper);
        return borrowPage;
    }

    /**
     * 根据id查询借阅信息,并分页
     *
     * @param page
     * @param size
     * @param id
     * @param uid
     * @param bid
     * @return
     */
    @GetMapping("/page/{uid}")
    @PreAuthorize("hasAnyAuthority('admin','user')")
    public IPage<Borrow> getByPageById(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String id,
                                       @PathVariable(required = false) String uid,
                                       @RequestParam(required = false) String bid
    ) {
        IPage<Borrow> borrowPage = new Page<>(page, size);
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        if (id != null && !"".equals(id)) {
            queryWrapper.like("id", id);
        }
        if (uid != null && !"".equals(uid)) {
            queryWrapper.eq("uid", uid);
        }
        if (bid != null && !"".equals(bid)) {
            queryWrapper.like("bid", bid);
        }
        borrowDao.selectPage(borrowPage, queryWrapper);
        return borrowPage;
    }

    /**
     * 根据id批量删除借阅信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/ids")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteByIds(@RequestBody List<String> ids) {
        int zh3317_flag = borrowDao.deleteBatchIds(ids);
        return new Result(zh3317_flag == 1 ? Code.zh3317_DELETE_OK : Code.zh3317_DELETE_ERR, zh3317_flag);
    }
}
