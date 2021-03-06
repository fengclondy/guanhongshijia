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

package cn.com.dyninfo.o2o.furniture.web.error;

import org.apache.log4j.Logger;

public class GoodsPriceError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void printStackTrace() {
		StackTraceElement[] stacks=new Throwable().getStackTrace();
		StackTraceElement stack=stacks[1];
//		System.out.println("获取商品价格发生错误:"+stack);
	}

}
