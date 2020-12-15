package com.anji.sp.service.impl;

import com.anji.sp.mapper.SpRoleMenuMapper;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpRoleMenuPO;
import com.anji.sp.model.vo.SpRoleMenuEditVO;
import com.anji.sp.model.vo.SpRoleMenuVO;
import com.anji.sp.service.SpRoleMenuService;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/27  10:44 上午
 * @description:
 */
@Service
public class SpRoleMenuServiceImpl implements SpRoleMenuService {


    @Autowired
    SpRoleMenuMapper spRoleMenuMapper;

    /**
     * 根据角色role_id 查询对应菜单
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel selectMenuListByRoleId(SpRoleMenuPO reqData) {

        if (reqData.getRoleId() == null) {
            return ResponseModel.errorMsg("角色Id不能为空");
        }
        List<SpRoleMenuVO> spMenuPOS = spRoleMenuMapper.selectMenuListByRoleId(reqData.getRoleId());
        return ResponseModel.successData(spMenuPOS);
    }

    /**
     * 更新角色菜单关联表信息
     *
     * @param spRoleMenuEditVO
     * @return
     */
    @Override
    public ResponseModel updateMenuToRole(SpRoleMenuEditVO spRoleMenuEditVO) {

        if (spRoleMenuEditVO == null) {
            return ResponseModel.errorMsg("参数不能为空");
        } else if (spRoleMenuEditVO.getRoleId() == null) {
            return ResponseModel.errorMsg("角色id不能为空");
        } else if (StringUtils.isEmpty(spRoleMenuEditVO.getMenuIds())) {
            return ResponseModel.errorMsg("列表参数不能为空");
        }

        //先删除RoleId对应的关联信息
        if (!deleteMenuByRoleId(spRoleMenuEditVO.getRoleId())) {
            return ResponseModel.errorMsg("删除失败");
        }
        int total = spRoleMenuEditVO.getMenuIds().size();
        int index = 0;
        //进行关联表插入
        for (Long menuId : spRoleMenuEditVO.getMenuIds()) {
            SpRoleMenuPO spRoleMenuPO = new SpRoleMenuPO();
            spRoleMenuPO.setRoleId(spRoleMenuEditVO.getRoleId());
            spRoleMenuPO.setMenuId(menuId);
            QueryWrapper objectQueryWrapper = new QueryWrapper();
            objectQueryWrapper.eq("role_id", spRoleMenuPO.getRoleId());
            objectQueryWrapper.eq("menu_id", spRoleMenuPO.getMenuId());
            SpRoleMenuPO findResult = spRoleMenuMapper.selectOne(objectQueryWrapper);
            if (findResult == null) { //不存在 插入
                int insert = spRoleMenuMapper.insert(spRoleMenuPO);
                if (insert > 0) {
                    index++;
                }
            } else { //存在 更新
                findResult.setRoleId(spRoleMenuPO.getRoleId());
                findResult.setMenuId(spRoleMenuPO.getMenuId());
                int update = spRoleMenuMapper.updateById(findResult);
                if (update > 0) {
                    index++;
                }
            }
        }
        if (total == index) {
            return ResponseModel.success();
        } else {
            return ResponseModel.errorMsg("更新失败");
        }
    }

    /**
     * 根据角色id删除菜单角色关联关系
     * @param spRoleMenuPO
     * @return
     */
    @Override
    public ResponseModel deleteMenuRoleByRoleId(SpRoleMenuPO spRoleMenuPO) {
        if (spRoleMenuPO.getRoleId() == null) {
            return ResponseModel.errorMsg("角色id不能为空");
        }
        if (deleteMenuByRoleId(spRoleMenuPO.getRoleId())) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("删除失败");
    }


    private boolean deleteMenuByRoleId(Long roleId) {
        List<SpRoleMenuVO> spMenuPOS = spRoleMenuMapper.selectMenuListByRoleId(roleId);
        //先删除角色菜单表对应数据
        if (StringUtils.isNotEmpty(spMenuPOS)) {
            List<Long> collects = spMenuPOS.stream().map(s -> {
                return s.getId();
            }).collect(Collectors.toList());

            int i = spRoleMenuMapper.deleteBatchIds(collects);
            int size = collects.size();
            if (i != size) {
                return false;
            }
        }
        return true;
    }
}
