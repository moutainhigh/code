package com.nork.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nork.common.model.Mapper;

/**   
 * @Title: ResRenderPic.java 
 * @Package com.nork.system.model
 * @Description:渲染图片资源库-渲染图片资源库
 * @createAuthor pandajun 
 * @CreateDate 2017-03-22 14:39:08
 * @version V1.0   
 */
public class ResRenderPic  extends Mapper implements Serializable{
private static final long serialVersionUID = 1L;
    private Integer id;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
	/**  设计方案  **/
	private Integer businessId;
	/**  图片编码  **/
	private String picCode;
	/**  图片名称  **/
	private String picName;
	/**  图片文件名称  **/
	private String picFileName;
	/**  图片类型  **/
	private String picType;
	/**  图片大小  **/
	private Integer picSize;
	/**  图片长  **/
	private Integer picWeight;
	/**  图片高  **/
	private Integer picHigh;
	/**  图片后缀  **/
	private String picSuffix;
	/**  图片级别  **/
	private Integer picLevel;
	/**  图片格式  **/
	private String picFormat;
	/**  图片路径  **/
	private String picPath;
	/**  图片描述  **/
	private String picDesc;
	/**  图片排序  **/
	private Integer picOrdering;
	/**  key标识  **/
	private String fileKey;
	/**  key标识（多个）  **/
	private String fileKeys;
	/**  图片对应的缩略图id信息  **/
	private String smallPicInfo;
	/**  渲染图视角  **/
	private Integer viewPoint;
	/**  渲染图场景  **/
	private Integer scene;
	/**  渲染类型  **/
	private Integer renderingType;
	/**  图片文件排序序号  **/
	private Integer sequence;
	/**  全景图地址  **/
	private String panoPath;
	/**  渲染任务创建时间  **/
	private Date taskCreateTime;
	/**  原图id  **/
	private Integer pid;
	/**  系统编码  **/
	private String sysCode;
	/**  创建者  **/
	private String creator;
	/**  创建时间  **/
	private Date gmtCreate;
	/**  修改人  **/
	private String modifier;
	/**  修改时间  **/
	private Date gmtModified;
	/**  是否删除  **/
	private Integer isDeleted;
	/**  字符备用1  **/
	private String att1;
	/**  字符备用2  **/
	private String att2;
	/**  整数备用1  **/
	private Integer numa1;
	/**  整数备用2  **/
	private Integer numa2;
	/**  备注  **/
	private String remark;
	private String OriginalPicPath;
	/** 原图ID。缩略图时使用 **/
	private Integer baseRenderId;

	/**
	 * 渲染任务截图
	 */
	private Integer sysTaskPicId;
	/** 720全景图清晰度等级 **/
	private Integer panoLevel;
	/** 漫游图片关系 **/
	private String roam;
	/**
	 * 设计方案副本id
	 */
	private Integer designSceneId;

	private Integer sourcePlanId;
	
	private Integer templateId;
	
	List<String>fileKeyList = new ArrayList<String>();
	
	private String renderTaskType;
	
	private Integer userId;
	
	/**
	 * 订单产品类型(具体可以见ProductType.java)
	 */
	private String productType;
	
	/**
	 * 订单总金额，单位为分
	 */
	private Double totalFee;
	
	/**
	 * 支付类型
	 */
	private String payType;
	//移动端我的替换列表需要这个值作为参数
	private Integer taskId;

	private List<Integer> recommendIds;
	private List<Integer> sceneIds;
	//平台id
	private Integer platformId;
	/**相机位置**/
	private String cameraLocation;

	public String getCameraLocation() {
		return cameraLocation;
	}

	public void setCameraLocation(String cameraLocation) {
		this.cameraLocation = cameraLocation;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public List<Integer> getRecommendIds() {
		return recommendIds;
	}

	public void setRecommendIds(List<Integer> recommendIds) {
		this.recommendIds = recommendIds;
	}

	public List<Integer> getSceneIds() {
		return sceneIds;
	}

	public void setSceneIds(List<Integer> sceneIds) {
		this.sceneIds = sceneIds;
	}

	public Integer getTaskId() {
			return taskId;
		}

		public void setTaskId(Integer taskId) {
			this.taskId = taskId;
		}
	
	
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRenderTaskType() {
		return renderTaskType;
	}

	public void setRenderTaskType(String renderTaskType) {
		this.renderTaskType = renderTaskType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<String> getFileKeyList() {
		return fileKeyList;
	}

	public void setFileKeyList(List<String> fileKeyList) {
		this.fileKeyList = fileKeyList;
	}

	public Integer getSourcePlanId() {
		return sourcePlanId;
	}

	public void setSourcePlanId(Integer sourcePlanId) {
		this.sourcePlanId = sourcePlanId;
	}



	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	/**
	 * 渲染时的实际方案名称，后面设计方案发生修改，名称不在同步
	 */
	private String designPlanName;
	/**
	 * 设计方案对应的房屋空间类型
	 */
	private String spaceType;
	/**
	 * 面积范围
	 */
	private String area;
	/**
	 * 创建者的userid
	 */
	
	private Integer planRecommendedId;
	//操作入口 0：我的设计  1：推荐方案
	private Integer operationSource;
	
	private Integer designPlanSceneId;
	
	
	public Integer getDesignPlanSceneId() {
		return designPlanSceneId;
	}

	public void setDesignPlanSceneId(Integer designPlanSceneId) {
		this.designPlanSceneId = designPlanSceneId;
	}

	public Integer getOperationSource() {
		return operationSource;
	}

	public void setOperationSource(Integer operationSource) {
		this.operationSource = operationSource;
	}

	public Integer getPlanRecommendedId() {
		return planRecommendedId;
	}

	public void setPlanRecommendedId(Integer planRecommendedId) {
		this.planRecommendedId = planRecommendedId;
	}

	private long createUserId;
	public Integer getDesignSceneId() {
		return designSceneId;
	}

	public void setDesignSceneId(Integer designSceneId) {
		this.designSceneId = designSceneId;
	}

	public String getOriginalPicPath() {
		return OriginalPicPath;
	}

	public void setOriginalPicPath(String originalPicPath) {
		OriginalPicPath = originalPicPath;
	}

	public Integer getBaseRenderId() {
		return baseRenderId;
	}

	public void setBaseRenderId(Integer baseRenderId) {
		this.baseRenderId = baseRenderId;
	}

	public Integer getSysTaskPicId() {
		return sysTaskPicId;
	}

	public void setSysTaskPicId(Integer sysTaskPicId) {
		this.sysTaskPicId = sysTaskPicId;
	}

	public  Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId){
		this.businessId = businessId;
	}
	public  String getPicCode() {
		return picCode;
	}
	public void setPicCode(String picCode){
		this.picCode = picCode;
	}
	public  String getPicName() {
		return picName;
	}
	public void setPicName(String picName){
		this.picName = picName;
	}
	public  String getPicFileName() {
		return picFileName;
	}
	public void setPicFileName(String picFileName){
		this.picFileName = picFileName;
	}
	public  String getPicType() {
		return picType;
	}
	public void setPicType(String picType){
		this.picType = picType;
	}
	public  Integer getPicSize() {
		return picSize;
	}
	public void setPicSize(Integer picSize){
		this.picSize = picSize;
	}
	public  Integer getPicWeight() {
		return picWeight;
	}
	public void setPicWeight(Integer picWeight){
		this.picWeight = picWeight;
	}
	public  Integer getPicHigh() {
		return picHigh;
	}
	public void setPicHigh(Integer picHigh){
		this.picHigh = picHigh;
	}
	public  String getPicSuffix() {
		return picSuffix;
	}
	public void setPicSuffix(String picSuffix){
		this.picSuffix = picSuffix;
	}
	public  Integer getPicLevel() {
		return picLevel;
	}
	public void setPicLevel(Integer picLevel){
		this.picLevel = picLevel;
	}
	public  String getPicFormat() {
		return picFormat;
	}
	public void setPicFormat(String picFormat){
		this.picFormat = picFormat;
	}
	public  String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath){
		this.picPath = picPath;
	}
	public  String getPicDesc() {
		return picDesc;
	}
	public void setPicDesc(String picDesc){
		this.picDesc = picDesc;
	}
	public  Integer getPicOrdering() {
		return picOrdering;
	}
	public void setPicOrdering(Integer picOrdering){
		this.picOrdering = picOrdering;
	}
	public  String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey){
		this.fileKey = fileKey;
	}
	public  String getFileKeys() {
		return fileKeys;
	}
	public void setFileKeys(String fileKeys){
		this.fileKeys = fileKeys;
	}
	public  String getSmallPicInfo() {
		return smallPicInfo;
	}
	public void setSmallPicInfo(String smallPicInfo){
		this.smallPicInfo = smallPicInfo;
	}
	public  Integer getViewPoint() {
		return viewPoint;
	}
	public void setViewPoint(Integer viewPoint){
		this.viewPoint = viewPoint;
	}
	public  Integer getScene() {
		return scene;
	}
	public void setScene(Integer scene){
		this.scene = scene;
	}
	public  Integer getRenderingType() {
		return renderingType;
	}
	public void setRenderingType(Integer renderingType){
		this.renderingType = renderingType;
	}
	public  Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence){
		this.sequence = sequence;
	}
	public  String getPanoPath() {
		return panoPath;
	}
	public void setPanoPath(String panoPath){
		this.panoPath = panoPath;
	}
	public  Date getTaskCreateTime() {
		return taskCreateTime;
	}
	public void setTaskCreateTime(Date taskCreateTime){
		this.taskCreateTime = taskCreateTime;
	}
	public  Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid){
		this.pid = pid;
	}
	public  String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode){
		this.sysCode = sysCode;
	}
	public  String getCreator() {
		return creator;
	}
	public void setCreator(String creator){
		this.creator = creator;
	}
	public  Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate){
		this.gmtCreate = gmtCreate;
	}
	public  String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier){
		this.modifier = modifier;
	}
	public  Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified){
		this.gmtModified = gmtModified;
	}
	public  Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted){
		this.isDeleted = isDeleted;
	}
	public  String getAtt1() {
		return att1;
	}
	public void setAtt1(String att1){
		this.att1 = att1;
	}
	public  String getAtt2() {
		return att2;
	}
	public void setAtt2(String att2){
		this.att2 = att2;
	}
	public  Integer getNuma1() {
		return numa1;
	}
	public void setNuma1(Integer numa1){
		this.numa1 = numa1;
	}
	public  Integer getNuma2() {
		return numa2;
	}
	public void setNuma2(Integer numa2){
		this.numa2 = numa2;
	}
	public  String getRemark() {
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public Integer getPanoLevel() {
		return panoLevel;
	}
	public void setPanoLevel(Integer panoLevel) {
		this.panoLevel = panoLevel;
	}
	public String getRoam() {
		return roam;
	}
	public void setRoam(String roam) {
		this.roam = roam;
	}

	@Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ResRenderPic other = (ResRenderPic) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBusinessId() == null ? other.getBusinessId() == null : this.getBusinessId().equals(other.getBusinessId()))
            && (this.getPicCode() == null ? other.getPicCode() == null : this.getPicCode().equals(other.getPicCode()))
            && (this.getPicName() == null ? other.getPicName() == null : this.getPicName().equals(other.getPicName()))
            && (this.getPicFileName() == null ? other.getPicFileName() == null : this.getPicFileName().equals(other.getPicFileName()))
            && (this.getPicType() == null ? other.getPicType() == null : this.getPicType().equals(other.getPicType()))
            && (this.getPicSize() == null ? other.getPicSize() == null : this.getPicSize().equals(other.getPicSize()))
            && (this.getPicWeight() == null ? other.getPicWeight() == null : this.getPicWeight().equals(other.getPicWeight()))
            && (this.getPicHigh() == null ? other.getPicHigh() == null : this.getPicHigh().equals(other.getPicHigh()))
            && (this.getPicSuffix() == null ? other.getPicSuffix() == null : this.getPicSuffix().equals(other.getPicSuffix()))
            && (this.getPicLevel() == null ? other.getPicLevel() == null : this.getPicLevel().equals(other.getPicLevel()))
            && (this.getPicFormat() == null ? other.getPicFormat() == null : this.getPicFormat().equals(other.getPicFormat()))
            && (this.getPicPath() == null ? other.getPicPath() == null : this.getPicPath().equals(other.getPicPath()))
            && (this.getPicDesc() == null ? other.getPicDesc() == null : this.getPicDesc().equals(other.getPicDesc()))
            && (this.getPicOrdering() == null ? other.getPicOrdering() == null : this.getPicOrdering().equals(other.getPicOrdering()))
            && (this.getFileKey() == null ? other.getFileKey() == null : this.getFileKey().equals(other.getFileKey()))
            && (this.getFileKeys() == null ? other.getFileKeys() == null : this.getFileKeys().equals(other.getFileKeys()))
            && (this.getSmallPicInfo() == null ? other.getSmallPicInfo() == null : this.getSmallPicInfo().equals(other.getSmallPicInfo()))
            && (this.getViewPoint() == null ? other.getViewPoint() == null : this.getViewPoint().equals(other.getViewPoint()))
            && (this.getScene() == null ? other.getScene() == null : this.getScene().equals(other.getScene()))
            && (this.getRenderingType() == null ? other.getRenderingType() == null : this.getRenderingType().equals(other.getRenderingType()))
            && (this.getSequence() == null ? other.getSequence() == null : this.getSequence().equals(other.getSequence()))
            && (this.getPanoPath() == null ? other.getPanoPath() == null : this.getPanoPath().equals(other.getPanoPath()))
            && (this.getTaskCreateTime() == null ? other.getTaskCreateTime() == null : this.getTaskCreateTime().equals(other.getTaskCreateTime()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getSysCode() == null ? other.getSysCode() == null : this.getSysCode().equals(other.getSysCode()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getModifier() == null ? other.getModifier() == null : this.getModifier().equals(other.getModifier()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getAtt1() == null ? other.getAtt1() == null : this.getAtt1().equals(other.getAtt1()))
            && (this.getAtt2() == null ? other.getAtt2() == null : this.getAtt2().equals(other.getAtt2()))
            && (this.getNuma1() == null ? other.getNuma1() == null : this.getNuma1().equals(other.getNuma1()))
            && (this.getNuma2() == null ? other.getNuma2() == null : this.getNuma2().equals(other.getNuma2()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBusinessId() == null) ? 0 : getBusinessId().hashCode());
        result = prime * result + ((getPicCode() == null) ? 0 : getPicCode().hashCode());
        result = prime * result + ((getPicName() == null) ? 0 : getPicName().hashCode());
        result = prime * result + ((getPicFileName() == null) ? 0 : getPicFileName().hashCode());
        result = prime * result + ((getPicType() == null) ? 0 : getPicType().hashCode());
        result = prime * result + ((getPicSize() == null) ? 0 : getPicSize().hashCode());
        result = prime * result + ((getPicWeight() == null) ? 0 : getPicWeight().hashCode());
        result = prime * result + ((getPicHigh() == null) ? 0 : getPicHigh().hashCode());
        result = prime * result + ((getPicSuffix() == null) ? 0 : getPicSuffix().hashCode());
        result = prime * result + ((getPicLevel() == null) ? 0 : getPicLevel().hashCode());
        result = prime * result + ((getPicFormat() == null) ? 0 : getPicFormat().hashCode());
        result = prime * result + ((getPicPath() == null) ? 0 : getPicPath().hashCode());
        result = prime * result + ((getPicDesc() == null) ? 0 : getPicDesc().hashCode());
        result = prime * result + ((getPicOrdering() == null) ? 0 : getPicOrdering().hashCode());
        result = prime * result + ((getFileKey() == null) ? 0 : getFileKey().hashCode());
        result = prime * result + ((getFileKeys() == null) ? 0 : getFileKeys().hashCode());
        result = prime * result + ((getSmallPicInfo() == null) ? 0 : getSmallPicInfo().hashCode());
        result = prime * result + ((getViewPoint() == null) ? 0 : getViewPoint().hashCode());
        result = prime * result + ((getScene() == null) ? 0 : getScene().hashCode());
        result = prime * result + ((getRenderingType() == null) ? 0 : getRenderingType().hashCode());
        result = prime * result + ((getSequence() == null) ? 0 : getSequence().hashCode());
        result = prime * result + ((getPanoPath() == null) ? 0 : getPanoPath().hashCode());
        result = prime * result + ((getTaskCreateTime() == null) ? 0 : getTaskCreateTime().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getSysCode() == null) ? 0 : getSysCode().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getModifier() == null) ? 0 : getModifier().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getAtt1() == null) ? 0 : getAtt1().hashCode());
        result = prime * result + ((getAtt2() == null) ? 0 : getAtt2().hashCode());
        result = prime * result + ((getNuma1() == null) ? 0 : getNuma1().hashCode());
        result = prime * result + ((getNuma2() == null) ? 0 : getNuma2().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
;
        return result;
    }
    
    /**获取对象的copy**/
    public ResRenderPic copy(){
       ResRenderPic obj =  new ResRenderPic();
       obj.setBusinessId(this.businessId);
       obj.setPicCode(this.picCode);
       obj.setPicName(this.picName);
       obj.setPicFileName(this.picFileName);
       obj.setPicType(this.picType);
       obj.setPicSize(this.picSize);
       obj.setPicWeight(this.picWeight);
       obj.setPicHigh(this.picHigh);
       obj.setPicSuffix(this.picSuffix);
       obj.setPicLevel(this.picLevel);
       obj.setPicFormat(this.picFormat);
       obj.setPicPath(this.picPath);
       obj.setPicDesc(this.picDesc);
       obj.setPicOrdering(this.picOrdering);
       obj.setFileKey(this.fileKey);
       obj.setFileKeys(this.fileKeys);
       obj.setSmallPicInfo(this.smallPicInfo);
       obj.setViewPoint(this.viewPoint);
       obj.setScene(this.scene);
       obj.setRenderingType(this.renderingType);
       obj.setSequence(this.sequence);
       obj.setPanoPath(this.panoPath);
       obj.setTaskCreateTime(this.taskCreateTime);
       obj.setPid(this.pid);
       obj.setSysCode(this.sysCode);
       obj.setCreator(this.creator);
       obj.setGmtCreate(this.gmtCreate);
       obj.setModifier(this.modifier);
       obj.setGmtModified(this.gmtModified);
       obj.setIsDeleted(this.isDeleted);
       obj.setAtt1(this.att1);
       obj.setAtt2(this.att2);
       obj.setNuma1(this.numa1);
       obj.setNuma2(this.numa2);
       obj.setRemark(this.remark);

       return obj;
    }
    
     /**获取对象的map**/
    public Map toMap(){
       Map map =  new HashMap();
       map.put("businessId",this.businessId);
       map.put("picCode",this.picCode);
       map.put("picName",this.picName);
       map.put("picFileName",this.picFileName);
       map.put("picType",this.picType);
       map.put("picSize",this.picSize);
       map.put("picWeight",this.picWeight);
       map.put("picHigh",this.picHigh);
       map.put("picSuffix",this.picSuffix);
       map.put("picLevel",this.picLevel);
       map.put("picFormat",this.picFormat);
       map.put("picPath",this.picPath);
       map.put("picDesc",this.picDesc);
       map.put("picOrdering",this.picOrdering);
       map.put("fileKey",this.fileKey);
       map.put("fileKeys",this.fileKeys);
       map.put("smallPicInfo",this.smallPicInfo);
       map.put("viewPoint",this.viewPoint);
       map.put("scene",this.scene);
       map.put("renderingType",this.renderingType);
       map.put("sequence",this.sequence);
       map.put("panoPath",this.panoPath);
       map.put("taskCreateTime",this.taskCreateTime);
       map.put("pid",this.pid);
       map.put("sysCode",this.sysCode);
       map.put("creator",this.creator);
       map.put("gmtCreate",this.gmtCreate);
       map.put("modifier",this.modifier);
       map.put("gmtModified",this.gmtModified);
       map.put("isDeleted",this.isDeleted);
       map.put("att1",this.att1);
       map.put("att2",this.att2);
       map.put("numa1",this.numa1);
       map.put("numa2",this.numa2);
       map.put("remark",this.remark);

       return map;
    }

    public String getDesignPlanName() {
        return designPlanName;
    }

    public void setDesignPlanName(String designPlanName) {
        this.designPlanName = designPlanName;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

	@Override
	public String toString() {
		return "ResRenderPic{" +
				"id=" + id +
				", businessId=" + businessId +
				", picCode='" + picCode + '\'' +
				", picName='" + picName + '\'' +
				", picFileName='" + picFileName + '\'' +
				", picType='" + picType + '\'' +
				", picSize=" + picSize +
				", picWeight=" + picWeight +
				", picHigh=" + picHigh +
				", picSuffix='" + picSuffix + '\'' +
				", picLevel=" + picLevel +
				", picFormat='" + picFormat + '\'' +
				", picPath='" + picPath + '\'' +
				", picDesc='" + picDesc + '\'' +
				", picOrdering=" + picOrdering +
				", fileKey='" + fileKey + '\'' +
				", fileKeys='" + fileKeys + '\'' +
				", smallPicInfo='" + smallPicInfo + '\'' +
				", viewPoint=" + viewPoint +
				", scene=" + scene +
				", renderingType=" + renderingType +
				", sequence=" + sequence +
				", panoPath='" + panoPath + '\'' +
				", taskCreateTime=" + taskCreateTime +
				", pid=" + pid +
				", sysCode='" + sysCode + '\'' +
				", creator='" + creator + '\'' +
				", gmtCreate=" + gmtCreate +
				", modifier='" + modifier + '\'' +
				", gmtModified=" + gmtModified +
				", isDeleted=" + isDeleted +
				", att1='" + att1 + '\'' +
				", att2='" + att2 + '\'' +
				", numa1=" + numa1 +
				", numa2=" + numa2 +
				", remark='" + remark + '\'' +
				", OriginalPicPath='" + OriginalPicPath + '\'' +
				", baseRenderId=" + baseRenderId +
				", sysTaskPicId=" + sysTaskPicId +
				", panoLevel=" + panoLevel +
				", roam='" + roam + '\'' +
				", designSceneId=" + designSceneId +
				", sourcePlanId=" + sourcePlanId +
				", templateId=" + templateId +
				", fileKeyList=" + fileKeyList +
				", renderTaskType='" + renderTaskType + '\'' +
				", userId=" + userId +
				", productType='" + productType + '\'' +
				", totalFee=" + totalFee +
				", payType='" + payType + '\'' +
				", taskId=" + taskId +
				", recommendIds=" + recommendIds +
				", sceneIds=" + sceneIds +
				", platformId=" + platformId +
				", designPlanName='" + designPlanName + '\'' +
				", spaceType='" + spaceType + '\'' +
				", area='" + area + '\'' +
				", planRecommendedId=" + planRecommendedId +
				", operationSource=" + operationSource +
				", designPlanSceneId=" + designPlanSceneId +
				", createUserId=" + createUserId +
				'}';
	}
}
