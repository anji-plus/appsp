package com.anji.sp.controller;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.QueryDictByTypeVO;
import com.anji.sp.model.vo.SpDictPageVO;
import com.anji.sp.model.vo.SpDictVO;
import com.anji.sp.service.SpDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by raodeming on 2020/6/23.
 */
@RestController
@RequestMapping("/dict")
@Api(tags = "字典接口")
public class SpDictController {

    @Autowired
    private SpDictService spDictService;

    @ApiOperation(value = "分页进行查询字典表", httpMethod = "POST")
    @PostMapping("/selectByPage/v1")
    public ResponseModel selectByPage(@RequestBody SpDictVO spDictVO) {
        return spDictService.queryByPage(spDictVO);
    }

    @ApiOperation(value = "根据字典类型查询字典表", httpMethod = "POST", notes = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ")
    @PostMapping("/selectByType/v1")
    public ResponseModel selectByType(@RequestBody QueryDictByTypeVO queryDictByTypeVO) {
        return spDictService.queryByType(queryDictByTypeVO);
    }

    @ApiOperation(value = "插入iOS或Android字典类型", httpMethod = "POST", notes = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ")
    @PostMapping("/insert/v1")
    public ResponseModel insert(@RequestBody SpDictVO spDictVO) {
        return spDictService.insertDict(spDictVO);
    }


    @ApiOperation(value = "检查iOS或Android字典类型的值是否可编辑或删除", httpMethod = "POST", notes = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ")
    @PostMapping("/checkVersion/v1")
    public ResponseModel checkVersion(@RequestBody QueryDictByTypeVO spDictVO) {
        return spDictService.checkVersion(spDictVO);
    }

    @ApiOperation(value = "根据版本id更新iOS或Android字典类型的值", httpMethod = "POST", notes = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ")
    @PostMapping("/updateById/v1")
    public ResponseModel updateById(@RequestBody SpDictPageVO spDictVO) {
        return spDictService.updateVersionInfoById(spDictVO);
    }

    @ApiOperation(value = "删除iOS或Android字典类型的值", httpMethod = "POST", notes = "传入字段 tpye: 1、IOS_VERSION iOS版本号 2、ANDROID_VERSION Android版本号 3、TEMPLATE 模板类型 ")
    @PostMapping("/deleteById/v1")
    public ResponseModel deleteById(@RequestBody SpDictPageVO spDictVO) {
        return spDictService.deleteVersionById(spDictVO);
    }

}
