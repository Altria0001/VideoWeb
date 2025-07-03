package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer uid;

    /**
     * 
     */
    private String user_name;

    /**
     * 
     */
    private String user_password;

    /**
     * 
     */
    private String iphone;

    /**
     * 
     */
    private Date createtime;

    /**
     用户更改数据的时候进行
     */
    private Date updatetime;

    /**
      管理员权限：admin
      普通用户权限：user
      游客权限：guest
      封禁用户: ban
      注销用户: delete
     */
    private String permession;
}