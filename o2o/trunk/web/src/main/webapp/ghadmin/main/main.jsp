<%@ page import="cn.com.dyninfo.o2o.furniture.sys.Constants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="<%=request.getContextPath() %>/img/llloo.ico" rel="SHORTCUT ICON" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<%@ taglib prefix="fmt" uri="/WEB-INF/taglib/fmt.tld" %>

<fmt:setBundle basename="locale" />
<title><fmt:message key="sys.welcome"/><fmt:message key="sys.name"/></title>
</head>



<frameset rows="60,*,0" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="<%=request.getContextPath() %>/<%=Constants.ADMIN_ADDRESS%>/main/banner.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset cols="200,*" rows="*" frameborder="no" border="0" framespacing="0">
    <frame src="<%=request.getContextPath() %>/html/manage/left" name="leftFrame" scrolling="no" noresize="noresize" id="leftFrame" title="leftFrame" style="width: 300px;" />
    <frame src="<%=request.getContextPath() %>/html/manage/main/right" name="mainFrame" id="mainFrame" title="index"  />
  </frameset>
  <frame src="#" id="downloadFrame" name="downloadFrame" scrolling="no" noresize="noresize"/>
</frameset>
<noframes><body>
</body>
</noframes>
</html>
