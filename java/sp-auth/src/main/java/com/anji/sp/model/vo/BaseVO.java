package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("BaseVO")
@Data
public class BaseVO implements Serializable {

    private Integer pageNo = 1;      //当前是每几页
    private Integer pageSize = 10;        //每页显示多少行

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
