package com.anji.sp.model.po;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by raodeming on 2020/6/29.
 */
@Data
@ApiModel("操作日志")
@TableName("sp_oper_log")
public class SpOperLogPO {

    @ApiModelProperty("日志主键")
    private Long operId;

    @ApiModelProperty("模块标题")
    private String title;

    @ApiModelProperty("业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("操作人员")
    private String operName;

    @ApiModelProperty("应用ID")
    private Long appId;

    @ApiModelProperty("请求url")
    private String operUrl;

    @ApiModelProperty("主机地址")
    private String operIp;

    @ApiModelProperty("操作地点")
    private String operLocation;

    @ApiModelProperty("请求参数")
    private String operParam;

    @ApiModelProperty("返回参数")
    private String jsonResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty("错误消息")
    private String errorMsg;

    @ApiModelProperty("操作时间")
    private Date operTime;

    @ApiModelProperty("请求开始时间")
    private Long beginTime;

    @ApiModelProperty("请求结束时间")
    private Long endTime;

    @ApiModelProperty("接口耗时")
    private Long time;

}
