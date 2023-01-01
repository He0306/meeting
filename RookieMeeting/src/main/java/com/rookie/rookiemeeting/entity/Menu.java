package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 何超
 * @since 2022-08-22
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路劲")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("父级ID")
    private Integer pid;

    @ApiModelProperty("页面路径")
    private String pagePath;

    @TableField(exist = false)
    private List<Menu> children;

    @ApiModelProperty("顺序")
    private Integer sortNum;


}
