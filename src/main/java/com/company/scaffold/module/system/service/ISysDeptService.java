package com.company.scaffold.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.scaffold.module.system.entity.SysDept;
import com.company.scaffold.module.system.vo.DeptVO;

public interface ISysDeptService extends IService<SysDept> {
    IPage<DeptVO> pageDept(Long current, Long size, String name);
    DeptVO getDeptById(Long id);
    boolean createDept(SysDept dept);
    boolean updateDept(SysDept dept);
    boolean deleteDept(Long id);
}
