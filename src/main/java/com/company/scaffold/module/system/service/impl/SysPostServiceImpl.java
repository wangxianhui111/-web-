package com.company.scaffold.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.scaffold.common.core.exception.BusinessException;
import com.company.scaffold.module.system.entity.SysPost;
import com.company.scaffold.module.system.mapper.SysPostMapper;
import com.company.scaffold.module.system.service.ISysPostService;
import com.company.scaffold.module.system.vo.PostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Override
    public IPage<PostVO> pagePost(Long current, Long size, String name) {
        Page<SysPost> page = new Page<>(current, size);
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, SysPost::getName, name).orderByAsc(SysPost::getSort);
        IPage<SysPost> postPage = page(page, wrapper);
        return postPage.convert(this::toVO);
    }

    @Override
    public PostVO getPostById(Long id) {
        SysPost post = getById(id);
        if (post == null) throw new BusinessException("岗位不存在");
        return toVO(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPost(SysPost post) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPost::getCode, post.getCode());
        if (getOne(wrapper) != null) throw new BusinessException("岗位编码已存在");
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        return save(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePost(SysPost post) {
        SysPost existPost = getById(post.getId());
        if (existPost == null) throw new BusinessException("岗位不存在");
        post.setUpdateTime(LocalDateTime.now());
        return updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Long id) {
        SysPost post = getById(id);
        if (post == null) throw new BusinessException("岗位不存在");
        return removeById(id);
    }

    private PostVO toVO(SysPost post) {
        return new PostVO(post.getId(), post.getCode(), post.getName(), post.getDescription(), post.getSort(), post.getStatus(), post.getCreateTime());
    }
}
