package com.anji.sp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author Kean 2020-06-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_dict")
@ApiModel("字典表")
public class SpDictPO extends BasePO {

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("标签名")
    private String name;

    @ApiModelProperty("数据值")
    private String value;

    @ApiModelProperty(value = "类型（android版本、ios版本、公告类型、展示类型）", required = true)
    private String type;

    @ApiModelProperty("描述（android版本、ios版本、公告类型、展示类型）")
    private String description;

    @ApiModelProperty("父级编号：0代表没有父级")
    private Long parentId;


    public SpDictPO() {
    }

}