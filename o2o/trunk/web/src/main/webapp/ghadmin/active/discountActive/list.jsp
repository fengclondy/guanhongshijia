<%@ page import="cn.com.dyninfo.o2o.furniture.sys.Constants" %>
<%@ page language="java" pageEncoding="UTF-8"%>
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
	var i=1;

	function allChoose(){
		$("[name='list']").attr("checked",'true');
	}
	
	function allCancel(){
		$("[name='list']").removeAttr("checked");
	}
	function xz(){
		if(i==1){
			allChoose();
			i=0;
		}else{
			allCancel();
			i=1;
		}
	}
	function ok(url){
		if(confirm("请确认是否启用该方案?")){
			window.location=url;
		}
	}
	function delall(){
		if(confirm("请确认是否执行删除操作!"))
			document.getElementById("del").submit();
	}
	function delUrl(url){
		if(confirm("请确认是否执行删除操作!")){
			window.location=url;
		}
	}
	function cz(){
		document.getElementById("czlist").submit();
	}
</script>




<table cellspacing="2" cellpadding="0" class="tab2">
	<tr>
		<td class="tab2_top">
			
		</td>
	</tr>
	<tr>
		<td>
			<form method="post" id="czlist" name="form1" action="<%=request.getContextPath()%>/html/manage/discountActive/list">
				<input type="hidden" name="pageNo" id="pageNo" value=${PAGE_INFO.pageNo } />
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="tab2_tou">
							<a href="<%=request.getContextPath()%>/html/manage/discountActive/disAdd">
								<img src="<%=request.getContextPath()%>/img/biao_07.gif" border="0" />
							</a>
						</td>
						<td class="tab2_tou">
							<a href="<%=request.getContextPath()%>/html/manage/discountActive/list">
								<img src="<%=request.getContextPath()%>/img/biao_03.gif" border="0" />
							</a>
						</td>
						<td class="chazhaofanshi1">活动主题：</td>
						<td><input  name="title" type="text" style="color:#494949" value="${title }"/></td>
						<td class="tab2_tou">
							<a href="javascript:cz();"><img src="<%=request.getContextPath()%>/img/222.gif" border="0" /></a>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr><td></td></tr>
	<tr>
		<td>
			<form method="post" id="del" action="<%=request.getContextPath()%>/html/manage/discountActive/delall">
				<input type="hidden" name="_method" value="delete" />
				<table cellspacing="0" cellpadding="0" class="table4_da">
					<thead>
					<tr>
						<td style="width:30px;"><input name="" type="checkbox" value="" onclick="xz();" /></td>
							<td>活动主题</td>
							<td>开始时间</td>
							<td>结束时间</td>
							<td>商品数量</td>
							<td>活动连接地址</td>
							<td>操作</td>
					</tr>
					</thead>
					<c:forEach var='info' items='${DATA}' varStatus='index'>
						 <tr>
							    <td><input id="list" name="list" type="checkbox" value="${info.active_id}" /></td>
							   <td class="td_a">${info.name}</td>
							   <td class="td_a">${info.bdate}&nbsp;${info.btime}</td>
							   <td class="td_a">${info.edate}&nbsp;${info.etime}</td>
							   <td class="td_a">${info.count}</td>
							   <td class="td_a"><%=request.getContextPath()%>/<%=Constants.ADMIN_ADDRESS%>/active-${info.active_id }.html</td>
								<td>
								<a href="<%=request.getContextPath()%>/html/manage/discountActive/${info.active_id}/show" class="zhu2">
									查看</a>&nbsp;
								<c:if test="${empty merchants||(info.role=='MERCHANTS')}">	
									<a href="<%=request.getContextPath()%>/html/manage/discountActive/${info.active_id}/disUpdate" class="zhu2">编辑</a>&nbsp;
									<a href="javascript:delUrl('<%=request.getContextPath()%>/html/manage/discountActive/${info.active_id}/del')" class="zhu2">删除</a>&nbsp;
								</c:if>
								<c:if test="${not empty merchants}">
									<a href="<%=request.getContextPath()%>/html/manage/discountActive/list/${info.active_id}/Goods" class="zhu2">
										添加打折商品
									</a>
								</c:if>
								<c:if test="${ empty merchants}">
									<a href="<%=request.getContextPath()%>/html/manage/discountActive/list/${info.active_id}/orderIndex" class="zhu2">
										商品排序
									</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<jsp:include page="/admin-inc/nofenye.jsp">
						<jsp:param name="url" value="/html/manage/discountActive/list" />
					</jsp:include>
				</table>
			</form>
		</td>
	</tr>
</table>
</html>