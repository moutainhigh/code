package com.sandu.user.model.view;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 4:19 2018/7/23 0023
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: 最新佣金信息详情
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/7/23 0023PM 4:19
 */
@Data
public class NewestcommissioninfoDTO implements Serializable{
    private static final long serialVersionUID = 2253707042595533495L;

    private Integer userId; //用户ID

    private String nickName; //用户昵称

    private Integer record; //记录

    private Date createTime; //佣金入账时间

    private String sign;//标识
}
