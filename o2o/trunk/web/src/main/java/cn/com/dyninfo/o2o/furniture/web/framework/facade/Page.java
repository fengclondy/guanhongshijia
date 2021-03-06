/*
 * Copyright (c) 2009-2016 SHENZHEN Eternal Dynasty Technology Co.,Ltd.
 * All rights reserved.
 *
 * This file contains valuable properties of  SHENZHEN Eternal Dynasty
 * Technology Co.,Ltd.,  embodying  substantial  creative efforts  and
 * confidential information, ideas and expressions.    No part of this
 * file may be reproduced or distributed in any form or by  any  means,
 * or stored in a data base or a retrieval system,  without  the prior
 * written permission  of  SHENZHEN Eternal Dynasty Technology Co.,Ltd.
 *
 */

package cn.com.dyninfo.o2o.furniture.web.framework.facade;

import cn.com.dyninfo.o2o.furniture.sys.error.ErrorMsg;
import cn.com.dyninfo.o2o.furniture.util.CityTool;
import cn.com.dyninfo.o2o.furniture.util.CookTool;
import cn.com.dyninfo.o2o.furniture.util.ValidationUtil;
import cn.com.dyninfo.o2o.furniture.web.address.service.AreaService;
import cn.com.dyninfo.o2o.furniture.web.framework.context.Context;
import cn.com.dyninfo.o2o.furniture.web.framework.context.SpringContext;
import cn.com.dyninfo.o2o.furniture.web.framework.facade.widget.WidgetXmlUtil;
import cn.com.dyninfo.o2o.furniture.web.goods.model.GoodsSort;
import cn.com.dyninfo.o2o.furniture.web.goods.service.GoodsService;
import cn.com.dyninfo.o2o.furniture.web.goods.service.GoodsSortService;
import cn.com.dyninfo.o2o.furniture.web.page.dao.YqljDao;
import cn.com.dyninfo.o2o.furniture.web.page.model.Yqlj;
import cn.com.dyninfo.o2o.furniture.web.page.service.YqljService;
import freemarker.template.Template;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

public class Page implements IPage{
	private Map pamtr;
	private HttpServletRequest httpRequest;
	/**
	 * 解析页面
	 * @param request
	 * @param response
	 */
	public void parse(HttpServletRequest request,HttpServletResponse response){
		Map data=new HashMap(Context.freeMakerData);
		Object obj=request.getSession().getAttribute(Context.SESSION_AEAR);
		if(obj==null){
			String city=CookTool.getCookIEValue("city", request);
			if(city==null||!city.equals("ALL")){
				if(city==null||city.equals("")){
					String cityName=CityTool.getClientCityId(request);
					AreaService areaService=SpringContext.getBean("areaService");
					if (!ValidationUtil.isEmpty(cityName)){
						List list=areaService.getListByWhere(new StringBuffer(" and n.name='"+cityName+"'"));//+"' and n.isDefault=1 "
						if(list.size()>0){
							data.put("areainfo",list.get(0) );
							request.getSession().setAttribute(Context.SESSION_AEAR, list.get(0));
						}else{
						}
					}
				}else{
					AreaService areaService=SpringContext.getBean("areaService");
					obj=areaService.getObjById(city);
					data.put("areainfo",obj );
					request.getSession().setAttribute(Context.SESSION_AEAR, obj);
				}
			}
		}else {
			data.put("areainfo",obj );
		}
		Set<String> urlkeys=Context.regular.keySet();
		String pageName=request.getServletPath();
		pageName=pageName.substring(pageName.lastIndexOf("/"));
		if(pageName.indexOf("?")>0)
			pageName=pageName.substring(0,pageName.indexOf("?"));
		boolean isPage=false;
		for(String key:urlkeys){
			if(Pattern.matches(key, pageName)){
				pageName=Context.regular.get(key);
				isPage=true;
			}
		}
		if(!isPage)new Exception(); 
		
		pamtr=new HashMap();
		httpRequest=request;
		parsePamtr();
		String url=request.getServletPath();
		String filePath=Context.webPath+Context.tempPath+"/widget.xml";
		Map pageMap=WidgetXmlUtil.parsePage(filePath);//获取挂件


		Map widgetMap=(Map) pageMap.get("common");
		Set<String> keys=widgetMap.keySet();
		for(String key:keys){
			Map attrMap=(Map) widgetMap.get(key);
			String widgetType=(String) attrMap.get("widgetType");
			Widget widget=SpringContext.getBean(widgetType);
			
			attrMap.putAll(pamtr);
			String context=widget.parseWidget(request, response,attrMap);
			
			data.put("widget_"+key, context);
		}
		widgetMap=(Map) pageMap.get(pageName);
		if(widgetMap!=null){
			keys=widgetMap.keySet();
			for(String key:keys){
				Map attrMap=(Map) widgetMap.get(key);
				String widgetType=(String) attrMap.get("widgetType");
				Widget widget=SpringContext.getBean(widgetType);
				attrMap.putAll(pamtr);
				String context="";
				try{
					context=widget.parseWidget(request, response,attrMap);
				}catch(Exception e){
					ErrorMsg.sendMsg(e, request);
				}
				data.put("widget_"+key, context);
				if(widget.getTitle()!=null){
					data.put("title", widget.getTitle());
				}
			}
			
		}else{
			pageName="404.html";
		}
		try{
			Context.freeMakercfg.setServletContextForTemplateLoading(Context.context, Context.tempPath);
			Template t=Context.freeMakercfg.getTemplate(pageName);
			if (request.getSession().getAttribute(Context.SESSION_MEMBER) != null) {
				data.put(Context.SESSION_MEMBER, request.getSession().getAttribute(Context.SESSION_MEMBER));
			}
			GoodsSortService goodsSortService=SpringContext.getBean("goodsSortService");
			GoodsService goodsService=SpringContext.getBean("goodsService");
			YqljService yqljService = SpringContext.getBean("yqljService");
			List<GoodsSort> dataList =(List<GoodsSort>)goodsSortService.getListByWhere(new StringBuffer(" and  n.parent is null"));
			for (int i=0;i<dataList.size();i++){
				for (int j=0;j<dataList.get(i).getChildren().size();j++){
					if(dataList.get(i).getChildren().get(j).getChildren().size()==1){

						int goodsCount=goodsService.getCountByWhere(new StringBuffer(" and n.goodsSort like '%"+dataList.get(i).getGoodsSort_id()+"%' order by n.indexs desc"));
						dataList.get(i).setGoodscount(goodsCount);
						break;
					}else {
						int goodsCount=goodsService.getCountByWhere(new StringBuffer(" and n.goodsSort like '%"+dataList.get(i).getChildren().get(j).getGoodsSort_id()+"%' order by n.indexs desc"));
						dataList.get(i).getChildren().get(j).setGoodscount(goodsCount);
					}

				}
			}
			data.put("sortDataList", dataList);
			List<Yqlj> list = (List<Yqlj>)yqljService.getListByWhere(new StringBuffer("order by n.yqlj_count desc"));
			data.put("yqljList",list);
			t.process(data, response.getWriter());
		}catch(Exception e){
			ErrorMsg.sendMsg(e, request);
			e.printStackTrace();
		}
	}
	public void parsePamtr(){
		Enumeration  el=httpRequest.getParameterNames();
		while(el.hasMoreElements()){
			String key=(String) el.nextElement();
			pamtr.put(key, httpRequest.getParameter(key).replace("'", "").replace("?", ""));
		}
	}
}
