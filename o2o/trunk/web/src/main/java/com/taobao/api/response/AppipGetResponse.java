package com.taobao.api.response;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: taobao.appip.get response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class AppipGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2779544499579216672L;

	/** 
	 * ISV发起请求服务器IP
	 */
	@ApiField("ip")
	private String ip;


	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp( ) {
		return this.ip;
	}
	


}
