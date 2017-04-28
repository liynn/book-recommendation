package com.wy.param;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * Created by wy on 2017/4/27.
 */
@Data
public class UserParam {
    @NotBlank(message = "名字不能为空")
    private String name;//姓名
    @NotBlank(message = "性别不能为空")
    private String sex; //性别
    @Max(value = 120, message = "年龄最大不能超过120")
    private Integer age; //年龄
    @Pattern(regexp = "^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号码格式不正确")
    private String phone; //手机号
    @Email(message = "邮箱格式不正确")
    private String email; //邮箱
    @NotBlank(message = "密码不能为空")
    private String password;//密码
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;//确认密码
}
