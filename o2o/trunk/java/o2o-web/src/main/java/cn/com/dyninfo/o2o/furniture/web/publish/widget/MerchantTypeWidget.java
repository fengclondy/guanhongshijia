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

package cn.com.dyninfo.o2o.furniture.web.publish.widget;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.com.dyninfo.o2o.furniture.web.framework.facade.Widget;
import cn.com.dyninfo.o2o.furniture.web.publish.service.MerchantTypeService;

@Component("merchantTypeWidget")
public class MerchantTypeWidget extends Widget {

	@Resource
	private MerchantTypeService merchantTypeService;
	
	@Override
	public void display(Map pamtr) {

		List list=merchantTypeService.getListByWhere(new StringBuffer(" and n.status=0 order by n.orderIndex asc "));
		this.putData("data", list);
	}

}
