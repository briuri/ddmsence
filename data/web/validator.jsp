<html>
<head>
	<%@page session="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>DDMSence: Online DDMS Validator</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
	<meta name="keywords" content="DDMSence,DDMS,Online,Validator,Validation,DoD" />
	<script type="text/javascript" src="./shared/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="./shared/jquery.validate.min.js"></script>	
	<script type="text/javascript">
	
	$(function($){
		// validate form on keyup and submit
		$("#record").validate({
			rules: {
				<c:if test="${type eq 'text'}">
					stringRecord: {
						required: true,
						maxlength: 50000
					}
				</c:if>
				<c:if test="${type eq 'file'}">
					upload: {
						required: true
					}
				</c:if>
				<c:if test="${type eq 'url'}">
					url: {
						required: true,
						maxlength: 2000
					}
				</c:if>
			},
			messages: {
				<c:if test="${type eq 'text'}">
					stringRecord: {
						required: "A DDMS record is required.",
						maxlength: "The DDMS record cannot exceed 50,000 characters."
					}
				</c:if>
				<c:if test="${type eq 'file'}">
					upload: {
						required: "A DDMS file is required."
					}
				</c:if>
				<c:if test="${type eq 'url'}">				
					url: {
						required: "A URL is required.",
						maxlength: "The URL cannot exceed 2000 characters."
					}
				</c:if>
			}		
		});
	});
	
	function showExample(form) {
		<c:if test="${type eq 'text'}">
			exampleRecord = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<ddms:Resource \n"
				+ "  xsi:schemaLocation=\"http://metadata.dod.mil/mdr/ns/DDMS/3.0/\" \n"
				+ "  xmlns:ddms=\"http://metadata.dod.mil/mdr/ns/DDMS/3.0/\" \n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
				+ "  xmlns:ICISM=\"urn:us:gov:ic:ism\" \n"
				+ "  ICISM:resourceElement=\"true\" ICISM:DESVersion=\"2\" \n"
				+ "  ICISM:createDate=\"2010-01-21\" ICISM:classification=\"U\" \n"
				+ "  ICISM:ownerProducer=\"USA\">\n"
				+ "  <ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence\"/>\n"
				+ "  <ddms:title ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\">\n"
				+ "    DDMSence\n"
				+ "  </ddms:title>\n"
				+ "  <ddms:description ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\">\n"
				+ "    An open source Java API for DDMS\n"
				+ "  </ddms:description>\n"
				+ "  <ddms:language ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" \n"
				+ "    ddms:value=\"en\"/>\n"
				+ "  <ddms:dates ddms:created=\"2010-03-24\" ddms:posted=\"2010-03-24\" />\n"
				+ "  <ddms:rights ddms:copyright=\"true\" ddms:privacyAct=\"false\" \n"
				+ "    ddms:intellectualProperty=\"true\"/>\n"
				+ "  <ddms:creator ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\">\n"
				+ "    <ddms:Person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:Person>\n"
				+ "  </ddms:creator>\n"
				+ "  <ddms:publisher ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">\n"
				+ "    <ddms:Person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:Person>\n"
				+ "  </ddms:publisher>\n"
				+ "  <ddms:pointOfContact>\n"
				+ "    <ddms:Person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:Person>\n"
				+ "  </ddms:pointOfContact>\n"
				+ "  <ddms:subjectCoverage>\n"
				+ "    <ddms:Subject>\n"
				+ "      <ddms:keyword ddms:value=\"DDMSence\"/>\n"
				+ "      <ddms:keyword ddms:value=\"metadata\"/>\n"
				+ "      <ddms:keyword ddms:value=\"discovery\"/>\n"
				+ "      <ddms:keyword ddms:value=\"DDMS\"/>\n"
				+ "      <ddms:keyword ddms:value=\"open source\"/>\n"
				+ "      <ddms:keyword ddms:value=\"Java\"/>\n"
				+ "      <ddms:category \n"
				+ "        ddms:qualifier=\"http://metadata.dod.mil/mdr/ns/TaxFG/0.75c/Core_Tax_0.75c.owl#Asset\" \n"
				+ "        ddms:code=\"Asset\" ddms:label=\"Asset\"/>\n"
				+ "    </ddms:Subject>\n"
				+ "</ddms:subjectCoverage>\n"
				+ "  <ddms:temporalCoverage>\n"
				+ "    <ddms:TimePeriod>\n"
				+ "      <ddms:start>2010-03-24T12:00:00Z</ddms:start>\n"
				+ "      <ddms:end>Not Applicable</ddms:end>\n"
				+ "    </ddms:TimePeriod>\n"
				+ "  </ddms:temporalCoverage>\n"
				+ "  <ddms:security ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\" \n"
				+ "    ICISM:excludeFromRollup=\"true\"/>\n"
				+ "</ddms:Resource>"
			form.stringRecord.value = exampleRecord;
		</c:if>
		<c:if test="${type eq 'url'}">
			form.url.value = "ddmsence.googlecode.com/svn/trunk/data/sample/DDMSence_Example.xml";
		</c:if>
	}
	
	function changeType(newType) {
		if ('${type}' != newType) 
			parent.location.href = "validator.uri?type=" + newType
	}
	
	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Validator</h1>

<p>This experimental tool uses the DDMSence library to validate <b>Unclassified</b> DDMS 2.0 and 3.0 records. Records 
can be submitted by pasting XML text, uploading a file, or referencing a URL. Information submitted through this tool is not retained
on the server.</p>

<form:form id="record" commandName="record" method="post" enctype="multipart/form-data">
	<form:hidden path="type" />
	
	<label id="ltype" for="selectType">Record Location:</label>
	<span class="formElement">
		<select name="selectType" id="selectType" onchange="changeType(this.options[this.selectedIndex].value)">
			<option value="text"
				<c:if test="${type eq 'text'}"> selected="true" </c:if>
			>Text</option>
			<option value="file"
				<c:if test="${type eq 'file'}"> selected="true" </c:if>
			>File Upload</option>
			<option value="url"
				<c:if test="${type eq 'url'}"> selected="true" </c:if>		
			>URL</option>
		</select>
	</span><br />
	
	<c:if test="${type eq 'text'}">
		<label id="lstringRecord" for="stringRecord">DDMS Record:</label>
		<form:textarea path="stringRecord" rows="16" cols="80" />
	</c:if>
	<c:if test="${type eq 'file'}">
		<label id="lfile" for="file">File:</label>
		<input size="50" type="file" name="upload" />
	</c:if>
	<c:if test="${type eq 'url'}">
		<label id="lurl" for="url">DDMS URL:</label>
		http://<form:input path="url" size="50" maxlength="2000" />
	</c:if>
	<br />
	<span class="formElement">
		<input type="submit" class="button" name="submit" value="Submit">
		<input type="reset" class="button" name="reset" value="Reset">
		<c:if test="${type eq 'text' or type eq 'url'}">
			<input type="button" class="button" name="example" value="Example" onclick="showExample(this.form)">
		</c:if>
	</span>
</form:form>

<h1>How This Works</h1>

<p>The source code for this tool is not bundled with DDMSence, because it has dependencies on the Spring Framework. However, the basic concepts are very simple:</p>

<ul>
	<li>After submitting this form, a Spring MVC controller checks to see whether the DDMS Resource is coming in as text, an uploaded file, or a URL.
	Files and URLs are loaded and converted into text.</li>
	<li>The Resource is now stored in a String containing the raw XML, <code>stringRepresentation</code>, and the following Java code is run:</li>
	
<pre class="brush: java">
Map&lt;String, Object&gt; model = new HashMap&lt;String, Object&gt;();
try {
   Resource resource = new DDMSReader().getDDMSResource(stringRepresentation);
   model.put("warnings", resource.getValidationWarnings());
}
catch (InvalidDDMSException e) {
   ValidationMessage message = ValidationMessage.newError(e.getMessage(), e.getLocator());
   model.put("error", message);
}</pre>

	<li>The DDMSReader method, <a href="/docs/buri/ddmsence/util/DDMSReader.html"><code>getDDMSResource()</code></a> attempts to build the entire DDMS Resource. It will fail immediately with an <code>InvalidDDMSException</code> if the Resource is invalid.</li>
	<li>If the constructor succeeds, the Resource is proven to be valid, although there may still be warnings.</li>
	<li>The Map containing errors or warnings, <code>model</code>, is then used to render the Validation Results page.</li>
</ul>	


<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>