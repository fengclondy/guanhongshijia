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

package cn.com.dyninfo.o2o.furniture.web.member.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.dyninfo.o2o.furniture.admin.service.BaseService;
import cn.com.dyninfo.o2o.furniture.web.member.dao.HuiyuanDao;
import cn.com.dyninfo.o2o.furniture.web.member.service.HuiyuanService;
@Service("huiyuanService")
public class HuiyuanServiceImpl extends BaseService implements HuiyuanService{

	    @Resource
	    private HuiyuanDao huiyuanDao;
	    
	    @Override
	    public void initDao(){
	    	super.initDao();
	    	this.baseDao=huiyuanDao;
	    }

		@Override
		public int validate(String sql) {
			return huiyuanDao.validate(sql);
		}
}
