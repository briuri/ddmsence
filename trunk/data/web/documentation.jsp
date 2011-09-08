<html>
<head>
	<title>DDMSence: Documentation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Documentation</h1>
<img src="./images/splashImage.jpg" width="302" height="210" title="DDMSence" align="right" />

<h2>Table of Contents</h2>

<ul>
	<li><a href="#started">Getting Started</a></li><ul>
		<li><a href="#samples">Sample Applications</a></li>
		<li><a href="#javadoc">JavaDoc API Documentation</a></li>
		</ul>	
	<li><a href="#design">Design Decisions</a></li>
	<li><a href="#tips">Power Tips</a></li><ul>
		<li><a href="#tips-version">Working with Different DDMS Versions</a></li>
		<li><a href="#tips-attributes">IC and GML Attribute Groups</a></li>
		<li><a href="#tips-extensible">The Extensible Layer</a></li>
		<li><a href="#tips-builders">Using Component Builders</a></li>
		<li><a href="#tips-schematron">Schematron Validation</a></li>
		<li><a href="#tips-configuration">Configurable Properties</a></li>
		</ul>
	<li><a href="#explorations">Explorations</a></li>
	<li><a href="#contributors">Contributors</a></li>
	<li><a href="#feedback">Feedback</a></li>
</ul>
<div class="clear"></div>

<a name="started"></a><h3>Getting Started</h3>

<p>Begin by visiting the <a href="downloads.jsp">Downloads</a> page and downloading a copy of DDMSence. The tutorials below will assume that you are working
with the "bin"-flavored download, which comes with the DDMSence JAR files pre-compiled, and contains source code for the sample applications.</p>

<p>Unzip the downloaded archive in a directory of your choice. You can then run the samples from the command line by entering the commands below (or by pasting them
into a batch/shell script and running that script):</p>

<pre class="brush: xml">REM Windows Commands
cd &lt;folderWhereDDMSenceIsUnzipped&gt;\ddmsence-bin-@ddmsence.version@
set DDMSENCE_CLASSPATH=lib/serializer-2.7.1.jar;lib/xalan-2.7.1.jar;lib/xercesImpl-2.9.1.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/xml-apis-1.3.04.jar;lib/xom-1.2.6.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/ddmsence-@ddmsence.version@.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/ddmsence-samples-@ddmsence.version@.jar
java -cp %DDMSENCE_CLASSPATH% buri.ddmsence.samples.Essentials</pre>
<p class="figure">Figure 1. Running from a Windows/DOS Command Line</p>

<pre class="brush: xml">#!/bin/sh
# Linux Commands
cd &lt;folderWhereDDMSenceIsUnzipped&gt;/ddmsence-bin-@ddmsence.version@
ddmsence_classpath=lib/serializer-2.7.1.jar:lib/xalan-2.7.1.jar:lib/xercesImpl-2.9.1.jar
ddmsence_classpath=$ddmsence_classpath:lib/xml-apis-1.3.04.jar:lib/xom-1.2.6.jar
ddmsence_classpath=$ddmsence_classpath:lib/ddmsence-@ddmsence.version@.jar
ddmsence_classpath=$ddmsence_classpath:lib/ddmsence-samples-@ddmsence.version@.jar
java -cp $ddmsence_classpath buri.ddmsence.samples.Essentials</pre>
<p class="figure">Figure 2. Running in Linux</p>

<p>Note: The syntax for setting a classpath in Linux may vary, depending on the shell you are using. If you are using an older version of DDMSence, make sure
to change the version numbers in the classpath examples.</p>

<a name="samples"></a><h4>Sample Applications</h4>
<!-- DDMScargo(t), DDMScrow, DDMSophagus, DDMStrogen, DDMStuary, DDMSquire, DDMSteem, DDMStablishment, DDMStimator, DDMScalator, DDMState -->

<p>
<img src="./images/essentials-thumb.png" width="300" height="199" title="Essentials Screenshot" align="right" />
DDMSence comes with three sample applications. The intention of these applications is to highlight notable aspects of DDMSence, not to create
real-world solutions.</p>

<h5>Essentials</h5>

<p><u>Essentials</u> is a simple reader application which loads an XML file containing a DDMS Resource and displays it in four different formats: the
original XML, HTML, Text, and Java code (which you can reuse to build your own components from scratch). The source code for this application provides
an example of how to create DDMS components from an XML file.</p>

<ul>
	<li><a href="tutorials-01.jsp">Tutorial #1: Essentials</a></li>
</ul>

<h5>Escort</h5>

<p><u>Escort</u> is a step-by-step wizard for building a DDMS Resource from scratch, and then saving it to a file. The source code for this application
shows an example of how the Java object model can be built with simple data types.</p>

<ul>
	<li><a href="tutorials-02.jsp">Tutorial #2: Escort</a></li>
</ul>

<img src="./images/escape-thumb.png" width="300" height="198" title="Escape Screenshot" align="right" />

<h5>Escape</h5>

<p><u>Escape</u> is a tool that traverses multiple DDMS Resource files and then exposes various statistics about them through the 
<a href="http://code.google.com/apis/visualization/documentation/gallery.html" target="_new">Google Visualization API</a>. Charts in this sample
application are non-interactive, but provide the foundation for more complex cases: for example, it would
be possible to plot Temporal Coverage on an 
<a href="http://code.google.com/apis/visualization/documentation/gallery/annotatedtimeline.html" target="_new">Annotated Timeline</a>, or Geospatial Coverage on 
<a href="http://code.google.com/apis/visualization/documentation/gallery/map.html" target="_new">Google Maps</a>. While the first two sample applications were 
designed to teach developers how DDMSence works, the intent of this one is to provide brainstorming ideas for leveraging DDMS in other contexts.</p>
	
<ul>
	<li><a href="tutorials-03.jsp">Tutorial #3: Escape</a></li>
</ul>

<div class="clear"></div>
	
<a name="javadoc"></a><h4>JavaDoc API Documentation</h4>

<img src="./images/docSample.png" width="310" height="231" title="JavaDoc sample" align="right" />

<ul>
	<li><a href="/docs/">Online API Documentation</a></li>
</ul>
The API documentation is also bundled with the "bin"-flavored download on the <a href="downloads.jsp">Downloads</a> page, and can be generated from source code 
in the "src"-flavored download. You should be aware of the following sections, which appear on every DDMS component's page:
<ul>
	<li>The class description describes cases where DDMSence is stricter than the DDMS specification, and allowed cases which are legal, but nonsensical. If this varies
	for different versions of DDMS, the version number will be indicated. If no version number is listed, the constraint applies to all versions.</li>
	<li>The class description describes any nested elements or attributes for the implemented component and a link to the DDMS website for the complete specification.</li>
	<li>The <code>validate()</code> method description lists the specific rules used to validate a component. This section may be useful when building your own components from scratch.</li>
	<li>If a component has any conditions that result in warnings, the <code>validateWarnings()</code> method description lists the specific conditions that trigger a warning.</li>
</ul>
<div class="clear"></div>

<a name="design"></a><h3>Design Decisions</h3>

<h4>Components Deserving an Object</h4>

<p>Not all of the elements defined in the DDMS schema are implemented as Java Objects. Many elements are defined globally but only used once, or exist merely as wrappers for other components. I have
followed these rules to determine which components are important enough to deserve a full-fledged Object:</p>

<ul>
	<li>Elements which are explicitly declared as DDMS Categories in the DDMS documentation are always implemented (<code>ddms:identifier</code>).</li>
	<li>Elements which merely enclose important data AND which have no special attributes are never implemented (<code>ddms:Media</code>).</li>
	<li>Data which can be represented as a simple Java type AND which has no special attributes is represented as a simple Java type (<code>ddms:email</code>).</li>
	<li>Attributes are generally implemented as properties on an Object. The exceptions to this are the 
		<a href="/docs/index.html?buri/ddmsence/ddms/security/ism/SecurityAttributes.html">ISM Attribute Group</a>, 
		which decorates many DDMS components, and the <a href="/docs/index.html?buri/ddmsence/ddms/summary/gml/SRSAttributes.html">SRS Attribute Group</a>,
		which decorates components in the GML profile.</li>
</ul>

<h4>Empty String vs. No Value</h4>

<p>When analyzing <code>xs:string</code>-based components, DDMSence treats the absence of some element/attribute in the same manner as it would treat that element/attribute if it were
present but had an empty string as a value. The DDMS schema generally uses <code>xs:string</code> without length restrictions, so an empty string is syntactically correct, even if the
resulting data is not logical. To provide some consistency in this library, I have tried to follow these rules:</p>

<ul>
	<li>A string-based accessor will always return an empty string instead of a <code>null</code> entity. This means that a missing element and an element with no value
	in an XML file are treated identically.</li>
	<li>If the DDMS specification marks a component as Mandatory, but the schema allows an empty string, DDMSence will also require that the component have a non-empty value.</li> 
	<li>If the DDMS specification marks a component as Optional, DDMSence will never be stricter than the DDMS schema. This could result in illogical constructs
	that are legal according to the schema, but I wanted to minimize the cases where this library might interfere with existing records.</li>
</ul>

<h4>Immutability</h4>

<p>All DDMS components are implemented as immutable objects, which means that their values cannot be changed after instantiation. Because the components are
validated during instantiation, this also means that it is impossible to have an invalid component at any given time: a component is either confirmed to be 
valid or does not exist. The <a href="#tips-builders">Component Builder</a> framework can be used to build DDMS Resources piece by piece, saving validation until the end.</li>
</p>

<h4>Constructor Parameter Order</h4>

<p>Because DDMS components are built in single-step constructors to support immutability, parameter lists can sometimes exceed more than a handful of information. 
The following convention is used to provide some consistency:</p>

<ul>
	<li>On constructors which build components from raw data:</li>
		<ul>
			<li>The data or components needed to construct any nested elements or child text comes next (such as the list of Keywords in a <a href="/docs/buri/ddmsence/ddms/summary/SubjectCoverage.html">SubjectCoverage</a> component).</li>
			<li>The data needed to construct any attributes comes next (such as the <a href="/docs/index.html?buri/ddmsence/ddms/security/ism/SecurityAttributes.html">ISM SecurityAttributes</a>).</li>
			<li>Any remaining information that DDMSence needs comes last (such as the boolean flag on a <a href="/docs/index.html?buri/ddmsence/ddms/summary/PostalAddress.html">PostalAddress</a> which toggles between states and provinces).</li>
		</ul>
	<li>On constructors which build components from XML files, a XOM element is generally the only parameter. Additional information is implicitly
	loaded from the XOM element. </li>
</ul>

<h4>Thread Safety</h4>

<p>Other than the immutability of objects, no special effort went into making DDMSence thread-safe, and no testing was done on its behavior in multithreaded environments.</p>

<h4>Accessor Consistency Across Versions</h4>

<p>Some attributes, such as <code>ISM:excludeFromRollup</code> and <code>ISM:resouceElement</code> are new in DDMS v3.0. When the accessors for these attributes are
called on a DDMS 2.0 component, a null value will be returned. This decision allows DDMS records of varying versions to be
traversed and queried in the same manner, without requiring too much knowledge of when specific attributes were introduced.</p>

<a name="tips"></a><h3>Power Tips</h3>

<div class="toc">
	<b><u>Table of Contents</u></b>
	<li><a href="#tips-version">Working with Different DDMS Versions</a></li>
	<li><a href="#tips-attributes">IC and GML Attribute Groups</a></li>
	<li><a href="#tips-extensible">The Extensible Layer</a></li>
	<li><a href="#tips-builders">Using Component Builders</a></li>
	<li><a href="#tips-schematron">Schematron Validation</a></li>
	<li><a href="#tips-configuration">Configurable Properties</a></li>
</div>
		
<a name="tips-version"></a><h4>Working with Different DDMS Versions</h4>

 <h5>Using Alternate Versions</h5>
 
<p>DDMSence currently supports three versions of DDMS: 2.0, 3.0, and 3.1. When building DDMS components from XML files, the 
DDMSReader class can automatically use the correct version of DDMS based on the XML namespace defined in the file.
When building DDMS components from scratch, the <a href="/docs/index.html?buri/ddmsence/util/DDMSVersion.html">DDMSVersion</a>
class controls the version being used. There is an instance of DDMSVersion for each supported version, and this 
instance contains the specific XML namespaces used for DDMS, GML, and ISM components.</p>

<div class="clear"></div>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
DDMSVersion version = DDMSVersion.getCurrentVersion();
System.out.println("In DDMS " + version.getVersion() + ", the following namespaces are used: ");
System.out.println("ddms: " + version.getNamespace());
System.out.println("gml: " + version.getGmlNamespace());
System.out.println("ISM: " + version.getIsmNamespace());
System.out.println("Are we using DDMS 2.0? " + DDMSVersion.isCurrentVersion("2.0"));
System.out.println("If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, "
   + "I know we are using DDMS "
   + DDMSVersion.getVersionForDDMSNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion());

Identifier identifier = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForDDMSNamespace(identifier.getNamespace()));

DDMSVersion.setCurrentVersion("3.0");
Identifier identifier2 = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForDDMSNamespace(identifier.getNamespace()));</pre>
<p class="figure">Figure 3. Using a different version of DDMS</p>

<pre class="brush: xml">In DDMS 2.0, the following namespaces are used: 
ddms: http://metadata.dod.mil/mdr/ns/DDMS/2.0/
gml: http://www.opengis.net/gml
ISM: urn:us:gov:ic:ism:v2
Are we using DDMS 2.0? true
If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, I know we are using DDMS 3.0
This identifier was created with DDMS 2.0
This identifier was created with DDMS 3.0</pre>
<p class="figure">Figure 4. Output of the code in Figure 3</p>

<p>Calling <code>DDMSVersion.setCurrentVersion("2.0")</code> will make any components you create from that point on obey DDMS 2.0 
validation rules. The default version if you never call this method is "3.1" (but as a best practice, you should always explicitly set the current version yourself,
in case this default changes in the future). The version is maintained as a static variable, so this 
is not a thread-safe approach, but I believe that the most common use cases will deal with DDMS components of a single version at a time,
and I wanted the versioning mechanism to be as unobtrusive as possible.</p>

<p>DDMS release 3.0.1 was merely a documentation release which clarified some of the supporting documentation on geospatial elements. Because none of the 
schemas or components themselves were updated, 3.0.1 reuses all of the same technical information from 3.0 (ncluding XML namespaces). DDMSence treats 3.0.1 as an alias for DDMS 3.0 --
you can set your DDMS version to 3.0.1, but DDMSence will continue to use DDMS 3.0 artifacts.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("3.0.1");
System.out.println(DDMSVersion.getCurrentVersion().getVersion());
</pre>
<p class="figure">Figure 5. This code will print out "3.0".</p>

<h5>Differences Between Versions</h5>

<p>The validation rules between versions of DDMS are very similar, but there are a few major differences. For example, the Unknown
entity for producers was not introduced until DDMS 3.0, so attempts to create one in DDMS 2.0 will fail.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
List&lt;String&gt; names = new ArrayList&lt;String&gt;();
names.add("Unknown Entity");
Unknown unknown = new Unknown("creator", names, null, null, null);</pre>
<p class="figure">Figure 6. This code will throw an InvalidDDMSException</p>

<p>The tables below identify the key differences between supported versions of DDMS components in DDMSence.</p>
<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 2.0 Notes</th><th width="32%">DDMS 3.0 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/2.0/</code></td><td>Changed to <code>http://metadata.dod.mil/mdr/ns/DDMS/3.0/</code></td></tr>
<tr><td><code>ddms:category/@*</code></td><td>No extensible attributes.</td><td>Supports optional extensible attributes.</td></tr>
<tr><td><code>ddms:geospatialCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:keyword/@*</code></td><td>No extensible attributes.</td><td>Supports optional extensible attributes.</td></tr>
<tr><td><code>ddms:Resource/@ISM:classification<br />ddms:Resource/@ISM:createDate<br />ddms:Resource/@ISM:ownerProducer<br />ddms:Resource/@ISM:resourceElement</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Required.</td></tr>
<tr><td><code>ddms:Resource/@ISM:DESVersion</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Required. DESVersion should be 2.</td></tr>
<tr><td><code>ddms:Resource/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:Resource/*</code></td><td>Supports zero or one extensible elements.</td><td>Supports zero to many extensible elements.</td></tr>
<tr><td><code>ddms:security/@ISM:excludeFromRollup</code></td><td>Cannot exist.</td><td>Required. Value must be "true".</td></tr>
<tr><td><code>ddms:source/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:subjectCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:temporalCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:Unknown</code></td><td>Cannot exist.</td><td>New producer entity introduced.</td></tr>
<tr><td><code>ddms:virtualCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
</table>
<p class="figure">Table 1. Component changes from DDMS 2.0 to DDMS 3.0</p>
<br /><br />

<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.0 Notes</th><th width="32%">DDMS 3.1 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.0/</code></td><td>Changed to <code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td></tr>
<tr><td><code>ddms:dates/@ddms:approvedOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:Resource/@ISM:DESVersion</td><td>Required. DESVersion should be 2.</td><td>Required. DESVersion must be 5.</td></tr>
</table>
<p class="figure">Table 2. Component changes from DDMS 3.0 to DDMS 3.1</p>
<br /><br />

<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.1 Notes</th><th width="32%">DDMS 4.0 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td><td>Changed to <code>urn:us:mil:ces:metadata:ddms:4</code></td></tr>
<tr><td><code>ddms:boundingBox/ddms:westBL<br />ddms:boundingBox/ddms:eastBL<br />ddms:boundingBox/ddms:southBL<br />ddms:boundingBox/ddms:northBL</td><td>Previously called "WestBL", "EastBL", "SouthBL", and "NorthBL".</td><td>Renamed.</td></tr>
<tr><td><code>ddms:category/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:creator/@ddms:POCType<br />ddms:contributor/@ddms:POCType<br />ddms:pointOfContact/@ddms:POCType<br />ddms:publisher/@ddms:POCType</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:dates/@ddms:receivedOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:format/ddms:Media</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:geographicIdentifier/ddms:subDivisionCode</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:geospatialCoverage/ddms:GeospatialExtent</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:geospatialCoverage/@ddms:precedence<br />ddms:geospatialCoverage/@ddms:order</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:keyword/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:link/@xlink:role</td><td>Could be empty.</td><td>Cannot be empty.</td></tr>
<tr><td><code>ddms:link/@xlink:label</td><td>Was an <code>xsd:string</code>.</td><td>Must be an <code>xsd:NCName</code>.</td></tr>
<tr><td><code>ddms:link/@ISM:classification<br />ddms:link/@ISM:ownerProducer</td><td>Cannot exist.</td><td>Required when the link is part of a ddms:revisionRecall element.</td></tr>
<tr><td><code>ddms:link/@ISM:*</td><td>Cannot exist.</td><td>Optional when the link is part of a ddms:revisionRecall element.</td></tr>
<tr><td><code>ddms:organization</td><td>Previously called "Organization"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:organization/@ddms:acronym</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:organization/ddms:subOrganization</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:person</td><td>Previously called "Person"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:relatedResources</td><td>Wraps around ddms:relatedResource elements</td><td>Removed.</td></tr>
<tr><td><code>ddms:relatedResource/@ddms:relationship</td><td>Cannot exist.</td><td>Required.</td></tr>
<tr><td><code>ddms:relatedResource/@ddms:direction</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:relatedResource/@ISM:*</td><td>Cannot exist.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:resource</td><td>Previously called "Resource".</td><td>Renamed.</td></tr>
<tr><td><code>ddms:resource/ddms:metacardInfo</td><td>Cannot exist.</td><td>New required component introduced (contains many other new components).</td></tr>
<tr><td><code>ddms:resource/ddms:resourceManagement</td><td>Cannot exist.</td><td>New optional component introduced (contains many other new components).</td></tr>
<tr><td><code>ddms:resource/ddms:subjectCoverage</td><td>Supports exactly 1 element.</td><td>Supports one to many elements.</td></tr>
<tr><td><code>ddms:resource/@ISM:DESVersion</td><td>Required. DESVersion must be 5.</td><td>Required. DESVersion must be 7.</td></tr>
<tr><td><code>ddms:resource/@ISM:noticeDate<br />ddms:resource/@ISM:noticeReason<br />ddms:resource/@ISM:noticeType<br />ddms:resource/@ISM:unregisteredNoticeType</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Optional.</td></tr>
<tr><td><code>ddms:resource/@NTK:DESVersion</td><td>Unsupported, but can exist as extensible attributes.</td><td>Required. DESVersion must be 5.</td></tr>
<tr><td><code>ddms:security/ddms:noticeList<br />ddms:security/NTK:Access</td><td>Cannot exist.</td><td>Optional (contains many other new components).</td></tr>
<tr><td><code>ddms:service</td><td>Previously called "Service"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:subjectCoverage/ddms:nonStateActor<br />ddms:subjectCoverage/ddms:productionMetric</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:subjectCoverage/ddms:Subject</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:temporalCoverage/ddms:TimePeriod</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:type</td><td>Has no child text.</td><td>Optional child text.</td></tr>
<tr><td><code>ddms:type/@ISM:*</td><td>Cannot exist.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:unknown</td><td>Previously called "Unknown"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:verticalExtent/ddms:minVerticalExtent<br />ddms:verticalExtent/ddms:maxVerticalExtent</td><td>Previously called "MinVerticalExtent" and "MaxVerticalExtent".</td><td>Renamed.</td></tr>
</table>
<p class="figure">Table 3. Component changes from DDMS 3.1 to DDMS 4.0</p>
<br /><br />

<p>The table below lists the complete set of ISM security attributes, and shows which attributes can be used with each version of DDMS.</p>

<table>
<tr><th>Attribute</th><th>DDMS 2.0 (ISM V2-PR)</th><th>DDMS 3.0 (ISM V2)</th><th>DDMS 3.1 (ISM V5)</th><th>DDMS 4.0 (ISM V7)</tr>
<tr><td><code>@ISM:atomicEnergyMarkings</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classification</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classificationReason</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:compilationReason</code></td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:compliesWith</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:dateOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:declassDate</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassEvent</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassException</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassManualReview</code></td><td>Yes</td><td>No</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:derivativelyClassifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:derivedFrom</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:displayOnlyTo</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:disseminationControls</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceOpen</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceProtected</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonICmarkings</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonUSControls</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:ownerProducer</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:pocType</code></td><td>No</td><td>No</td><td>No</td><td>Yes</td></tr>
<tr><td><code>@ISM:releasableTo</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SARIdentifier</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SCIcontrols</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:typeOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td></tr></table>
<p class="figure">Table 4. Security Attribute changes from DDMS 2.0 to DDMS 4.0</p>

<div class="powerTipDivider"></div>

<a name="tips-attributes"></a><h4>IC and GML Attribute Groups</h4>

<h5>ISM Security Attributes</h5>

<p>
ISM security attributes are defined in the Intelligence Community's "XML Data Encoding Specification for Information Security Marking Metadata" document (DES) and
implemented in the <a href="/docs/index.html?buri/ddmsence/ddms/security/ism/SecurityAttributes.html">SecurityAttributes</a> class. This class encapsulates
the ISM attributes which can decorate various DDMS components, such as <code>ddms:Resource</code> or <code>ddms:security</code>. The constructor which builds
the attributes from a XOM element will simply load these attributes from the element itself. The constructor which builds the attributes from raw data is defined as:

<pre class="brush: java">public SecurityAttributes(String classification, List&lt;String&gt; ownerProducers, Map&lt;String,String&gt; otherAttributes)</pre>
<p class="figure">Figure 7. SecurityAttributes constructor</p>

<p>Because the <code>classification</code> and <code>ownerProducers</code> are the most commonly referenced attributes, they are explicit parameters. Any other
attribute can be added in the String-based map called <code>otherAttributes</code>. If an attribute does not match an
expected attribute name, it is ignored. Here is an example which creates Confidential markings and puts them on a <code>ddms:title</code> element:</p>

<pre class="brush: java">List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("USA");
ownerProducers.add("AUS");
Map&lt;String, String&gt; otherAttributes = new HashMap&lt;String, String&gt;();
otherAttributes.put("SCIcontrols", "SI");
otherAttributes.put("SARIdentifier", "SAR-USA");
// The next line will be ignored, because the "classification" parameter takes precedence.
otherAttributes.put("classification", "U"); 
SecurityAttributes security = new SecurityAttributes("C", ownerProducers, otherAttributes);
Title title = new Title("My Confidential Notes", security);
System.out.println(title.toXML());</pre>
<p class="figure">Figure 8. Code to generate SecurityAttributes</p>

<p>Note: The actual values assigned to each attribute in Figure 8 are for example's sake only, and might be illogical in real-world metadata.</p>

<pre class="brush: xml">&lt;ddms:title xmlns:ddms="http://metadata.dod.mil/mdr/ns/DDMS/3.0/" xmlns:ISM="urn:us:gov:ic:ism" 
   ISM:classification="C" ISM:ownerProducer="USA AUS" ISM:SCIcontrols="SI"
   ISM:SARIdentifier="SAR-USA"&gt;
   My Confidential Notes
&lt;/ddms:title&gt;</pre>
<p class="figure">Figure 9. The resultant XML element with security attributes</p>

<p>The DES also defines many logical constraints on these attributes, but DDMSence does not validate these rules today. A set of Schematron files is bundled with ISM.XML V5 (which is used by DDMS 3.1),
and sample code for using DDMSence with these files can be found below in the <a href="#tips-schematron">Schematron Validation</a> Power Tip.</p>

<p>The values assigned to some attributes must come from the Controlled Vocabulary Enumerations (CVEs) defined by the Intelligence Community. The 
enumerations used by DDMSence are taken from Public Release versions of ISM.XML, so DDMSence will not be able to recognize enumeration values from 
higher classification levels. This restriction will be addressed in a future release.</p>

<h5>SRS Attributes</h5>

<p>Spatial Reference System (SRS) attributes are defined in the DDMS' GML Profile and implemented as an <a href="/docs/index.html?buri/ddmsence/ddms/summary/gml/SRSAttributes.html">SRSAttributes</a> class.
They can be applied to <code>gml:Point</code>, <code>gml:Polygon</code>, and <code>gml:pos</code>.</p>

<pre class="brush: java">SRSAttributes(String srsName, Integer srsDimension, List&lt;String&gt; axisLabels,
   List&lt;String&gt; uomLabels)</pre>
<p class="figure">Figure 10. SRSAttributes constructor</p>

<p>Here is an example which creates SRS attributes on a <code>gml:pos</code> element:</p>

<pre class="brush: java">List&lt;String&gt; axisLabels = new ArrayList&lt;String&gt;();
axisLabels.add("X");
axisLabels.add("Y");
List&lt;String&gt; uomLabels = new ArrayList&lt;String&gt;();
uomLabels.add("Meter");
uomLabels.add("Meter");
SRSAttributes srsAttributes = new SRSAttributes("http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D",
   new Integer(10), axisLabels, uomLabels);
List&lt;Double&gt; coordinates = new ArrayList&lt;Double&gt;();
coordinates.add(new Double(32.1));
coordinates.add(new Double(40.1));
Position position = new Position(coordinates, srsAttributes);
System.out.println(position.toXML());</pre>
<p class="figure">Figure 11. Code to generate SRSAttributes</p>

<pre class="brush: xml">&lt;gml:pos srsName="http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D" srsDimension="10" 
   axisLabels="X Y" uomLabels="Meter Meter"&gt;32.1 40.1&lt;/gml:pos&gt;</pre>
<p class="figure">Figure 12. The resultant XML element with SRS attributes</p>
  
<p>Please note that the SRSAttributes do not belong in any XML namespace -- this is correct according to the DDMS GML Profile.</p>

<div class="powerTipDivider"></div>

<a name="tips-extensible"></a><h4>The Extensible Layer</h4>

<p>DDMS is composed of four Core Sets (Security, Resource, Summary Content, and Format) and the Extensible Layer. This layer supports extensibility
by providing space for custom attributes and elements within a <code>ddms:Resource</code>. Specifically, custom attributes can be added to any producer
(Organization, Person, Service, and Unknown), a Keyword, a Category, and the Resource itself. A Resource can also have an unlimited number of custom
elements after the <code>ddms:security</code> element. These extensions are identified by the <code>xs:any</code> and <code>xs:anyAttribute</code>
definitions in the schema. The main restriction on content is that custom elements and attributes must belong to an XML namespace other than the
DDMS namespace.</p>

<p>Because any manner of content could appear in the Extensible Layer, DDMSence merely provides a consistent interface to access to the underlying 
XOM Elements and Attributes. Any business logic to be performed on this Layer is left up to the implementation, so some knowledge of 
<a href="http://xom.nu/">XOM</a> will be useful. </p>

<p>The relevant code can be found in the <code>buri.ddmsence.ddms.extensible</code> package. It may also be useful to load the sample file,  
<code>3.0-extensibleLayerExample.xml</code> into the <u>Essentials</u> application, because it has an example of each extension.</p>

<h5>ExtensibleElements</h5>

<p>An unlimited number of elements from any XML namespace other than the DDMS namespace can appear at the end of a <code>ddms:Resource</code>. (In DDMS 2.0,
only 1 can appear). These elements are implemented with the <a href="/docs/index.html?buri/ddmsence/ddms/extensible/ExtensibleElement.html">ExtensibleElement</a> class,
which acts like any other IDDMSComponent and exposes <code>getXOMElementCopy()</code> to return a copy of the underlying XOM Element. Below is an
example of an extensible element as it might appear in an XML file.</p> 

<pre class="brush: xml">   [...]
   &lt;/ddms:subjectCoverage&gt;
   &lt;ddms:security ISM:ownerProducer="USA" ISM:classification="U" ISM:excludeFromRollup="true"/&gt;
   &lt;ddmsence:extension xmlns:ddmsence="http://ddmsence.urizone.net/"&gt;
      This is an extensible element.
   &lt;/ddmsence:extension&gt;
&lt;/ddms:Resource&gt;</pre>
<p class="figure">Figure 13. An extensible element as it would appear in a ddms:Resource</p>

<p>Unlike most DDMS components, which have a constructor for XOM elements and a constructor for raw data, ExtensibleElement only has one constructor
(since the raw data is, itself, a XOM element). If you are using a DDMSReader instance to load data from an XML file, the ExtensibleElements will be created automatically,
and can be accessed with <code>Resource.getExtensibleElements()</code>. Here is an example of how you might build a simple one from scratch:</p>

<pre class="brush: java">Element element = new Element("ddmsence:extension", "http://ddmsence.urizone.net/");
element.appendChild("This is an extensible element.");
ExtensibleElement component = new ExtensibleElement(element);</pre>
<p class="figure">Figure 14. Creating a simple ExtensibleElement from scratch</p>

<p>Once you have an ExtensibleElement, you can add it to a list of top-level components (like any other IDDMSComponent), and pass it into a Resource constrcutor.
Creating more complex Elements from scratch requires XOM knowledge, and is outside the scope of this documentation.</p>

<h5>ExtensibleAttributes</h5>

<p>The <a href="/docs/index.html?buri/ddmsence/ddms/extensible/ExtensibleAttributes.html">ExtensibleAttributes</a> class follows the same implementation
pattern as SecurityAttributes and SRSAttributes. The accessor, <code>getAttributes()</code> will return a read-only list of all the underlying XOM Attributes.
Below is an example of an extensible attribute as it might appear in an XML file, and how it could be created from scratch:</p>

<pre class="brush: xml">&lt;ddms:keyword xmlns:ddmsence="http://ddmsence.urizone.net/" ddms:value="XML" ddmsence:relevance="99" /&gt;</pre>
<p class="figure">Figure 15. An XML element with extensible attributes</p>

<pre class="brush: java">List&lt;Attribute&gt; extAttributes = new ArrayList&lt;Attribute&gt;();
extAttributes.add(new Attribute("ddmsence:relevance", "http://ddmsence.urizone.net/", "99"));
ExtensibleAttributes extensions = new ExtensibleAttributes(extAttributes);
Keyword keyword = new Keyword("XML", extensions);</pre>
<p class="figure">Figure 16. Creating the extensible attribute from scratch</p>

<h5>ExtensibleAttributes on a Resource</h5>

<p>The <code>ddms:Resource</code> element is the only extensible element which has additional (ISM) attributes that might conflict with your extensible
attributes. The situation is trickier in DDMS 2.0, where the ISM attributes are not explicitly defined in the schema, but can exist nonetheless.</p>

<p>When creating an ExtensibleAttributes instance based upon a <code>ddms:Resource</code> XOM Element:</p>
<ul>
	<li>First, ISM resource attributes such as <code>ISM:DESVersion</code> will be "claimed", if present.</li>
	<li>Next, the ISM attributes such as <code>ISM:classification</code> will be converted into a SecurityAttributes instance.</li>
	<li>Any remaining attributes are considered to be ExtensibleAttributes.</li>
</ul>

<p>When building an ExtensibleAttributes instance from scratch and placing it on a Resource:</p>
<ul>
	<li>ISM resource attributes which exist as constructor parameters, such as <code>ISM:DESVersion</code> are processed first, if present.</li>
	<li>Next, the SecurityAttributes, such as <code>ISM:classification</code> are processed.</li>
	<li>Finally, the ExtensibleAttributes are processed. This means that these attributes cannot conflict with any attributes from the first two steps.</li>
</ul>
<p>In DDMS 2.0, it is perfectly legal to implement one of the resource attributes or security attributes as an extensible attribute:</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");

// DESVersion as a resource attribute
Resource resource1 = new Resource(myComponents, null, null, new Integer(2), null);

// DESVersion as an extensible attribute
String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
List&lt;Attribute&gt; attributes = new ArrayList&lt;Attribute&gt;();
attributes.add(new Attribute("ISM:DESVersion", icNamespace, "2"));
ExtensibleAttributes extensions = new ExtensibleAttributes(attributes);
Resource resource2 = new Resource(myComponents, null, null, null, null, extensions);</pre>
<p class="figure">Figure 17. These two approaches for a resource attribute are both legal in DDMS 2.0</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");

// classification and ownerProducer as security attributes
List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("USA");
SecurityAttributes secAttributes = new SecurityAttributes("U", ownerProducers, null);
Resource resource = new Resource(myComponents, null, null, null, secAttributes);

// classification and ownerProducer as extensible attributes
String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
List&lt;Attribute&gt; attributes = new ArrayList&lt;Attribute&gt;();
attributes.add(new Attribute("ISM:classification", icNamespace, "U"));
attributes.add(new Attribute("ISM:ownerProducer", icNamespace, "USA"));
ExtensibleAttributes extensions = new ExtensibleAttributes(attributes);
Resource resource = new Resource(myComponents, null, null, null, null, extensions);</pre>
<p class="figure">Figure 18. These two approaches for security attributes are both legal in DDMS 2.0</p>

<p>As a best practice, it is recommended that you create these attributes as explicitly as possible: if an attribute can be defined with constructor parameters or inside
of a SecurityAttributes instance, it should. This will make DDMS 2.0 resources more consistent with their DDMS 3.x counterparts.</p>

<h5>ExtensibleAttributes on a Keyword or Category</h5>

<p>Starting with DDMS 4.0, <code>ddms:keyword</code> and <code>ddms:category</code> can have both extensible and security attributes, just like a Resource. The same guidelines apply in this
situation -- define your security attributes as explicitly as possible to avoid confusion.</p>

<div class="powerTipDivider"></div>

<a name="tips-builders"></a><h4>Using Component Builders</h4>

<p>Beginning with DDMSence 1.8.0, every DDMS component has an associated <a href="/docs/buri/ddmsence/ddms/IBuilder.html">Builder</a> class
which offers a mutable way to build components. A Builder class can be the form bean behind an HTML form on a website, allowing someone to fill in the details page
by page. A Builder class can also be initialized with an existing component to allow for editing after it has already been saved. 
Properties on a Builder class can be set or re-set, and the strict DDMSence validation does not occur until
you are ready to <code>commit()</code> the changes.</p>

<p>Builder classes for components that have child components flexibly handle any nested Builders, so you do not have to make your edits from the lowest level component. This differs
from the approach described in <a href="tutorials-02.jsp">Tutorial #2: Escort</a>, where all child components must be complete and valid before proceeding up the hierarchy.
The following three figures provide an example of this difference, using SubjectCoverage as a representative component.</p>

<pre class="brush: xml">&lt;ddms:subjectCoverage ISM:classification="U"&gt;
   &lt;ddms:Subject&gt;
      &lt;ddms:keyword ddms:value="DDMSence" /&gt;
   &lt;/ddms:Subject&gt;
&lt;/ddms:subjectCoverage&gt;</pre>

<p class="figure">Figure 19. An XML fragment containing Subject Coverage information</p>

<pre class="brush: java">List&lt;Keyword&gt; keywords = new ArrayList&lt;Keyword&gt;();
keywords.add(new Keyword("DDMSence", null)); // Keyword validated here
SecurityAttributes securityAttributes = new SecurityAttributes("U", null, null); // SecurityAttributes validated here
SubjectCoverage subjectCoverage = new SubjectCoverage(keywords, null, securityAttributes); // SubjectCoverage validated here</pre>

<p class="figure">Figure 20. The "bottoms-up" approach for building a SubjectCoverage component</p>

<pre class="brush: java">SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
builder.getKeywords().get(0).setValue("DDMSence");
builder.getSecurityAttributes().setClassification("U");
SubjectCoverage subjectCoverage = builder.commit(); // All validation occurs here</pre>

<p class="figure">Figure 21. The Builder approach for building a SubjectCoverage component</p>

<p>As you can see, the Builder approach treats the building process from a "top-down" perspective. By using 
a <a href="http://ddmsence.urizone.net/docs/buri/ddmsence/ddms/Resource.Builder.html">Resource.Builder</a> instance, you can edit and traverse
a complete DDMS Resource, without necessarily needing to understand the intricacies of the components you aren't worried about. The code sample
below takes a List of pre-existing DDMS Resources, uses the Builder framework to edit a <code>ddms:dates</code> attribute on each one, and saves the results in a new List.</p>
 
<pre class="brush: java">List&lt;Resource&gt; updatedResources = new ArrayList&lt;Resource&gt;();
for (Resource resource : myExistingResources) {
   Resource.Builder builder = new Resource.Builder(resource);
   builder.getDates().setPosted("2011-05-13");
   updatedResources.add(builder.commit());
}</pre>

<p class="figure">Figure 22. Programatically editing a batch of Resources</p>

<p>There are a few implementation details to keep in mind when working with Builders:</p>
<ol>
<li>Calling a <code>get()</code> method that returns a child Builder instance (or a List of them) will never return <code>null</code>. A new Builder will be lazily instantiated if 
one does not already exist. An example of this can be seen on line 2 of Figure 21: When the value of the Keyword is set to "DDMSence", the List of Keyword.Builders
returned by <code>getKeywords()</code>, and the first Keyword.Builder returned by <code>get(0)</code> are both instantiated upon request.
This should make it easier for Component Builders to be used in a web-based form, where the number of child components might grow dynamically with JavaScript.</li>
<li>The <code>commit()</code> method on any given Builder will only return a component if the Builder was actually used, according its
implementation of <code>IBuilder.isEmpty()</code>. This decision was made to handle form beans more flexibly: if a user has not filled 
in any of the fields on a <code>ddms:dates</code> form, it is presumed that their intent is NO <code>ddms:dates</code> element, and not an empty, useless one.</li>
<li>The <code>commit()</code> method will use the version of DDMS defined in <code>DDMSVersion.getCurrentVersion()</code> for validation and XML namespaces. Changing the current version during
the building process has no effect up until the moment that <code>commit()</code> is called. In addition, initializing a Builder with an existing resource will not change the current DDMSVersion value.</li>
</ol>

<p>The last detail is important, because it allows you to load a resource from an old version of DDMS and transform it into a newer version. The code sample
below takes a DDMS 2.0 resource, adds the new fields required in DDMS 3.0, and commits it. The resulting Resource will use the DDMS 3.0 XML namespace.</p>

<pre class="brush: java">Resource.Builder builder = new Resource.Builder(myDdms20Resource);
builder.setResourceElement(Boolean.TRUE);
builder.setCreateDate("2011-07-05");
builder.setDESVersion(new Integer(2));
builder.getSecurityAttributes().setClassification("U");
builder.getSecurityAttributes().getOwnerProducers().add("USA");

DDMSVersion.setCurrentVersion("3.0");
Resource myDdms30Resource = builder.commit();</pre>
<p class="figure">Figure 23. Transforming a DDMS 2.0 resource with the Builder Framework</p>

<p>The similar code shown below will transform a 3.0 resource into a 3.1 resource.</p>

<pre class="brush: java">Resource.Builder builder = new Resource.Builder(myDdms30Resource);
builder.setDESVersion(new Integer(5));
DDMSVersion.setCurrentVersion("3.1");
Resource myDdms30Resource = builder.commit();</pre>
<p class="figure">Figure 24. Transforming a DDMS 3.0 resource with the Builder Framework</p>

<p>The Component Builder framework is a very recent addition to DDMSence, and I would love to hear your <a href="#feedback">feedback</a> on ways
the framework or documentation could be improved to better support your Resource editing needs. I have also created a sample <a href="builder.uri">DDMS Builder</a> 
web application which puts these features to work.</li></p> 

<div class="powerTipDivider"></div>

<a name="tips-schematron"></a><h4>Schematron Validation</h4>

<p>It is expected that organizations and communities of interest may have additional constraints on the data in their DDMS Resources, besides the rules in the DDMS specification.
DDMSence provides support for these rules through the <a href="http://www.schematron.com/">ISO Schematron</a> standard. Using a combination of a configurable XSLT engine
and <a href="http://xom.nu/">XOM</a>, DDMSence can validate a Resource against a custom Schematron file (<code>.sch</code>) and return the results of validation as
a list of <a href="/docs/index.html?buri/ddmsence/ddms/ValidationMessage.html">ValidationMessage</a>s. The XSLT transformation makes use of Rick Jelliffe's <a href="http://www.schematron.com/implementation.html">mature implementation</a>
of ISO Schematron.</p> 

<p>Creating a custom Schematron file is outside the scope of this documentation, but there are plenty of Schematron tutorials available online, and I have also codified several
complex rules from the DDMS Specification for example's sake in the <a href="#explorations">Explorations</a> section. There are two very simple examples in the 
<code>/data/sample/schematron/</code> directory. The file, <code><a href="http://ddmsence.googlecode.com/svn/trunk/data/sample/schematron/testPublisherValueXslt1.sch">testPublisherValueXslt1.sch</a></code> examines the surname of person designated as a publisher and 
fails if the surname is "<b>Uri</b>".</p>

<pre class="brush: xml">&lt;iso:pattern title="Fixed Surname Value"&gt;
   &lt;iso:rule context="//ddms:publisher/ddms:person/ddms:surname"&gt;
      &lt;iso:report test="normalize-space(.) = 'Uri'"&gt;Members of the Uri family cannot be publishers.&lt;/iso:report&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>
<p class="figure">Figure 25. The test from testPublisherValueXslt1.sch</p>

<p>The file, <code><a href="http://ddmsence.googlecode.com/svn/trunk/data/sample/schematron/testPositionValuesXslt2.sch">testPositionValuesXslt2.sch</a></code> forces any positions to match an exact location in Reston, Virginia. It makes use of the
XPath 2.0 function, <code>tokenize()</code>, so it must be handled with an XSLT2-compatible engine. DDMSence decides whether to use XSLT1 or XSLT2 based on the <code>queryBinding</code>
attribute on the root element of your Schematron file. The supported values are <code>xslt</code> or <code>xslt2</code>, and the former will be the default if this attribute does not exist.</p>

<pre class="brush: xml">&lt;iso:pattern id="FGM_Reston_Location"&gt;
   &lt;iso:rule context="//gml:pos"&gt;
      &lt;iso:let name="firstCoord" value="number(tokenize(text(), ' ')[1])"/&gt;
      &lt;iso:let name="secondCoord" value="number(tokenize(text(), ' ')[2])"/&gt;
      &lt;iso:assert test="$firstCoord = 38.95"&gt;The first coordinate in a gml:pos element must be 38.95 degrees.&lt;/iso:assert&gt;
      &lt;iso:assert test="$secondCoord = -77.36"&gt;The second coordinate in a gml:pos element must be -77.36 degrees.&lt;/iso:assert&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>
<p class="figure">Figure 26. The test from testPositionValuesXslt2.sch</p>

<p>The following code sample will build a DDMS Resource from one of the sample XML files, and then validate it through Schematron:</p>

<pre class="brush: java">File resourceFile = new File("data/sample/4.0-ddmsenceExample.xml");
File schFile = new File("data/sample/schematron/testPublisherValueXslt1.sch");

DDMSReader reader = new DDMSReader();
Resource resource = reader.getDDMSResource(resourceFile);
List&lt;ValidationMessage&gt; schematronMessages = resource.validateWithSchematron(schFile);
for (ValidationMessage message : schematronMessages) {
   System.out.println("Location: " + message.getLocator());
   System.out.println("Message: " + message.getText());
}</pre>
<p class="figure">Figure 27. Sample code to validate 4.0-ddmsenceExample.xml with testPublisherValueXslt1.sch</p>

<pre class="brush: xml">Location: //*[local-name()='Resource' and namespace-uri()='urn:us:mil:ces:metadata:ddms:4']
   /*[local-name()='publisher' and namespace-uri()='urn:us:mil:ces:metadata:ddms:4']
   /*[local-name()='Person' and namespace-uri()='urn:us:mil:ces:metadata:ddms:4']
   /*[local-name()='surname' and namespace-uri()='urn:us:mil:ces:metadata:ddms:4']
Message: Members of the Uri family cannot be publishers.</pre>
<p class="figure">Figure 28. Ouput of the code from Figure 27</p>

<p>Schematron files are made up of a series of patterns and rules which assert rules and report information. The raw output of Schematron validation
is a series of <code>failed-assert</code> and <code>successful-report</code> elements in Schematron Validation Report Language (SVRL). DDMSence converts
this output into ValidationMessages with a locator value taken from the <code>location</code> attribute in SVRL. The type returned is "warning" for 
"successful-report" messages and "error" for "failed-assert" messages. 
It is important to notice that 1) Schematron validation can only be performed on Resources which are already valid according to the DDMS specification and 
2) the results of Schematron validation will <b>never</b> invalidate the DDMSence object model. It is the responsibility of the Schematron user to react 
to any ValidationMessages.</p>

<p>Schematron files contain the XML namespaces of any elements you might traverse -- please make sure you use the correct namespaces for the version
of DDMS you are employing. The sample files described above are written only for DDMS 3.1.</p>

<h5>Validating with ISM.XML Schematron Files</h5>

<p>Recent versions of ISM.XML include Schematron files for validating logical constraints on the ISM security attributes. DDMSence does not include these files,
but you can download the Public Release versions from the <a href="http://www.dni.gov/ICEA/ism/default.htm">ODNI website</a>, and your organization might have
access to versions from higher classification levels as well. The top-level Schematron file, <code>ISM/Schematron/ISM/ISM_XML.sch</code> is the orchestration
point for each of the supporting files and the vocabularies needed for validation.</p>

<p>Here is an example which validates one of the sample DDMS resources against the ISM.XML Schematron files. It assumes that the top-level file and all of
the files and subdirectories it depends on have been copied into the working directory.</p>

<pre class="brush: java">File schematronFile = new File("ISM_XML.sch");
Resource resource = new DDMSReader().getDDMSResource(new File("data/sample/4.0-ddmsenceExample.xml"));
List&lt;ValidationMessage&gt; messages = resource.validateWithSchematron(schematronFile);
for (ValidationMessage message : messages) {
   System.out.println("Location: " + message.getLocator());
   System.out.println("Message: " + message.getText());
}</pre>
<p class="figure">Figure 29. Sample code to validate with ISM.XML Schematron Files</p>

<p>Running this code will not display any errors or warnings, but we can make the output more exciting by intentionally breaking a rule. One of the rules described
in the DES states that <code>ISM:ownerProducer</code> token values must be in alphabetical order (ISM-ID-00100). If you edit this attribute on the root node
of the DDMS resource file so the value is <code>"USA AUS"</code> and then run the code again, you should get the following output.</p>

<pre class="brush: xml">Location: //*:Resource[namespace-uri()='urn:us:mil:ces:metadata:ddms:4'][1]
   /*:title[namespace-uri()='urn:us:mil:ces:metadata:ddms:4'][1]
Message: [ISM-ID-00100][Error] If ISM-CAPCO-RESOURCE and attribute ownerProducer is specified, then each of its values must 
   be ordered in accordance with CVEnumISMOwnerProducer.xml. The following values are out of order [AUS] for [USA AUS]</pre>
<p class="figure">Figure 30. Schematron output when intentionally flaunting the rules</p>

<h5>Supported XSLT Engines</h5>

<p>DDMSence comes bundled with Saxon Home Edition (v9.3.0.5) because it supports both XSLT1 and XSLT2 transformations. Support for alternate engines is provided through the 
<code>xml.transform.TransformerFactory</code> configurable property, which can be set to the class name of another processor. Please 
see the Power Tip on <a href="#tips-configuration">Configurable Properties</a> for details on how to set this property. The table below lists the engines I have tested with.</p>

<table>
<tr><th>Name and Version</th><th>Class Name</th><th>XSLT1</th><th>XSLT2</th></tr>
<tr><td>Saxon HE 9.3.0.5</td><td><code>net.sf.saxon.TransformerFactoryImpl</code></td><td>supported</td><td>supported</td></tr>
<tr><td>Xalan interpretive, v2.7.1</td><td><code>org.apache.xalan.processor.TransformerFactoryImpl</code></td><td>supported</td><td>fails, doesn't support XSLT 2.0</td></tr>
<tr><td>Xalan XSLTC, v2.7.1</td><td><code>org.apache.xalan.xsltc.trax.TransformerFactoryImpl</code></td><td>fails, SVRL transformation doesn't seem to occur properly</td><td>fails, doesn't support XSLT 2.0</td></tr>
<tr><td>Xalan XSLTC, bundled with Java 1.5</td><td><code>com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl</code></td><td>fails, Xalan bug treats XSLT warning as an error</td><td>fails, doesn't support XSLT 2.0</td></tr>
<tr><td>Xalan XSLTC, bundled with Java 1.6</td><td><code>com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl</code></td><td>supported</td><td>fails, doesn't support XSLT 2.0</td></tr>
</table>
<p class="figure">Table 5. XSLT Engines for Schematron Validation</p>

<div class="powerTipDivider"></div>

<a name="tips-configuration"></a><h4>Configurable Properties</h4>

<p>DDMSence exposes some properties, such as the namespace prefixes used for each XML namespace, so they can be configured by the end user. For example, if you are 
building components from scratch, and you wish to use "ic" as a namespace prefix for the Intelligence Community namespace
instead of "ISM", you would set the "ism.prefix" property with a custom value of <code>ic</code>.</p>

<pre class="brush: java">PropertyReader.setProperty("ism.prefix", "ic");</pre>
<p class="figure">Figure 31. Command to change a configurable property.</p>

<p>Only the subset of properties listed below can be set programmatically. Attempts to change other DDMSence properties will result in an exception.</p>

<table>
<tr><th>Property Name</th><th>Description</th><th>Default Value</th></tr>
<tr><td>ddms.prefix</td><td>Default DDMS prefix used when generating components from scratch</td><td><code>ddms</code></td></tr>
<tr><td>gml.prefix</td><td>Default GML prefix used when generating components from scratch</td><td><code>gml</code></td></tr>
<tr><td>ism.cve.validationAsErrors</td><td>When validating SecurityAttributes in DDMS 2.0 and 3.0, ISM Controlled Vocabulary checks should return errors, instead of warnings</td><td><code>true</code></td></tr>
<tr><td>ism.prefix</td><td>Default ISM prefix used when generating components from scratch</td><td><code>ISM</code></td></tr>
<tr><td>sample.data</td><td>Default data directory used by sample applications</td><td><code>data/sample/</code></td></tr>
<tr><td>xlink.prefix</td><td>Default XLink prefix used when generating components from scratch</td><td><code>xlink</code></td></tr>
<tr><td>xml.transform.TransformerFactory</td><td>XSLT Engine class name, for Schematron validation<td><code>net.sf.saxon.TransformerFactoryImpl</code></td></tr>
</table>
<p class="figure">Table 6. Configurable Properties</p>

<a name="explorations"></a><h3>Explorations</h3>

<p>This section contains links to DDMS-related research and personal experimentation which may be useful to DDMSence in the future.</p>

<ul>
	<li><a href="builder.uri">DDMS Builder</a>: An experimental tool to build DDMS resources with a form-based UI.</li>
	<li><a href="validator.uri">DDMS Validator</a>: An experimental tool to validate DDMS resources.</li>
	<li><a href="relationalTables.jsp">Relational Database Model for DDMS</a>: A mapping of the DDMS specification to relational database tables (database-agnostic).</li>
	<li><a href="schematron.jsp">Schematron Implementation for DDMS</a>: An attempt to model some of the more complex rules in the DDMS specification with ISO Schematron.</li>
</ul>

<a name="contributors"></a><h3>Contributors</h3>

<p>DDMSence is a <a href="http://www.oss-watch.ac.uk/resources/benevolentdictatorgovernancemodel.xml" target="_new">benevolent dictatorship</a> -- I
am the sole committer on the project for the forseeable future. However, I welcome any suggestions you might have on ways to improve the project
or correct deficiencies!</p>

<p>The source code for DDMSence can be found in the "src"-flavored download on the <a href="downloads.jsp">Downloads</a> page. If you are interested in viewing the latest
(unreleased and possibly unstable) source code, you can download it with any Subversion client:</p>
<pre>svn checkout <a href="http://ddmsence.googlecode.com/svn/trunk/">http://ddmsence.googlecode.com/svn/trunk/</a> ddmsence-read-only</pre>

<p>An <a href="http://code.google.com/feeds/p/ddmsence/svnchanges/basic">Atom feed</a> of SVN commits is also available -- there are no email notifications at this time.</p>

<a name="feedback"></a><h3>Feedback</h3>

<p>I would love to hear about your experiences working with DDMSence, or suggestions for future functionality. Did you find it to be useful? Are there requirements that I
should consider supporting? Is the documentation clear and complete?</p>

<p>You can officially report a bug or enhancement suggestion on the <a href="http://code.google.com/p/ddmsence/issues/list">Issue Tracking</a> page,
or use the <a href="http://groups.google.com/group/ddmsence">Discussion</a> area as a less formal method of discussion. Finally, you can
contact me directly by email at
	<script type="text/javascript">
		document.write("<a href=\"mailto:" + eMail + "\">" + eMail + "</a>.");
	</script>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>