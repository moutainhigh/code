package com.sandu.web.user;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in AM 10:09 2018/5/14 0014
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.nork.common.model.LoginUser;
import com.sandu.common.LoginContext;
import com.sandu.common.model.ResponseEnvelope;
import com.sandu.common.util.BadWordUtil;
import com.sandu.common.util.StringUtils;
import com.sandu.common.util.Utils;
import com.sandu.user.model.input.UserPrivateMessageAdd;
import com.sandu.user.model.input.UserPrivateMessageInput;
import com.sandu.user.model.view.UserPrivateMessageVo;
import com.sandu.user.service.SysUserService;
import com.sandu.user.service.UserPrivateMessageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author weisheng
 * @Title: 用户留言 私信
 * @Package
 * @Description:
 * @date 2018/5/14 0014AM 10:09
 */
@Slf4j
@RestController
@RequestMapping("/v1/union/message")
public class UserPrivateMessageController {
    private final static String CLASS_LOG_PREFIX = "[用户留言.私信服务]";

    private final static String filePath = "config/sensitivewords.properties";

    @Autowired
    private UserPrivateMessageService userPrivateMessageService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * @Title: 用户留言.私信列表
     * @Package
     * @Description:
     * @author weisheng
     * @date 2018/4/27 0027PM 6:02
     */
    @RequestMapping(value = "/getprivatemessagelist", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户留言列表", response = ResponseEnvelope.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "friendId", value = "接受者ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "curPage", value = "当前页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", required = false, paramType = "query", dataType = "int")
    })
    public ResponseEnvelope getUserPrivateMessageList(@RequestBody UserPrivateMessageAdd userPrivateMessageAdd, HttpServletRequest request) {
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (loginUser == null) {
            return new ResponseEnvelope(false, "请先登录");
        }
        userPrivateMessageAdd.setUserId(loginUser.getId());
        List<UserPrivateMessageVo> userPrivateMessageVoList = userPrivateMessageService.getUserPrivateMessageList(userPrivateMessageAdd);
        if (userPrivateMessageVoList == null || userPrivateMessageVoList.size() == 0) {
            return new ResponseEnvelope(false, "\n" +
                    "\n" +
                    "您暂时没有收到信息哦！\n" +
                    "\n" +
                    "逛逛同城看看有没有您需要的服务！\n");
        }


        return new ResponseEnvelope(true, "", userPrivateMessageVoList);
    }


    /**
     * @Title: 用户留言.私信列表详情
     * @Package
     * @Description:
     * @author weisheng
     * @date 2018/4/27 0027PM 6:02
     */
    @RequestMapping(value = "/getprivatemessageinfolist", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户留言列表", response = ResponseEnvelope.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "friendId", value = "接受者ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "curPage", value = "当前页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "messageType", value = "消息类型,1：普通消息 2：系统消息", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "消息状态 1：未读 2：已读 3：删除", required = false, paramType = "query", dataType = "int")
    })
    public ResponseEnvelope getUserPrivateMessageInfoList(@RequestBody UserPrivateMessageAdd userPrivateMessageAdd, HttpServletRequest request) {
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (loginUser == null) {
            return new ResponseEnvelope(false, "请先登录");
        }
        userPrivateMessageAdd.setUserId(loginUser.getId());
        List<UserPrivateMessageVo> userPrivateMessageInfoList = userPrivateMessageService.getUserPrivateMessageInfoList(userPrivateMessageAdd);
        if (userPrivateMessageInfoList == null || userPrivateMessageInfoList.size() == 0) {
            return new ResponseEnvelope(false, "还没有留言");
        }


        for (UserPrivateMessageVo uerPrivateMessageVo : userPrivateMessageInfoList) {
            String friendName = uerPrivateMessageVo.getFriendName();
            String senderName = uerPrivateMessageVo.getSenderName();
            if (StringUtils.isEmpty(friendName) && StringUtils.isNotBlank(uerPrivateMessageVo.getFriendNickName()) && Utils.isMobile(uerPrivateMessageVo.getFriendNickName())) {
                uerPrivateMessageVo.setFriendName(uerPrivateMessageVo.getFriendNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            if (StringUtils.isEmpty(friendName) && StringUtils.isNotBlank(uerPrivateMessageVo.getFriendNickName()) && !Utils.isMobile(uerPrivateMessageVo.getFriendNickName())) {
                uerPrivateMessageVo.setFriendName(uerPrivateMessageVo.getFriendNickName());
            }

            if (StringUtils.isEmpty(senderName) && StringUtils.isNotBlank(uerPrivateMessageVo.getSenderNickName()) && Utils.isMobile(uerPrivateMessageVo.getSenderNickName())) {
                uerPrivateMessageVo.setSenderName(uerPrivateMessageVo.getSenderNickName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }

            if (StringUtils.isEmpty(senderName) && StringUtils.isNotBlank(uerPrivateMessageVo.getSenderNickName()) && !Utils.isMobile(uerPrivateMessageVo.getSenderNickName())) {
                uerPrivateMessageVo.setSenderName(uerPrivateMessageVo.getSenderNickName());
            }

            if (StringUtils.isNotBlank(senderName) && Utils.isMobile(senderName)) {
                uerPrivateMessageVo.setSenderName(senderName.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }

            if (StringUtils.isNotBlank(friendName) && Utils.isMobile(friendName)) {
                uerPrivateMessageVo.setFriendName(friendName.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }

            if (StringUtils.isEmpty(uerPrivateMessageVo.getFriendPicPath())) {
                if (uerPrivateMessageVo.getFriendSex() == null || uerPrivateMessageVo.getFriendSex() == 0 || uerPrivateMessageVo.getFriendSex() == 1) {
                    String senderPic = sysUserService.getUserDefaultPic(1);
                    uerPrivateMessageVo.setFriendPicPath(senderPic);
                } else if (uerPrivateMessageVo.getFriendSex() != null && uerPrivateMessageVo.getFriendSex() == 2) {
                    String senderPic = sysUserService.getUserDefaultPic(2);
                    uerPrivateMessageVo.setFriendPicPath(senderPic);
                }
            }
            if( StringUtils.isEmpty(uerPrivateMessageVo.getSenderPicPath())){
                if (uerPrivateMessageVo.getSenderSex() == null || uerPrivateMessageVo.getSenderSex() == 0 || uerPrivateMessageVo.getSenderSex() == 1) {
                    String senderPic = sysUserService.getUserDefaultPic(1);
                    uerPrivateMessageVo.setSenderPicPath(senderPic);
                } else if (uerPrivateMessageVo.getSenderSex() != null && uerPrivateMessageVo.getSenderSex() == 2) {
                    String senderPic = sysUserService.getUserDefaultPic(2);
                    uerPrivateMessageVo.setSenderPicPath(senderPic);
                }
            }


        }

        return new ResponseEnvelope(true, "", userPrivateMessageInfoList);
    }


    /**
     * @Title: 用户留言.私信列表页面删除整个回话
     * @Package
     * @Description:
     * @author weisheng
     * @date 2018/4/27 0027PM 6:02
     */
    @RequestMapping(value = "/deluserprivatemessage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户留言列表", response = ResponseEnvelope.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "friendId", value = "接受者ID", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEnvelope delUserPrivateMessage(@RequestBody UserPrivateMessageAdd userPrivateMessageAdd, HttpServletRequest request) {
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (loginUser == null) {
            return new ResponseEnvelope(false, "请先登录");
        }
        userPrivateMessageAdd.setUserId(loginUser.getId());
        Integer messageId = userPrivateMessageService.delUserPrivateMessage(userPrivateMessageAdd);
        if (messageId == 0) {
            return new ResponseEnvelope(false, "删除留言回话失败");
        }
        return new ResponseEnvelope(true, "", messageId);
    }

    /**
     * @Title: 发送一条私信
     * @Package
     * @Description:
     * @author weisheng
     * @date 2018/4/27 0027PM 6:02
     */

    @RequestMapping(value = "/adduserprivatemessage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户留言详情", response = ResponseEnvelope.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageContent", value = "消息内容", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "friendId", value = "接受者ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "messageType", value = "消息类型,1：普通消息 2：系统消息", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEnvelope addUserPrivateMessage(@RequestBody @Valid UserPrivateMessageInput userPrivateMessageInput, HttpServletRequest request) {
      LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (loginUser == null) {
            return new ResponseEnvelope(false, "请先登录");
        }

        UserPrivateMessageAdd userPrivateMessageAdd = new UserPrivateMessageAdd();

        userPrivateMessageAdd.setMessageContent(userPrivateMessageInput.getMessageContent());
        userPrivateMessageAdd.setFriendId(userPrivateMessageInput.getFriendId());
        userPrivateMessageAdd.setMessageType(userPrivateMessageInput.getMessageType());
        userPrivateMessageAdd.setUserId(loginUser.getId());

        String messageContent = userPrivateMessageAdd.getMessageContent();

        //校验敏感词汇
        if (StringUtils.isNotBlank(messageContent)) {
            boolean containtBadWord = BadWordUtil.isContaintBadWord(messageContent, 2);
            if (containtBadWord) {
                userPrivateMessageAdd.setMessageContent(BadWordUtil.replaceBadWord(messageContent, 2, "*"));
            }
        }


        Integer messageId = userPrivateMessageService.addUserPrivateMessage(userPrivateMessageAdd);
        if (messageId == 0) {
            return new ResponseEnvelope(false, "留言失败");
        }
        return new ResponseEnvelope(true, "", messageId);
    }


}
