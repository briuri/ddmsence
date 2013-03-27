<html>
<head>
	<%@page session="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<title>DDMSence: Online DDMS Validation Result</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Validation Results</h1>

<c:if test="${not empty model.error}">
	<c:choose>
		<c:when test="${empty model.error.locator}">
			<c:set var="locator" value="unknown location" />
		</c:when>
		<c:otherwise>
			<c:set var="locator" value="${model.error.locator}" />
		</c:otherwise>
	</c:choose>
	<li><b>${model.error.type}</b> at <code><c:out value="${locator}" /></code>: <c:out value="${model.error.text}" /></li>
</c:if>

<c:forEach items="${model.warnings}" var="warning" varStatus="rowInfo">
	<li><b>${warning.type}</b> at <code><c:out value="${warning.locator}" /></code>: <c:out value="${warning.text}" /></li>
</c:forEach>

<c:if test="${empty model.error && empty model.warnings}">
	<p>No errors or warnings were encountered in this DDMS record.</p>
</c:if>

<c:if test="${not empty model.record}">
	<pre class="brush: xml">
<c:out value="${model.record}" escapeXml="true" />
	</pre>
</c:if>	

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>