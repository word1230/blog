package com.cheems.blog.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 统一分页结果包装，对齐 MyBatis-Plus 的 IPage
 *
 * @author cheems
 * @date 2026/08/23
 */
@Data
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long total;

    private long pageNum;

    private long pageSize;

    private List<T> records;

    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.total = page.getTotal();
        result.pageNum = page.getCurrent();
        result.pageSize = page.getSize();
        result.records = page.getRecords();
        return result;
    }

    public static <T> PageResult<T> empty(long pageNum, long pageSize) {
        PageResult<T> result = new PageResult<>();
        result.total = 0;
        result.pageNum = pageNum;
        result.pageSize = pageSize;
        result.records = Collections.emptyList();
        return result;
    }
}
