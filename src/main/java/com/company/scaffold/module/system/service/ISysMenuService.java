package com.company.scaffold.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.scaffold.module.system.entity.SysMenu;
import com.company.scaffold.module.system.vo.MenuVO;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {
    IPage<MenuVO> pageMenu(Long current, Long size, String name);
    MenuVO getMenuById(Long id);
    boolean createMenu(SysMenu menu);
    boolean updateMenu(SysMenu menu);
    boolean deleteMenu(Long id);
}
