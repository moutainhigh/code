package com.nork.design.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.websocket.ClientEndpoint;

import com.nork.common.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nork.common.model.FileModel;
import com.nork.common.model.LoginUser;
import com.nork.common.properties.AesProperties;
import com.nork.common.properties.ResProperties;
import com.nork.design.dao.DesignPlanAutoRenderMapper;
import com.nork.design.dao.DesignPlanMapper;
import com.nork.design.dao.DesignPlanProductMapper;
import com.nork.design.dao.OptimizePlanMapper;
import com.nork.design.model.DesignPlan;
import com.nork.design.model.DesignRenderRoam;
import com.nork.design.model.RenderConfig;
import com.nork.design.service.DesignPlanAutoRenderResouceService;
import com.nork.design.service.DesignPlanProductService;
import com.nork.design.service.DesignPlanService;
import com.nork.design.service.DesignRenderRoamService;
import com.nork.design.service.DesignTempletProductService;
import com.nork.design.service.DesignTempletService;
import com.nork.design.service.OptimizePlanService;
import com.nork.design.service.UsedProductsService;
import com.nork.home.dao.SpaceCommonMapper;
import com.nork.home.model.SpaceCommon;
import com.nork.product.service.BaseProductService;
import com.nork.render.model.RenderTypeCode;
import com.nork.system.dao.SysDictionaryMapper;
import com.nork.system.model.ResDesign;
import com.nork.system.model.ResRenderPic;
import com.nork.system.model.ResRenderVideo;
import com.nork.system.model.SysDictionary;
import com.nork.system.service.ResDesignService;
import com.nork.system.service.ResFileService;
import com.nork.system.service.ResModelService;
import com.nork.system.service.ResPicService;
import com.nork.system.service.ResRenderPicService;
import com.nork.system.service.ResRenderVideoService;
import com.nork.system.service.ResTextureService;
import com.nork.system.service.SysUserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @version V1.0
 */
@Service
@Transactional
@ClientEndpoint
public class DesignPlanAutoRenderResouceServiceImpl implements DesignPlanAutoRenderResouceService {

	private static Logger logger = Logger.getLogger(DesignPlanAutoRenderResouceServiceImpl.class);
	/*** 获取配置文件 webSocket.properties */
	private final static ResourceBundle webSocket = ResourceBundle.getBundle("config/webSocket");
	private final String SERVERURL = Utils.getValue("app.server.url", "http://localhost:18080/onlineDecorate");
	private final String RESOURCESURL = Utils.getValue("app.resources.url",
			"http://localhost:18080/onlineDecorate/upload");
	private final String ISNEWIMG = Utils.getValue("newImgEnable","1");

	private static String PASSWORD_CRYPT_KEY = Utils.getValueByFileKey(AesProperties.AES,
			AesProperties.AES_RESOURCES_ENCRYPT_KEY_FILEKEY, "41e5c74dd46e4ddcb942dc8ce6224a2e").trim();
	@Resource
	private DesignPlanMapper designPlanMapper;
	@Resource
	private ResDesignService resDesignService;
	@Resource
	private DesignPlanService designPlanService;
	@Autowired
	private DesignRenderRoamService designRenderRoamService;
	@Autowired
	private SpaceCommonMapper spaceCommonMapper;
	@Autowired
	private SysDictionaryMapper sysDictionaryMapper;
	@Autowired
	private OptimizePlanMapper optimizePlanMapper;
	@Autowired
	private OptimizePlanService optimizePlanService;

	/**
	 * 获取数据详情
	 *
	 * @param id
	 * @return DesignPlan
	 */
	@Override
	public DesignPlan get(Integer id) {
		return designPlanMapper.selectByPrimaryKey(id);
	}

	public ResRenderPic assembleResRenderPic(Map map) {
		ResRenderPic renderPic = new ResRenderPic();
		if (map.get(FileModel.PIC_WEIGHT) != null) {
			renderPic.setPicWeight(Integer.getInteger((map.get(FileModel.PIC_WEIGHT).toString())));
		}
		if (map.get(FileModel.PIC_HEIGHT) != null) {
			renderPic.setPicHigh(Integer.getInteger((map.get(FileModel.PIC_HEIGHT).toString())));
		}
		if (map.get(FileModel.FILE_KEY) != null) {
			renderPic.setFileKey(map.get(FileModel.FILE_KEY).toString());
		}
		if (map.get(FileModel.FILE_NAME) != null) {
			renderPic.setPicName(map.get(FileModel.FILE_NAME).toString());
		}
		if (map.get(FileModel.FILE_ORIGINAL_NAME) != null) {
			renderPic.setPicFileName(map.get(FileModel.FILE_ORIGINAL_NAME).toString());
		}
		if (map.get(FileModel.FILE_SUFFIX) != null) {
			renderPic.setPicSuffix(map.get(FileModel.FILE_SUFFIX).toString());
		}
		if (map.get(FileModel.FILE_SIZE) != null) {
			renderPic.setPicSize(Integer.valueOf(map.get(FileModel.FILE_SIZE).toString()));
		}
		if (map.get(FileModel.FORMAT) != null) {
			renderPic.setPicFormat(map.get(FileModel.FORMAT).toString());
		}
		if (map.get(FileModel.FILE_PATH) != null) {
			renderPic.setPicPath(map.get(FileModel.FILE_PATH).toString());
		}
		return renderPic;
	}

	/** 解析固定格式字符串 */
	private Map<String, String> readFileDesc(String fileDesc) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotBlank(fileDesc)) {
			String[] strs = fileDesc.split(";");
			for (String str : strs) {
				if (str.split(":").length == 2) {
					map.put(str.split(":")[0].trim(), str.split(":")[1].trim());
				}
			}
		}
		return map;
	}

	// TODO:增加了一个“720全景图清晰度等级”的参数panoLevel add by yangzhun
	public Integer saveRenderPicOf720(DesignPlan designPlan, Map<String, MultipartFile> fileMap, Integer viewPoint,
			Integer scene, Integer isTurnOn, Integer renderingType, LoginUser loginUser, Integer taskId,
			Integer panoLevel, String roamJson, Integer sourcePlanId, Integer templateId) {
		/* 应用根节点目录 */
		/* 渲染图存放相对路径 */
		String storePath = Utils
				.getPropertyName("config/res", "auto.design.designPlan.render.upload.path", "/auto/design/designPlan/render/")
				.trim();
		/* 渲染图存放绝对路径 */
		storePath = ("linux".equals(FileUploadUtils.SYSTEM_FORMAT) ? storePath : storePath.replace("/", "\\"));
		String uploadRoot = Tools.getRootPath(storePath, "D:\\nork\\Resource");
		String storeRealPath = uploadRoot + storePath;

		Integer resPicId = 0;
		Integer resScreenPicId = null;// 小图回填父id
		Integer smallPicId = null;// 缩略图id
		Integer renderRoamId = null;// 漫游ID。design_render_roam表ID
		ResRenderPic resRenderPic = new ResRenderPic();
		Date date = new Date();
		// 720漫游使用（多720图片上传）
		JSONArray roamArray = new JSONArray();
		Map<String, JSONObject> roamMap = null;
		if (!StringUtils.isEmpty(roamJson) && RenderTypeCode.ROAM_720_LEVEL == renderingType) {
			// 720漫游使用（多720图片上传）
			roamArray = JSONArray.fromObject(roamJson);
			roamMap = getRoamMap(roamArray);
			roamArray = new JSONArray();
		}
		Integer roamMainPicId = null;// 多点漫游，主场景720图片ID
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			String fileName = mf.getOriginalFilename();
			String filedName = mf.getName();
			logger.info("上传文件名称fileName = " + mf.getName() + "   --" + mf.getOriginalFilename());
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String fName = fileName.substring(0, fileName.indexOf("."));

			/* 解决[code]没有替换问题 */
			storePath = storePath.replace("[code]", designPlan.getPlanCode());
			storeRealPath = Utils.replaceDate(storeRealPath.replace("[code]", designPlan.getPlanCode()));
			File f = new File(storeRealPath);
			if (!f.exists()) {
				f.mkdirs();
			}
			String bigFileName = designPlan.getPlanCode() + "_" + Utils.generateRandomDigitString(6) + suffix;
			logger.info("图片路径================" + bigFileName);
			String serverFilePath = "";
			try {
				if ("renderSmallPicOf720".equals(filedName)) {// 保存截图
					Map map = new HashMap();
					if (RenderTypeCode.ROAM_720_LEVEL == renderingType.intValue()) {
						map.put(FileUploadUtils.UPLOADPATHTKEY,
								ResProperties.AUTO_DESIGNPLAN_RENDER_ROAM_SCREEN_PIC_FILEKEY);
					} else {
						map.put(FileUploadUtils.UPLOADPATHTKEY, "auto.design.designPlan.render.upload.path");
					}
					boolean flag = false;
					map.put("code", designPlan.getPlanCode());
					map.put(FileUploadUtils.FILE, mf);

					//是否使用新的算法生成渲染截图 1：是， 其他：否
					if ("1".equals(ISNEWIMG)) {
						flag = ThumbnailUtil.createNewPic(map);
					} else {
						flag = FileUploadUtils.saveFile(map);
					}

					// 720度渲染生成水印图 ->start
					if (RenderTypeCode.COMMON_720_LEVEL == renderingType.intValue()
							|| RenderTypeCode.ROAM_720_LEVEL == renderingType.intValue()) {
						// if(new Integer(4).equals(renderingType)){
						serverFilePath = map.get(FileUploadUtils.SERVER_FILE_PATH).toString();
						try {
							ImageUtils.watermarking2(serverFilePath, scene, 2, isTurnOn);
						} catch (IOException e) {
							logger.error("水印图生成失败:" + e);
						}
					}
					// 720度渲染生成水印图 ->end
					if (flag) {
						ResRenderPic resPic = new ResRenderPic();
						resPic = assembleResRenderPic(map);
						resPic.setPicPath(map.get("dbFilePath") + "");
						resPic.setPicFormat(map.get("format") + "");
						resPic.setPicCode(map.get("code") + "");
						resPic.setPicWeight(Integer.parseInt(map.get("picWeight") + ""));
						resPic.setFileKey(map.get("fileKey") + "");
						resPic.setPicName(map.get("fileName") + "");
						resPic.setPicHigh(Integer.parseInt("" + map.get("picHeight")));
						resPic.setPicType("渲染截图");
						resPic.setRenderingType(renderingType);
						resPic.setSysCode(
								Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
						resPic.setGmtCreate(new Date());
						resPic.setCreator(loginUser.getLoginName());
						resPic.setGmtModified(resPic.getGmtCreate());
						resPic.setModifier(resPic.getCreator());
						resPic.setIsDeleted(0);
						resPic.setBusinessId(designPlan.getId());
						resPic.setRenderingType(renderingType);
						resPic.setTaskCreateTime(date);// 任务创建时间
						resPic.setViewPoint(viewPoint);
						resPic.setScene(scene);
						resPic.setPanoLevel(panoLevel);
						resPic.setSourcePlanId(sourcePlanId);
						resPic.setTemplateId(templateId);
						resRenderPic.setCreator(loginUser.getLoginName());
						resRenderPic.setModifier(loginUser.getLoginName());
//						 resScreenPicId = resRenderPicService.add(resPic);
						resRenderPic.setPlanRecommendedId(sourcePlanId);
						resScreenPicId = this.add(resPic);
						if (RenderTypeCode.ROAM_720_LEVEL == renderingType) {// 720
																				// 漫游的截图id和SysTaskPicId
																				// 一样
							resPicId = resScreenPicId;
							// 保存720漫游记录
							DesignRenderRoam designRenderRoam = new DesignRenderRoam();
							designRenderRoam.setCreator(loginUser.getLoginName());
							designRenderRoam.setGmtCreate(new Date());
							designRenderRoam.setModifier(loginUser.getLoginName());
							designRenderRoam.setGmtModified(designRenderRoam.getGmtCreate());
							designRenderRoam.setIsDeleted(0);
							designRenderRoam.setScreenShotId(resScreenPicId);
							designRenderRoam.setUuid(Utils.getUUID());
							renderRoamId = designRenderRoamService.add(designRenderRoam);
						}
					} else {
						logger.error("方案id:" + designPlan.getId() + "生成720水印图失败");
					}
					if (resPicId != null && resPicId > 0) {
						ResRenderPic rrp = new ResRenderPic();
						rrp.setId(resPicId);
						rrp.setSysTaskPicId(resScreenPicId);
						optimizePlanService.update(rrp);
//						resRenderPicService.update(rrp);
					} else {

					}
				} else {// 保存曲线图
					// 如果为720漫游
					if (RenderTypeCode.ROAM_720_LEVEL == renderingType) {
						resPicId = saveRoam720(resRenderPic, storePath, storeRealPath, mf, bigFileName, viewPoint,
								scene, renderingType, loginUser, designPlan, panoLevel, resScreenPicId, date,sourcePlanId);
						JSONObject roamObj = roamMap.get(filedName);
						if (roamObj.getInt("index") == 1) {// 默认把第一张图片作为主场景
							roamMainPicId = resPicId;
						}
						roamObj.remove("fieldName");
						roamObj.accumulate("fieldName", resPicId);
						roamArray.add(roamObj);
					} else {
						/* 生成渲染图片 */
						//是否使用新的算法生成渲染图 1：是， 其他：否
						if ("1".equals(ISNEWIMG)) {
							ThumbnailUtil.createRoamPic(mf.getInputStream(), new File(storeRealPath, bigFileName),panoLevel);
						} else {
							FileUtils.copyInputStreamToFile(mf.getInputStream(), new File(storeRealPath, bigFileName));
						}

						/* 保存渲染图片 */
						File renderPic = new File(storeRealPath + bigFileName);
						Map map = FileUploadUtils.getMap(renderPic, ResProperties.AUTO_DESIGNPLAN_RENDER_PIC_FILEKEY, false);
						resRenderPic = assembleResRenderPic(map);
						resRenderPic.setIsDeleted(0);
						resRenderPic.setGmtCreate(new Date());
						resRenderPic.setViewPoint(viewPoint);
						resRenderPic.setScene(scene);
						resRenderPic.setRenderingType(renderingType);
						resRenderPic.setCreator(loginUser.getLoginName());
						resRenderPic.setGmtModified(new Date());
						resRenderPic.setModifier(loginUser.getLoginName());
						resRenderPic.setSysCode(
								Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
						resRenderPic.setBusinessId(designPlan.getId());
						resRenderPic.setPicType("3DMax渲染原图");
						resRenderPic.setSourcePlanId(sourcePlanId);
						resRenderPic.setTemplateId(templateId);
						resRenderPic.setTaskCreateTime(date);// 任务创建时间
						resRenderPic.setPlanRecommendedId(sourcePlanId);
						// TODO： 720全景图清晰度等级
						resRenderPic.setPanoLevel(panoLevel);
						if (resScreenPicId != null && resScreenPicId > 0) {
							resRenderPic.setSysTaskPicId(resScreenPicId);// 关联截图信息
							// resPicId = resRenderPicService.add(resRenderPic);
							resPicId = this.add(resRenderPic);// 保存曲线图
						} else {
							resPicId = this.add(resRenderPic);// 保存曲线图
						}
					}
				}

				if (resPicId > 0) {

				} else {
					logger.error("保存渲染图失败,删除已存入的图片信息resPicId=" + resPicId);
					/* 删除目录 */
					logger.warn(Utils.getLineNumber() + "删除目录：");
					/* 删除图片 */
					if (f.exists()) {
						f.delete();
					}
					return resPicId;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				if (f.exists()) {
					f.delete();
				}
				return resPicId;
			}
			// 生成缩略图
			if ("renderSmallPicOf720".equals(filedName) && resScreenPicId != null && resScreenPicId > 0) {// app端传过来的截图才需要生成缩略图。
				try {
					/* 生成缩略图 */
					String smallFileName = Utils.generateRandomDigitString(6) + "_"
							+ Utils.getCurrentDateTime(Utils.DATETIMESSS) + fileName.substring(fileName.indexOf("."));

					String targetSmallFilePath = Utils.replaceDate(storeRealPath) + "small/" + smallFileName;
					try {
//						ResizeImage.createThumbnail(serverFilePath, ("linux".equals(FileUploadUtils.SYSTEM_FORMAT))
//								? targetSmallFilePath : targetSmallFilePath.replaceAll("/", "\\\\"), 280, 215);
						ThumbnailUtil.createThumbnail(serverFilePath,("linux".equals(FileUploadUtils.SYSTEM_FORMAT))
								? targetSmallFilePath : targetSmallFilePath.replaceAll("/", "\\\\"),
								ThumbnailUtil.PIC_WIDTH,ThumbnailUtil.PIC_HIGHT);
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* 保存缩略图 */
					File targetSmallFile = new File(targetSmallFilePath);
					if (!targetSmallFile.exists()) {
						logger.error("路径targetSmallFilePath：" + targetSmallFilePath + "不存在");
					}
					Map smallFileMap = FileUploadUtils.getMap(targetSmallFile, // 缩略图file_key此时根本不是DESIGNPLAN_RENDER_PIC_SMALL_FILEKEY
							ResProperties.AUTO_DESIGNPLAN_RENDER_PIC_SMALL_FILEKEY, false);
					ResRenderPic smallRenderResPic = assembleResRenderPic(smallFileMap);
					smallRenderResPic.setIsDeleted(0);
					smallRenderResPic.setGmtCreate(new Date());
					smallRenderResPic.setPicPath(smallFileMap.get(FileModel.FILE_PATH).toString());
					smallRenderResPic.setCreator(loginUser.getLoginName());
					smallRenderResPic.setGmtModified(new Date());
					smallRenderResPic.setRenderingType(renderingType);
					smallRenderResPic.setModifier(loginUser.getLoginName());
					smallRenderResPic.setSysCode(
							Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
					smallRenderResPic.setBusinessId(designPlan.getId());
					smallRenderResPic.setPicType("3DMax渲染图-缩略图");
					smallRenderResPic.setViewPoint(viewPoint);
					smallRenderResPic.setScene(scene);
					smallRenderResPic.setPid(resPicId);
					smallRenderResPic.setTaskCreateTime(date);// 任务创建时间
					smallRenderResPic.setSourcePlanId(sourcePlanId);
					smallRenderResPic.setTemplateId(templateId);
					smallRenderResPic.setPlanRecommendedId(sourcePlanId);
					// TODO： 720全景图清晰度等级
					smallRenderResPic.setPanoLevel(panoLevel);
					if (resScreenPicId != null && resScreenPicId > 0) {
						smallRenderResPic.setSysTaskPicId(resScreenPicId);
						smallPicId = this.add(smallRenderResPic);
					} else {
						// smallPicId =
						// resRenderPicService.add(smallRenderResPic);
						smallPicId = this.add(smallRenderResPic);
					}

					if (smallPicId != null) {

					} else {
						logger.error("保存渲染缩略图异常,删除已存入的图片信息smallResPicId=" + smallPicId);
						/* 删除目录 */
						logger.warn(Utils.getLineNumber() + "删除目录：");
						/* 删除图片 */
						if (f.exists()) {
							f.delete();
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					if (f.exists()) {
						f.delete();
					}
					return resPicId;
				}
			}

		}
		if (RenderTypeCode.ROAM_720_LEVEL == renderingType) {
			// 把多张720图片的关联关系存到漫游表中
			/*
			 * ResRenderPic roamRenderPic = new ResRenderPic();
			 * roamRenderPic.setId(roamMainPicId);
			 * roamRenderPic.setRoam(roamArray.toString());
			 * resRenderPicService.update(roamRenderPic);
			 */
			DesignRenderRoam designRenderRoam = new DesignRenderRoam();
			designRenderRoam.setId(renderRoamId);
			// 关联关系保存到文件中
			int resFileId = saveRenderRoamConfig(loginUser, roamArray.toString(), renderRoamId);
			designRenderRoam.setRoamConfig(resFileId);
			designRenderRoamService.update(designRenderRoam);
			return roamMainPicId;
		}
		return resPicId;
	}

	/**
	 * 将720漫游的位置关系保存到文件中
	 * 
	 * @param context
	 * @return
	 */
	public Integer saveRenderRoamConfig(LoginUser loginUser, String context, Integer renderRoamId) {
		Integer roamConfigId = 0;
		if (StringUtils.isNotBlank(context)) {
			// 保存文件
			String roamConfigPath = Utils.getPropertyName("config/res",
					"auto.design.designPlan.render.roam.config.upload.path", "");
			String filePath = FileUploadUtils.UPLOAD_ROOT + roamConfigPath;
			String fileName = Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6)
					+ ".txt";
			boolean flag = FileUploadUtils.writeTxtFile(Utils.replaceDate(filePath) + fileName, context);
			if (flag) {
				File file = new File(Utils.replaceDate(filePath) + fileName);
				Map<String, String> map = FileUploadUtils.getMap(file,
						"auto.design.designPlan.render.roam.config.upload.path", true);
				ResDesign resDesign = new ResDesign();
				resDesign.setFileCode(map.get(FileModel.FILE_NAME));
				resDesign.setFileName(map.get(FileModel.FILE_NAME));
				resDesign.setFileOriginalName(map.get(FileModel.FILE_ORIGINAL_NAME));
				resDesign.setFileType("720漫游位置关系文件");
				resDesign.setFileSize(map.get(FileModel.FILE_SIZE));
				resDesign.setFileSuffix(map.get(FileModel.FILE_SUFFIX));
				resDesign.setFilePath(map.get(FileModel.FILE_PATH));
				resDesign.setFileKey(map.get(FileModel.FILE_KEY));
				resDesign.setBusinessId(renderRoamId);

				resDesign.setSysCode(map.get(FileModel.FILE_NAME));
				resDesign.setCreator(loginUser.getName());
				resDesign.setModifier(loginUser.getName());
				resDesign.setGmtCreate(new Date());
				resDesign.setGmtModified(resDesign.getGmtCreate());
				resDesign.setIsDeleted(0);
				// TODO 是否将数据插入一键生成配置文件表 
//				roamConfigId = optimizePlanService.add(resDesign);
			    roamConfigId = resDesignService.add(resDesign);
			}
		}
		return roamConfigId;
	}

	/**
	 * 720漫游
	 * 
	 * @return
	 */
	public Map<String, JSONObject> getRoamMap(JSONArray roamArray) {
		Map<String, JSONObject> roamMap = new HashMap<>();
		if (roamArray != null && roamArray.size() > 0) {
			for (Object roam : roamArray) {
				if (roam != null) {
					JSONObject roamJ = (JSONObject) roam;
					String filedName = roamJ.getString("fieldName");
					roamMap.put(filedName, roamJ);
				}
			}
		}
		return roamMap;
	}

	@Override
	public Integer saveRenderPicOfPhoto(DesignPlan designPlan, Map<String, MultipartFile> fileMap, Integer viewPoint,
			Integer scene, Integer isTurnOn, Integer renderingType, String level, LoginUser loginUser, Integer taskId,
			Integer sourcePlanId, Integer templateId) {
		String fileIds = "";
		String fileName = null;
		String code = "code";
		if (designPlan != null) {
			code = designPlan.getPlanCode();
		}
		List<Map> list = new ArrayList<Map>();
		Integer renderSuccess = 0;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			logger.info("文件上传名称：" + fileName);
			// response.setContentType("text/html;charset=utf-8");
			// 获取文件列表并将物理文件保存到服务器中

			// filePath设置到model对象中，由model存入数据库中
			Map map = new HashMap();
			map.put(FileUploadUtils.UPLOADPATHTKEY, "auto.design.designPlan.render.upload.path");
			boolean flag = false;
			map.put("code", code);
			map.put(FileUploadUtils.FILE, mf);

			//是否使用新的算法生成渲染图 1：是， 其他：否
			if ("1".equals(ISNEWIMG)) {
				flag = ThumbnailUtil.createNewPic(map);
			} else {
				flag = FileUploadUtils.saveFile(map);
			}

			String serverFilePath = map.get(FileUploadUtils.SERVER_FILE_PATH).toString();
			logger.info("追加渲染图水印,serverFilePath=" + serverFilePath);
			try {
				ImageUtils.watermarking2(serverFilePath, scene, 2, isTurnOn);
			} catch (IOException e) {
				logger.error("水印图生成失败");
				e.printStackTrace();
			}
			// 生成图片,支持多张,需写到server
			if (flag) {
				/**
				 * TODO 获取上传方式。1：上传到web服务器；2：上传到ftp；3：同时上传到web服务器和ftp。如果没有取到值， 则上传到web服务器。
				 **/
				Integer ftpUploadMethod = Integer.valueOf(FileUploadUtils.FTP_UPLOAD_METHOD);
				String finalFlieName = (String) map.get("finalFlieName");
				String dbFilePath = (String) map.get(FileUploadUtils.DB_FILE_PATH);
				String ftpFilePath = (String) map.get("filePath");
				// 上传方式为2或者3都需要上传到ftp
				boolean uploadFtpFlag = false;
				if (ftpUploadMethod == 2 || ftpUploadMethod == 3) {
					uploadFtpFlag = FtpUploadUtils.uploadFile(finalFlieName, serverFilePath, ftpFilePath);
				}
				// 生成缩略图
				try {
					String smallFileName = Utils.generateRandomDigitString(6) + "_"
							+ Utils.getCurrentDateTime(Utils.DATETIMESSS) + fileName.substring(fileName.indexOf("."));
					String storePath = Utils
							.getPropertyName("config/res", map.get(FileUploadUtils.UPLOADPATHTKEY) + "",
									"/auto/design/designPlan/[code]/render/")
							.replace("[code]", code) + "/small/" + smallFileName;
					storePath = Utils.replaceDate(storePath);
					/*
					 * String targetSmallFilePath = Tools.getRootPath(storePath,"D:\\app") +
					 * storePath;
					 */
					String targetSmallFilePath = Utils.getAbsolutePath(storePath, null);
					if ("windows".equals(Utils.getValue("app.system.format", "linux").trim())) {
						targetSmallFilePath = targetSmallFilePath.replace("/", "\\");
					}
//					ResizeImage.createThumbnail(serverFilePath, targetSmallFilePath, 280, 215);
					ThumbnailUtil.createThumbnail(serverFilePath,targetSmallFilePath,ThumbnailUtil.PIC_WIDTH,ThumbnailUtil.PIC_HIGHT);
					File targetSmallFile = new File(targetSmallFilePath);
					Map smallFileMap = FileUploadUtils.getMap(targetSmallFile, "auto.design.designPlan.render.upload.path",
							false);
					map.put("viewPoint", viewPoint);
					map.put("scene", scene);

					smallFileMap.put("original", map);
					/* 传递渲染图的视角和渲染图场景 */
					smallFileMap.put("viewPoint", viewPoint);
					smallFileMap.put("scene", scene);
					list.add(smallFileMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 如果上传方式为2，则删除掉临时上传的文件
				if (ftpUploadMethod == 2) {
					if (uploadFtpFlag) {
						// 删除本地
						FileUploadUtils.deleteFile(dbFilePath);
					}
				}
			}
		}
		// 将图片信息记录到数据库中
		logger.info("designPlan.getId()=" + designPlan.getId());
		renderSuccess = this.savePlanRenderPicOfPhot(designPlan.getId(), list, level, renderingType, sourcePlanId,
				templateId);
		return renderSuccess;
	}

	public Integer savePlanRenderPicOfPhot(Integer planId, List<Map> list, String level, Integer renderingType,
			Integer sourcePlanId, Integer templateId) {

		// Integer smallPicId=0;
		int original = 0;
		int small = 0;
		Date date = new Date();

		if (planId != null) {
			DesignPlan designPlan = optimizePlanService.getPlan(Integer.valueOf(planId));
			ResRenderPic smallRenderPic = null;
			ResRenderPic renderPic = null;
			for (Map smallRenderPicMap : list) {
				// 保存渲染图原图
				Map renderPicMap = (Map) smallRenderPicMap.get("original");
				renderPic = assembleResPicAuto(renderPicMap);
				renderPic.setIsDeleted(0);
				renderPic.setCreator(designPlan.getCreator());
				renderPic.setModifier(designPlan.getCreator());
				renderPic.setBusinessId(planId);
				// renderPic.setPicLevel(level);
				renderPic.setRenderingType(renderingType);
				renderPic.setPicType("照片级原图");
				renderPic.setFileKey(ResProperties.AUTO_DESIGNPLAN_RENDER_PIC_FILEKEY);
				renderPic.setTaskCreateTime(date);
				renderPic.setSourcePlanId(sourcePlanId);
				renderPic.setTemplateId(templateId);
				renderPic.setPlanRecommendedId(sourcePlanId);
				logger.info("addrenderPic=" + planId);
				// TODO
				original = this.add(renderPic);

				logger.info("endrenderPic=" + original);
				smallRenderPic = assembleResPicAuto(smallRenderPicMap);
				smallRenderPic.setPid(original);
				smallRenderPic.setCreator(designPlan.getCreator());
				smallRenderPic.setIsDeleted(0);
				smallRenderPic.setModifier(designPlan.getCreator());
				smallRenderPic.setBusinessId(planId);
				// smallRenderPic.setPicLevel(level);
				smallRenderPic.setFileKey(ResProperties.AUTO_DESIGNPLAN_RENDER_PIC_SMALL_FILEKEY);
				smallRenderPic.setPicType("照片级缩略图");
				smallRenderPic.setRenderingType(renderingType);
				smallRenderPic.setTaskCreateTime(date);
				smallRenderPic.setSourcePlanId(sourcePlanId);
				smallRenderPic.setTemplateId(templateId);
				smallRenderPic.setPlanRecommendedId(sourcePlanId);
				// 保存缩略图
				logger.info("add_small=" + original);
				// TODO
				small = this.add(smallRenderPic);
				logger.info("end_small=" + small);
			}
		} else {
			logger.error("planId is null!");
		}
		logger.info("small=" + small);
		if (small > 0 && original > 0) {
			return original;
		} else {
			return original;
		}
	}

	/**
	 * 组装resPic
	 * 
	 * @param map
	 * @return
	 */
	public ResRenderPic assembleResPicAuto(Map map) {
//		ResPic resPic = new ResPic();
		ResRenderPic resPic = new ResRenderPic();
		String dbFilePath = Utils.dealWithPath(map.get(FileUploadUtils.DB_FILE_PATH).toString(), "linux");
		/*String dbFilePath = map.get(FileUploadUtils.DB_FILE_PATH).toString();*/
		resPic.setSysCode(Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
		resPic.setPicCode(resPic.getSysCode());
		resPic.setGmtCreate(new Date());
		resPic.setGmtModified(new Date());
		resPic.setIsDeleted(0);
		resPic.setPicName(map.get(FileModel.FILE_ORIGINAL_NAME)==null?"":map.get(FileModel.FILE_ORIGINAL_NAME).toString());
		resPic.setPicSize(map.get(FileModel.FILE_SIZE)==null?-1:Integer.valueOf(map.get(FileModel.FILE_SIZE).toString()));
		resPic.setPicSuffix(map.get(FileModel.FILE_SUFFIX).toString());
		resPic.setPicFileName(dbFilePath.substring(dbFilePath.lastIndexOf("/") + 1, dbFilePath.lastIndexOf(".")));
		resPic.setPicPath(dbFilePath);
		resPic.setFileKey(map.get(FileModel.FILE_KEY).toString());
		resPic.setPicType("无");
		resPic.setViewPoint(Integer.parseInt(map.get("viewPoint").toString()));
		resPic.setScene(Integer.parseInt(map.get("scene").toString()));
		return resPic;
	}

	/**
	 * 保存720渲染视频 add by yangzhun
	 * 
	 * @param designPlan
	 * @param fileMap
	 * @param renderingType
	 * @param loginUser
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Integer saveRenderPicOfVideo(DesignPlan designPlan, Map<String, MultipartFile> fileMap,
			Integer renderingType, LoginUser loginUser, Integer taskId, Integer sourcePlanId, Integer templateId) {
		// 应用根节点目录
		// 渲染视频存放相对路径
		String storePath = Utils.getPropertyName("config/res", "auto.design.designPlan.render.video.upload.path",
				"/AA/d_userdesign_auto/[yyyy]/[MM]/[dd]/[HH]/design/designPlan/render/video/").trim();
		String storePath2 = Utils.getPropertyName("config/res", "auto.design.designPlan.render.video.cover.upload.path",
				"/AA/d_userdesign_auto/[yyyy]/[MM]/[dd]/[HH]/design/designPlan/render/video/cover/").trim();
		String uploadRoot = Tools.getRootPath(storePath, "D:\\nork\\Resource");
		// 渲染视频存放绝对路径
		String storeRealPath = uploadRoot
				+ ("linux".equals(FileUploadUtils.SYSTEM_FORMAT) ? storePath : storePath.replace("/", "\\"));

		ResRenderVideo resRenderVideo = null;// 720渲染视频
		ResRenderPic resRenderPic = null;// 720视频封面图
		Integer resVideoId = null;// 渲染视频Id
		Integer resPicId = null;// 封面图id
		Date date = new Date();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			String fileName = mf.getOriginalFilename();
			String filedName = mf.getName();
			logger.info("上传文件名称fileName = " + filedName + "   --" + fileName);
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			// String fName = fileName.substring(0, fileName.indexOf("."));
			// 解决[code]没有替换问题
			storePath = storePath.replace("[code]", designPlan.getPlanCode());
			storePath2 = storePath2.replace("[code]", designPlan.getPlanCode());
			storeRealPath = storeRealPath.replace("[code]", designPlan.getPlanCode());// 替换code
			// 生成一个随机的文件名：设计方案编码 + _ + 6位的随机数 + 后缀
			String bigFileName = designPlan.getPlanCode() + "_" + Utils.generateRandomDigitString(6) + "_" + fileName;
			String serverFilePath = "";// 定义一个服务器保存文件的路径

			// 保存720渲染视频
			if ("renderSmallPicOfVideo".equals(filedName)) {
				Map map = new HashMap();
				map.put(FileUploadUtils.UPLOADPATHTKEY, "auto.design.designPlan.render.video.upload.path");
				boolean flag = false;
				map.put("code", designPlan.getPlanCode());
				map.put(FileUploadUtils.FILE, mf);
				flag = FileUploadUtils.saveVideoFile(map);// 上传文件
				if (flag) {
					// 保存好视频文件后，把视频文件保存到数据库
					resRenderVideo = this.assembleResRenderVideo(map);// 将map中的值set到resRenderVideo中
					resRenderVideo.setIsDeleted(0);
					resRenderVideo.setVideoPath((String) map.get(FileUploadUtils.DB_FILE_PATH));
					resRenderVideo.setGmtCreate(new Date());
					resRenderVideo.setRenderingType(renderingType);
					resRenderVideo.setCreator(loginUser == null ? "" : loginUser.getLoginName());
					resRenderVideo.setGmtModified(new Date());
					resRenderVideo.setModifier(loginUser == null ? "" : loginUser.getLoginName());
					// 随机生成系统编码作为sys_code和video_code
					String sysCode = Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_"
							+ Utils.generateRandomDigitString(6);
					resRenderVideo.setVideoCode(sysCode);
					resRenderVideo.setSysCode(sysCode);
					resRenderVideo.setBusinessId(designPlan.getId());
					resRenderVideo.setVideoType("720渲染漫游视频");
					resRenderVideo.setTaskCreateTime(date);// 任务创建时间
					resRenderVideo.setSourcePlanId(sourcePlanId);
					resRenderVideo.setTemplateId(templateId);
					// resVideoId = resRenderVideoService.add(resRenderVideo);//
					// 保存视频文件到数据库中
					resVideoId = optimizePlanService.add(resRenderVideo);
					logger.info(resVideoId + ">0,保存成功");
				}
			}
			if ("renderSmallPicOfVideoCover".equals(filedName)) {
				Map map = new HashMap();
				map.put(FileUploadUtils.UPLOADPATHTKEY, "auto.design.designPlan.render.video.cover.upload.path");
				boolean flag = false;
				map.put("code", designPlan.getPlanCode());
				map.put(FileUploadUtils.FILE, mf);

				//是否使用新的算法生成渲染图 1：是， 其他：否
				if ("1".equals(ISNEWIMG)) {
					flag = ThumbnailUtil.createNewPic(map);
				} else {
					flag = FileUploadUtils.saveFile(map);
				}

				// 文件保存成功后给封面图追加水印
				if (flag) {
					serverFilePath = map.get(FileUploadUtils.SERVER_FILE_PATH).toString();// 获取封面图的服务器保存路径
					try {
						ImageUtils.watermarking2(serverFilePath, null, 2, null);// 给封面图加水印
					} catch (IOException e) {
						logger.error("水印图生成失败");
						e.printStackTrace();
					}
					// 保存好封面图文件后，把封面图保存在数据库
					resRenderPic = this.assembleResRenderPic(map);// 将map中的值set到resRenderPic中
					resRenderPic.setIsDeleted(0);
					resRenderPic.setPicPath((String) map.get(FileUploadUtils.DB_FILE_PATH));
					resRenderPic.setGmtCreate(new Date());
					resRenderPic.setRenderingType(renderingType);
					resRenderPic.setCreator(loginUser == null ? "" : loginUser.getLoginName());
					resRenderPic.setGmtModified(new Date());
					resRenderPic.setModifier(loginUser == null ? "" : loginUser.getLoginName());
					// 随机生成系统编码作为sys_code和pic_code
					String sysCode = Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_"
							+ Utils.generateRandomDigitString(6);
					resRenderPic.setPicCode(sysCode);
					resRenderPic.setSysCode(sysCode);
					resRenderPic.setBusinessId(designPlan.getId());
					resRenderPic.setPicType("720渲染视频封面");
					resRenderPic.setTaskCreateTime(date);// 任务创建时间
					resRenderPic.setSourcePlanId(sourcePlanId);
					resRenderPic.setTemplateId(templateId);
					resRenderPic.setPlanRecommendedId(sourcePlanId);
					resPicId = this.add(resRenderPic);// 保存视频的封面图到数据库中
					// resPicId = resRenderPicService.add(resRenderPic);//
					// 保存视频的封面图到数据库中


					// 将封面图Id赋给video的sysTaskVideoId
					if (resPicId != null && resPicId > 0) {
						ResRenderVideo resRenderVideo2 = new ResRenderVideo();
						resRenderVideo2.setId(resVideoId);
						resRenderVideo2.setSysTaskPicId(resPicId);// 关联封面图信息
						//	TODO 
						optimizePlanService.update(resRenderVideo2);
						//resRenderVideoService.update(resRenderVideo2);
						logger.error("漫游视频关联封面图成功");
					}
				}
			}
		}
		return resVideoId;
	}

	/**
	 * 将map中的值赋给resRenderVideo对象
	 * 
	 * @param map
	 * @return
	 */
	private ResRenderVideo assembleResRenderVideo(Map map) {
		ResRenderVideo resRenderVideo = new ResRenderVideo();
		if (map.get(FileModel.FILE_KEY) != null) {
			resRenderVideo.setFileKey(map.get(FileModel.FILE_KEY).toString());
		}
		if (map.get(FileModel.FILE_NAME) != null) {
			resRenderVideo.setVideoName(map.get(FileModel.FILE_NAME).toString());
		}
		if (map.get(FileModel.FILE_ORIGINAL_NAME) != null) {
			resRenderVideo.setVideoFileName(map.get(FileModel.FILE_ORIGINAL_NAME).toString());
		}
		if (map.get(FileModel.FILE_SUFFIX) != null) {
			resRenderVideo.setVideoSuffix(map.get(FileModel.FILE_SUFFIX).toString());
		}
		if (map.get(FileModel.FILE_SIZE) != null) {
			resRenderVideo.setVideoSize(Integer.valueOf(map.get(FileModel.FILE_SIZE).toString()));
		}
		if (map.get(FileModel.FORMAT) != null) {
			resRenderVideo.setVideoFormat(map.get(FileModel.FORMAT).toString());
		}
		return resRenderVideo;
	}

	/**
	 * 保存720漫游图片
	 * 
	 * @param resRenderPic
	 * @param storePath
	 * @param storeRealPath
	 * @param mf
	 * @param bigFileName
	 * @param viewPoint
	 * @param scene
	 * @param renderingType
	 * @param loginUser
	 * @param designPlan
	 * @param panoLevel
	 * @param resScreenPicId
	 * @param date
	 * @return
	 */
	public int saveRoam720(ResRenderPic resRenderPic, String storePath, String storeRealPath, MultipartFile mf,
			String bigFileName, Integer viewPoint, Integer scene, Integer renderingType, LoginUser loginUser,
			DesignPlan designPlan, Integer panoLevel, Integer resScreenPicId, Date date,Integer sourcePlanId) {
		int resPicId = 0;
		try {
			/* 生成渲染图片 */
			storeRealPath = FileUploadUtils.UPLOAD_ROOT
					+ Utils.getPropertyName("config/res", ResProperties.AUTO_DESIGNPLAN_RENDER_ROAM_PIC_FILEKEY,
							"/AA/d_userdesign_auto/[yyyy]/[MM]/[dd]/[HH]/design/designPlan/render/roam/config/");
			storeRealPath = Utils.replaceDate(storeRealPath);
			//是否使用新的算法生成渲染图 1：是， 其他：否
			if ("1".equals(ISNEWIMG)) {
				ThumbnailUtil.createRoamPic(mf.getInputStream(), new File(storeRealPath, bigFileName),panoLevel);
			} else {
				FileUtils.copyInputStreamToFile(mf.getInputStream(), new File(storeRealPath, bigFileName));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			return resPicId;
		}
		/* 保存渲染图片 */
		File renderPic = new File(storeRealPath + bigFileName);
		Map map = FileUploadUtils.getMap(renderPic, ResProperties.AUTO_DESIGNPLAN_RENDER_ROAM_PIC_FILEKEY, false);
		resRenderPic = assembleResRenderPic(map);
		resRenderPic.setIsDeleted(0);
		// 随机生成系统编码作为sys_code和pic_code
		String sysCode = Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6);
		resRenderPic.setPicCode(sysCode);
		resRenderPic.setGmtCreate(new Date());
		resRenderPic.setViewPoint(viewPoint);
		resRenderPic.setScene(scene);
		resRenderPic.setRenderingType(renderingType);
		resRenderPic.setCreator(loginUser.getLoginName());
		resRenderPic.setGmtModified(new Date());
		resRenderPic.setModifier(loginUser.getLoginName());
		resRenderPic.setSysCode(sysCode);
		resRenderPic.setBusinessId(designPlan.getId());
		resRenderPic.setPicType("3DMax渲染原图");
		resRenderPic.setTaskCreateTime(date);// 任务创建时间
		// TODO： 720全景图清晰度等级
		resRenderPic.setPanoLevel(panoLevel);
		resRenderPic.setPlanRecommendedId(sourcePlanId);
		if (resScreenPicId != null && resScreenPicId > 0) {
			resRenderPic.setSysTaskPicId(resScreenPicId);// 关联截图信息
			resPicId = this.add(resRenderPic);// 保存曲线图
		} else {
			// resPicId = resRenderPicService.add(resRenderPic);// 保存曲线图
			resPicId = this.add(resRenderPic);// 保存曲线图
		}
		return resPicId;
	}

	/**
	 * 新增数据
	 *
	 * @param resRenderPic
	 * @return int
	 */
	@Override
	public int add(ResRenderPic resRenderPic) {
		if (null == resRenderPic || null == resRenderPic.getBusinessId()
				|| resRenderPic.getBusinessId().intValue() <= 0) {
			logger.error("resRenderPic is null=" + resRenderPic.getBusinessId());
			return 0;
		}
			
//		DesignPlan designPlan = designPlanMapper.selectByPrimaryKey(resRenderPic.getBusinessId());
		DesignPlan designPlan = optimizePlanMapper.selectByPrimaryKeyPlan(resRenderPic.getBusinessId());
		if (designPlan == null) {
			logger.error("do not find object for id=" + resRenderPic.getBusinessId());
			return 0;
		}

		resRenderPic.setDesignSceneId(designPlan.getDesignSceneId());
		resRenderPic.setDesignPlanName(designPlan.getPlanName());
		SpaceCommon spaceCommon = spaceCommonMapper.selectByPrimaryKey(designPlan.getSpaceCommonId());
		logger.info("spaceCommon=" + (spaceCommon == null));

		if (spaceCommon != null) {
			resRenderPic.setArea(spaceCommon.getSpaceAreas());// 空间面积大小
			SysDictionary query = new SysDictionary();
			query.setType(Constants.SYS_DICTIONARY_TYPE_HOUSETYPE);
			query.setValue(spaceCommon.getSpaceFunctionId());
			SysDictionary sysDictionary = sysDictionaryMapper.getNameByType(query);
			logger.info("sysDictionary=" + (sysDictionary != null));
			if (sysDictionary != null)
				resRenderPic.setSpaceType(sysDictionary.getName());// 设置房屋空间类型
		}
		resRenderPic.setCreateUserId(designPlan.getUserId());
		optimizePlanMapper.insertResRenderPicOnekey(resRenderPic);
		return resRenderPic.getId();
	}
}
