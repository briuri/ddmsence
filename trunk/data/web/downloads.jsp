<html>
<head>
	<title>DDMSence: Downloads</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Downloads</h1>

<p>Before you begin, you will need a <a href="http://java.sun.com/javase/downloads/index.jsp" target="_new">JRE or JDK</a> which supports 1.5.x or higher.</p>

<p>Downloads come in two flavor:</p>

<p><b>ddmsence-bin-version.zip</b>: This download is intended for end users who wish to used DDMSence in their own projects. It contains the DDMSence JAR files,
the complete API documentation (which is also available <a href="/docs/">online</a>), source code for the sample applications,
and supporting 3rd-party JAR files. Sample applications can be run from the <a href="documentation.jsp#started">command line</a>.</p>

<p><b>ddmsence-src-version.zip</b>: This download is intended for developers who wish to see what DDMSence is doing under the hood. It contains source 
code for the main library, unit tests, and the sample applications, as well as supporting 3rd-party JAR files. This download contains a <b>build.xml</b> 
file which will allow you to compile source code, run unit tests, generate API documentation, or even generate the "bin"-flavored archive with <a href="http://ant.apache.org/">Apache Ant</a>. 
I have include the <b>.project</b> file if you wish to import the project into Eclipse.</p>

<h3>Latest Release</h3>

<p>Version <b>0.9.b</b> is the initial beta release. It provides a full implementation of the core layers of DDMS 3.0, in addition to a few
sample applications. This version is believed to be stable, and is intended to be a limited audience release for early feedback.</p>

<b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-0.9.b.zip">ddmsence-bin-0.9.b.zip</a></b> (1.8 MB)<br />
<br />
<b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-0.9.b.zip">ddmsence-src-0.9.b.zip</a></b> (2.5 MB)<br />

<h3>Older Releases</h3>

<p>There are no earlier releases available at this time.</p>

<h3>Versioning</h3>

<p>DDMSence will adopt a standard "major.minor.patch" versioning system for as long as it makes sense to do so:</p>
<ul>
	<li>A change in major version will include major architecture changes or break backwards compatibility.</li>
	<li>A change in minor version will include new features or minor bug fixes.</li>
	<li>A change in patch version will be limited to minor bug fixes.</li>
	<li>If the patch version is a letter, such as "<b>b</b>", the release may be unstable. Major/minor numbers will increment after an unstable release (so the beta/pre-release version of 1.2 is probably 1.1.b, and NOT 1.2.b).</li>
</ul>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>