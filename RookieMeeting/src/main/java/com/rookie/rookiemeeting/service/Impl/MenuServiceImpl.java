package com.rookie.rookiemeeting.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.rookiemeeting.entity.Menu;
import com.rookie.rookiemeeting.mapper.MenuMapper;
import com.rookie.rookiemeeting.mapper.RoleMapper;
import com.rookie.rookiemeeting.mapper.RoleMenuMapper;
import com.rookie.rookiemeeting.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 何超
 * @since 2022-08-22
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    IMenuService menuService;

    @Override
    public List<Menu> getRoleMenu(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出所有菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_num");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        // 查询所有数据
        List<Menu> list = list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            // 筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
