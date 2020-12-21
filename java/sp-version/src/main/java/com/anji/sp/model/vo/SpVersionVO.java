package com.anji.sp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 版本管理
 *
 * @author kean 2020-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("版本管理")
public class SpVersionVO extends BaseVO implements Serializable {

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    @ApiModelProperty(value = "平台名称 iOS、Android", required = true)
    private String platform;

    @ApiModelProperty(value = "版本名称", required = true)
    private String versionName;

    @ApiModelProperty(value = "版本号", required = true)
    private String versionNumber;

    @ApiModelProperty(value = "更新日志", required = true)
    private String updateLog;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("版本配置列表(需要版本更新的版本号例如 [10,11,12])")
    private List<String> versionConfigStrList;

    @ApiModelProperty("0--已禁用 1--已启用  是否可编辑 默认启用")
    private Integer enableEdit;

    @ApiModelProperty("发布状态")
    private Integer published;

//    @ApiModelProperty("版本配置（需要版本更新的版本号例如: 1.1.1,1.1.2,1.1.3）")
//    private String needUpdateVersions;

    @ApiModelProperty("版本配置列表（需要版本更新的版本号例如: [1.1.1,1.1.2,1.1.3]）")
    private List<String> needUpdateVersionList;

//    @ApiModelProperty("发布阶段 2,10,30,40,50,100,100")
//    private String canaryReleaseStage;

    @ApiModelProperty("发布阶段列表 2,10,30,40,50,100,100")
    private List<String> canaryReleaseStageList;


    @ApiModelProperty("0--已禁用 1--已启用 是否灰度发布 默认禁用")
    private Integer canaryReleaseEnable;

    @ApiModelProperty("灰度发已用时间（毫秒）")
    private Long canaryReleaseUseTime;
//    old_canary_release_use_time
    @ApiModelProperty("已用时间（用于启用一段时间关闭的状态保存）")
    private Long oldCanaryReleaseUseTime;

    @ApiModelProperty("启用时间")
    private Date enableTime;
}