package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 公告管理
 *
 * @author kean 2020-06-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_notice")
@ApiModel("公告管理")
public class SpNoticePO extends BasePO {

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("公告名称")
    private String name;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String details;

    @ApiModelProperty("模板类型（来自字典表）")
    private String templateType;

    @ApiModelProperty("模板类型名")
    private String templateTypeName;

    @ApiModelProperty("效果类型（来自字典表）")
    private Integer resultType;

    @ApiModelProperty("效果类型Name")
    private String resultTypeName;


    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    public SpNoticePO() {
    }

}