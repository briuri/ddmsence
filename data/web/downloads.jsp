<html>
<head>
	<title>DDMSence: Downloads</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Downloads</h1>

<p>Before you begin, you will need a <a href="http://java.sun.com/javase/downloads/index.jsp" target="_new">JRE or JDK</a> which supports Java 1.5 or higher. Downloads come in two flavors:</p>

<p><b><code>ddmsence-bin-version.zip</code></b>: This download is intended for end users who wish to use DDMSence in their own projects. It contains the compiled DDMSence JAR files,
the complete API documentation (which is also available <a href="/docs/">online</a>), source code for the sample applications,
and supporting 3rd-party JAR files. Sample applications can be run from the <a href="documentation.jsp#started">command line</a>.</p>

<p><b><code>ddmsence-src-version.zip</code></b>: This download is intended for developers who wish to see what DDMSence is doing under the hood. It contains source 
code for the main library, unit tests, and the sample applications, as well as supporting 3rd-party JAR files. This download contains a <code>build.xml</code> 
file which will allow you to compile source code, run unit tests, generate API documentation, or even generate the "bin"-flavored archive with <a href="http://ant.apache.org/">Apache Ant</a>. 
I have include the <code>.project</code> file if you wish to import the project into Eclipse.</p>

<h1>Latest Release: v1.5.0</h1>

<p>Version <b>1.5.0</b> (7/05/2010) is a stable release, adds DDMSReader support for Strings, Readers, and InputStreams. It also updates the bundled version of XOM
to 1.2.6. This release is backwards compatible with v1.4.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.5.0.zip"><code>ddmsence-bin-1.5.0.zip</code></a></b> (4.0 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.5.0.zip"><code>ddmsence-src-1.5.0.zip</code></a></b> (4.3 MB)</li>
</ul>

<h3>Versioning</h3>

<p>DDMSence will follow a "major.minor.patch" versioning system for as long as it makes sense to do so:</p>
<ul>
	<li>A change in major version will generally include major architecture changes and will likely break backwards compatibility.</li>
	<li>A change in minor version can include new features and bug fixes. On rare occasions, minor releases may break backwards compatibility.</li>
	<li>A change in patch version will be limited to bug fixes.</li>
	<li>If the patch version is a letter, such as "<b>b</b>", the release may be unstable. Major/minor numbers will increment after an unstable release (so the beta/pre-release version of 1.2 is probably 1.1.b, and NOT 1.2.b).</li>
</ul>

<p>If a new version is listed as "backwards compatible", it means that you can update to the new version without changing any of your code that worked with the old version.  
All changes which are not backwards compatible will be linked from this page.</p> 

<h3>Older Releases</h3>

<p>Version <b>1.4.0</b> (6/08/2010) is a stable release, and changes configurable property support to be code-driven rather than property-file driven.
It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=46">backwards compatible</a> with earlier versions.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.4.0.zip"><code>ddmsence-bin-1.4.0.zip</code></a></b> (4.0 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.4.0.zip"><code>ddmsence-src-1.4.0.zip</code></a></b> (4.3 MB)</li>
</ul>

<p>Version <b>1.3.2</b> (5/14/2010) is a stable patch release, which fixes <a href="http://code.google.com/p/ddmsence/issues/list?can=1&q=milestone=v1.3.2">four minor bugs</a>. It is backwards compatible with all versions since v1.1.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.3.2.zip"><code>ddmsence-bin-1.3.2.zip</code></a></b> (3.6 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.3.2.zip"><code>ddmsence-src-1.3.2.zip</code></a></b> (4.3 MB)</li>
</ul>

<p>Version <b>1.3.1</b> (5/9/2010) is a stable patch release, which fixes a bug related to namespace resolution when working with more than one version
of DDMS at a time. It is backwards compatible with all versions since v1.1.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.3.1.zip"><code>ddmsence-bin-1.3.1.zip</code></a></b> (3.6 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.3.1.zip"><code>ddmsence-src-1.3.1.zip</code></a></b> (4.3 MB)</li>
</ul>

<p>Version <b>1.3.0</b> (5/7/2010) is a stable release, and adds support for custom validation through the 
<a href="documentation.jsp#tips-schematron">ISO Schematron</a> standard. It is backwards compatible with all versions since v1.1.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.3.0.zip"><code>ddmsence-bin-1.3.0.zip</code></a></b> (3.6 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.3.0.zip"><code>ddmsence-src-1.3.0.zip</code></a></b> (4.3 MB)</li>
</ul>

<p>Version <b>1.2.1</b> (4/24/2010) is a stable patch release, which fixes a bug where QNames were compared based on namespace prefixes rather than URIs. 
It is backwards compatible with v1.2.0 and v1.1.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.2.1.zip"><code>ddmsence-bin-1.2.1.zip</code></a></b> (1.7 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.2.1.zip"><code>ddmsence-src-1.2.1.zip</code></a></b> (2.4 MB)</li>
</ul>

<p>Version <b>1.2.0</b> (4/21/2010) is a stable release, and adds support for configurable properties. It also has improved unit tests and documentation. It is backwards
compatible with v1.1.0.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.2.0.zip"><code>ddmsence-bin-1.2.0.zip</code></a></b> (1.7 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.2.0.zip"><code>ddmsence-src-1.2.0.zip</code></a></b> (2.4 MB)</li>
</ul>

<p>Version <b>1.1.0</b> (4/8/2010) is a stable release, and adds support for DDMS 2.0 and the Extensible Layer. 
It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=31">backwards compatible</a> with earlier versions.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.1.0.zip"><code>ddmsence-bin-1.1.0.zip</code></a></b> (1.7 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.1.0.zip"><code>ddmsence-src-1.1.0.zip</code></a></b> (2.4 MB)</li>
</ul>

<p>Version <b>1.0.0</b> (4/1/2010) is the first official stable release of DDMSence. It is backwards compatible with v0.9.d and v0.9.c.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-1.0.0.zip"><code>ddmsence-bin-1.0.0.zip</code></a></b> (1.7 MB, compiled with JDK1.5.0_22)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-1.0.0.zip"><code>ddmsence-src-1.0.0.zip</code></a></b> (2.3 MB)</li>
</ul>

<p>Version <b>0.9.d</b> (3/29/2010) includes further validation improvements. This version is believed to be stable, and is intended to be the test candidate
for v1.0.0. It is backwards compatible with v0.9.c.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-0.9.d.zip"><code>ddmsence-bin-0.9.d.zip</code></a></b> (1.9 MB, compiled with JDK1.6.0_16)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-0.9.d.zip"><code>ddmsence-src-0.9.d.zip</code></a></b> (2.3 MB)</li>
</ul>

<p>Version <b>0.9.c</b> (3/25/2010) adds warning support to the validation system. This version is believed to be stable, and is intended to be a limited audience release
for early feedback. It is not <a href="http://code.google.com/p/ddmsence/issues/detail?id=32">backwards compatible</a> with earlier versions.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-0.9.c.zip"><code>ddmsence-bin-0.9.c.zip</code></a></b> (1.8 MB, compiled with JDK1.6.0_16)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-0.9.c.zip"><code>ddmsence-src-0.9.c.zip</code></a></b> (2.5 MB)</li>
</ul>

<p>Version <b>0.9.b</b> (3/24/2010) is the initial beta release. This version is believed to be stable, and is intended to be a limited audience release for early feedback.</p>

<ul>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-bin-0.9.b.zip"><code>ddmsence-bin-0.9.b.zip</code></a></b> (1.8 MB, compiled with JDK1.6.0_16)</li>
<li><b><a href="http://ddmsence.googlecode.com/files/ddmsence-src-0.9.b.zip"><code>ddmsence-src-0.9.b.zip</code></a></b> (2.5 MB)</li>
</ul>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>