package com.cheems.blog.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheems.blog.common.constant.ThumbConstant;
import com.cheems.blog.entity.Thumb;
import com.cheems.blog.entity.User;
import com.cheems.blog.mapper.ThumbMapper;
import com.cheems.blog.service.ThumbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cheems
 * @description 针对表【thumb】的数据库操作Service实现
 * @createDate 2026-08-23 18:36:41
 */
@RequiredArgsConstructor
@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb>
        implements ThumbService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean isDoThumb(Long blogId, User loginUser) {
        //todo redis 命令 + 为什么这样构造key ，为什么使用hash
        Boolean exists = redisTemplate.opsForHash().hasKey(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogId.toString());

        if (!exists) {
            Thumb thumb = this.lambdaQuery()
                    .eq(Thumb::getBlogId, blogId)
                    .eq(Thumb::getUserId, loginUser.getId())
                    .one();
            if (thumb != null) {
                exists = true;
                redisTemplate.opsForHash().put(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogId.toString(), thumb.getId());
                redisTemplate.expire(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), Duration.ofDays(30));
            }
        }
        return exists;
    }

    @Override
    public Map<Long, Boolean> isDoThumbMap(List<Long> blogIds, User loginUser) {
        if (blogIds == null || blogIds.size() == 0) {
            return Map.of();
        }
        if (loginUser == null) {
            return Map.of();
        }
        //从redis 中查询 ， 查不到的条目，到数据库中查询
        // todo
        List<Object> blogStringlist = blogIds.stream().map(String::valueOf).collect(Collectors.toList());
        HashMap<Long, Boolean> isDoThumbMap = new HashMap<>();
        List<Object> thumbList = redisTemplate.opsForHash().multiGet(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogStringlist);

        List<Long> missIdList = new ArrayList<>();
        if(thumbList==null ||  thumbList.size()==0){
            missIdList.addAll(blogIds);
        }else {
            for (int i = 0; i < blogIds.size(); i++) {
                if (thumbList.get(i) == null) {
                    missIdList.add(blogIds.get(i));
                    continue;
                }
                isDoThumbMap.put(blogIds.get(i), true);
            }
        }
        if(!missIdList.isEmpty()){
            List<Thumb> misscacheList = this.lambdaQuery()
                    .in(Thumb::getBlogId, missIdList)
                    .eq(Thumb::getUserId, loginUser.getId())
                    .list();
            misscacheList.forEach(misscache -> {
                if (misscache != null) {
                    redisTemplate.opsForHash().put(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), misscache.getBlogId().toString(), misscache.getId());
                    redisTemplate.expire(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), Duration.ofDays(30));
                    isDoThumbMap.put(misscache.getBlogId(), true);
                }
            });
        }

        return isDoThumbMap;
    }
}




