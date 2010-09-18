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
		<li><a href="#tips-version">Working with DDMS 2.0</a></li>
		<li><a href="#tips-attributes">Attribute Groups</a></li>
		<li><a href="#tips-extensible">The Extensible Layer</a></li>
		<li><a href="#tips-schematron">Schematron Validation</a></li>
		<li><a href="#tips-configuration">Configurable Properties</a></li>
		</ul>
	<li><a href="#contributors">Contributors</a></li>
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
		<a href="/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">ICISM Attribute Group</a>, 
		which decorates many DDMS components, and the <a href="/docs/index.html?buri/ddmsence/ddms/summary/SRSAttributes.html">SRS Attribute Group</a>,
		which decorates components in the GML profile.</li>
</ul>

<h4>Producers and Producer Entities</h4>

<p>In DDMS terms, there are producer roles (like "creator") and producer entities (like "Organization"). The DDMS schema models the relationship between
the two as "a producer role which is filled by some entity". In the Java object model, this hierarchy is simplified as "a producer entity which fills 
some role". The producer entity is modelled as an Object, and the producer role it is filling is a property on that Object. This design decision does not 
affect any output -- it is only intended to make the producer hierarchy easier to understand on the Java side. I tried modeling producers both ways,
and the approach I chose seemed more understandable from an object-oriented perspective.</p> 

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
validated during instantiation, this also means that it is impossible to have an invalid component at any given time: a component is either confirmed to be valid or does not exist.</p>

<h4>Constructor Parameter Order</h4>

<p>Because DDMS components are built in single-step constructors to support immutability, parameter lists can sometimes exceed more than a handful of information. 
The following convention is used to provide some consistency:</p>

<ul>
	<li>On constructors which build components from raw data:</li>
		<ul>
			<li>Information about the enclosing component that may affect this new component comes first (such as the producerType of an <a href="/docs/index.html?buri/ddmsence/ddms/resource/Organization.html">Organization</a>).</li>
			<li>The data or components needed to construct any nested elements comes next (such as the list of Keywords in a <a href="/docs/buri/ddmsence/ddms/summary/SubjectCoverage.html">SubjectCoverage</a> component).</li>
			<li>The data needed to construct any attributes comes next (such as the <a href="/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">ICISM SecurityAttributes</a>).</li>
			<li>Any remaining information that DDMSence needs comes last (such as the boolean flag on a <a href="/docs/index.html?buri/ddmsence/ddms/summary/PostalAddress.html">PostalAddress</a> which toggles between states and provinces).</li>
		</ul>
	<li>On constructors which build components from XML files, a XOM element is generally the only parameter. Additional information is implicitly
	loaded from the XOM element. </li>
</ul>

<h4>Thread Safety</h4>

<p>Other than the immutability of objects, no special effort went into making DDMSence thread-safe, and no testing was done on its behavior in multithreaded environments.</p>

<h4>Accessor Consistency Across Versions</h4>

<p>Some attributes, such as <code>ICISM:excludeFromRollup</code> and <code>ICISM:resouceElement</code> are new in DDMS v3.0. When the accessors for these attributes are
called on a DDMS 2.0 component, a null value will be returned. This decision allows DDMS records of varying versions to be
traversed and queried in the same manner, without requiring too much knowledge of when specific attributes were introduced.</p>

<a name="tips"></a><h3>Power Tips</h3>

<div class="toc">
	<b><u>Table of Contents</u></b>
	<li><a href="#tips-version">Working with DDMS 2.0</a></li>
	<li><a href="#tips-attributes">Attribute Groups</a></li>
	<li><a href="#tips-extensible">The Extensible Layer</a></li>
	<li><a href="#tips-schematron">Schematron Validation</a></li>
	<li><a href="#tips-configuration">Configurable Properties</a></li>
</div>
		
<a name="tips-version"></a><h4>Working with DDMS 2.0</h4>

<p>Starting with DDMSence v1.1, two versions of DDMS are supported: 2.0 and 3.0. When building DDMS components from XML files, the 
DDMSReader class can automatically use the correct version of DDMS based on the XML namespace defined in the file.
When building DDMS components from scratch, the <a href="/docs/index.html?buri/ddmsence/util/DDMSVersion.html">DDMSVersion</a>
class controls the version being used. There is an instance of DDMSVersion for each supported version, and this 
instance contains the specific XML namespaces used for DDMS, GML, and ICISM components.</p>

<div class="clear"></div>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
DDMSVersion version = DDMSVersion.getCurrentVersion();
System.out.println("In DDMS v" + version.getVersion() + ", the following namespaces are used: ");
System.out.println("ddms: " + version.getNamespace());
System.out.println("gml: " + version.getGmlNamespace());
System.out.println("ICISM: " + version.getIcismNamespace());
System.out.println("Are we using DDMS 2.0? " + DDMSVersion.isCurrentVersion("2.0"));
System.out.println("If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, "
   + "I know we are using DDMS v"
   + DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion());

Identifier identifier = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS v" + identifier.getDDMSVersion());

DDMSVersion.setCurrentVersion("3.0");
Identifier identifier2 = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS v" + identifier2.getDDMSVersion());</pre>
<p class="figure">Figure 3. Using a different version of DDMS</p>

<pre class="brush: xml">In DDMS v2.0, the following namespaces are used: 
ddms: http://metadata.dod.mil/mdr/ns/DDMS/2.0/
gml: http://www.opengis.net/gml
ICISM: urn:us:gov:ic:ism:v2
Are we using DDMS 2.0? true
If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, I know we are using DDMS v3.0
This identifier was created with DDMS v2.0
This identifier was created with DDMS v3.0</pre>
<p class="figure">Figure 4. Output of the code in Figure 3</p>

<p>Calling <code>DDMSVersion.setCurrentVersion("2.0")</code> will make any components you create from that point on obey DDMS 2.0 
validation rules. The default version if you never call this method is "3.0" (but as a best practice, you should always explicitly set the current version yourself,
in case this default changes in the future). The version is maintained as a static variable, so this 
is not a thread-safe approach, but I believe that the most common use cases will deal with DDMS components of a single version at a time,
and I wanted the versioning mechanism to be as unobtrusive as possible.</p>

<p>The validation rules between DDMS 2.0 and 3.0 are very similar, but there are a few major differences. For example, the Unknown
entity for producers was not introduced until DDMS 3.0, so attempts to create one in DDMS 2.0 will fail. Also note that you cannot mix multiple DDMS versions within the same Resource.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
List&lt;String&gt; names = new ArrayList&lt;String&gt;();
names.add("Unknown Entity");
Unknown unknown = new Unknown("creator", names, null, null, null);</pre>
<p class="figure">Figure 5. This code will throw an InvalidDDMSException</p>

<p>The table below identifies the differences between the DDMS 2.0 and DDMS 3.0 implementations of components in DDMSence.</p>

<table>
<tr><th>Element or Attribute</th><th>DDMS 2.0 Notes</th><th>DDMS 3.0 Notes</td></tr>
<tr><td><code>ddms:Resource@ICISM:createDate<br />ddms:Resource@ICISM:DESVersion<br />ddms:Resource@ICISM:resourceElement</code></td><td>Not explicitly defined in the schema, but can live in the <code>xs:anyAttribute</code> extensible area. Can also be passed in as a constructor parameter.</td><td>New and required in this version.</td></tr>
<tr><td><code>ddms:Resource@ICISM:classification<br />ddms:Resource@ICISM:ownerProducer</code></td><td>Not explicitly defined in the schema, but can live in the <code>xs:anyAttribute</code> extensible area. Can also be defined in a SecurityAttributes instance.</td><td>New and required in this version.</td></tr>
<tr><td><code>ddms:Resource@ICISM (additional security)</td><td>Not explicitly defined in the schema, but can live in the <code>xs:anyAttribute</code> extensible area. Can also be defined in a SecurityAttributes instance.</td><td>New but optional in this version.</td></tr>
<tr><td><code>ddms:Resource (xs:any)</code></td><td>Zero or one extensible element can appear after the <code>ddms:security</code> element.</td><td>Zero to many extensible elements can appear after the <code>ddms:security</code> element.</td></tr>
<tr><td><code>ddms:Unknown</code></td><td>Cannot exist in this version.</td><td>Can be used to fill a producer role in this version.</td></tr>
<tr><td><code>ddms:category</code></td><td>No extensible attributes can be used.</td><td>Zero to many attributes can live in the <code>xs:anyAttribute</code> extensible area.</td></tr>
<tr><td><code>ddms:countryCode@ddms:qualifier<br />ddms:countryCode@ddms:value</code></td><td>Follows 3.0 rules: both attributes are required.</td><td>Both attributes are required.</td></tr>
<tr><td><code>ddms:geospatialCoverage@ICISM:classification<br />ddms:geospatialCoverage@ICISM:ownerProducer<br />ddms:geospatialCoverage@ICISM (additional security)</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr>
<tr><td><code>ddms:keyword</code></td><td>No extensible attributes can be used.</td><td>Zero to many attributes can live in the <code>xs:anyAttribute</code> extensible area.</td></tr>
<tr><td><code>ddms:relatedResources</code></td><td>Follows 3.0 rules: at least 1 ddms:RelatedResource must exist.</td><td>At least 1 ddms:RelatedResource must exist.</td></tr>
<tr><td><code>ddms:security@ICISM:excludeFromRollup</code></td><td>Cannot exist in this version.</td><td>New and required in this version. Value must be "true".</td></tr>
<tr><td><code>ddms:source@ICISM:classification<br />ddms:source@ICISM:ownerProducer<br />ddms:source@ICISM (additional security)</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr>
<tr><td><code>ddms:subjectCoverage@ICISM:classification<br />ddms:subjectCoverage@ICISM:ownerProducer<br />ddms:subjectCoverage@ICISM (additional security)</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr>
<tr><td><code>ddms:temporalCoverage@ICISM:classification<br />ddms:temporalCoverage@ICISM:ownerProducer<br />ddms:temporalCoverage@ICISM (additional security)</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr>
<tr><td><code>ddms:virtualCoverage@ICISM:classification<br />ddms:virtualCoverage@ICISM:ownerProducer<br />ddms:virtualCoverage@ICISM (additional security)</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr></table>
<p class="figure">Table 1. Component changes between DDMS 2.0 and DDMS 3.0</p>

<p>The table below identifies changes made to the ICISM security attributes. These changes apply on any DDMS component that can be decorated with security attributes.</p>

<table>
<tr><th>Element or Attribute</th><th>DDMS 2.0 Notes</th><th>DDMS 3.0 Notes</td></tr>
<tr><td><code>@ICISM:classification</code></td><td>NS-S and NS-A are allowed values.</td><td>NS-S and NS-A cannot be used.</td></tr>
<tr><td><code>@ICISM:declassManualReview</code></td><td>This Boolean attribute is optional.</td><td>Cannot exist in this version.</td></tr>
<tr><td><code>@ICISM:declassException</code></td><td>This attribute is modeled as a space-delimited string of multiple values (TOKENS)</td><td>This attribute is a single value (TOKEN)</td></tr>
<tr><td><code>@ICISM:typeOfExemptedSource</code></td><td>This attribute is modeled as a space-delimited string of multiple values (TOKENS)</td><td>This attribute is a single value (TOKEN)</td></tr>
<tr><td><code>@ICISM:compilationReason</code></td><td>Cannot exist in this version.</td><td>New but optional in this version.</td></tr></table>
<p class="figure">Table 2. Security Attribute changes between DDMS 2.0 and DDMS 3.0</p>

<a name="tips-attributes"></a><h4>Attribute Groups</h4>

<h5>ICISM Security Attributes</h5>

<p>
ICISM security attributes are defined in the Intelligence Community's "XML Data Encoding Specification for Information Security Marking Metadata" document (DES) and
implemented in the <a href="/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">SecurityAttributes</a> class. This class encapsulates
the ICISM attributes which can decorate various DDMS components, such as <code>ddms:Resource</code> or <code>ddms:security</code>. The constructor which builds
the attributes from a XOM element will simply load these attributes from the element itself. The constructor which builds the attributes from raw data is defined as:

<pre class="brush: java">public SecurityAttributes(String classification, List&lt;String&gt; ownerProducers, Map&lt;String,String&gt; otherAttributes)</pre>
<p class="figure">Figure 6. SecurityAttributes constructor</p>

<p>Because the <code>classification</code> and <code>ownerProducers</code> are the most commonly referenced attributes, they are explicit parameters. Any other
attribute can be added in the String-based map called <code>otherAttributes</code>. If an attribute is repeated, the last one in the list is used, and if an attribute does not match an
expected attribute name, it is ignored. Here is an example which creates Confidential markings and puts them on a <code>ddms:title</code> element:</p>

<pre class="brush: java">List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("USA");
ownerProducers.add("AUS");
Map&lt;String, String&gt; otherAttributes = new HashMap&lt;String, String&gt;();
otherAttributes.put("SCIcontrols", "HCS"); // This will be overwritten by "SI"
otherAttributes.put("SCIcontrols", "SI");
otherAttributes.put("SARIdentifier", "SAR-USA");
// The next line will be ignored, because the "classification" parameter takes precedence.
otherAttributes.put("classification", "U"); 
SecurityAttributes security = new SecurityAttributes("C", ownerProducers, otherAttributes);
Title title = new Title("My Confidential Notes", security);
System.out.println(title.toXML());</pre>
<p class="figure">Figure 7. Code to generate SecurityAttributes</p>

<p>Note: The actual values assigned to each attribute in Figure 7 are for example's sake only, and might be illogical in real-world metadata.</p>

<pre class="brush: xml">&lt;ddms:title xmlns:ddms="http://metadata.dod.mil/mdr/ns/DDMS/3.0/" xmlns:ICISM="urn:us:gov:ic:ism" 
   ICISM:classification="C" ICISM:ownerProducer="USA AUS" ICISM:SCIcontrols="SI"
   ICISM:SARIdentifier="SAR-USA"&gt;
   My Confidential Notes
&lt;/ddms:title&gt;</pre>
<p class="figure">Figure 8. The resultant XML element with security attributes</p>

<p>The DES also defines many logical constraints on these attributes, but DDMSence does not validate these rules today. I believe that the IC will be 
releasing Schematron files to support these rules in the future.</p>

<p>The values assigned to some attributes must come from the <a href="http://ddmsence.googlecode.com/svn/trunk/data/CVEnumISM/">Controlled 
Vocabulary Enumerations</a> (CVEs) defined by the Intelligence Community. The enumerations used by DDMSence are taken from the DES Version 3 
Public Release, so DDMSence will not be able to recognize enumeration values from higher classification levels. There are two approaches 
you can use to eliminate this restriction:</p>

<ol>
<li>If you have access to enumeration files from more restricted distributions of ISM (or wish to use enumeration files from previous DESVersions), 
you can create a classpath-accessible directory containing your files, and then use the <code>icism.cve.enumLocation</code> configurable property 
to point to your files. DDMSence will expect this directory to contain the <a href="http://ddmsence.googlecode.com/svn/trunk/data/CVEnumISM/">same 
set of files</a> that come natively bundled in its JAR file, and will use your files to compare enumeration values.</li>
<li>If you do not have access to any other enumeration files, you can set the <code>icism.cve.validationAsErrors</code> configurable 
property to <b>false</b>, which will force DDMSence to return warnings on enumerations, rather than errors.</li>
</ol>

<p>Please see the Power Tip on <a href="#tips-configuration">Configurable Properties</a> for details on how to set these properties.</p>

<h5>SRS Attributes</h5>

<p>Spatial Reference System (SRS) attributes are defined in the DDMS' GML Profile and implemented as an <a href="/docs/index.html?buri/ddmsence/ddms/summary/SRSAttributes.html">SRSAttributes</a> class.
They can be applied to <code>gml:Point</code>, <code>gml:Polygon</code>, and <code>gml:pos</code>.</p>

<pre class="brush: java">SRSAttributes(String srsName, Integer srsDimension, List&lt;String&gt; axisLabels,
   List&lt;String&gt; uomLabels)</pre>
<p class="figure">Figure 9. SRSAttributes constructor</p>

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
<p class="figure">Figure 10. Code to generate SRSAttributes</p>

<pre class="brush: xml">&lt;gml:pos srsName="http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D" srsDimension="10" 
   axisLabels="X Y" uomLabels="Meter Meter"&gt;32.1 40.1&lt;/gml:pos&gt;</pre>
<p class="figure">Figure 11. The resultant XML element with SRS attributes</p>
  
<p>Please note that the SRSAttributes do not belong in any XML namespace -- this is correct according to the DDMS GML Profile.</p>

<a name="tips-extensible"></a><h4>The Extensible Layer</h4>

<p>DDMS is composed of four Core Layers (Security, Resource, Summary Content, and Format) and the Extensible Layer. This layer supports extensibility
by providing space for custom attributes and elements within a <code>ddms:Resource</code>. Specifically, custom attributes can be added to any producer
(Organization, Person, Service, and Unknown), a Keyword, a Category, and the Resource itself. A Resource can also have an unlimited number of custom
elements after the <code>ddms:security</code> element. These extensions are identified by the <code>xs:any</code> and <code>xs:anyAttribute</code>
definitions in the schema. The main restriction on content is that custom elements and attributes must belong to an XML namespace other than the
DDMS namespace.</p>

<p>Because any manner of content could appear in the Extensible Layer, DDMSence merely provides a consistent interface to access to the underlying 
XOM Elements and Attributes. Any business logic to be performed on this Layer is left up to the implementation, so some knowledge of 
<a href="http://xom.nu/">XOM</a> will be useful. </p>

<p>The relevant code can be found in the <code>buri.ddmsence.ddms.extensible</code> package. It may also be useful to load the sample file,  
<code>Extensible_Layer_Example.xml</code> into the <u>Essentials</u> application, because it has an example of each extension.</p>

<h5>ExtensibleElements</h5>

<p>An unlimited number of elements from any XML namespace other than the DDMS namespace can appear at the end of a <code>ddms:Resource</code>. (In DDMS 2.0,
only 1 can appear). These elements are implemented with the <a href="/docs/index.html?buri/ddmsence/ddms/extensible/ExtensibleElement.html">ExtensibleElement</a> class,
which acts like any other IDDMSComponent and exposes <code>getXOMElementCopy()</code> to return a copy of the underlying XOM Element. Below is an
example of an extensible element as it might appear in an XML file.</p> 

<pre class="brush: xml">   [...]
   &lt;/ddms:subjectCoverage&gt;
   &lt;ddms:security ICISM:ownerProducer="USA" ICISM:classification="U" ICISM:excludeFromRollup="true"/&gt;
   &lt;ddmsence:extension xmlns:ddmsence="http://ddmsence.urizone.net/"&gt;
      This is an extensible element.
   &lt;/ddmsence:extension&gt;
&lt;/ddms:Resource&gt;</pre>
<p class="figure">Figure 12. An extensible element as it would appear in a ddms:Resource</p>

<p>Unlike most DDMS components, which have a constructor for XOM elements and a constructor for raw data, ExtensibleElement only has one constructor
(since the raw data is, itself, a XOM element). If you are using a DDMSReader instance to load data from an XML file, the ExtensibleElements will be created automatically,
and can be accessed with <code>Resource.getExtensibleElements()</code>. Here is an example of how you might build a simple one from scratch:</p>

<pre class="brush: java">Element element = new Element("ddmsence:extension", "http://ddmsence.urizone.net/");
element.appendChild("This is an extensible element.");
ExtensibleElement component = new ExtensibleElement(element);</pre>
<p class="figure">Figure 13. Creating a simple ExtensibleElement from scratch</p>

<p>Once you have an ExtensibleElement, you can add it to a list of top-level components (like any other IDDMSComponent), and pass it into a Resource constrcutor.
Creating more complex Elements from scratch requires XOM knowledge, and is outside the scope of this documentation.</p>

<h5>ExtensibleAttributes</h5>

<p>The <a href="/docs/index.html?buri/ddmsence/ddms/extensible/ExtensibleAttributes.html">ExtensibleAttributes</a> class follows the same implementation
pattern as SecurityAttributes and SRSAttributes. The accessor, <code>getAttributes()</code> will return a read-only list of all the underlying XOM Attributes.
Below is an example of an extensible attribute as it might appear in an XML file, and how it could be created from scratch:</p>

<pre class="brush: xml">&lt;ddms:keyword xmlns:ddmsence="http://ddmsence.urizone.net/" ddms:value="XML" ddmsence:relevance="99" /&gt;</pre>
<p class="figure">Figure 14. An XML element with extensible attributes</p>

<pre class="brush: java">List&lt;Attribute&gt; extAttributes = new ArrayList&lt;Attribute&gt;();
extAttributes.add(new Attribute("ddmsence:relevance", "http://ddmsence.urizone.net/", "99"));
ExtensibleAttributes extensions = new ExtensibleAttributes(extAttributes);
Keyword keyword = new Keyword("XML", extensions);</pre>
<p class="figure">Figure 15. Creating the extensible attribute from scratch</p>

<p>Note: Currently, all of the <code>xs:anyAttribute</code> definitions use lax processing <i>except</i> for the definition on the producer elements. This means
that any extensible attributes you add to a producer will be strictly validated when loaded by a schema parser. I am in contact with the DDMS Team to determine
whether this is an <a href="http://code.google.com/p/ddmsence/issues/detail?id=12">intentional design decision</a> or an oversight.</p>

<h5>ExtensibleAttributes on a Resource</h5>

<p>The <code>ddms:Resource</code> element is the only extensible element which has additional (ICISM) attributes that might conflict with your extensible
attributes. The situation gets trickier in DDMS 2.0, where the ICISM attributes are not explicitly defined in the schema, but can exist nonetheless.</p>

<p>When creating an ExtensibleAttributes instance based upon a <code>ddms:Resource</code> XOM Element:</p>
<ul>
	<li>First, ICISM resource attributes such as <code>ICISM:DESVersion</code> will be "claimed", if present.</li>
	<li>Next, the ICISM attributes such as <code>ICISM:classification</code> will be converted into a SecurityAttributes instance.</li>
	<li>Any remaining attributes are considered to be ExtensibleAttributes.</li>
</ul>

<p>When building an ExtensibleAttributes instance from scratch and placing it on a Resource:</p>
<ul>
	<li>ICISM resource attributes which exist as constructor parameters, such as <code>ICISM:DESVersion</code> are processed first, if present.</li>
	<li>Next, the SecurityAttributes, such as <code>ICISM:classification</code> are processed.</li>
	<li>Finally, the ExtensibleAttributes are processed. This means that these attributes cannot conflict with any attributes from the first two steps.</li>
</ul>
<p>In DDMS 2.0, it is perfectly legal to implement one of the resource attributes or security attributes as an extensible attribute:</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");

// DESVersion as a resource attribute
Resource resource1 = new Resource(myComponents, null, null, new Integer(2), null);

// DESVersion as an extensible attribute
String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
List&lt;Attribute&gt; attributes = new ArrayList&lt;Attribute&gt;();
attributes.add(new Attribute("ICISM:DESVersion", icNamespace, "2"));
ExtensibleAttributes extensions = new ExtensibleAttributes(attributes);
Resource resource2 = new Resource(myComponents, null, null, null, null, extensions);</pre>
<p class="figure">Figure 16. These two approaches for a resource attribute are both legal in DDMS 2.0</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");

// classification and ownerProducer as security attributes
List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("USA");
SecurityAttributes secAttributes = new SecurityAttributes("U", ownerProducers, null);
Resource resource = new Resource(myComponents, null, null, null, secAttributes);

// classification and ownerProducer as extensible attributes
String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
List&lt;Attribute&gt; attributes = new ArrayList&lt;Attribute&gt;();
attributes.add(new Attribute("ICISM:classification", icNamespace, "U"));
attributes.add(new Attribute("ICISM:ownerProducer", icNamespace, "USA"));
ExtensibleAttributes extensions = new ExtensibleAttributes(attributes);
Resource resource = new Resource(myComponents, null, null, null, null, extensions);</pre>
<p class="figure">Figure 17. These two approaches for security attributes are both legal in DDMS 2.0</p>

<p>As a best practice, it is recommended that you create these attributes as explicitly as possible: if an attribute can be defined with constructor parameters or inside
of a SecurityAttributes instance, it should. This will make DDMS 2.0 resources more consistent with their DDMS 3.0 counterparts.</p>

<a name="tips-schematron"></a><h4>Schematron Validation</h4>

<p>It is expected that organizations and communities of interest may have additional constraints on the data in their DDMS Resources, besides the rules in the DDMS specification.
DDMSence provides support for these rules through the <a href="http://www.schematron.com/">ISO Schematron</a> standard. Using a combination of the <a href="http://xml.apache.org/xalan-j/">Xalan</a> interpretive XSLT processor
and <a href="http://xom.nu/">XOM</a>, DDMSence can validate a Resource against a custom Schematron file (<code>.sch</code>) and return the results of validation as
a list of <a href="/docs/index.html?buri/ddmsence/ddms/ValidationMessage.html">ValidationMessage</a>s. The XSLT transformation makes use of Rick Jelliffe's <a href="http://www.schematron.com/implementation.html">mature implementation</a>
of ISO Schematron.</p> 

<p>Creating a custom Schematron file is outside the scope of this documentation, but there are plenty of Schematron tutorials available online, and I hope to provide additional samples
in the future. For now, a very simple example, <code>testPublisherValue.sch</code>, can be found in <code>/data/sample/schematron/</code>. This file examines the surname of person designated as a publisher and fails if the surname is "<b>Uri</b>".</p>

<pre class="brush: xml">&lt;iso:pattern name="Fixed Surname Value"&gt;
   &lt;iso:rule context="//ddms:publisher/ddms:Person/ddms:surname"&gt;
      &lt;iso:report test="normalize-space(.) = 'Uri'"&gt;Members of the Uri family cannot be publishers.&lt;/iso:report&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>
<p class="figure">Figure 18. The test from testPublisherValue.sch</p>

<p>The following code sample will build a DDMS Resource from one of the sample XML files, and then validate it through Schematron:</p>

<pre class="brush: java">File resourceFile = new File("data/sample/DDMSence_Example.xml");
File schFile = new File("data/sample/schematron/testPublisherValue.sch");

DDMSReader reader = new DDMSReader();
Resource resource = reader.getDDMSResource(resourceFile);
List&lt;ValidationMessage&gt; schematronMessages = resource.validateWithSchematron(schFile);
for (ValidationMessage message : schematronMessages) {
   System.out.println("Location: " + message.getLocator());
   System.out.println("Message: " + message.getText());
}</pre>
<p class="figure">Figure 19. Sample code to validate DDMSence_Example.xml with testPublisherValue.sch</p>

<pre class="brush: xml">Location: //*[local-name()='Resource' and namespace-uri()='http://metadata.dod.mil/mdr/ns/DDMS/3.0/']
   /*[local-name()='publisher' and namespace-uri()='http://metadata.dod.mil/mdr/ns/DDMS/3.0/']
   /*[local-name()='Person' and namespace-uri()='http://metadata.dod.mil/mdr/ns/DDMS/3.0/']
   /*[local-name()='surname' and namespace-uri()='http://metadata.dod.mil/mdr/ns/DDMS/3.0/']
Message: Members of the Uri family cannot be publishers.</pre>
<p class="figure">Figure 20. Ouput of the code from Figure 19</p>

<p>Schematron files are made up of a series of patterns and rules which assert rules and report information. The raw output of Schematron validation
is a series of <code>failed-assert</code> and <code>successful-report</code> elements in Schematron Validation Report Language (SVRL). DDMSence converts
this output into ValidationMessages with a locator value taken from the <code>location</code> attribute in SVRL. The type returned is "warning" for "successful-report" messages and "error" for "failed-assert" messages. 
It is important to notice that 1) Schematron validation can only be performed on Resources which are already valid according to the DDMS specification and 
2) the results of Schematron validation will <b>never</b> invalidate the DDMSence object model. It is the responsibility of the Schematron user to react 
to any ValidationMessages.</p>

<p>Today, Schematron validation is not part of the base validation that automatically happens to every DDMS component, but the DDMS
team has begun exploratory work to create a reference implementation of a DDMS Schematron file. Based on their work, I may choose to integrate an official
DDMS Schematron file in a future version.</p>

<p>Schematron files contain the XML namespaces of any elements you might traverse -- please make sure you use the correct namespaces for the version
of DDMS you are employing. The sample file, <code>testPublisherValue.sch</code>, is written only for DDMS 3.0.</p>

<p>DDMSence comes bundled with the the Xalan interpretive XSLT engine (v2.7.1) for stylesheet transformations. I have performed cursory tests
of other engines and the (unscientific) results are shown below.</p>

<table>
<tr><th>Name and Version</th><th>Class Name</th><th>Benchmark</th></tr>
<tr><td>Xalan interpretive, v2.7.1</td><td><code>org.apache.xalan.processor.TransformerFactoryImpl</code></td><td>0.891s (default engine)</td></tr>
<tr><td>Xalan XSLTC, v2.7.1</td><td><code>org.apache.xalan.xsltc.trax.TransformerFactoryImpl</code></td><td>fails, SVRL transformation doesn't seem to occur properly</td></tr>
<tr><td>Xalan XSLTC, bundled with Java 1.5</td><td><code>com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl</code></td><td>fails, Xalan bug treats XSLT warning as an error</td></tr>
<tr><td>Xalan XSLTC, bundled with Java 1.6</td><td><code>com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl</code></td><td>0.703s</td></tr>
<tr><td>Saxon HE 9.2</td><td><code>net.sf.saxon.TransformerFactoryImpl</code></td><td>1.469s</td></tr>
<tr><td>Saxon-B 9.1.0.8</td><td><code>net.sf.saxon.TransformerFactoryImpl</code></td><td>1.266s</td></tr>
</table>
<p class="figure">Table 3. XSLT Engines for Schematron Validation</p>

<p>Support for Schematron validation through alternative XSLT engines (other than the default Xalan engine) is provided through the 
<code>xml.transform.TransformerFactory</code> configurable property, which can be set to the class name of another processor.</p>

<a name="tips-configuration"></a><h4>Configurable Properties</h4>

<p>DDMSence exposes some properties, such as the namespace prefixes used for each XML namespace, so they can be configured by the end user. For example, if you are 
building components from scratch, and you wish to use "ic" as a namespace prefix for the Intelligence Community namespace
instead of "ICISM", you would set the "icism.prefix" property with a custom value of <code>ic</code>.</p>

<pre class="brush: java">PropertyReader.setProperty("icism.prefix", "ic");</pre>
<p class="figure">Figure 21. Command to change a configurable property.</p>

<p>Only the subset of properties listed below can be set programmatically. Attempts to change other DDMSence properties will result in an exception.</p>

<table>
<tr><th>Property Name</th><th>Description</th><th>Default Value</th></tr>
<tr><td>ddms.prefix</td><td>Default DDMS prefix used when generating components from scratch</td><td><code>ddms</code></td></tr>
<tr><td>gml.prefix</td><td>Default GML prefix used when generating components from scratch</td><td><code>gml</code></td></tr>
<tr><td>icism.cve.enumLocation</td><td>Classpath resource location for the ICISM Controlled Vocabulary files</td><td><code>/CVEnumISM/</code></td></tr>
<tr><td>icism.cve.validationAsErrors</td><td>When validating SecurityAttributes, ICISM Controlled Vocabulary checks should return errors, instead of warnings</td><td><code>true</code></td></tr>
<tr><td>icism.prefix</td><td>Default ICISM prefix used when generating components from scratch</td><td><code>ICISM</code></td></tr>
<tr><td>sample.data</td><td>Default data directory used by sample applications</td><td><code>data/sample/</code></td></tr>
<tr><td>xlink.prefix</td><td>Default XLink prefix used when generating components from scratch</td><td><code>xlink</code></td></tr>
<tr><td>xml.transform.TransformerFactory</td><td>XSLT Engine class name, for Schematron validation<td><code>org.apache.xalan.processor.TransformerFactoryImpl</code></td></tr>
</table>
<p class="figure">Table 4. Configurable Properties</p>

<a name="contributors"></a><h3>Contributors</h3>

<p>DDMSence is a <a href="http://en.wikipedia.org/wiki/Benevolent_dictatorship#Open_Source_Usage" target="_new">benevolent dictatorship</a> -- I
am the sole committer on the project for the forseeable future. However, I welcome any suggestions you might have on ways to improve the project
or correct deficiencies!</p>

<p>The source code for DDMSence can be found in the "src"-flavored download on the <a href="downloads.jsp">Downloads</a> page. If you are interested in viewing the latest
(unreleased and possibly unstable) source code, you can download it with any Subversion client:</p>
<pre>svn checkout <a href="http://ddmsence.googlecode.com/svn/trunk/">http://ddmsence.googlecode.com/svn/trunk/</a> ddmsence-read-only</pre>

<p>An <a href="http://code.google.com/feeds/p/ddmsence/svnchanges/basic">Atom feed</a> of SVN commits is also available -- there are no email notifications at this time.</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>