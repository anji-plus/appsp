package com.anji.sp.service.impl;

import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.mapper.SpDictMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpDictPO;
import com.anji.sp.model.vo.QueryDictByTypeVO;
import com.anji.sp.model.vo.SpDictPageVO;
import com.anji.sp.model.vo.SpDictVO;
import com.anji.sp.service.SpDictService;
import com.anji.sp.util.SecurityUtils;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/23  5:00 下午
 * @description:
 */
@Service
@Slf4j
public class SpDictServiceImpl implements SpDictService {
    @Autowired
    SpDictMapper spDictMapper;
//    @Autowired
//    SpVersionMapper spVersionMapper;

    /**
     * 查询字典信息 进行分页查询
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel queryByPage(SpDictVO reqData) {
        PageQueryRep<SpDictVO> pageQueryRep = new PageQueryRep<>();
        PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
        QueryWrapper<SpDictPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("type", reqData.getType());
        objectQueryWrapper.eq("enable_flag", 1);
        objectQueryWrapper.eq("delete_flag", 0);
        objectQueryWrapper.orderByAsc("id");
        List<SpDictPO> spDictPOS = spDictMapper.selectList(objectQueryWrapper);
        PageInfo<SpDictPO> pageInfo = new PageInfo<>(spDictPOS);
        pageQueryRep.setTotal(pageInfo.getTotal());
        List<SpDictVO> collects = spDictPOS.stream().map(s -> {
            SpDictVO sp = new SpDictVO();
            BeanUtils.copyProperties(s, sp);
            return sp;
        }).collect(Collectors.toList());
        if (reqData.getPageNo() > pageInfo.getPageNum()) {
            pageQueryRep.setRows(new ArrayList<>());
        } else {
            pageQueryRep.setRows(collects);
        }
        return ResponseModel.successData(pageQueryRep);
    }

    /**
     * 根据type 查询字典信息 升序排练
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel queryByType(QueryDictByTypeVO reqData) {
        if (StringUtils.isEmpty(reqData.getType())) {
            return ResponseModel.errorMsg("类型不匹配");
        }
        QueryWrapper<SpDictPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("type", reqData.getType());
        objectQueryWrapper.eq("enable_flag", 1);
        objectQueryWrapper.eq("delete_flag", 0);
        if ("IOS_VERSION".equals(reqData.getType()) || "ANDROID_VERSION".equals(reqData.getType())) {
            // 这样就是按照 大小降序排列
            // SELECT *  FROM testTable where fcode=40006 and fmotype='bu100101' order by fvalue+0 desc limit 0,10
            objectQueryWrapper.orderByAsc("name+0");
        } else {
            objectQueryWrapper.orderByAsc("id");
        }
        List<SpDictPO> spDictPOS = spDictMapper.selectList(objectQueryWrapper);
        List<SpDictPageVO> collects = spDictPOS.stream().map(s -> {

            SpDictPageVO sp = new SpDictPageVO();
            BeanUtils.copyProperties(s, sp);
            return sp;
        }).collect(Collectors.toList());
        return ResponseModel.successData(collects);
    }

    /**
     * 新增字典内容
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel insertDict(SpDictVO reqData) {
        ResponseModel responseModel = new ResponseModel();

        if (StringUtils.isEmpty(reqData.getType())) {
            return ResponseModel.errorMsg("字典类型不匹配");
        } else if (StringUtils.isEmpty(reqData.getValue())) {
            return ResponseModel.errorMsg("新增内容为空");
        }

        if ("TEMPLATE".equals(reqData.getType())) {
            if (StringUtils.isEmpty(reqData.getName())) {
                return ResponseModel.errorMsg("模板类型不能为空");
            }
        }

        //查询数据是否存在
        QueryWrapper<SpDictPO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("type", reqData.getType());
        objectQueryWrapper.eq("enable_flag", 1);
        objectQueryWrapper.eq("delete_flag", 0);
        objectQueryWrapper.eq("value", reqData.getValue()).or().eq("name", reqData.getName());
        List<SpDictPO> spDictPOS = spDictMapper.selectList(objectQueryWrapper);

        //如果存在 提示存在
        if (spDictPOS.size() > 0) {
            if ("IOS_VERSION".equals(reqData.getType()) || "ANDROID_VERSION".equals(reqData.getType())) {
                responseModel.setRepCode(RepCodeEnum.VERSION_EXIST.getCode());
                responseModel.setRepMsg("版本" + reqData.getValue() + "已存在");
            } else {
                return ResponseModel.errorMsg("数据已存在");
            }
            return responseModel;
        }

        //查询最大一条数据
        QueryWrapper<SpDictPO> dictsQueryMapper = new QueryWrapper<>();
        dictsQueryMapper.eq("type", reqData.getType());
        dictsQueryMapper.eq("enable_flag", 1);
        dictsQueryMapper.eq("delete_flag", 0);
        dictsQueryMapper.orderByDesc("id");
        List<SpDictPO> spDictPOS1 = spDictMapper.selectList(dictsQueryMapper);
        SpDictPO spDictPO1 = spDictPOS1.get(0);
        //不存在 添加
        SpDictPO spDictAddPO = new SpDictPO();
        if ("TEMPLATE".equals(reqData.getType())) {
            //模板类型 手动赋值
            spDictAddPO.setName(reqData.getName());
        } else {
            Integer i = Integer.parseInt(spDictPO1.getName());
            i = i + 1;
            //将name+1 传入新的数据中
            spDictAddPO.setName("" + i);
        }


//        if ("IOS_VERSION".equals(reqData.getType()) || "ANDROID_VERSION".equals(reqData.getType())){
//            spDictAddPO.setName(reqData.getValue());
//        } else{
//            Integer i = Integer.parseInt(spDictPO1.getName());
//            i = i+1;
//            //将name+1 传入新的数据中
//            spDictAddPO.setName("" + i);
//        }

        spDictAddPO.setValue(reqData.getValue());
        spDictAddPO.setType(spDictPO1.getType());
        spDictAddPO.setDescription(spDictPO1.getDescription());
        spDictAddPO.setCreateBy(SecurityUtils.getUserId());
        spDictAddPO.setUpdateBy(SecurityUtils.getUserId());
        spDictAddPO.setCreateDate(new Date());
        spDictAddPO.setUpdateDate(new Date());

        int insert = spDictMapper.insert(spDictAddPO);

        if (insert == 0) {
            responseModel.setRepCodeEnum(RepCodeEnum.VERSION_INSERT_FAILURE);
            return responseModel;
        } else {
            return ResponseModel.success();
        }
    }


    private int getVersionListSize(String platform) {
//        QueryWrapper<SpVersionPO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("enable_flag", 1);
//        queryWrapper.eq("delete_flag", 0);
//        queryWrapper.eq("platform", platform);
//        Integer size = spVersionMapper.selectCount(queryWrapper);
        return 0;
    }

    /**
     * 新增字典内容
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel checkVersion(QueryDictByTypeVO reqData) {
        if (StringUtils.isEmpty(reqData.getType())) {
            return ResponseModel.errorMsg("类型为空");
        } else if (StringUtils.isEmpty(reqData.getValue())) {
            return ResponseModel.errorMsg("版本号为空");
        }
        String platform = "";
        if ("IOS_VERSION".equals(reqData.getType())) {
            platform = "iOS";
        } else if ("ANDROID_VERSION".equals(reqData.getType())) {
            platform = "Android";
        } else {
            return ResponseModel.errorMsg("类型有误");
        }

        int size = getVersionListSize(platform);
        if (size == 0) {
            QueryWrapper objectQueryWrapper = new QueryWrapper();
            objectQueryWrapper.eq("type", reqData.getType());
            objectQueryWrapper.eq("enable_flag", 1);
            objectQueryWrapper.eq("delete_flag", 0);
            objectQueryWrapper.eq("value", reqData.getValue());
            SpDictPO findResult = spDictMapper.selectOne(objectQueryWrapper);
            if (findResult != null) {
                SpDictPageVO sp = new SpDictPageVO();
                BeanUtils.copyProperties(findResult, sp);
                return ResponseModel.successData(sp);
            } else {
                return ResponseModel.errorMsg("未匹配到对应版本");
            }
        }
        if (reqData.getCheckType() != null) {
            if (reqData.getCheckType() == 1) {
                return ResponseModel.errorMsg("使用中,无法编辑" + platform + "版本" + reqData.getValue());
            } else if (reqData.getCheckType() == 2) {
                return ResponseModel.errorMsg("使用中,无法删除" + platform + "版本" + reqData.getValue());
            }
        }
        return ResponseModel.errorMsg("使用中,无法编辑" + platform + "版本" + reqData.getValue());
    }

    /**
     * 根据id 更新版本信息 (value)
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel updateVersionInfoById(SpDictPageVO reqData) {
        if (reqData.getId() == null) {
            return ResponseModel.errorMsg("版本Id不能空");
        } else if (StringUtils.isEmpty(reqData.getValue())) {
            return ResponseModel.errorMsg("版本数据不能为空");
        }
        SpDictPO spDictPO = new SpDictPO();
        spDictPO.setId(reqData.getId());
        spDictPO.setValue(reqData.getValue());
        spDictPO.setUpdateBy(SecurityUtils.getUserId());
        spDictPO.setUpdateDate(new Date());
        int update = spDictMapper.updateById(spDictPO);
        if (update == 0) {
            return ResponseModel.errorMsg("更新失败");
        }
        return ResponseModel.success();
    }

    /**
     * 逻辑删除 并不是真的删除
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel deleteVersionById(SpDictPageVO reqData) {
        if (reqData.getId() == null) {
            return ResponseModel.errorMsg("版本Id不能空");
        }
        SpDictPO spDictPO = new SpDictPO();
        spDictPO.setId(reqData.getId());
        spDictPO.setEnableFlag(0);
        spDictPO.setDeleteFlag(1);
        spDictPO.setUpdateBy(SecurityUtils.getUserId());
        spDictPO.setUpdateDate(new Date());
        int update = spDictMapper.updateById(spDictPO);
        if (update == 0) {
            return ResponseModel.errorMsg("删除失败");
        }
        return ResponseModel.success();
    }


}
