<html>
<head>
	<title>DDMSence: Downloads</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="The open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Downloads</h1>

<p>Downloads come in two flavors:</p>

<ul>
	<li><b>Binary (<code>bin</code>)</b>: This download is intended for end users who wish to use DDMSence in their own projects. It contains the compiled DDMSence JAR files,
the complete API documentation (which is also available <a href="/docs/">online</a>), source code for the sample applications,
and supporting 3rd-party JAR files. Sample applications can be run from the <a href="documentation.jsp#started">command line</a>.</li>

	<li><p><b>Source (<code>src</code>)</b>: This download is intended for developers who wish to see what DDMSence is doing under the hood. It contains source 
code for the main library, unit tests, and the sample applications, as well as supporting 3rd-party JAR files. The included <code>build.xml</code> 
file allows you to compile source code, run unit tests, generate API documentation, or even generate the "bin"-flavored archive with <a href="http://ant.apache.org/">Apache Ant</a>. 
I have also included a <code>.project</code> file if you wish to import the project into Eclipse.</li>
</ul>

<p>Regardless of your choice, you will need a <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_new">JRE or JDK</a> which supports Java 1.6 or higher.</p>

<h1>Latest Release: v2.4.0</h1>

<p>Version <b>2.4.0</b> (11/20/2014) is a stable release which improves the handling of date values in various components (<a href="http://code.google.com/p/ddmsence/issues/detail?id=221">Issue #221</a>).
It is backwards compatible with all versions since 2.0.0.</p>

<ul>
<li><b><a href="http://ddmsence.urizone.net/files/ddmsence-bin-2.4.0.zip"><code>ddmsence-bin-2.4.0.zip</code></a></b> (8.4 MB, compiled with JDK1.7.0_17, source level 1.6, target level 1.6)</li>
<li><b><a href="http://ddmsence.urizone.net/files/ddmsence-src-2.4.0.zip"><code>ddmsence-src-2.4.0.zip</code></a></b> (8.3 MB)</li>
<br />
<li><a href="http://code.google.com/p/ddmsence/issues/list?can=1&q=milestone=v2.4.0">Complete List of Changes</a></li>
</ul>

<h3>DDMSence and Maven</h3>

<p>The DDMSence JAR file is also available in the <a href="http://search.maven.org/#browse|57355829">Maven Central Repository</a>. It can be added to your <code>pom.xml</code>
file as shown in Figure 1.</p>

<pre class="brush: xml">&lt;dependency&gt;
   &lt;groupId&gt;net.urizone&lt;/groupId&gt;
   &lt;artifactId&gt;ddmsence&lt;/artifactId&gt;
   &lt;version&gt;2.4.0&lt;/version&gt;
&lt;/dependency&gt;</pre>
<p class="figure">Figure 1. Setting up DDMSence as a Maven dependency</p>

<h3>Versioning</h3>

<p>DDMSence will follow a "major.minor.patch" versioning system for as long as it makes sense to do so:</p>
<ul>
	<li>A change in major version will generally include major architecture changes and may break backwards compatibility in non-trivial ways.</li>
	<li>A change in minor version can include new features and bug fixes. On rare occasions, minor releases may break backwards compatibility, but the changes will require minimal upgrade effort.</li>
	<li>A change in patch version will be limited to bug fixes.</li>
</ul>

<p>If a new version is listed as "backwards compatible", it means that you can update to the new version without changing any of your code that worked with the old version.  
All changes which are not backwards compatible will be linked from this page with information on how to upgrade your old code.</p> 

<h3>Previous Release</h3>

<!-- 184 downloads of 2.2.0 as of 5/13/2014 -->
<!-- 345 downloads of 2.1.0 as of 12/15/2013 -->
<!-- 339 downloads of 2.0.0 as of 1/19/2013 -->
<!-- 828 downloads of 1.x -->

<p>Version <b>2.3.0</b> (05/30/2014) is a stable release which adds built-in support for JSON output. It is backwards compatible with all versions since 2.0.0.
Additional information about JSON output can be found in <a href="tutorials-01.jsp">Tutorial #1: Essentials</a>.</p>

<ul>
<li><b><a href="http://ddmsence.urizone.net/files/ddmsence-bin-2.3.0.zip"><code>ddmsence-bin-2.3.0.zip</code></a></b> (7.1 MB, compiled with JDK1.7.0_17, source level 1.6, target level 1.6)</li>
<li><b><a href="http://ddmsence.urizone.net/files/ddmsence-src-2.3.0.zip"><code>ddmsence-src-2.3.0.zip</code></a></b> (7.0 MB)</li>
</ul>

<h3>Ancient Releases</h3>

<p>Earlier versions of DDMSence are no longer available for download (although they can still be built from the tagged source code). Historical information about earlier releases can be
found below.</p>

<p>Version <b>2.2.1</b> (05/13/2014) is a stable release which is backwards compatible with all versions since 2.0.0. It is functionally equivalent
to 2.2.0, but has been recompiled to run with Java 1.6 (<a href="https://code.google.com/p/ddmsence/issues/detail?id=215">Issue #215</a>). If 2.2.0 is already working for you, there is no need to upgrade at this time. 
</p>

<p>Version <b>2.2.0</b> (12/15/2013) is a stable release, which adds support for DDMS 5.0. It is backwards compatible with all versions since 2.0.0.
<a href="documentation.jsp#javadoc">API documentation</a> has also been updated, making it easier to track components across DDMS versions. 
<a href="releaseNotes-2.2.0.jsp">Release notes</a> describing what's new in DDMS 5.0 and DDMSence 2.2.0 are also available.</a> 
</p>

<p>Version <b>2.1.0</b> (01/19/2013) is a stable release, which adds support for DDMS 4.1. It is backwards compatible with 2.0.0. Because DDMS 4.1 shares an XML namespace with DDMS 4.0.1,
older 4.0.1 metacards are validated against the DDMS 4.1 schemas in this release.</p>

<p>Version <b>2.0.0</b> (12/01/2011) is a stable, major release, which adds support for DDMS 4.0.1. It is <i>not</i> backwards compatible with previous versions. Upgrading your existing
code to use this version should be straightforward, but may require a non-trivial effort. An <a href="upgrade-2.0.0.jsp">Upgrade Guide</a> is available for
migrating from 1.x.x to 2.x.x.</p>

<p>Version <b>1.11.0</b> (07/17/2011) is a stable release, which adds support for 
<a href="documentation-schematron.jsp">XSLT2-based Schematron validation</a> (<a href="http://code.google.com/p/ddmsence/issues/detail?id=79">Issue #79</a>),
improved performance when using multiple Controlled Vocabularies (<a href="http://code.google.com/p/ddmsence/issues/detail?id=90">Issue #90</a>), and a few 
minor bug fixes. This release is backwards compatible with v1.10.0.</p>

<p>Version <b>1.10.0</b> (07/05/2011) is a stable release, which adds support for DDMS 3.1 (<a href="http://code.google.com/p/ddmsence/issues/list?can=1&q=milestone=v1.10.0">Issue #82 - 85</a>).
It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=88">backwards compatible</a> with earlier versions.</p>

<p>Version <b>1.9.1</b> (06/02/2011) is a stable patch release, which corrects a few minor bugs related to the lazily instantiated lists
in the Component Builder framework (<a href="http://code.google.com/p/ddmsence/issues/detail?id=76">Issue #76</a>,
<a href="http://code.google.com/p/ddmsence/issues/detail?id=77">Issue #77</a>
and <a href="http://code.google.com/p/ddmsence/issues/detail?id=78">Issue #78</a>). It is backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.9.0</b> (05/24/2011) is a stable release, which makes the Component Builder framework more amenable to web-based forms
by implementing the Serializable interface and using lazily instantiated lists for child components (<a href="http://code.google.com/p/ddmsence/issues/detail?id=74">Issue #74</a>
and <a href="http://code.google.com/p/ddmsence/issues/detail?id=75">Issue #75</a>). It is backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.8.0</b> (05/13/2011) is a stable release, which adds <a href="documentation-builders.jsp">mutable Builders</a> for all DDMS components 
(<a href="http://code.google.com/p/ddmsence/issues/detail?id=68">Issue #68</a>). It is backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.7.2</b> (03/10/2011) is a stable patch release, which corrects syntax errors in the sample Schematron files 
(<a href="http://code.google.com/p/ddmsence/issues/detail?id=67">Issue #67</a>), and adds "3.0.1" as an alias for DDMS 3.0
(<a href="http://code.google.com/p/ddmsence/issues/detail?id=69">Issue #69</a>). It is backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.7.1</b> (11/16/2010) is a stable patch release, which corrects a very minor issue where northbound latitude values were validated as longitudes 
 (<a href="http://code.google.com/p/ddmsence/issues/detail?id=65">Issue #65</a>). It is backwards compatible 
with all versions since v1.4.0.</p>

<p>Version <b>1.7.0</b> (09/18/2010) is a stable release, which allows toggling between errors and warnings on
Intelligence Community Controlled Vocabulary validation (<a href="http://code.google.com/p/ddmsence/issues/detail?id=36">Issue #36</a>)
and adds support for swappable Controlled Vocabulary files
(<a href="http://code.google.com/p/ddmsence/issues/detail?id=59">Issue #59</a>). It is backwards compatible 
with all versions since v1.4.0.</p>

<p>Version <b>1.6.0</b> (09/07/2010) is a stable release, which updates bundled schemas and files for DDMS 3.0. Because the changes
mainly affected the ISM Controlled Vocabulary files, this release is still backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.5.1</b> (07/07/2010) is a stable patch release, which fixes a <a href="http://code.google.com/p/ddmsence/issues/detail?id=50">Glassfish deployment issue</a>. It is backwards compatible with all versions since v1.4.0.</p>

<p>Version <b>1.5.0</b> (07/05/2010) is a stable release, and adds DDMSReader support for Strings, Readers, and InputStreams. It also updates the bundled version of XOM
to 1.2.6. This release is backwards compatible with v1.4.0.</p>

<p>Version <b>1.4.0</b> (06/08/2010) is a stable release, and changes configurable property support to be code-driven rather than property-file driven.
It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=46">backwards compatible</a> with earlier versions.</p>

<p>Version <b>1.3.2</b> (05/14/2010) is a stable patch release, which fixes <a href="http://code.google.com/p/ddmsence/issues/list?can=1&q=milestone=v1.3.2">four minor bugs</a>. It is backwards compatible with all versions since v1.1.0.</p>

<p>Version <b>1.3.1</b> (05/09/2010) is a stable patch release, which fixes a bug related to namespace resolution when working with more than one version
of DDMS at a time. It is backwards compatible with all versions since v1.1.0.</p>

<p>Version <b>1.3.0</b> (05/07/2010) is a stable release, and adds support for custom validation through the 
<a href="documentation-schematron.jsp">ISO Schematron</a> standard. It is backwards compatible with all versions since v1.1.0.</p>

<p>Version <b>1.2.1</b> (04/24/2010) is a stable patch release, which fixes a bug where QNames were compared based on namespace prefixes rather than URIs. 
It is backwards compatible with v1.2.0 and v1.1.0.</p>

<p>Version <b>1.2.0</b> (04/21/2010) is a stable release, and adds support for configurable properties. It also has improved unit tests and documentation. It is backwards
compatible with v1.1.0.</p>

<p>Version <b>1.1.0</b> (04/08/2010) is a stable release, and adds support for DDMS 2.0 and the Extensible Layer. 
It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=31">backwards compatible</a> with earlier versions.</p>

<p>Version <b>1.0.0</b> (04/01/2010) is the first official stable release of DDMSence. <!--  It is backwards compatible with v0.9.d and v0.9.c. --></p>

<p>Version <b>0.9.d</b> (03/29/2010) includes further validation improvements. This version is believed to be stable, and is intended to be the test candidate
for v1.0.0. It is backwards compatible with v0.9.c.</p>

<p>Version <b>0.9.c</b> (03/25/2010) adds warning support to the validation system. This version is believed to be stable, and is intended to be a limited audience release
for early feedback. It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=32">backwards compatible</a> with earlier versions.</p>

<p>Version <b>0.9.b</b> (03/24/2010) is the initial beta release. This version is believed to be stable, and is intended to be a limited audience release for early feedback.</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>