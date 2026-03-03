package com.company.scaffold.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.scaffold.module.system.entity.SysPost;
import com.company.scaffold.module.system.vo.PostVO;

public interface ISysPostService extends IService<SysPost> {
    IPage<PostVO> pagePost(Long current, Long size, String name);
    PostVO getPostById(Long id);
    boolean createPost(SysPost post);
    boolean updatePost(SysPost post);
    boolean deletePost(Long id);
}
