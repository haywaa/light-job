package cn.chf.lightjob.controller.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-06 14:11
 */
@Data
public class GroupAddOrUpdateReq {

    /**
     * 修改时提交
     */
    private Integer id;

    /**
     * 执行器Code
     */
    @NotBlank(message = "执行器Code不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "英文字母开头，Code仅允许由英文字母，数字或下划线(_)组成")
    @Size(max = 20, message = "Code不能超过20个字符")
    private String groupCode;

    /**
     * 执行器名称
     */
    @NotBlank(message = "执行器名称不能为空")
    @Size(max = 20, message = "名称不能超过20个字符")
    private String groupName;
}
