<%@ page import="cn.com.dyninfo.o2o.furniture.sys.Constants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/admin-inc/top.jsp" %>
<%--
  ~ Copyright (c) 2009-2016 SHENZHEN Eternal Dynasty Technology Co.,Ltd.
  ~ All rights reserved.
  ~
  ~ This file contains valuable properties of  SHENZHEN Eternal Dynasty
  ~ Technology Co.,Ltd.,  embodying  substantial  creative efforts  and
  ~ confidential information, ideas and expressions.    No part of this
  ~ file may be reproduced or distributed in any form or by  any  means,
  ~ or stored in a data base or a retrieval system,  without  the prior
  ~ written permission  of  SHENZHEN Eternal Dynasty Technology Co.,Ltd.
  ~
  --%>

<script type="text/javascript">
	function submit(){
		if($("#form1").checkall()){
		document.form1.submit();
	}
}
$(function(){
	$("#form1").validate();
})
</script>
<table width="100%" border="1" cellspacing="2" cellpadding="0" class="tab2">
<tr>
<td style="border:none; vertical-align:top">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
        <td class="tab2_tou"><a href="javascript:submit();">
        	<img src="<%=request.getContextPath()%>/img/submit_btn.gif" border="0" /></a></td>
		<td class="tab2_tou" >
			<a href="javascript:location.reload();">
			<img src="<%=request.getContextPath()%>/img/biao_03.gif" border="0" /></a></td>
		<td class="tab2_tou" >
					<a href="<%=request.getContextPath()%>/html/manage/sendorder/list">
			<img src="<%=request.getContextPath()%>/img/return_btn.gif" border="0" /></a></td>

<td>&nbsp;</td>
</tr>
</table>

</td>
</tr>

<tr>
<td style="border:#c5c5c5 solid 1px; vertical-align:top">

<form name="form1" id="form1" action="<%=request.getContextPath() %>/html/manage/sendorder" method="post">
<input type="hidden" name="_method" value="put" />
<input type="hidden" name="sendorder_id" value="${ info.sendorder_id }"/>
  <table cellspacing="0" cellpadding="0" class="table3_da">
   
  <tr style="height:25px;background-image:url(<%=request.getContextPath()%>/img/biao_22top.gif); background-repeat:repeat-x;color: #ffffff;">
    <td colspan="4"><strong>修改送货单信息</strong></td>
  </tr>
    <tr class="sangj">
     <td style="width: 20%;" class="discription" >送货单编号:</td>
  	  <td  style="width: 30%;">${ info.sendorder_id }</td>
  	   <td style="width: 20%;" class="discription" >订单编号:</td>
  	  <td style="width: 30%;">${ info.order.order_id }</td>
  </tr>
  <tr class="sangj">
     <td style="width: 20%;" class="discription" >支付方式:</td>
  	  <td  style="width: 30%;">${info.order.playType.name}
		</td>
  	   <td style="width: 20%;" class="discription" >会员名称:</td>
  	  <td style="width: 30%;">${info.order.huiyuan.name }</td>
  </tr>
				<tr class="sangj">
  	   <td style="width: 20%;" class="discription" >收货人:</td>
  	  <td style="width: 30%;"> 
  	   <input name="shouMen" id="shouMen" value="${info.order.shouMen  }" class="noNull" msg="退货人不能为空！"/>
  	  </td>
  </tr>
 </table>
</form>
</td>
</tr>
</table>

