<html>
<head>
	<title>DDMSence: Documentation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>
<img src="./images/splashImage.jpg" width="302" height="210" title="DDMSence" align="right" />
<h1>Documentation</h1>


<h2>Table of Contents</h2>

<ul>
	<li><a href="#started">Getting Started</a></li><ul>
		<li><a href="#samples">Sample Applications</a></li>
		<li><a href="#javadoc">JavaDoc API Documentation</a></li>
		</ul>
	<li><a href="#useCases">Suggested Use Cases</a></li>	
	<li><a href="#design">Design Decisions</a></li>
	<li><a href="#tips">Power Tips</a></li>
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
set DDMSENCE_CLASSPATH=lib/saxon9he-9.4.0.4.jar;lib/xercesImpl-2.11.0.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/xml-apis-1.4.01.jar;lib/xom-1.2.8.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/ddmsence-@ddmsence.version@.jar
set DDMSENCE_CLASSPATH=%DDMSENCE_CLASSPATH%;lib/ddmsence-samples-@ddmsence.version@.jar
java -cp %DDMSENCE_CLASSPATH% buri.ddmsence.samples.Essentials</pre>
<p class="figure">Figure 1. Running from a Windows/DOS Command Line</p>

<pre class="brush: xml">#!/bin/sh
# Linux Commands
cd &lt;folderWhereDDMSenceIsUnzipped&gt;/ddmsence-bin-@ddmsence.version@
ddmsence_classpath=lib/saxon9he-9.4.0.4.jar:lib/xercesImpl-2.11.0.jar
ddmsence_classpath=$ddmsence_classpath:lib/xml-apis-1.4.01.jar:lib/xom-1.2.8.jar
ddmsence_classpath=$ddmsence_classpath:lib/ddmsence-@ddmsence.version@.jar
ddmsence_classpath=$ddmsence_classpath:lib/ddmsence-samples-@ddmsence.version@.jar
java -cp $ddmsence_classpath buri.ddmsence.samples.Essentials</pre>
<p class="figure">Figure 2. Running in Linux</p>

<p>Note: The syntax for setting a classpath in Linux may vary, depending on the shell you are using. If you are using an older version of DDMSence, make sure
to change the version numbers in the classpath examples.</p>

<a name="samples"></a><h4>Sample Applications</h4>
<!-- DDMScargo(t), DDMScrow, DDMSophagus, DDMSpresso, DDMStrogen, DDMStuary, DDMSquire, DDMSteem, DDMStablishment, DDMStimator, DDMScalator, DDMState -->

<p>
<img src="./images/essentials-thumb.png" width="300" height="200" title="Essentials Screenshot" align="right" />
DDMSence comes with three sample applications. The intention of these applications is to highlight notable aspects of DDMSence, not to create
real-world solutions.</p>

<h5>Essentials</h5>

<p><u>Essentials</u> is a simple reader application which loads an XML file containing a DDMS Resource and displays it in three different formats: the
original XML, HTML, and Text. The source code for this application provides an example of how to create DDMS components from an XML file.</p>

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
<a href="http://code.google.com/apis/visualization/documentation/gallery/map.html" target="_new">Google Maps</a>.</p>
	
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
in the "src"-flavored download. Classes which represent DDMS elements are grouped into packages based on which of the five 
core sets they belong to. In some cases, packages are further divided to identify any components which originate from other 
XML namespaces, such as GML or XLink. If a component is reused across multiple sets, it will be found in the package of the 
set where it was first introduced.</p>
<p>The overview page contains a quick reference chart of all implemented DDMS components. You should also be aware of the following sections, which appear on every DDMS component's page:
<ul>
	<li>The class description describes cases where DDMSence is stricter than the DDMS specification, and allowed cases which are legal, but nonsensical. If this varies
	for different versions of DDMS, the version number will be indicated. If no version number is listed, the constraint applies to all versions.</li>
	<li>The class description describes any nested elements or attributes for the implemented component.</li>
	<li>The <code>validate()</code> method description lists the specific rules used to validate a component. This section may be useful when building your own components from scratch.</li>
	<li>If a component has any conditions that result in warnings, the <code>validateWarnings()</code> method description lists the specific conditions that trigger a warning.</li>
</ul>
<div class="clear"></div>

<a name="useCases"></a><h3>Suggested Use Cases</h3>

<p>This section describes common use cases that are a good fit for the DDMSence library.</p>

<h4>Reading existing metacards</h4>

<p>As the tutorials show, it is trivially easy for DDMSence to load a DDMS metacard from an XML file, examine its contents in a Java way, and display it as XML, HTML or Text. If you
have a search web service which queries a catalog of DDMS metacards, DDMSence could be used to process those metacards individually, or gather statistics about the catalog as a whole.
You could also use DDMSence to quickly validate a collection of DDMS metacards, simply by loading them into a DDMSReader.</p>

<h4>Creating new metacards</h4>

<p>There are two ways to create DDMS metacards from scratch in DDMSence. The first way makes use of standard Java constructors to build up each component and then group them together
into a complete DDMS metacard. The second way uses the <a href="documentation-builders.jsp">Component Builder</a> framework to model a top-down construction of a metacard, and provides
more flexibility of implementation. The latter approach follows the pattern of a step-by-step wizard that builds the metacard over several steps, and then commits and validates the metacard
at the very end.</p>

<h4>Editing existing metacards</h4>

<p>The Component Builder framework also allows you to load an existing metacard, make changes to it, and then revalidate it. It might also be used to programmatically update or
correct errors in a collection of DDMS metacards. For example, if your assets have moved to a different web server, you could programmatically update all of your DDMS Identifiers
to point to the new location. Alternately, you could change all occurences of a producer's last name after a marriage.</p>

<p>As a specialized example of editing, you could transform older metacards to the latest version of DDMS. With the Component Builder framework, you could load a DDMS 2.0 metacard,
add all of the fields required for DDMS 4.1, and then save it as a DDMS 4.1 metacard. Example code for this use case can be found in the <a href="documentation-builders.jsp">Component Builder</a> Power Tip.</p>

<h4>Applying custom constraints with Schematron</h4>

<p>In many cases, schema validation is not sufficient to truly show conformance. The Intelligence Community codifies constraints on ISM attributes with a set of Schematron files. DDMSence
requires just a few lines of code to validate your DDMS metacards against these Schematron files. You could also validate against Schematron files developed by your Community of Interest.
Additional details can be found in the <a href="documentation-schematron.jsp">Schematron Validation</a> Power Tip.</p>

<div class="clear"></div>

<a name="design"></a><h3>Design Decisions</h3>

<p>This section identifies a few basic design principles of DDMSence which might affect you as incorporate it into your own projects.</p>

<h4>Components Deserving an Object</h4>

<p>Not all of the elements defined in the DDMS schema are implemented as Java Objects. Many elements are defined globally but only used once, or exist merely as wrappers for other components. I have
followed these rules to determine which components are important enough to deserve a full-fledged Object:</p>

<ul>
	<li>Elements which are explicitly declared as DDMS Categories in the DDMS documentation are always implemented (<code>ddms:identifier</code>).</li>
	<li>Elements which merely enclose important data AND which have no special attributes are never implemented (<code>ddms:Media</code>).</li>
	<li>Data which can be represented as a simple Java type AND which has no special attributes is represented as a simple Java type (<code>ddms:email</code>).</li>
	<li>Attributes are generally implemented as properties on an Object. The exceptions to this are the attributes which tend to be used together, such as the
		<a href="/docs/index.html?buri/ddmsence/ddms/security/ism/SecurityAttributes.html">ISM SecurityAttributes</a>, 
		which decorates many DDMS components, and the <a href="/docs/index.html?buri/ddmsence/ddms/summary/gml/SRSAttributes.html">GML SRSAttributes</a>,
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

<h4>Lists of Strings</h4>

<p>In some instances, the DDMS specification supports multiple string values, either by  multiple elements (<code>ddms:name</code> elements on a <code>ddms:person</code>)
or by supporting the <code>xsd:list</code> datatype (the <code>ISM:ownerProducer</code> security attribute). The accessor methods for these fields in DDMSence will always deal with
a List of String values, rather than a space-separated String containing multiple values. However, because list management can be tedious, there is a shortcut utility method available:

<pre class="brush: java">// These two approaches to generating ownerProducers are equivalent.
List&lt;String&gt; ownerProducers = new ArrayList&lt;String&gt;();
ownerProducers.add("AUS");
ownerProducers.add("USA");

List&lt;String&gt; ownerProducers = Util.getXsListAsList("AUS USA");</pre>
<p class="figure">Figure 3. Conveniently creating a list of Strings from a space-delimited String</p>

<h4>Immutability</h4>

<p>All DDMS components are implemented as immutable objects, which means that their values cannot be changed after instantiation. Because the components are
validated during instantiation, this also means that it is impossible to have an invalid component at any given time: a component is either confirmed to be 
valid or does not exist. The <a href="documentation-builders.jsp">Component Builder</a> framework can be used to build DDMS Resources piece by piece, saving validation until the end.</li>
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

<p>Some non-string-based attributes, such as <code>ISM:excludeFromRollup</code> and <code>ISM:resouceElement</code> do not appear in earlier versions of DDMS. When the accessors for 
these attributes are called on an older version, a null value will be returned. This decision allows DDMS records of varying versions to be
traversed and queried in the same manner, without requiring too much knowledge of when specific attributes were introduced.</p>

<a name="tips"></a><h3>Power Tips</h3>

<p>Power Tips provide instructions and sample code to maximize the benefits of DDMSence, once you are comfortable with the basics of the library.</p>

<ul>
	<li><a href="documentation-version.jsp">Working With Different DDMS Versions</a>: Shows how to read and create DDMS Resources and components using other supported versions. 
	Also outlines the important changes between each version of DDMS in table form.</li><br />

	<li><a href="documentation-attributes.jsp">Common Attribute Groups</a>: Discusses the classes which represent the attribute groups used throughout DDMS, such as
	Information Security Marking (ISM) attributes.</li><br />

	<li><a href="documentation-builders.jsp">Using Component Builders</a>: Shows a "top-down" alternative to building a DDMS metacard from scratch. Components can be built up over
	time and validated once at the end, rather than the "bottoms-up" constructor-based approach which validates each component individually and groups them together.</li><br />

	<li><a href="documentation-schematron.jsp">Schematron Validation</a>: Explains the process for using custom Schematron files with DDMSence, and provides example
	code for using the official ISM.XML Schematron files.</li><br />

	<li><a href="documentation-differentIsm.jsp">Using Alternate Versions of ISM/NTK</a>: Shows how to use a local set of ISM/NTK schemas and CVE files instead of the files bundled with DDMSence.</li><br />

	<li><a href="documentation-configuration.jsp">Configurable Properties</a>: Identifies DDMSence properties which can be changed at runtime.</li><br />

	<li><a href="documentation-extensible.jsp">The Extensible Layer</a>: Discusses how the library provides support for the custom elements and attributes allowed by the DDMS specification. Also discusses
	the way DDMSence proritizes the various types of attributes which can exist on a DDMS Resource.</li><br />
</ul>

<a name="explorations"></a><h3>Explorations</h3>

<p>This section contains links to DDMS-related research and personal experimentation which may be useful to DDMSence in the future.</p>

<ul>
	<li><a href="builder.uri">DDMS Builder</a>: An experimental tool to build DDMS metacards with a form-based UI.</li><br />
	<li><a href="validator.uri">DDMS Validator</a>: An experimental tool to validate DDMS metacards.</li><br />
	<li><a href="relationalTables.jsp">Relational Database Model for DDMS 3.1</a>: A mapping of the DDMS 3.1 specification to relational database tables (database-agnostic).</li><br />
	<li><a href="schematron.jsp">Schematron Implementation for DDMS 4.1</a>: An attempt to model some of the more complex rules in the DDMS specification with ISO Schematron.</li>
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

<br /><br /><br /><br /><br /><br />

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>