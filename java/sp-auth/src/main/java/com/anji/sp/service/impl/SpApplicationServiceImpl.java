package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpApplicationMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpApplicationPO;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.service.SpApplicationService;
import com.anji.sp.util.Constants;
import com.anji.sp.util.RandCodeUtil;
import com.anji.sp.util.SecurityUtils;
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
 * @author : kean_qi
 * create at:  2020/6/27  5:47 下午
 * @description:
 */
@Service
public class SpApplicationServiceImpl implements SpApplicationService {

    @Autowired
    SpApplicationMapper spApplicationMapper;

    /**
     * 查询所有应用
     *
     * @return
     */
    @Override
    public ResponseModel queryApplicationList() {
        return ResponseModel.successData(selectApplicationList());
    }

    /**
     * 查询应用列表
     * @return
     */
    @Override
    public List<SpApplicationPO> selectApplicationList() {
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        List<SpApplicationPO> spDictPOS = spApplicationMapper.selectList(objectQueryWrapper);
        return spDictPOS;
    }

    /**
     * 分页查询应用
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel queryApplicationByPage(SpApplicationVO reqData) {
        PageQueryRep<SpApplicationVO> pageQueryRep = new PageQueryRep<>();
        PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        List<SpApplicationPO> spDictPOS = spApplicationMapper.selectList(objectQueryWrapper);
        PageInfo<SpApplicationPO> pageInfo = new PageInfo<>(spDictPOS);
        pageQueryRep.setTotal(pageInfo.getTotal());
        List<SpApplicationVO> collects = spDictPOS.stream().map(s -> {
            SpApplicationVO sp = new SpApplicationVO();
            BeanUtils.copyProperties(s, sp);
            return sp;
        }).collect(Collectors.toList());
        pageQueryRep.setRows(collects);
        return ResponseModel.successData(pageQueryRep);
    }


    /**
     * 新建应用
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel insertApplication(SpApplicationVO reqData) {
        if (!SecurityUtils.isAdmin()) {
            return ResponseModel.errorMsg(RepCodeEnum.NOT_OPERATION);
        }
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        objectQueryWrapper.eq("name", reqData.getName());
        SpApplicationPO res = spApplicationMapper.selectOne(objectQueryWrapper);
        if (Objects.nonNull(res)) {
            return ResponseModel.errorMsg(RepCodeEnum.APP_EXIST);
        }
        SpApplicationPO spApplicationPO = new SpApplicationPO();
        BeanUtils.copyProperties(reqData, spApplicationPO);
        //创建appkey
        spApplicationPO.setAppKey(RandCodeUtil.getUUID());
        spApplicationPO.setCreateBy(SecurityUtils.getUserId());
        spApplicationPO.setUpdateBy(SecurityUtils.getUserId());
        spApplicationPO.setCreateDate(new Date());
        spApplicationPO.setUpdateDate(new Date());
        spApplicationPO.setPublicKey(Constants.APP_PUBLIC_KEY);
        spApplicationPO.setPrivateKey(Constants.APP_PRIVATE_KEY);
        int insert = spApplicationMapper.insert(spApplicationPO);
        if (insert > 0) {
            return ResponseModel.success();
        }

        return ResponseModel.errorMsg("新建应用失败");
    }

    /**
     * 删除应用
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel deleteApplication(SpApplicationVO reqData) {
        if (!SecurityUtils.isAdmin()) {
            return ResponseModel.errorMsg(RepCodeEnum.NOT_OPERATION);
        }
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        objectQueryWrapper.eq("app_id", reqData.getAppId());
        SpApplicationPO res = spApplicationMapper.selectOne(objectQueryWrapper);
        //判断是否存在
        if (Objects.isNull(res)) {
            return ResponseModel.errorMsg(RepCodeEnum.APP_NOT_EXIST);
        }
        res.setEnableFlag(UserStatus.DISABLE.getIntegerCode());
        res.setDeleteFlag(IsDeleteEnum.DELETE.getIntegerCode());
        res.setUpdateBy(SecurityUtils.getUserId());
        res.setUpdateDate(new Date());
        int update = spApplicationMapper.update(res, objectQueryWrapper);
        if (update > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("删除失败");
    }

    /**
     * 更新应用名及app_key
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel updateApplication(SpApplicationVO reqData) {
        if (!SecurityUtils.isAdmin()) {
            return ResponseModel.errorMsg(RepCodeEnum.NOT_OPERATION);
        }
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        objectQueryWrapper.eq("app_id", reqData.getAppId());

        QueryWrapper<SpApplicationPO> selectWqpper = new QueryWrapper<>();
        selectWqpper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        selectWqpper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        selectWqpper.eq("name", reqData.getName());
        SpApplicationPO selectOne = spApplicationMapper.selectOne(selectWqpper);
        //判断是否存在
        if (!Objects.isNull(selectOne)) {
            return ResponseModel.errorMsg(RepCodeEnum.APP_EXIST);
        }

        SpApplicationPO res = spApplicationMapper.selectOne(objectQueryWrapper);
        //判断是否存在
        if (Objects.isNull(res)) {
            return ResponseModel.errorMsg(RepCodeEnum.APP_NOT_EXIST);
        }
        //更新app_key
//        res.setAppKey(reqData.getAppKey());
        //更新项目名
        res.setName(reqData.getName());
        res.setUpdateBy(SecurityUtils.getUserId());
        res.setUpdateDate(new Date());
        int update = spApplicationMapper.update(res, objectQueryWrapper);
        if (update > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("更新失败");
    }

    /**
     * 根据appId查询应用信息
     *
     * @param appId
     * @return
     */
    @Override
    public SpApplicationPO selectByAppId(Long appId) {
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        objectQueryWrapper.eq("app_id", appId);
        SpApplicationPO spApplicationPO = spApplicationMapper.selectOne(objectQueryWrapper);
        return spApplicationPO;
    }

    /**
     * 根据appKey查询应用信息
     *
     * @param appKey
     * @return
     */
    @Override
    public SpApplicationPO selectByAppKey(String appKey) {
        QueryWrapper<SpApplicationPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        objectQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        objectQueryWrapper.eq("app_key", appKey);
        SpApplicationPO spApplicationPO = spApplicationMapper.selectOne(objectQueryWrapper);
        return spApplicationPO;
    }

}
