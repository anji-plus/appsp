package com.anji.sp.service.impl;

import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.mapper.SpUserAppRoleMapper;
import com.anji.sp.mapper.UserMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpUserAppRolePO;
import com.anji.sp.model.vo.SpAppInfoVO;
import com.anji.sp.model.vo.SpMenuVO;
import com.anji.sp.model.vo.SpUserAppInfoVO;
import com.anji.sp.model.vo.SpUserMenuVO;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.SpMenuService;
import com.anji.sp.service.SpUserAppRoleService;
import com.anji.sp.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/30  10:15 下午
 * @description:
 */
@Service
@Slf4j
public class SpUserAppRoleServiceImpl implements SpUserAppRoleService {

    @Autowired
    SpUserAppRoleMapper spUserAppRoleMapper;

    @Autowired
    UserMapper userMapper;


    @Autowired
    SpMenuService spMenuService;


    /**
     * 插入关联关系
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel insert(SpUserAppRolePO reqData) {
        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        if (Objects.isNull(reqData.getUserId())) {
            return ResponseModel.errorMsg("用户id不能为空");
        }
        if (Objects.isNull(reqData.getRoleId())) {
            return ResponseModel.errorMsg("角色id不能为空");
        }
        QueryWrapper<SpUserAppRolePO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("user_id", reqData.getUserId());
        objectQueryWrapper.eq("app_id", reqData.getAppId());
        Integer integer = spUserAppRoleMapper.selectCount(objectQueryWrapper);
        if (integer>0){
            return ResponseModel.errorMsg("添加失败，成员已存在");
        }
        try {
            spUserAppRoleMapper.insert(reqData);
        } catch (Exception e) {
            return ResponseModel.errorMsg("新增用户项目角色关系失败");
        }

        return ResponseModel.success();
    }

    /**
     * 删除用户项目关联表数据
     * 删除用户项目角色关联表数据
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel delete(SpUserAppRolePO reqData) {
        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        if (Objects.isNull(reqData.getUserId())) {
            return ResponseModel.errorMsg("用户id不能为空");
        }

        try {
            QueryWrapper<SpUserAppRolePO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("user_id", reqData.getUserId());
            objectQueryWrapper.eq("app_id", reqData.getAppId());
            spUserAppRoleMapper.delete(objectQueryWrapper);
        } catch (Exception e) {
            log.info("resError: {}", e.getMessage());
            return ResponseModel.errorMsg("删除用户项目角色关系失败");
        }

        return ResponseModel.success();

    }

    /**
     * 更改项目用户角色
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel update(SpUserAppRolePO reqData) {
        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        if (Objects.isNull(reqData.getRoleId())) {
            return ResponseModel.errorMsg("角色id不能为空");
        }
        if (Objects.isNull(reqData.getUserId())) {
            return ResponseModel.errorMsg("用户id不能为空");
        }
        try {
            QueryWrapper<SpUserAppRolePO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("user_id", reqData.getUserId());
            objectQueryWrapper.eq("app_id", reqData.getAppId());
            SpUserAppRolePO spUserAppRolePO = spUserAppRoleMapper.selectOne(objectQueryWrapper);
            spUserAppRolePO.setRoleId(reqData.getRoleId());
            spUserAppRoleMapper.updateById(spUserAppRolePO);
        } catch (Exception e) {
            log.info("resError: {}", e.getMessage());
            return ResponseModel.errorMsg("更改项目用户角色失败");
        }
        return ResponseModel.success();
    }


    /**
     * 分页查询关联关系用户信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel selectUserAppRoleByAppId(SpUserAppInfoVO reqData) {
        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        ResponseModel responseModel = new ResponseModel();
        try {
            PageQueryRep<SpUserAppInfoVO> pageQueryRep = new PageQueryRep<>();
            PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
            List<SpUserAppInfoVO> spUserAppInfoVOS = userMapper.selectUserAppRoleByAppId(reqData.getAppId());
            PageInfo<SpUserAppInfoVO> pageInfo = new PageInfo<>(spUserAppInfoVOS);
            pageQueryRep.setTotal(pageInfo.getTotal());
            pageQueryRep.setRows(pageInfo.getList());
            responseModel.setRepCodeEnum(RepCodeEnum.SUCCESS);
            responseModel.setRepData(pageQueryRep);
        } catch (Exception e) {
            log.info("reqError: {}", e.getMessage());
            return ResponseModel.errorMsg("查询关联关系用户信息失败");
        }
        return responseModel;
    }

    /**
     * -- 根据用户ID查询项目信息
     *
     * @return
     */
    @Override
    public ResponseModel selectAppInfoByUserId() {
        ResponseModel responseModel = new ResponseModel();

        try {
            SpUserAppInfoVO reqData = new SpUserAppInfoVO();
            List<SpAppInfoVO> spAppInfoVOS;
            if (!SecurityUtils.isAdmin()) {
                reqData.setUserId(SecurityUtils.getUserId());
                spAppInfoVOS = spUserAppRoleMapper.selectAppInfoByUserId(reqData);
            } else {
                spAppInfoVOS = spUserAppRoleMapper.selectAppInfoByAdmin(reqData);
            }
            responseModel.setRepCodeEnum(RepCodeEnum.SUCCESS);
            responseModel.setRepData(spAppInfoVOS);
        } catch (Exception e) {
            responseModel.setRepCodeEnum(RepCodeEnum.ERROR);
            responseModel.setRepMsg("查询项目信息失败");
            log.info("resError: {}", e.getMessage());
        }

        return responseModel;
    }

    /**
     * 根据 appId 和 userId 查询菜单信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel selectMenuPermissionsByAppIDAndUserId(SpUserAppInfoVO reqData) {

        if (SecurityUtils.isAdmin()) {
            return spMenuService.selectMenuList();
        }

        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("应用id不能为空");
        }

        ResponseModel responseModel = new ResponseModel();
        try {
            reqData.setUserId(SecurityUtils.getUserId());
            List<SpMenuVO> spMenuVOS = spUserAppRoleMapper.selectMenuPermissionsByAppIDAndUserId(reqData);
            responseModel.setRepCodeEnum(RepCodeEnum.SUCCESS);
            responseModel.setRepData(spMenuVOS);
        } catch (Exception e) {
            responseModel.setRepCodeEnum(RepCodeEnum.ERROR);
            responseModel.setRepMsg("查询失败");
            log.info("resError: {}", e.getMessage());
        }
        return responseModel;
    }

    /**
     * 查询用户所有菜单权限
     *
     * @return
     */
    @Override

    public Map<Long, Set<String>> selectUserMenuPerms(SpUserVO spUserVO) {
        if (spUserVO.getIsAdmin() == 1L) {
            return null;
        }
        SpUserMenuVO spUserMenuVO = new SpUserMenuVO();
        spUserMenuVO.setUserId(spUserVO.getUserId());
        List<SpUserMenuVO> spUserMenuVOS = spUserAppRoleMapper.selectUserMenuPerms(spUserMenuVO);

        Map<Long, Set<String>> collect = spUserMenuVOS.stream()
                .collect(Collectors.groupingBy(SpUserMenuVO::getAppId,
                        Collectors.mapping(SpUserMenuVO::getPerms, Collectors.toSet())));
        return collect;
    }


}
