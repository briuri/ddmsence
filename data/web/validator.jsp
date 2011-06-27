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
				+ "  xsi:schemaLocation=\"http://metadata.dod.mil/mdr/ns/DDMS/3.0/ DDMS-v3_1.xsd\" \n"
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

<p>This experimental tool uses the DDMSence library to validate <b>Unclassified</b> DDMS 2.0, 3.0, and 3.1 records. Records 
can be submitted by pasting XML text, uploading a file, or referencing a URL. Information submitted through this tool is not retained
on the server.</p>
<p>Starred fields (<b>*</b>) are required.</p>

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
		<label id="lstringRecord" for="stringRecord">DDMS Record: *</label>
		<form:textarea path="stringRecord" rows="16" cols="80" />
	</c:if>
	<c:if test="${type eq 'file'}">
		<label id="lfile" for="file">File: *</label>
		<input size="50" type="file" name="upload" />
	</c:if>
	<c:if test="${type eq 'url'}">
		<label id="lurl" for="url">DDMS URL: *</label>
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

<h3>How This Works</h3>

<p>Compilable source code for this tool is not bundled with DDMSence, because it has dependencies on the Spring Framework. However, all of the pieces you need create 
a similar web application are shown below. A basic understanding of <a href="http://en.wikipedia.org/wiki/Spring_Framework#Model-view-controller_framework">Spring MVC</a> 
will be necessary to understand the code.</p>

<ol>
	<li>A Spring configuration file maps the URI, <code>validator.uri</code> to the appropriate Spring controller. A <code>multipartResolver</code> bean
	is used to handle file uploads. Here is the relevant excerpt from this server's configuration file:</li>
<pre class="brush: xml; collapse: true">&lt;bean id="multipartResolver" class="org.springframework.web.multipart.cos.CosMultipartResolver"&gt;
   &lt;property name="maxUploadSize" value="50000" /&gt;
&lt;/bean&gt;
&lt;bean id="validatorControl" class="buri.web.ddmsence.ValidatorControl" /&gt;
&lt;bean id="urlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping"&gt;
   &lt;property name="urlMap"&gt;
      &lt;map&gt;
         &lt;entry key="validator.uri" value-ref="validatorControl" /&gt;
      &lt;/map&gt;
   &lt;/property&gt;
&lt;/bean&gt;</pre>

	<li>A Spring controller, ValidatorControl, handles incoming requests. The <code>type</code> parameter is used to determine what sort of form should be displayed -- changing
	the "Record Location" drop-down selection redraws the form.</li>
<pre class="brush: java; collapse: true">package buri.web.ddmsence;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import buri.app.util.Util;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.DDMSReader;

/**
 * Controller class for validating DDMS Records
 *
 * @author	Brian Uri!
 */
public class ValidatorControl extends SimpleFormController {

   protected final Log logger = LogFactory.getLog(getClass());
               
    /**
     * Constructor
     */
    public ValidatorControl() {
       setCommandName("record");
       setCommandClass(ValidatorRecord.class);
       setFormView("validator");
    }
    
    /**
     * @see SimpleFormController#formBackingObject(HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
       String type = request.getParameter("type");
       return (new ValidatorRecord(type));
    }    
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    public Map&lt;String, Object&gt; referenceData(HttpServletRequest request) throws Exception {
       String type = request.getParameter("type");
       if (Util.isEmpty(type))
          type = ValidatorRecord.DEFAULT_TYPE;
       Map&lt;String, Object&gt; data = new HashMap&lt;String, Object&gt;();
       data.put("type", type);
       return (data);
    }  
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
   protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
       ValidatorRecord record = (ValidatorRecord) command;
       Map&lt;String, Object&gt; model = new HashMap&lt;String, Object&gt;();
       String stringRepresentation = null;
      try {
         if (ValidatorRecord.TYPE_TEXT.equals(record.getType())) {
            stringRepresentation = record.getStringRecord();
         }
         else if (ValidatorRecord.TYPE_FILE.equals(record.getType())) {
            stringRepresentation = readStream(getFile(request));
         }
         else if (ValidatorRecord.TYPE_URL.equals(record.getType())) {
            String fullUrl = "http://" + record.getUrl();
            try {
               URL url = new URL(fullUrl);
               URLConnection uc = url.openConnection();
               stringRepresentation = readStream(new BufferedReader(new InputStreamReader(uc.getInputStream())));
            }
            catch (IOException e) {
               throw new IOException("Could not connect to URL: " + fullUrl);
            }
         }         
         model.put("record", stringRepresentation);
         Resource resource = new DDMSReader().getDDMSResource(stringRepresentation);
         if (isUnclassified(resource)) {
            model.put("warnings", resource.getValidationWarnings());
         } else {
            model.remove("record");
            throw new InvalidDDMSException("This tool can only be used on Unclassified data.");
         }
      }
        catch (InvalidDDMSException e) {
           ValidationMessage message = ValidationMessage.newError(e.getMessage(), e.getLocator());
            model.put("error", message);
         }
         catch (Exception e) {
            ValidationMessage message = ValidationMessage.newError(e.getMessage(), null);
            model.put("error", message);
         }
       return (new ModelAndView("validatorResult", "model", model));
    }
    
   /**
    * Converts the contents of a stream into a String
    * 
    * @param streamReader the reader around the original input stream
    * @return a String
    * @throws IOException
    */
   private String readStream(Reader streamReader) throws IOException {
      LineNumberReader reader = new LineNumberReader(streamReader);
      StringBuffer buffer = new StringBuffer();
      String currentLine = reader.readLine();
      while (currentLine != null) {
         buffer.append(currentLine).append("\n");
         currentLine = reader.readLine();
      }
      return (buffer.toString());
   }
    /**
     * Gets the uploaded file from the request if it exists and wraps a reader around it.
     * 
     * @param request 
     * @throws IOException
     */
    private Reader getFile(HttpServletRequest request) throws IOException {
       if (request instanceof MultipartHttpServletRequest) {
          MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
          MultipartFile file = fileRequest.getFile("upload");
          if (!file.isEmpty()) {
             return (new InputStreamReader(new BufferedInputStream(file.getInputStream())));
          }
       }       
       return (null);
    }
    
    /**
     * Prevents classified data from being validated here.
     * 
     * @param resource the DDMS Resource
     */
    private boolean isUnclassified(Resource resource) throws InvalidDDMSException {
       Set&lt;SecurityAttributes&gt; allAttributes = new HashSet&lt;SecurityAttributes&gt;();
       allAttributes.add(resource.getSecurityAttributes());
      for (IDDMSComponent component : resource.getTopLevelComponents()) {
         if (component.getSecurityAttributes() != null)
            allAttributes.add(component.getSecurityAttributes());
      }
      for (SecurityAttributes attr : allAttributes) {
         if (!"U".equals(attr.getClassification()) && !"".equals(attr.getClassification()))
            return (false);
      }
      return (true);
    }
}</pre>
	<li>The ValidatorControl starts by creating a new form bean, ValidatorRecord, in the <code>formBackingObject()</code> method. 
	This is a simple data class which supports the form you see on this page.</li>
<pre class="brush: java; collapse: true">package buri.web.ddmsence;

import buri.app.util.Util;

/**
 * Form bean for online DDMS validation
 *
 * @author	Brian Uri!
 */
public class ValidatorRecord {
	
   public static final String TYPE_TEXT = "text";
   public static final String TYPE_FILE = "file";
   public static final String TYPE_URL = "url";   
   public static final String DEFAULT_TYPE = TYPE_TEXT;
   

   private String _type;
   private String _stringRecord;
   private String _url;

   /**
    * Constructor
    * @param type the type of record being submitted.
    */
   public ValidatorRecord(String type) {
      if (Util.isEmpty(type))
         type = DEFAULT_TYPE;
      _type = type;
   }
   
   /**
    * Accessor for the string version of the record.
    */
   public String getStringRecord() {
      return _stringRecord;
   }

   /**
    * Accessor for the string version of the record.
    */
   public void setStringRecord(String stringRecord) {
      _stringRecord = stringRecord;
   }

   /**
    * Accessor for the type (file, url, text)
    */
   public String getType() {
      return _type;
   }

   /**
    * Accessor for the url
    */
   public String getUrl() {
      return _url;
   }

   /**
    * Accessor for the url
    */
   public void setUrl(String url) {
      _url = url;
   }
}</pre>
	<li>The <a href="http://ddmsence.googlecode.com/svn/trunk/data/web/validator.jsp">initial form view</a> is rendered. This is the page you are currently viewing. The JSP file also contains the JavaScript code used for client-side validation (with jQuery).</li>
	<li>Once the form has been filled in and submitted, the <code>onSubmit()</code> method of the ValidatorControl is called. This method checks to see whether the DDMS 
	Resource is coming in as text, an uploaded file, or a URL. Files and URLs are loaded and converted into text.</li>
	<li>The DDMSReader method, <a href="/docs/buri/ddmsence/util/DDMSReader.html"><code>getDDMSResource()</code></a> attempts to build the entire DDMS Resource. 
	It will fail immediately with an <code>InvalidDDMSException</code> if the Resource is invalid.</li>
	<li>If the constructor succeeds, the Resource is proven to be valid, although there may still be warnings. The Map containing errors or warnings, <code>model</code>, 
	is then used to render the <a href="http://ddmsence.googlecode.com/svn/trunk/data/web/validatorResult.jsp">Validation Results page</a>.</li>
</ol>	

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#explorations">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>