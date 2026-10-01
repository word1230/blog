package com.cheems.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import com.cheems.blog.mapper.BlogMapper;
import com.cheems.blog.mapper.ThumbMapper;
import com.cheems.blog.service.ThumbService;
import com.cheems.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheems
 * @description 针对表【thumb】的数据库操作Service实现
 * @createDate 2026-08-23 18:36:41
 */
@RequiredArgsConstructor
@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb>
        implements ThumbService {

    @Override
    public Boolean isDoThumb(Long blogId, User loginUser) {
        LambdaQueryWrapper<Thumb> thumbLambdaQueryWrapper = new LambdaQueryWrapper<>();
        thumbLambdaQueryWrapper.eq(Thumb::getBlogId, blogId);
        thumbLambdaQueryWrapper.eq(Thumb::getUserId, loginUser.getId());
        return this.exists(thumbLambdaQueryWrapper);
    }

    @Override
    public Map<Long, Boolean> isDoThumbMap(List<Long> blogIds, User loginUser) {
        if(blogIds == null || blogIds.size() == 0){
            return Map.of();
        }
        if(loginUser == null){
            return Map.of();
        }
        List<Thumb> thumbList = this.lambdaQuery()
                .in(Thumb::getBlogId, blogIds)
                .eq(Thumb::getUserId, loginUser.getId())
                .list();
        HashMap<Long, Boolean> isDoThumbMap = new HashMap<>();

        thumbList.forEach(thumb -> {
            isDoThumbMap.put(thumb.getBlogId(),  true);
        });

        return isDoThumbMap;
    }
}




