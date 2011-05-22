<html>
<head>
	<%@page session="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>DDMSence: Online DDMS Builder</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
	<meta name="keywords" content="DDMSence,DDMS,Online,Builder,DoD" />
	<script type="text/javascript" src="./shared/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="./shared/jquery.validate.min.js"></script>	
	<script type="text/javascript">
		
	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Builder</h1>

<p>This experimental tool uses the DDMSence library to create an XML DDMS Resource based on form input. It uses
the <a href="documentation.jsp#tips-builders">Component Builder</a> framework, which was introduced in DDMSence 1.8.0. 
Information submitted through this tool is not retained on the server.</p>

<form:form id="resource" commandName="resource" method="post">

	<label class="ddmsComponent">Identifier <img src="./images/info.png" width="18" height="17" title="ddms:identifier (at least 1 required)" /></label>
	<div class="clear"></div>
	
	<label>test</label>
	<span class="formElement">value</span><br />

	<label class="ddmsComponent">Title <img src="./images/info.png" width="18" height="17" title="ddms:title (at least 1 required)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Subtitle <img src="./images/info.png" width="18" height="17" title="ddms:subtitle (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Description <img src="./images/info.png" width="18" height="17" title="ddms:description (only 1 allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Language <img src="./images/info.png" width="18" height="17" title="ddms:language (any number allowed)" /></label>
	<div class="clear"></div>			

	<label class="ddmsComponent">Dates <img src="./images/info.png" width="18" height="17" title="ddms:dates (only 1 allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Rights <img src="./images/info.png" width="18" height="17" title="ddms:rights (only 1 allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Source <img src="./images/info.png" width="18" height="17" title="ddms:source (any number allowed)" /></label>
	<div class="clear"></div>			

	<label class="ddmsComponent">Type <img src="./images/info.png" width="18" height="17" title="ddms:type (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Producer <img src="./images/info.png" width="18" height="17" title="Producers: creator, publisher, contributor, and pointOfContact (at least 1 required)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Format <img src="./images/info.png" width="18" height="17" title="ddms:format (only 1 allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">SubjectCoverage <img src="./images/info.png" width="18" height="17" title="ddms:subjectCoverage (exactly 1 required)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">VirtualCoverage <img src="./images/info.png" width="18" height="17" title="ddms:virtualCoverage (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">TemporalCoverage <img src="./images/info.png" width="18" height="17" title="ddms:temporalCoverage (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">GeospatialCoverage <img src="./images/info.png" width="18" height="17" title="ddms:geospatialCoverage (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">RelatedResources <img src="./images/info.png" width="18" height="17" title="ddms:relatedResources (any number allowed)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Security <img src="./images/info.png" width="18" height="17" title="ddms:security (exactly 1 required)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">ExtensibleElement <img src="./images/info.png" width="18" height="17" title="Extensible Elements (any number required)" /></label>
	<div class="clear"></div>

	<label class="ddmsComponent">Resource Attributes <img src="./images/info.png" width="18" height="17" title="ddms:Resource Attributes (all required)" /></label>
	<div class="clear"></div>

	<br />
	<span class="formElement">
		<input type="submit" class="button" name="submit" value="Submit">
		<input type="reset" class="button" name="reset" value="Reset">
		<c:if test="${type eq 'text' or type eq 'url'}">
			<input type="button" class="button" name="example" value="Example" onclick="showExample(this.form)">
		</c:if>
	</span>
</form:form>

<h3>How This Works</h3>

<p>The source code for this tool is not bundled with DDMSence, because it has dependencies on the Spring Framework. However, the basic concepts are very simple:</p>

<ul>
	<li>The form on this page is backed by an instance of <a href="/docs/buri/ddmsence/ddms/Resource.Builder.html">Resource.Builder</a>.</li>
	<li>When the form is submitted, the method, <code>commit()</code> is called, which attempts to create a DDMS Resource based on the form data. 
		It will fail immediately with an <code>InvalidDDMSException</code> if the Resource is invalid.</li>
	<li>If the constructor succeeds, the Resource is proven to be valid, although there may still be warnings.</li>
	<li>The Map containing errors or warnings, <code>model</code>, is then used to render the Builder Results page.</li>
</ul>	

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>