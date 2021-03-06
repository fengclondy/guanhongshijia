<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/Dress/include/top.jsp" %>
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

<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/Dress/css/jquery.multiselect2side.css" title="win2k-cold-1" />
<script type="text/javascript" src="<%=request.getContextPath()%>/Dress/js/jquery.multiselect2side.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/Dress/shangpin/goodsType/addSpec.js"></script>

<script type="text/javascript">
function submit(){
	if($("#form1").checkall()){
		document.form1.submit();
	}
}
$(function(){
	$("#form1").validate();
	$("#searchablems2side__sx").multiselect2side({
		selectedPosition: 'right',
		moveOptions: false,
		labelsx: '',
		labeldx: '',
		autoSort: true,
		autoSortAvailable: true,
	})
	<c:forEach items="${brandList}" var="brand">
		//.find("option[value='${brand.brand_id}']").attr("selected",true);
	</c:forEach>
})
</script>


<table width="100%" border="1" cellspacing="2" cellpadding="0" class="tab2" height="100%" style="vertical-align:top;">
<tr>
	<td class="tab2_top" >
	</td>
</tr>
<tr>
<td style="border:none; vertical-align:top">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
        <td class="tab2_tou"><a href="javascript:submit();">
        	<img src="<%=request.getContextPath()%>/Dress/img/submit_btn.gif" border="0" /></a></td>
		<td class="tab2_tou" >
			<a href="javascript:location.reload();">
			<img src="<%=request.getContextPath()%>/Dress/img/biao_03.gif" border="0" /></a></td>
		<td class="tab2_tou" >
			<a href="<%=request.getContextPath()%>/html/manage/goodsType/list">
			<img src="<%=request.getContextPath()%>/Dress/img/return_btn.gif" border="0" /></a></td>

<td>&nbsp;</td>
</tr>
</table>

</td>
</tr>
	<tr><td></td></tr>
<tr>
<td style="border:#c5c5c5 solid 1px; vertical-align:top">

<form name="form1" id="form1" action="<%=request.getContextPath() %>/html/manage/goodsType" method="post">
<input type="hidden" name="_method" value="put" />
<input type="hidden" name="goodsType_id" value="${info.goodsType_id }" />
<input type="hidden" name="status" value="0" />
  <table cellspacing="0" cellpadding="0" class="table3_da">
   
  <tr style="height:25px;background-image:url(<%=request.getContextPath()%>/Dress/img/biao_22top.gif); background-repeat:repeat-x;color: #ffffff;">
    <td colspan="4"><strong>编辑</strong></td>
  </tr>
  
   <tr>
      <td class="discription" style="width: 150px;"><span style="color:#ff0000">*</span>商品类型：</td>
  	  <td ><input name="name" id="name" value="${info.name }" class="noNull" msg="商品类型名称不能为空！"/></td>
  </tr>
  <tr>
      <td class="discription" style="width: 150px;"><span style="color:#ff0000">*</span>是否关联品牌：</td>
  	  <td >
  	  		<input type="radio" name="linkBrank" value="1" <c:if test="${info.linkBrank==1 }">checked</c:if>/>是
  	  		<input type="radio" name="linkBrank" value="0" <c:if test="${info.linkBrank==0 }">checked</c:if> />否
  	  </td>
  </tr>
  <tr>
      <td class="discription" style="width: 150px;"><span style="color:#ff0000">*</span>是否拥有属性：</td>
  	  <td >
  	  		<input type="radio" name="ownSpec" value="1"  <c:if test="${info.ownSpec==1 }">checked</c:if>/>是
  	  		<input type="radio" name="ownSpec" value="0"  <c:if test="${info.ownSpec==0 }">checked</c:if> />否
  	  </td>
  </tr>
  <tr id="brandTr">
  	<td class="discription">选择品牌：</td>
  	<td>
  		<select id="searchablems2side__sx" multiple="multiple" size="10" name="brands" >
  			<c:forEach items="${brandList}" var="brand">
  				<c:set var="sss">:${brand.brand_id }:</c:set>
  					<option value='${brand.brand_id }' <c:if test="${fn:contains(info.brandStr,sss) }">selected</c:if>>${brand.name }</option>
  			</c:forEach>
  		</select>
  	</td>
  </tr>
  <tr id="specTr">
  	<td class="discription">添加属性：</td>
  	<td>
  		 <table cellspacing="0" cellpadding="0" class="table3_da">
			  <tr style="height:25px;background-image:url(<%=request.getContextPath()%>/Dress/img/biao_22top.gif); background-repeat:repeat-x;color: #ffffff;">
			    <td >属性名称</td>
			    <td >属性类型</td>
			    <td >属性值</td>
			    <td ><a href="javascript:;" class="addSpec" style="color:#fff">添加</a></td>
			  </tr>
			  <tbody id="specBox">
			  	<c:forEach items="${info.specList}" var="spec">
			  		<tr>
						<td ><input type="text" name="specName" value="${spec.spec_name }" class="noNull" msg="属性名称不能为空！"/></td>
				  		<td >
				  		<select name="specFlag">
				    	<option value="0" <c:if test="${spec.flag==0 }">selected</c:if>>选择项</option>
				    	<option value="1" <c:if test="${spec.flag==1 }">selected</c:if>>输入项</option>
				    	</select>
				    	</td>
				    	<td ><input type="text" name="specVal" value="${spec.valStr }" />&nbsp;多值以,分隔</td>
				    	<td ><a href="javascript:;" class="delSpec" style="color:#ff0000">删除</a></td>
			    	</tr>
			  	</c:forEach>
			  </tbody>
		 </table>
  		
  	</td>
  </tr>
</table>
</form>
</td>
</tr>
</table>
  </body>
</html>
