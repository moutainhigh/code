package com.nork.mobile.service;

import java.util.List;

import com.nork.common.model.ResponseEnvelope;
import com.nork.design.model.ThumbData;
import com.nork.product.model.BaseProductStyle;
import com.nork.product.model.search.BaseProductStyleSearch;

public interface MobileDesignPlanService {
	/**
	 * 移动端我的设计获取所有效果图
	 * @param designPlan
	 * @return
	 */
	public ResponseEnvelope getMyDesignPlanListMobile(ThumbData thumbData);
	
	/**
	 * 获取所有房间类型
	 * @param baseProductStyle
	 * @return
	 */
	List<BaseProductStyle> getSpace(BaseProductStyleSearch baseProductStyle);
	
	
	/**
     * 移动端我的设计获取720的效果图
     * @param thumbData
     * @return
     */
    public ResponseEnvelope get720renderPicByPage(ThumbData thumbData);

}
