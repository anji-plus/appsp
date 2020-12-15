package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpUserAppRoleMapper;
import com.anji.sp.mapper.UserMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpUserAppRolePO;
import com.anji.sp.model.po.SpUserPO;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.model.vo.SpUserVO;
import com.anji.sp.service.UserService;
import com.anji.sp.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by raodeming on 2020/6/22.
 */
@Service
public class UserServiceImpl implements UserService {
    //新建用户默认密码
    private static final String PASSWORD_KEY = "123456";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    SpUserAppRoleMapper spUserAppRoleMapper;

    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    @Override
    public SpUserVO selectUserByUserName(String username) {
        QueryWrapper<SpUserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
//        queryWrapper.eq("enable_flag", 1);
//        queryWrapper.eq("delete_flag", 0);
        SpUserPO spUserPO = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(spUserPO)) {
            return null;
        }
        SpUserVO spUserVO = new SpUserVO();
        BeanUtils.copyProperties(spUserPO, spUserVO);
        return spUserVO;
    }

    /**
     * 添加用户
     * @param spUserVO
     * @return
     */
    @Override
    public ResponseModel addUser(SpUserVO spUserVO) {
        QueryWrapper<SpUserPO> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("username", spUserVO.getUsername());
        SpUserPO spUserPO0 = userMapper.selectOne(queryWrapper0);
        //如果用户存在 且 已经被删除 重新更新
        if (Objects.nonNull(spUserPO0) && spUserPO0.getDeleteFlag() == IsDeleteEnum.DELETE.getIntegerCode()) { //不为空
            spUserPO0.setDeleteFlag(IsDeleteEnum.NOT_DELETE.getIntegerCode());
            spUserPO0.setEnableFlag(UserStatus.OK.getIntegerCode());
            spUserPO0.setName(spUserVO.getName());
            if (!updateUserPO(spUserPO0, queryWrapper0)) {
                return ResponseModel.errorMsg("新建失败");
            } else {
                return ResponseModel.successData(spUserPO0);
            }
        }
        QueryWrapper<SpUserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", spUserVO.getUsername());
        queryWrapper.eq("enable_flag", 1);
        queryWrapper.eq("delete_flag", 0);
        SpUserPO spUserPO1 = userMapper.selectOne(queryWrapper);
        if (!Objects.isNull(spUserPO1)) {
            return ResponseModel.errorMsg("用户已存在");
        }
        SpUserPO spUserPO = new SpUserPO();
        BeanUtils.copyProperties(spUserVO, spUserPO);
        //新建用户默认密码
        spUserPO.setPassword(SecurityUtils.encryptPassword(PASSWORD_KEY));
        spUserPO.setCreateBy(SecurityUtils.getUserId());
        spUserPO.setCreateDate(new Date());
        spUserPO.setEnableFlag(UserStatus.OK.getIntegerCode());
        spUserPO.setDeleteFlag(IsDeleteEnum.NOT_DELETE.getIntegerCode());
        spUserPO.setUpdateBy(SecurityUtils.getUserId());
        spUserPO.setUpdateDate(new Date());
        int insert = userMapper.insert(spUserPO);
        if (insert > 0) {
            return ResponseModel.successData(spUserPO);
        }
        return ResponseModel.errorMsg("新建失败");
    }

    /**
     * 分页查询用户信息
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel queryByPage(SpUserVO reqData) {
        PageQueryRep<SpUserVO> poPageQueryRep = new PageQueryRep<>();
        PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
        QueryWrapper<SpUserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", 1);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.eq("is_admin", 0);
        queryWrapper.like("username", reqData.getUsername());
        queryWrapper.like("name", reqData.getName());
        List<SpUserPO> spUserPOS = userMapper.selectList(queryWrapper);
        PageInfo<SpUserPO> pageInfo = new PageInfo<>(spUserPOS);
        List<SpUserVO> collect = spUserPOS.stream().map(source -> {
            SpUserVO spUserVO = new SpUserVO();
            BeanUtils.copyProperties(source, spUserVO);
            spUserVO.setPassword(null);
            return spUserVO;
        }).collect(Collectors.toList());
        poPageQueryRep.setTotal(pageInfo.getTotal());
        poPageQueryRep.setRows(collect);
        return ResponseModel.successData(poPageQueryRep);
    }

    /**
     * 删除用户 逻辑删除
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel deleteUserById(SpUserVO reqData) {
        QueryWrapper<SpUserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", 1);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.eq("user_id", reqData.getUserId());
        SpUserPO spUserPO = userMapper.selectOne(queryWrapper);
        spUserPO.setEnableFlag(0);
        spUserPO.setDeleteFlag(1);
        spUserPO.setUpdateBy(SecurityUtils.getUserId());
        spUserPO.setUpdateDate(new Date());
        if (spUserPO.getIsAdmin() == 1) {
            return ResponseModel.errorMsg("不能删除管理员");
        }
        //删除用户
        if (!updateUserPO(spUserPO, queryWrapper)) {
            return ResponseModel.errorMsg("删除失败");
        }
        // 清除用户关联
        // 删除用户项目管理
        try {
            QueryWrapper<SpUserAppRolePO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("user_id", spUserPO.getUserId());
            spUserAppRoleMapper.delete(objectQueryWrapper);
        } catch (Exception e) {
            return ResponseModel.errorMsg("删除用户项目角色关系失败");
        }

        return ResponseModel.success();
    }

    /**
     * 根据用户id 更新用户信息
     *
     * @param spUserVO
     * @return
     */
    @Override
    public ResponseModel updateUserById(SpUserVO spUserVO) {
        QueryWrapper<SpUserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", 1);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.eq("user_id", spUserVO.getUserId());
        SpUserPO userPO = new SpUserPO();
        BeanUtils.copyProperties(spUserVO, userPO);
        if (updateUserPO(userPO, queryWrapper)) {
            ResponseModel.success();
        }
        return ResponseModel.errorMsg("修改失败");
    }

    /**
     * 查询该项目未添加的用户
     *
     * @param spApplicationVO
     * @return
     */
    @Override
    public ResponseModel selectNoJoinApplicationByAppId(SpApplicationVO spApplicationVO) {
        if (Objects.isNull(spApplicationVO.getAppId())) {
            return ResponseModel.errorMsg("项目id不能为空");
        }
        ResponseModel responseModel = new ResponseModel();
        try {
            List<SpUserVO> spUserVOS = userMapper.selectNoJoinApplicationByAppId(spApplicationVO);
            responseModel.setRepCodeEnum(RepCodeEnum.SUCCESS);
            responseModel.setRepData(spUserVOS);
        } catch (Exception e) {
            responseModel.setRepCode(RepCodeEnum.ERROR.getCode());
            responseModel.setRepMsg("查询失败");
            return responseModel;
        }
        return responseModel;
    }


    public Boolean updateUserPO(SpUserPO userPO, Wrapper updateWrapper) {
        int update = userMapper.update(userPO, updateWrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }


}
