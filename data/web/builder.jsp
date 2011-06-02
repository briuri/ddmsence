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
		
	$(function($){
		// validate form on keyup and submit
		$("#resource").validate({
			rules: {
				'identifiers[0].qualifier': {
					required: true
				},
				'identifiers[0].value': {
					required: true
				},
				'titles[0].value': {
					required: true
				},
				'titles[0].securityAttributes.classification': {
					required: true
				},
				'titles[0].securityAttributes.ownerProducers': {
					required: true
				},				
				'organizations[0].producerType': {
					required: true
				},
				'organizations[0].names[0]': {
					required: true
				},
				'organizations[0].phones[0]': {
					required: true
				},
				'organizations[0].emails[0]': {
					required: true
				},
				'subjectCoverage.keywords[0].value': {
					required: true
				},
				'security.securityAttributes.classification': {
					required: true
				},
				'security.securityAttributes.ownerProducers': {
					required: true
				},
				'createDate': {
					required: true
				},
				'DESVersion': {
					required: true
				},
				'securityAttributes.classification': {
					required: true
				},
				'securityAttributes.ownerProducers': {
					required: true
				}
			}, 
			messages: {
				'identifiers[0].qualifier': {
					required: "A qualifier is required."
				},
				'identifiers[0].value': {
					required: "A value is required."
				},
				'titles[0].value': {
					required: "A title is required."
				},
				'titles[0].securityAttributes.classification': {
					required: "A security classification is required."
				},
				'titles[0].securityAttributes.ownerProducers': {
					required: "At least one owner/producer is required."
				},
				'organizations[0].producerType': {
					required: "A producer type is required."
				},
				'organizations[0].names[0]': {
					required: "A name is required."
				},
				'organizations[0].phones[0]': {
					required: "A phone number is required."
				},
				'organizations[0].emails[0]': {
					required: "An email address is required."
				},
				'subjectCoverage.keywords[0].value': {
					required: "A keyword is required."
				},
				'security.securityAttributes.classification': {
					required: "A security classification is required."
				},
				'security.securityAttributes.ownerProducers': {
					required: "At least one owner/producer is required."
				},
				'createDate': {
					required: "A create date is required."
				},
				'DESVersion': {
					required: "A DES Version is required."
				},
				'securityAttributes.classification': {
					required: "A security classification is required."
				},
				'securityAttributes.ownerProducers': {
					required: "At least one owner/producer is required."
				}
			}		
		});
	});
		
	function showExample(form) {
		form.elements['identifiers[0].qualifier'].value = 'URI';
		form.elements['identifiers[0].value'].value = 'urn:buri:ddmsence';
		form.elements['titles[0].value'].value = 'DDMSence';
		form.elements['titles[0].securityAttributes.classification'].value = 'U';
		form.elements['titles[0].securityAttributes.ownerProducers'].value = 'USA';
		form.elements['organizations[0].producerType'].value = 'contributor';
		form.elements['organizations[0].names[0]'].value = 'FGM, Inc.';
		form.elements['organizations[0].phones[0]'].value = '703-885-1000';
		form.elements['organizations[0].emails[0]'].value = 'ddmsence@urizone.net';
		form.elements['subjectCoverage.keywords[0].value'].value = 'DDMSence';
		form.elements['security.securityAttributes.classification'].value = 'U';
		form.elements['security.securityAttributes.ownerProducers'].value = 'USA';
		form.elements['resourceElement'].checked = true;
		form.elements['createDate'].value = '2010-01-21';
		form.elements['DESVersion'].value = '2';
		form.elements['securityAttributes.classification'].value = 'U';
		form.elements['securityAttributes.ownerProducers'].value = 'USA';
	}	
	
	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Builder</h1>

<p>This experimental tool uses the DDMSence library to create a DDMS 3.0 Resource from form input. It uses
the <a href="documentation.jsp#tips-builders">Component Builder</a> framework, which was introduced in DDMSence 
1.8.0. To simplify the example source code, the form only asks for the minimal subset of elements and attributes required for a valid Resource
 (this is similar to running the <u>Escort</u> <a href="tutorials-02.jsp">tutorial</a> in FAST mode). 
Information submitted through this tool is not retained on the server.</p> 

<p>Starred fields (<b>*</b>) are required.</p>

<form:form id="resource" commandName="resource" method="post">
	<label class="builderComponent">Identifier</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="identifiers[0].qualifier">Qualifier: *</label>
	<form:input path="identifiers[0].qualifier" size="25" maxlength="2048" /></div>
	<div class="clear"><label class="builderField" for="identifiers[0].value">Value: *</label>
	<form:input path="identifiers[0].value" size="25" maxlength="256" /></div>
			
	<br />
	<label class="builderComponent">Title</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="titles[0].value">Value: *</label>
	<form:input path="titles[0].value" size="25" maxlength="2048" /></div>
	<div class="clear"><label class="builderField" for="titles[0].securityAttributes.classification">Security Classification: *</label>
 	<form:select path="titles[0].securityAttributes.classification"  multiple="false" size="1">
 		<form:option value=""></form:option>
		<form:option value="U">Unclassified</form:option>
	</form:select></div>
	<div class="clear"><label class="builderField" for="titles[0].securityAttributes.ownerProducers">Owner/Producers: *</label>
	<form:select path="titles[0].securityAttributes.ownerProducers"  multiple="true" size="4">
		<c:forEach var="ownerProducer" items="${ownerProducers}">
			<form:option value="${ownerProducer}">${ownerProducer}</form:option>
		</c:forEach>
	</form:select></div>
				
	<br />
	<label class="builderComponent">Producer</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="organizations[0].producerType">Producer Type: *</label>
	 	<form:select path="organizations[0].producerType"  multiple="false" size="1">
 		<form:option value=""></form:option>
 		<c:forEach var="producerType" items="${producerTypes}">
			<form:option value="${producerType}">${producerType}</form:option>
		</c:forEach>
	</form:select></div>
	<div class="clear"><label class="builderField" for="organizations[0].names[0]">Organization Name: *</label>
	<form:input path="organizations[0].names[0]" size="25" maxlength="256" /></div>
	<div class="clear"><label class="builderField" for="organizations[0].phones[0]">Organization Phone: *</label>
	<form:input path="organizations[0].phones[0]" size="25" maxlength="256" /></div>
	<div class="clear"><label class="builderField" for="organizations[0].emails[0]">Organization Email: *</label>
	<form:input path="organizations[0].emails[0]" size="25" maxlength="2048" /></div>

	<br />
	<label class="builderComponent">SubjectCoverage</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="subjectCoverage.keywords[0].value">Keyword #1: *</label>
	<form:input path="subjectCoverage.keywords[0].value" size="25" maxlength="256" /></div>
	<div class="clear"><label class="builderField" for="subjectCoverage.keywords[1].value">Keyword #2:</label>
	<form:input path="subjectCoverage.keywords[1].value" size="25" maxlength="256" /></div>
	
	<br />
	<label class="builderComponent">Security</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="security.excludeFromRollup">Exclude from Rollup?: *</label>
	<span class="formElement">yes<input type="hidden" name="security.excludeFromRollup" value="true" /></span></div>
	<div class="clear"><label class="builderField" for="security.securityAttributes.classification">Security Classification: *</label> 
 	<form:select path="security.securityAttributes.classification" multiple="false" size="1">
 		<form:option value=""></form:option>
		<form:option value="U">Unclassified</form:option>
	</form:select></div>	
	<div class="clear"><label class="builderField" for="security.securityAttributes.ownerProducers">Owner/Producers: *</label>
	<form:select path="security.securityAttributes.ownerProducers"  multiple="true" size="4">
		<c:forEach var="ownerProducer" items="${ownerProducers}">
			<form:option value="${ownerProducer}">${ownerProducer}</form:option>
		</c:forEach>
	</form:select></div>
	
	<br />
	<label class="builderComponent">Resource Attributes</label>
	<div class="clear"></div>
	<div class="clear"><label class="builderField" for="resourceElement">Resource Element?: *</label>
	<form:checkbox path="resourceElement" /> yes</div>
	<div class="clear"><label class="builderField" for="createDate">Create Date: *</label>
	<form:input path="createDate" size="10" maxlength="32" /></div>
	<div class="clear"><label class="builderField" for="DESVersion">DES Version: *</label>
	<form:input path="DESVersion" size="1" maxlength="1" /></div>
	<div class="clear"><label class="builderField" for="securityAttributes.classification">Security Classification: *</label>
 	<form:select path="securityAttributes.classification"  multiple="false" size="1">
 		<form:option value=""></form:option>
		<form:option value="U">Unclassified</form:option>
	</form:select></div>
	<div class="clear"><label class="builderField" for="securityAttributes.ownerProducers">Owner/Producers: *</label>
	<form:select path="securityAttributes.ownerProducers"  multiple="true" size="4">
		<c:forEach var="ownerProducer" items="${ownerProducers}">
			<form:option value="${ownerProducer}">${ownerProducer}</form:option>
		</c:forEach>
	</form:select></div>
	
	<br />
	<label class="error"><form:errors path="*"/></label><br />
	<span class="formElement">
		<input type="submit" class="button" name="submit" value="Submit">
		<input type="reset" class="button" name="reset" value="Reset">
		<input type="button" class="button" name="example" value="Example" onclick="showExample(this.form)">
	</span>
</form:form>

<h3>How This Works</h3>

<p>Compilable source code for this tool is not bundled with DDMSence, because it has dependencies on the Spring Framework. However, all of the pieces you need create 
a similar web application are shown below. A basic understanding of <a href="http://en.wikipedia.org/wiki/Spring_Framework#Model-view-controller_framework">Spring MVC</a> 
will be necessary to understand the code.</p>

<ol>
	<li>A Spring configuration file maps the URI, <code>builder.uri</code> to the appropriate Spring controller.
	 Here is the relevant excerpt from this server's configuration file:</li>
<pre class="brush: xml; collapse: true">&lt;bean id="builderControl" class="buri.web.ddmsence.BuilderControl" /&gt;
&lt;bean id="urlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping"&gt;
   &lt;property name="urlMap"&gt;
      &lt;map&gt;
         &lt;entry key="builder.uri" value-ref="builderControl" /&gt;
      &lt;/map&gt;
   &lt;/property&gt;
&lt;/bean&gt;</pre>

	<li>A Spring controller, BuilderControl, handles incoming requests.</li>
<pre class="brush: java; collapse: true">package buri.web.ddmsence;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.Document;
import nu.xom.Serializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import buri.ddmsence.ddms.AbstractProducer;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ISMVocabulary;
import buri.ddmsence.util.Util;

/**
 * Controller class for building DDMS Records
 *
 * @author	Brian Uri!
 */
public class BuilderControl extends SimpleFormController {
	
   protected final Log logger = LogFactory.getLog(getClass());
               
    /**
     * Constructor
     */
    public BuilderControl() {
       setCommandName("resource");
       setCommandClass(Resource.Builder.class);
       setFormView("builder");
    }
    
    /**
     * @see SimpleFormController#formBackingObject(HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
       return (new Resource.Builder());
    }    
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    public Map&lt;String, Object&gt; referenceData(HttpServletRequest request) throws Exception {
       Map&lt;String, Object&gt; data = new HashMap&lt;String, Object&gt;();
       data.put("ownerProducers", ISMVocabulary.getEnumerationTokens(ISMVocabulary.CVE_OWNER_PRODUCERS));
       data.put("producerTypes", AbstractProducer.PRODUCER_TYPES);
       return (data);
    }  
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
   protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
      Resource.Builder builder = (Resource.Builder) command;
       Map&lt;String, Object&gt; model = new HashMap&lt;String, Object&gt;();
       try {
          Resource resource = builder.commit();
          if (resource == null)
             throw new InvalidDDMSException("No information was entered to create a DDMS Resource.");
          // Skipping resource.toXML() so I can control formatting.
          Document document = new Document(resource.getXOMElementCopy());
          ByteArrayOutputStream os = new ByteArrayOutputStream();
         Serializer serializer = new Serializer(os, "ISO-8859-1");
         serializer.setIndent(3);
         serializer.setMaxLength(120);
         serializer.write(document);          
          model.put("xml", os.toString());
         model.put("warnings", resource.getValidationWarnings());
         return (new ModelAndView("builderResult", "model", model));
      }
        catch (InvalidDDMSException e) {
           ValidationMessage message = ValidationMessage.newError(e.getMessage(), e.getLocator());
           String location = Util.isEmpty(message.getLocator()) ? "unknown location" : message.getLocator();
           errors.reject(null, null, "&lt;b&gt;" + message.getType() + "&lt;/b&gt; at &lt;code&gt;" + location + "&lt;/code&gt;: " + message.getText());
         }
         catch (Exception e) {
            errors.reject(e.getMessage());
         }
      return showForm(request, response, errors, model);       
    }
}</pre>
	<li>The BuilderControl starts by creating a new form bean, Resource.Builder, in the <code>formBackingObject()</code> method. 
	This is a Component Builder which supports the form you see on this page.</li>
	<li>The <a href="http://ddmsence.googlecode.com/svn/trunk/data/web/builder.jsp">initial form view</a> is rendered. This is the page you are currently viewing. The JSP file also contains the JavaScript code used for client-side validation (with jQuery).</li>
	<li>Once the form has been filled in and submitted, the <code>onSubmit()</code> method of the BuilderControl is called. This method commits the Resource.Builder.
	It will fail immediately with an <code>InvalidDDMSException</code> if the Resource is invalid.</li>
	<li>If the Resource is invalid, <code>showForm()</code> returns to the initial form view, displaying the errors and allowing for editing.</li>
	<li>If the Builder succeeds, the Resource is proven to be valid, although there may still be warnings. The warnings and the XML output of the Resource are stored in a Map,
	<code>model</code>,	which is then used to render the <a href="http://ddmsence.googlecode.com/svn/trunk/data/web/builderResult.jsp">Builder Results page</a>.</li>
</ol>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>