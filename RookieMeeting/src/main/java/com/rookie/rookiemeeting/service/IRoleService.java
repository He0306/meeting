package com.rookie.rookiemeeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.rookiemeeting.entity.Role;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 何超
 * @since 2022-08-22
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    Object getRoleMenu(Integer roleId);

}
