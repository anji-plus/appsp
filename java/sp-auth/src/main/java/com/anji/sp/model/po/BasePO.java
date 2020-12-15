package com.anji.sp.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * @author
 * @Title: Entity
 * @Description:
 */
@ToString(callSuper = true)
@Data
@ApiModel("BasePO")
public class BasePO implements Serializable {

    @ApiModelProperty("0--已禁用 1--已启用 dic_name=enable_flag")
    private Integer enableFlag;

    @ApiModelProperty("0--未删除 1--已删除 dic_name=del_flag")
    private Integer deleteFlag;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("备注信息")
    private String remarks;
}
