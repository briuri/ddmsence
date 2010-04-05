<html>
<head>
	<title>DDMSence: Documentation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Documentation</h1>

<h2>Table of Contents</h2>
<ul>
	<li><a href="#feedback">Feedback</a></li>
	<li><a href="#started">Getting Started</a></li>
	<li><a href="#samples">Sample Applications</a></li>
	<li><a href="#javadoc">JavaDoc API Documentation</a></li>
	<li><a href="#design">Design Decisions</a></li>
	<li><a href="#tips">Advanced Tips</a></li>
	<li><a href="#contributors">Contributors</a></li>
</ul>

<a name="feedback"></a><h3>Feedback</h3>

<p>I would love to hear about your experiences working with DDMSence, or suggestions for future functionality. 
You can officially report a bug or enhancement suggestion on the <a href="http://code.google.com/p/ddmsence/issues/list">Issue Tracking</a> page,
or use the <a href="http://groups.google.com/group/ddmsence">Discussion</a> area as a less formal method of discussion. Finally, you can also
contact me directly by email at
	<script type="text/javascript">
		document.write("<a href=\"mailto:" + eMail + "\">" + eMail + "</a>.");
	</script>
</p>

<a name="started"></a><h3>Getting Started</h3>

<p>Begin by visiting the <a href="downloads.jsp">Downloads</a> page and downloading a copy of DDMSence. The tutorials below will assume that you are working
with the "bin"-flavored download, which comes with the DDMSence JAR files pre-compiled, and contains source code for the sample applications.</p>

<p>Unzip the downloaded archive in a directory of your choice. You can then run the samples from the command line by entering the lines below (or paste them
into a batch/shell script and run that script):</p>

<div class="example"><pre>REM Windows Commands
cd &lt;<i>folderWhereDDMSenceIsUnzipped</i>&gt;\ddmsence-bin-1.0.0
set DDMSENCE_CLASSPATH=lib/xercesImpl-2.9.1.jar;lib/xml-apis-1.3.04.jar;lib/xom-1.2.4.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/ddmsence-1.0.0.jar;lib/ddmsence-samples-1.0.0.jar
java -cp %DDMSENCE_CLASSPATH% buri.ddmsence.samples.Essentials</pre></div>
<p class="figure">Figure 1. Running from a Windows/DOS Command Line</p>

<div class="example"><pre>#!/bin/sh
# Linux Commands
cd &lt;<i>folderWhereDDMSenceIsUnzipped</i>&gt;/ddmsence-bin-1.0.0
ddmsence_classpath=lib/xercesImpl-2.9.1.jar:lib/xml-apis-1.3.04.jar:lib/xom-1.2.4.jar
ddmsence_classpath=$ddmsence_classpath:lib/ddmsence-1.0.0.jar:lib/ddmsence-samples-1.0.0.jar
java -cp $ddmsence_classpath buri.ddmsence.samples.Essentials</pre></div>
<p class="figure">Figure 2. Running in Linux</p>

<p>Note: The syntax for setting a classpath in Linux may vary, depending on the shell you are using.</p>

<a name="samples"></a><h3>Sample Applications</h3>
<!-- DDMScargo(t), DDMScrow, DDMSophagus, DDMStrogen, DDMStuary, DDMSquire, DDMSteem, DDMStablishment, DDMStimator, DDMScalator, DDMState -->

<p>
<img src="../images/essentials-thumb.png" width="300" height="199" title="Essentials Screenshot" align="right" />
DDMSence comes with three sample applications. The intention of these applications is to highlight notable aspects of DDMSence, not to create
real-world solutions.</p>

<h4>Essentials</h4>

<p><u>Essentials</u> is a simple reader application which loads an XML file containing a DDMS Resource and displays it in four different formats: the
original XML, HTML, Text, and Java code (which you can reuse to build your own components from scratch). The source code for this application provides
an example of how to create DDMS components from an XML file.</p>

<ul>
	<li><a href="tutorials-01.jsp">Tutorial #1: Essentials</a></li>
</ul>

<h4>Escort</h4>

<p><u>Escort</u> is a step-by-step wizard for building a DDMS Resource from scratch, and then saving it to a file. The source code for this application
shows an example of how the Java object model can be built with simple data types.</p>

<ul>
	<li><a href="tutorials-02.jsp">Tutorial #2: Escort</a></li>
</ul>

<img src="../images/escape-thumb.png" width="300" height="198" title="Escape Screenshot" align="right" />

<h4>Escape</h4>

<p><u>Escape</u> is a tool that traverses multiple DDMS Resource files and then exposes various statistics about them through the 
<a href="http://code.google.com/apis/visualization/documentation/gallery.html" target="_new">Google Visualization API</a>. Charts in this sample
application are non-interactive, but provide the foundation for more complex cases: for example, it would
be possible to plot Temporal Coverage on an Annotated Timeline, or Geospatial Coverage on Google Maps. While the first two sample applications were 
designed to teach developers how DDMSence works, the intent of this one is to provide brainstorming ideas for leveraging DDMS in other contexts.</p>
	
<ul>
	<li><a href="tutorials-03.jsp">Tutorial #3: Escape</a></li>
</ul>

<div class="clear"></div>
	
<a name="javadoc"></a><h3>JavaDoc API Documentation</h3>

<img src="../images/docSample.png" width="310" height="231" title="JavaDoc sample" align="right" />

<ul>
	<li><a href="/docs/">Online API Documentation</a></li>
</ul>
The API documentation is also bundled with the "bin"-flavored download on the <a href="downloads.jsp">Downloads</a> page, and can be generated from source code 
in the "src"-flavored download. You should be aware of the following sections, which appear on every DDMS component's page:
<ul>
	<li>The class description describes cases where DDMSence is stricter than the DDMS specification, and allowed cases which are legal, but nonsensical. If this varies
	for different versions of DDMS, the version number will be indicated. If no version number is listed, the constraint applies to all versions.</li>
	<li>The class description describes any nested elements or attributes for the implemented component and a link to the DDMS website for the complete specification.</li>
	<li>The <code>validate</code> method description lists the specific rules used to validate a component. This section may be useful when building your own components.</li>
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
		<a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">ICISM Attribute Group</a>, 
		which decorates many DDMS components, and the <a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/summary/SRSAttributes.html">SRS Attribute Group</a>,
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
			<li>Information about the enclosing component that may affect this new component comes first (such as the producerType of an <a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/resource/Organization.html">Organization</a>).</li>
			<li>The data or components needed to construct any nested elements comes next (such as the list of Keywords in a <a href="http://ddmsence.urizone.net/docs/buri/ddmsence/ddms/summary/SubjectCoverage.html">SubjectCoverage</a> component).</li>
			<li>The data needed to construct any attributes comes next (such as the <a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">ICISM SecurityAttributes</a>).</li>
			<li>Any remaining information that DDMSence needs comes last (such as the boolean flag on a <a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/summary/PostalAddress.html">PostalAddress</a> which toggles between states and provinces).</li>
		</ul>
	<li>On constructors which build components from XML files, a XOM element is generally the only parameter. Additional information is implicitly
	loaded from the XOM element. </li>
</ul>

<h4>New Attributes in DDMS v3.0</h4>

<p>Some attributes, such as ICISM:excludeFromRollup and ICISM:resouceElement are new in DDMS v3.0. When the accessors for these attributes are
caleld on a DDMS 2.0 component, an arbitrary value will be returned instead of an UnsupportedVersionException. This decision allows DDMS records of varying versions to be
traversed and queried in the same manner, without requiring too much knowledge of when specific attributes were introduced. The default value for the attribute
will be listed in the API documentation for that attribute's accessor -- in general, boolean attributes will be <code>false</code> and other data types will be <code>null</code>.</p>

<h4>Thread Safety</h4>

<p>Other than the immutability of objects, no special effort went into making DDMSence thread-safe, and no testing was done on its behavior in multithreaded environments.</p>

<a name="tips"></a><h3>Advanced Tips</h3>

<h4>ICISM Security Attributes</h4>

<p>
ICISM security attributes are defined in the Intelligence Community's "XML Data Encoding Specification for Information Security Marking Metadata" document (DES) and
implemented in the <a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/security/SecurityAttributes.html">SecurityAttributes</a> class. This class encapsulates
the ICISM attributes which can decorate various DDMS components, such as <code>ddms:Resource</code> or <code>ddms:security</code>. The constructor which builds
the attributes from a XOM element will simply load these attributes from the element itself. The constructor which builds the attributes from raw data is defined as:

<div class="example"><pre>public SecurityAttributes(String classification, List&lt;String&gt; ownerProducers, Map&lt;String,String&gt; otherAttributes)</pre></div>
<p class="figure">Figure 3. SecurityAttributes constructor</p>

<p>Because the <code>classification</code> and <code>ownerProducers</code> are the most commonly referenced attributes (especially for Unclassified metadata) they are explicit parameters. Any other
attribute can be added in a String-based map called <code>otherAttributes</code>. If an attribute is repeated, the last one in the list is used, and if an attribute does not match an
expected attribute name, it is ignored. Here is an example which creates Confidential markings and puts them on a <code>ddms:title</code> element:</p>

<div class="example"><pre>List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("USA");
ownerProducers.add("AUS");
Map&lt;String, String&gt; otherAttributes = new HashMap&lt;String, String&gt;();
otherAttributes.put("SCIcontrols", "HCS"); // This will be ignored, because there is a later duplicate.
otherAttributes.put("SCIcontrols", "SI");
otherAttributes.put("SARIdentifier", "SAR-USA");
otherAttributes.put("classification", "U"); // This will be ignored, because the "classification" parameter takes precedence.
SecurityAttributes security = new SecurityAttributes("C", ownerProducers, otherAttributes);
Title title = new Title("My Confidential Notes", security);
System.out.println(title.toXML());</pre></div>
<p class="figure">Figure 4. Code to generate SecurityAttributes</p>

<p>Note: The actual values assigned to each attribute in Figure 4 are for example's sake only, and might be illogical in real-world metadata.</p>

<div class="example"><pre>&lt;ddms:title xmlns:ddms="http://metadata.dod.mil/mdr/ns/DDMS/3.0/" xmlns:ICISM="urn:us:gov:ic:ism" 
   ICISM:classification="C" ICISM:ownerProducer="USA AUS" ICISM:SCIcontrols="SI" ICISM:SARIdentifier="SAR-USA"&gt;
   My Confidential Notes
&lt;/ddms:title&gt;</pre></div>
<p class="figure">Figure 5. The resultant XML element with security attributes</p>

<p>The values assigned to some attributes must come from the <a href="http://ddmsence.googlecode.com/svn/trunk/data/CVEnumISM/">Controlled Vocabulary Enumerations</a>
defined by the Intelligence Community. The DES also defines many logical constraints on these attributes, but DDMSence does not validate these rules today.
I would like to add this level of validation after the IC has finalized version 2 of the DES.</p>

<h4>SRS Attributes</h4>

<p>Spatial Reference System (SRS) attributes are defined in the DDMS' GML Profile and implemented as an <a href="<a href="http://ddmsence.urizone.net/docs/index.html?buri/ddmsence/ddms/summary/SRSAttributes.html">SRSAttributes</a> class.
They can be applied to <code>gml:Point</code>, <code>gml:Polygon</code>, and <code>gml:pos</code>.</p>

<div class="example"><pre>SRSAttributes(String srsName, Integer srsDimension, List&lt;String&gt; axisLabels, List&lt;String&gt; uomLabels)</pre></div>
<p class="figure">Figure 6. SRSAttributes constructor</p>

<p>Here is an example which creates SRS attributes on a <code>gml:pos</code> element:</p>

<div class="example"><pre>List&lt;String&gt; axisLabels = new ArrayList&lt;String&gt;();
axisLabels.add("X");
axisLabels.add("Y");
List&lt;String&gt; uomLabels = new ArrayList&lt;String&gt;();
uomLabels.add("Meter");
uomLabels.add("Meter");
SRSAttributes srsAttributes = new SRSAttributes("http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D", new Integer(10), axisLabels, uomLabels);
List&lt;Double&gt; coordinates = new ArrayList&lt;Double&gt;();
coordinates.add(new Double(32.1));
coordinates.add(new Double(40.1));
Position position = new Position(coordinates, srsAttributes);
System.out.println(position.toXML());</pre></div>
<p class="figure">Figure 7. Code to generate SRSAttributes</p>

<div class="example"><pre>&lt;gml:pos srsName="http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D" srsDimension="10" 
   axisLabels="X Y" uomLabels="Meter Meter"&gt;32.1 40.1&lt;/gml:pos&gt;</pre></div>
<p class="figure">Figure 8. The resultant XML element with SRS attributes</p>
  
<p>Please note that the SRSAttributes do not belong in any XML namespace -- this is correct according to the DDMS GML Profile.</p>

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