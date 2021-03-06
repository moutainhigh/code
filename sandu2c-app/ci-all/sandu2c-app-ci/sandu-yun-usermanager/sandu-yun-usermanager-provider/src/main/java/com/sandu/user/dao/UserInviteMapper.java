package com.sandu.user.dao;


import com.sandu.user.model.IntermediaryCommision;
import com.sandu.user.model.MiniProgramShareRecord;
import com.sandu.user.model.UserInvite;
import com.sandu.user.model.UserInviteTrack;
import com.sandu.user.model.input.UserInviteAdd;
import com.sandu.user.model.search.UserInviteSearch;
import com.sandu.user.model.view.UserInviteInfoVo;
import com.sandu.user.model.view.UserInviteListVo;
import com.sandu.user.model.view.UserInviteTopListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInviteMapper {
    /**
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int insert(UserInvite record);

    /**
     *
     * @mbggenerated
     */
    int insertSelective(UserInvite record);

    /**
     *
     * @mbggenerated
     */
    UserInvite selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserInvite record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserInvite record);



    List<UserInviteTopListVo> selectAllInviteList(UserInviteAdd userInviteAdd);

    UserInvite selectUserInviteInfoByFid(int fid);

    List<UserInviteInfoVo> selectUserInviteInfoByInviteId(UserInviteAdd userInviteAdd);

    int selectAllInviteCount();

    int selectUserInviteInfoCountByInviteId(UserInviteAdd userInviteAdd);

    int updateUserInviteByRemark(String openId);

    List<UserInviteListVo> selectMyUserInviteList(@Param("inviteId")Integer inviteId, @Param("status") Integer status,@Param("start") Integer start,@Param("limit") Integer limit);

    int selectMyUserInviteCount(@Param("inviteId") Integer inviteId,@Param("status") Integer status);

    int insertUserInviteTrack(UserInviteTrack userInviteTrack);

    int insertIntermediaryCommision(IntermediaryCommision intermediaryCommision);

    int insertMiniProgramShareRecord(MiniProgramShareRecord miniProgramShareRecord);

    List<MiniProgramShareRecord> selectMiniProgramShareRecordByFid(Integer id);
}