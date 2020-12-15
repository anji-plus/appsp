package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpRoleMapper;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpRolePO;
import com.anji.sp.model.vo.SpRoleMenuEditVO;
import com.anji.sp.model.vo.SpRoleMenuInsertVO;
import com.anji.sp.model.vo.SpRoleVO;
import com.anji.sp.service.SpRoleMenuService;
import com.anji.sp.service.SpRoleService;
import com.anji.sp.util.SecurityUtils;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/25  10:10 下午
 * @description:
 */
@Service
public class SpRoleServiceImpl implements SpRoleService {
    @Autowired
    SpRoleMapper spRoleMapper;

    @Autowired
    private SpRoleMenuService spRoleMenuService;


    /**
     * 查询角色列表
     *
     * @return
     */
    @Override
    public ResponseModel selectRoleList() {
        QueryWrapper<SpRolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        queryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        List<SpRolePO> spRolePOS = spRoleMapper.selectList(queryWrapper);
        List<SpRoleVO> collects = spRolePOS.stream().map(s -> {
            SpRoleVO sp = new SpRoleVO();
            BeanUtils.copyProperties(s, sp);
            return sp;
        }).collect(Collectors.toList());
        return ResponseModel.successData(collects);
    }

    /**
     * 根据角色id进行删除角色  逻辑删除
     *
     * @param spRoleVO
     * @return
     */
    @Override
    public ResponseModel deleteRoleByRoleId(SpRoleVO spRoleVO) {
        QueryWrapper<SpRolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", spRoleVO.getRoleId());
        SpRolePO spRolePO = new SpRolePO();
        spRolePO.setRoleId(spRoleVO.getRoleId());
        spRolePO.setUpdateBy(SecurityUtils.getUserId());
        spRolePO.setUpdateDate(new Date());
        spRolePO.setDeleteFlag(IsDeleteEnum.DELETE.getIntegerCode());
        spRolePO.setEnableFlag(UserStatus.DISABLE.getIntegerCode());
        int update = spRoleMapper.update(spRolePO, queryWrapper);
        if (update > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("删除失败");
    }

    /**
     * 根据角色id更新角色
     *
     * @param spRoleVO
     * @return
     */
    @Override
    public ResponseModel updateRoleByRoleId(SpRoleVO spRoleVO) {
        if (Objects.isNull(spRoleVO.getRoleId())) {
            return ResponseModel.errorMsg("角色Id不存在");
        }

        QueryWrapper<SpRolePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", spRoleVO.getRoleId());
        SpRolePO spRolePO = new SpRolePO();
        spRolePO.setRoleId(spRoleVO.getRoleId());
        spRolePO.setUpdateBy(SecurityUtils.getUserId());
        spRolePO.setUpdateDate(new Date());
        spRolePO.setRoleName(spRoleVO.getRoleName());
        int update = spRoleMapper.update(spRolePO, queryWrapper);

        if (update > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("删除失败");
    }

    /**
     * 插入角色并给用户添加菜单
     *
     * @param spRoleMenuInsertVO
     * @return
     */
    @Override
    public ResponseModel insertRoleAndMenu(SpRoleMenuInsertVO spRoleMenuInsertVO) {
        //校验
        if (spRoleMenuInsertVO == null) {
            return ResponseModel.errorMsg("参数不能为空");
        } else if (StringUtils.isEmpty(spRoleMenuInsertVO.getRoleName())) {
            return ResponseModel.errorMsg("角色名不能为空");
        } else if (StringUtils.isEmpty(spRoleMenuInsertVO.getMenuIds())) {
            return ResponseModel.errorMsg("列表参数不能为空");
        }
        //查询角色名是否存在
        QueryWrapper objectQueryWrapper = new QueryWrapper();
        objectQueryWrapper.eq("role_name", spRoleMenuInsertVO.getRoleName());
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        SpRolePO findResult = spRoleMapper.selectOne(objectQueryWrapper);
        if (findResult != null) { //存在就更新
            SpRoleMenuEditVO spRoleMenuEditVO = new SpRoleMenuEditVO();
            spRoleMenuEditVO.setRoleId(findResult.getRoleId());
            spRoleMenuEditVO.setMenuIds(spRoleMenuInsertVO.getMenuIds());
            return spRoleMenuService.updateMenuToRole(spRoleMenuEditVO);
        }

        //新增角色
        SpRolePO spRolePO = new SpRolePO();
        spRolePO.setRoleName(spRoleMenuInsertVO.getRoleName());
        spRolePO.setRoleSign("1");
        spRolePO.setCreateBy(SecurityUtils.getUserId());
        spRolePO.setUpdateBy(SecurityUtils.getUserId());
        spRolePO.setEnableFlag(1);
        spRolePO.setDeleteFlag(0);
        spRolePO.setCreateDate(new Date());
        spRolePO.setUpdateDate(new Date());
        int insert = spRoleMapper.insert(spRolePO);
        if (insert == 0) {
            return ResponseModel.errorMsg("添加角色失败");
        }
        //添加菜单
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_name", spRoleMenuInsertVO.getRoleName());
        queryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        queryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        SpRolePO findResult1 = spRoleMapper.selectOne(objectQueryWrapper);
        if (findResult1 != null) { //存在就更新
            SpRoleMenuEditVO spRoleMenuEditVO = new SpRoleMenuEditVO();
            spRoleMenuEditVO.setRoleId(findResult1.getRoleId());
            spRoleMenuEditVO.setMenuIds(spRoleMenuInsertVO.getMenuIds());
            return spRoleMenuService.updateMenuToRole(spRoleMenuEditVO);
        }
        return ResponseModel.errorMsg("菜单角色失败");
    }
}
