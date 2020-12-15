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
 * 版本管理
 *
 * @author kean 2020-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_version")
@ApiModel("版本管理")
public class SpVersionPO extends BasePO {

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    @ApiModelProperty(value = "平台名称 iOS、Android", required = true)
    private String platform;

    @ApiModelProperty("版本名称")
    private String versionName;

    @ApiModelProperty("版本号")
    private String versionNumber;

    @ApiModelProperty("更新日志")
    private String updateLog;

    @ApiModelProperty("内部下载url")
    private String internalUrl;

    @ApiModelProperty("外部下载url")
    private String externalUrl;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("版本配置(需要版本更新的版本号例如 10,11,12)")
    private String versionConfig;

    @ApiModelProperty("0--已禁用 1--已启用  是否可编辑 默认启用")
    private Integer enableEdit;

    @ApiModelProperty("版本配置（需要版本更新的版本号例如: 1.1.1，1.1.2，1.1.3）")
    private String needUpdateVersions;

    @ApiModelProperty("例如 2,10,30,40,50,100,100")
    private String canaryReleaseStage;

    @ApiModelProperty("0--已禁用 1--已启用 是否灰度发布 默认禁用")
    private Integer canaryReleaseEnable;

    @ApiModelProperty("灰度发已用时间（毫秒）")
    private Long canaryReleaseUseTime;

    @ApiModelProperty("已用时间（用于启用一段时间关闭的状态保存）")
    private Long oldCanaryReleaseUseTime;

    @ApiModelProperty("启用时间")
    private Date enableTime;

}