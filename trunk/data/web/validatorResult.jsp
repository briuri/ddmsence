<html>
<head>
	<%@page session="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<title>Online DDMS Validation Result| DDMSence</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="The open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<script type="text/javascript" src="./shared/jquery-1.10.2.min.js"></script>
	<script type="text/javascript">
	
	var current = "xml";
	
	function changeFormat(newFormat) {
		$("#" + current).hide();
		$("#" + newFormat).show();
		current = newFormat;
	}
	
	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Validation Results</h1>

<c:if test="${not empty error}">
	<c:choose>
		<c:when test="${empty error.locator}">
			<c:set var="locator" value="unknown location" />
		</c:when>
		<c:otherwise>
			<c:set var="locator" value="${error.locator}" />
		</c:otherwise>
	</c:choose>
	<li><b>${error.type}</b> at <code><c:out value="${locator}" /></code>: <c:out value="${error.text}" /></li>
</c:if>

<c:forEach items="${warnings}" var="warning" varStatus="rowInfo">
	<li><b>${warning.type}</b> at <code><c:out value="${warning.locator}" /></code>: <c:out value="${warning.text}" /></li>
</c:forEach>

<c:if test="${empty error && empty warnings}">
	<p>No errors or warnings were encountered in this DDMS record.</p>
</c:if>

<c:if test="${not empty xml}">
	<c:if test="${empty error}">		
		<form>
			<label id="lformat" for="selectFormat">Format:</label>
			<span class="formElement">
				<select name="selectFormat" id="selectFormat" onchange="changeFormat(this.options[this.selectedIndex].value)">
					<option value="html">HTML</option>
					<option value="json">JSON</option>
					<option value="text">Text</option>
					<option value="xml" selected="true">XML</option>
				</select>			
			</span>
		</form>
	</c:if>
	<div id="xml">
		<pre class="brush: xml"><c:out value="${xml}" escapeXml="true" /></pre>
	</div>
	<c:if test="${empty error}">
		<div id="html" style="display: none;">
			<pre class="brush: xml"><c:out value="${html}" escapeXml="true" /></pre>
		</div>
		<div id="text" style="display: none;">
			<pre class="brush: xml"><c:out value="${text}" escapeXml="true" /></pre>
		</div>
		<div id="json" style="display: none;">
			<pre class="brush: javascript"><c:out value="${json}" escapeXml="true" /></pre>
		</div>
	</c:if>
</c:if>	

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>