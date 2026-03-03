package com.company.scaffold.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.scaffold.common.core.exception.BusinessException;
import com.company.scaffold.module.system.entity.SysMenu;
import com.company.scaffold.module.system.mapper.SysMenuMapper;
import com.company.scaffold.module.system.service.ISysMenuService;
import com.company.scaffold.module.system.vo.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public IPage<MenuVO> pageMenu(Long current, Long size, String name) {
        Page<SysMenu> page = new Page<>(current, size);
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, SysMenu::getName, name).orderByAsc(SysMenu::getSort);
        IPage<SysMenu> menuPage = page(page, wrapper);
        return menuPage.convert(this::toVO);
    }

    @Override
    public MenuVO getMenuById(Long id) {
        SysMenu menu = getById(id);
        if (menu == null) throw new BusinessException("菜单不存在");
        return toVO(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(SysMenu menu) {
        if (menu.getParentId() == null) menu.setParentId(0L);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        return save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(SysMenu menu) {
        SysMenu existMenu = getById(menu.getId());
        if (existMenu == null) throw new BusinessException("菜单不存在");
        menu.setUpdateTime(LocalDateTime.now());
        return updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Long id) {
        long childCount = count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (childCount > 0) throw new BusinessException("该菜单下存在子菜单，无法删除");
        return removeById(id);
    }

    private MenuVO toVO(SysMenu menu) {
        return new MenuVO(menu.getId(), menu.getParentId(), menu.getName(), menu.getPath(), menu.getComponent(), menu.getIcon(), menu.getSort(), menu.getVisible(), menu.getPermission(), menu.getMenuType(), menu.getCreateTime(), null);
    }
}
