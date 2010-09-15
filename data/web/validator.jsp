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
				stringRecord: {
					required: true,
					maxlength: 50000
				},
				url: {
					required: true
				}
			},
			messages: {
				stringRecord: {
					required: "A DDMS record is required.",
					maxlength: "The DDMS record cannot exceed 50,000 characters."
				},
				url: {
					required: "A URL is required."
				}
			}		
		});
	});
	
	function showExample(form) {
		if (form.type.value == "text") {
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
		}
		else if (form.type.value == "url")
			form.url.value = "ddmsence.googlecode.com/svn/trunk/data/sample/DDMSence_Example.xml";
	}
	
	function changeType(form, newType) {
		form.type.value = newType;
		if (newType == "text") {
			document.getElementById("textOptions").style.display = "block";
			document.getElementById("fileOptions").style.display = "none";
			document.getElementById("urlOptions").style.display = "none";
			document.getElementById("exampleOptions").style.display = "block";
			document.getElementById("noExampleOptions").style.display = "none";
			form.stringRecord.value = ""
			form.url.value = "unused"
		}
		if (newType == "file") {
			document.getElementById("textOptions").style.display = "none";
			document.getElementById("fileOptions").style.display = "block";
			document.getElementById("urlOptions").style.display = "none";
			document.getElementById("exampleOptions").style.display = "none";
			document.getElementById("noExampleOptions").style.display = "block";
			form.upload.value = ""
			form.stringRecord.value = "unused"
			form.url.value = "unused"
		}
		if (newType == "url") {
			document.getElementById("textOptions").style.display = "none";
			document.getElementById("fileOptions").style.display = "none";
			document.getElementById("urlOptions").style.display = "block";
			document.getElementById("exampleOptions").style.display = "block";
			document.getElementById("noExampleOptions").style.display = "none";
			form.stringRecord.value = "unused"
			form.url.value = ""
		}		
	}
	
	</script>
</head>
<body onload="changeType(document.forms[0], 'text')">
<%@ include file="../shared/header.jspf" %>

<h1>Online DDMS Validator</h1>

<p>This experimental tool uses the DDMSence library to validate <b>Unclassified</b> DDMS 2.0 and 3.0 records. Records 
can be submitted by pasting XML text, uploading a file, or referencing a URL. Information submitted through this tool is not retained
on the server.</p>

<form:form id="record" commandName="record" method="post" enctype="multipart/form-data">
	<input type="hidden" name="type" id="type" value="text" />
	
	<label id="ltype" for="selectType">Record Location:</label>
	<span class="formElement">
		<select name="selectType" id="selectType" onchange="changeType(this.form, this.options[this.selectedIndex].value)">
			<option value="text" selected="true">Text</option>
			<option value="file">File Upload</option>
			<option value="url">URL</option>
		</select>
	</span><br />	

	<div id="textOptions">
		<label id="lstringRecord" for="stringRecord">DDMS Record:</label>
		<form:textarea path="stringRecord" rows="16" cols="80" />
	</div>
	
	<div id="fileOptions" style="display: none">
		<label id="lfile" for="file">File:</label>
		<input size="50" type="file" name="upload" />
	</div>
	
	<div id="urlOptions" style="display: none">
		<label id="lurl" for="url">DDMS URL:</label>
		http://<form:input path="url" size="50" maxlength="512" />
	</div>
	
	<br />
	<span class="formElement">
		<div id="exampleOptions">
			<input type="submit" class="button" name="submit" value="Submit">
			<input type="reset" class="button" name="reset" value="Reset">
			<input type="button" class="button" name="example" value="Example" onclick="showExample(this.form)">
		</div>
		<div id="noExampleOptions" style="display: none">
			<input type="submit" class="button" name="submit" value="Submit">
			<input type="reset" class="button" name="reset" value="Reset">
		</div>		
	</span>
</form:form>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>