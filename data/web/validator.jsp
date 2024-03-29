<html>
<head>
	<%@page session="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Online DDMS Validator | DDMSence</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="The open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
	<meta name="keywords" content="DDMSence,DDMS,Online,Validator,Validation,DoD" />
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<script type="text/javascript" src="./shared/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="./shared/jquery.validate-1.8.1.min.js"></script>
	<script type="text/javascript">

	$(function($){
		// validate form on keyup and submit
		$("#record").validate({
			rules: {
				<c:if test="${record.type eq 'text'}">
					stringRecord: {
						required: true,
						maxlength: 50000
					}
				</c:if>
				<c:if test="${record.type eq 'url'}">
					url: {
						required: true,
						maxlength: 2000
					}
				</c:if>
			},
			messages: {
				<c:if test="${record.type eq 'text'}">
					stringRecord: {
						required: "A DDMS record is required.",
						maxlength: "The DDMS record cannot exceed 50,000 characters."
					}
				</c:if>
				<c:if test="${record.type eq 'url'}">
					url: {
						required: "A URL is required.",
						maxlength: "The URL cannot exceed 2000 characters."
					}
				</c:if>
			}
		});
	});

	function showExample(form) {
		<c:if test="${record.type eq 'text'}">
			exampleRecord = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<ddms:resource \n"
				+ "  xmlns:ddms=\"urn:us:mil:ces:metadata:ddms:5\" \n"
				+ "  xmlns:ism=\"urn:us:gov:ic:ism\" \n"
				+ "  xmlns:ntk=\"urn:us:gov:ic:ntk\" \n"
				+ "  xmlns:virt=\"urn:us:gov:ic:virt\" \n"
				+ "  ddms:compliesWith=\"DDMSRules\">\n"
				+ "  <ddms:metacardInfo ism:classification=\"U\" ism:ownerProducer=\"USA\">\n"
				+ "    <ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />\n"
				+ "    <ddms:dates ddms:created=\"2011-09-24\" />\n"
				+ "    <ddms:publisher>\n"
				+ "      <ddms:person>\n"
				+ "        <ddms:name>Brian</ddms:name>\n"
				+ "        <ddms:surname>Uri</ddms:surname>\n"
				+ "      </ddms:person>\n"
				+ "    </ddms:publisher>\n"
				+ "  </ddms:metacardInfo>\n"
				+ "  <ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence\"/>\n"
				+ "  <ddms:title ism:ownerProducer=\"USA\" ism:classification=\"U\">\n"
				+ "    DDMSence\n"
				+ "  </ddms:title>\n"
				+ "  <ddms:description ism:ownerProducer=\"USA\" ism:classification=\"U\">\n"
				+ "    The open-source Java library for DDMS\n"
				+ "  </ddms:description>\n"
				+ "  <ddms:language ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" \n"
				+ "    ddms:value=\"en\"/>\n"
				+ "  <ddms:dates ddms:created=\"2010-03-24\" ddms:posted=\"2010-03-24\" />\n"
				+ "  <ddms:rights ddms:copyright=\"true\" ddms:privacyAct=\"false\" \n"
				+ "    ddms:intellectualProperty=\"true\"/>\n"
				+ "  <ddms:creator ism:ownerProducer=\"USA\" ism:classification=\"U\">\n"
				+ "    <ddms:person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:person>\n"
				+ "  </ddms:creator>\n"
				+ "  <ddms:publisher ism:classification=\"U\" ism:ownerProducer=\"USA\">\n"
				+ "    <ddms:person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:person>\n"
				+ "  </ddms:publisher>\n"
				+ "  <ddms:pointOfContact>\n"
				+ "    <ddms:person>\n"
				+ "      <ddms:name>Brian Uri</ddms:name>\n"
				+ "      <ddms:name>Brian</ddms:name>\n"
				+ "      <ddms:surname>Uri</ddms:surname>\n"
				+ "      <ddms:email>ddmsence@urizone.net</ddms:email>\n"
				+ "    </ddms:person>\n"
				+ "  </ddms:pointOfContact>\n"
				+ "  <ddms:subjectCoverage>\n"
				+ "    <ddms:keyword ddms:value=\"DDMSence\"/>\n"
				+ "    <ddms:keyword ddms:value=\"metadata\"/>\n"
				+ "    <ddms:keyword ddms:value=\"discovery\"/>\n"
				+ "    <ddms:keyword ddms:value=\"DDMS\"/>\n"
				+ "    <ddms:keyword ddms:value=\"open source\"/>\n"
				+ "    <ddms:keyword ddms:value=\"Java\"/>\n"
				+ "    <ddms:category \n"
				+ "      ddms:qualifier=\"http://metadata.dod.mil/mdr/ns/TaxFG/0.75c/Core_Tax_0.75c.owl#Asset\" \n"
				+ "      ddms:code=\"Asset\" ddms:label=\"Asset\"/>\n"
				+ "  </ddms:subjectCoverage>\n"
				+ "  <ddms:temporalCoverage>\n"
				+ "    <ddms:approximableStart>\n"
				+ "      <ddms:description>Early 2010</ddms:description>\n"
				+ "      <ddms:approximableDate ddms:approximation=\"1st qtr\">2010</ddms:approximableDate>\n"
				+ "    </ddms:approximableStart>\n"
				+ "    <ddms:end>Unknown</ddms:end>\n"
				+ "  </ddms:temporalCoverage>\n"
				+ "</ddms:resource>"
			form.stringRecord.value = exampleRecord;
		</c:if>
		<c:if test="${record.type eq 'url'}">
			form.url.value = "raw.githubusercontent.com/briuri/ddmsence/master/data/sample/5.0-ddmsenceExample.xml";
		</c:if>
	}

	function changeType(newType) {
		if ('${record.type}' != newType)
			parent.location.href = "validator.uri?type=" + newType
	}

	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<a name="top"></a><h1>DDMS Validator</h1>

<p>This experimental tool uses the DDMSence library to validate <b>Unclassified</b> DDMS 2.0, 3.0, 3.1, 4.0.1, 4.1, and 5.0 records. Records
can be submitted by pasting XML text or referencing a URL.</p>
<p>Starred fields (<b>*</b>) are required.</p>

<form:form id="record" modelAttribute="record" method="post">
	<form:hidden path="type" />

	<label id="ltype" for="selectType">Record Location:</label>
	<span class="formElement">
		<select name="selectType" id="selectType" onchange="changeType(this.options[this.selectedIndex].value)">
			<option value="text"
				<c:if test="${record.type eq 'text'}"> selected="true" </c:if>
			>Text</option>
			<option value="url"
				<c:if test="${record.type eq 'url'}"> selected="true" </c:if>
			>URL</option>
		</select>
	</span><br />

	<c:if test="${record.type eq 'text'}">
		<label id="lstringRecord" for="stringRecord">DDMS Record: *</label>
		<form:textarea path="stringRecord" rows="16" cols="80" />
	</c:if>
	<c:if test="${record.type eq 'url'}">
		<label id="lurl" for="url">DDMS URL: *</label>
		https://<form:input path="url" size="50" maxlength="2000" />
	</c:if>
	<br /><br />
	<span class="formElement">
		<input type="submit" class="button" name="submit" value="Submit">
		<input type="reset" class="button" name="reset" value="Reset">
		<c:if test="${record.type eq 'text' or record.type eq 'url'}">
			<input type="button" class="button" name="example" value="Example" onclick="showExample(this.form)">
		</c:if>
	</span>
</form:form>

<h3>How This Works</h3>

<p>Compilable source code for this tool is not bundled with DDMSence, because it has dependencies on the Spring Framework (v6.1.2). However, all of the pieces you need create
a similar web application are shown below. A basic understanding of <a href="http://en.wikipedia.org/wiki/Spring_Framework#Model-view-controller_framework">Spring MVC</a>
will be necessary to understand the code.</p>

<ol>
	<li>A Spring controller, ValidatorControl, handles incoming requests at the URI, <code>validator.uri</code>.
	The <code>type</code> parameter is used to determine what sort of form should be displayed -- changing
	the "Record Location" drop-down selection redraws the form.</li>
<pre class="brush: java; collapse: true">package buri.urizone.web.control.ddmsence;

import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.UnsupportedVersionException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSReader;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import jakarta.servlet.http.HttpServletRequest;
import nu.xom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller class for validating DDMS Records
 *
 * @author Brian Uri!
 */
@Controller
@SessionAttributes({ "record" })
public class ValidatorControl extends AbstractControl {

	/**
	 * Entry point for creating a new form
	 */
	@RequestMapping(value = "/validator.uri", method = RequestMethod.GET)
	public String newForm(
		@RequestParam(value = "type", required = false, defaultValue = ValidatorRecord.DEFAULT_TYPE) String type,
		Model model) {
		model.addAttribute("record", new ValidatorRecord(type));
		return ("validator");
	}

	/**
	 * Entry point for validating the form contents
	 */
	@RequestMapping(value = "/validator.uri", method = RequestMethod.POST)
	public String validate(@ModelAttribute("record") ValidatorRecord record, BindingResult result,
		SessionStatus status, Model model, HttpServletRequest request) {
		String stringRepresentation = null;
		try {
			if (ValidatorRecord.TYPE_TEXT.equals(record.getType())) {
				stringRepresentation = record.getStringRecord();
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
			model.addAttribute("xml", stringRepresentation);

			DDMSVersion version = guessVersion(stringRepresentation);
			PropertyReader.setProperty("output.json.prettyPrint", "true");
			PropertyReader.setProperty("output.indexLevel", "1");
			Resource resource = new DDMSReader(version).getDDMSResource(stringRepresentation);
			if (isUnclassified(resource)) {
				model.addAttribute("warnings", resource.getValidationWarnings());
				model.addAttribute("html", resource.toHTML());
				model.addAttribute("text", resource.toText());
				model.addAttribute("json", resource.toJSON());
			}
			else {
				model.addAttribute("xml", null);
				throw new InvalidDDMSException("This tool can only be used on Unclassified data.");
			}
		}
		catch (InvalidDDMSException e) {
			ValidationMessage message = ValidationMessage.newError(e.getMessage(), e.getLocator());
			model.addAttribute("error", message);
		}
		catch (Exception e) {
			ValidationMessage message = ValidationMessage.newError(e.getMessage(), null);
			model.addAttribute("error", message);
		}
		return ("validatorResult");
	}

	/**
	 * Helper method to attempt to guess which version of DDMS to use, based
	 * upon the namespace URI of the root element, via a non-validating builder.
	 *
	 * @param potentialResource a String containing the resource
	 * @return the version
	 * @throws UnsupportedVersionException if the version could not be guessed.
	 * @throws InvalidDDMSException if the file could not be parsed.
	 */
	private DDMSVersion guessVersion(String potentialResource) throws InvalidDDMSException {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader(PropertyReader.getProperty("xml.reader.class"));
			nu.xom.Builder builder = new nu.xom.Builder(reader, false);
			Document doc = builder.build(new StringReader(potentialResource));
			String namespace = doc.getRootElement().getNamespaceURI();
			return (DDMSVersion.getVersionForNamespace(namespace));
		}
		catch (Exception e) {
			throw new InvalidDDMSException("Could not create a valid element from potential resource: "
				+ e.getMessage());
		}
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
	<li>The ValidatorControl starts by creating a new form bean, ValidatorRecord, in the <code>newForm()</code> method.
	This is a simple data class which supports the form you see on this page.</li>
<pre class="brush: java; collapse: true">package buri.urizone.web.control.ddmsence;

import buri.ddmsence.util.Util;

/**
 * Form bean for online DDMS validation
 *
 * @author Brian Uri!
 */
public class ValidatorRecord {

	public static final String TYPE_TEXT = "text";
	public static final String TYPE_URL = "url";
	public static final String DEFAULT_TYPE = TYPE_TEXT;

	private String _type;
	private String _stringRecord;
	private String _url;

	/**
	 * Constructor
	 *
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
	 * Accessor for the type (url, text)
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
}
</pre>
	<li>The <a href="https://github.com/briuri/ddmsence/blob/master/data/web/validator.jsp">initial form view</a> is rendered. This is the page you are currently viewing. The JSP file also contains the JavaScript code used for client-side validation (with jQuery).</li>
	<li>Once the form has been filled in and submitted, the <code>validate()</code> method of the ValidatorControl is called. This method checks to see whether the DDMS
	Resource is coming in as text or a URL. URLs are loaded and converted into text.</li>
	<li>The DDMSReader method, <a href="/docs/buri/ddmsence/util/DDMSReader.html"><code>getDDMSResource()</code></a> attempts to build the entire DDMS Resource.
	It will fail immediately with an <code>InvalidDDMSException</code> if the Resource is invalid.</li>
	<li>If the constructor succeeds, the Resource is proven to be valid, although there may still be warnings. The Map containing errors or warnings, <code>model</code>,
	is then used to render the <a href="https://github.com/briuri/ddmsence/blob/master/data/web/validatorResult.jsp">Validation Results page</a>.</li>
</ol>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#explorations">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>