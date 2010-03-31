<html>
<head>
	<title>DDMSence: An open source Java library for DDMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
	
	<!-- DDMS Resource Record --> 
	<meta name="security.resourceElement" content="true" />
	<meta name="security.createDate" content="2010-03-21" />
	<meta name="security.DESVersion" content="2" />
	<meta name="security.classification" content="U" />
	<meta name="security.ownerProducer" content="USA" />
	<meta name="identifier.qualifier" content="http://purl.org/dc/terms/URI" />
	<meta name="identifier.value" content="urn:buri:ddmsence:website" />
	<meta name="title" content="DDMSence Home Page" />
	<meta name="title.classification" content="U" />
	<meta name="title.ownerProducer" content="USA" />
	<meta name="subtitle" content="Multiple Versions" />
	<meta name="subtitle.classification" content="U" />
	<meta name="subtitle.ownerProducer" content="USA" />
	<meta name="description" content="An open source Java library for DDMS." />
	<meta name="description.classification" content="U" />
	<meta name="description.ownerProducer" content="USA" />
	<meta name="language.qualifier" content="http://purl.org/dc/elements/1.1/language" />
	<meta name="language" content="en" />
	<meta name="date.created" content="2010-03-13" />
	<meta name="date.posted" content="2003-03-13" />
	<meta name="rights.privacy" content="false" />
	<meta name="rights.intellectualproperty" content="true" />
	<meta name="rights.copy" content="true" />
	<meta name="type.qualifier" content="DCMITYPE" />
	<meta name="type.value" content="http://purl.org/dc/dcmitype/Text" />
	<meta name="creator.entityType" content="Person" />
	<meta name="creator.name" content="Brian" />
	<meta name="creator.name" content="BU" />
	<meta name="creator.surname" content="Uri" />
	<meta name="publisher.entityType" content="Person" />
	<meta name="publisher.name" content="Brian" />
	<meta name="publisher.name" content="BU" />
	<meta name="publisher.surname" content="Uri" />
	<meta name="pointOfContact.entityType" content="Person" />
	<meta name="pointOfContact.name" content="Brian" />
	<meta name="pointOfContact.name" content="BU" />
	<meta name="pointOfContact.surname" content="Uri" />
	<meta name="format.media" content="text/html" />
	<meta name="format.medium" content="digital" />
	<meta name="subject.keyword" content="DDMSence" />
	<meta name="subject.keyword" content="DDMS" />
	<meta name="subject.keyword" content="Metadata" />
	<meta name="subject.keyword" content="Discovery" />
	<meta name="subject.keyword" content="Java" />
	<meta name="subject.keyword" content="DoD" />
	<meta name="virtual.address" content="http://ddmsence.urizone.net/" />
	<meta name="virtual.networkProtocol" content="URL" />
	<meta name="temporal.TimePeriod" content="Unknown" />
	<meta name="temporal.DateStart" content="2010-03-13" />
	<meta name="temporal.DateEnd" content="Unknown" />
	<meta name="security.excludeFromRollup" content="true" />
	<meta name="security.classification" content="U" />
	<meta name="security.ownerProducer" content="USA" />
	<meta name="ddms.generator" content="DDMSence 0.9.c" />
	
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMSence</h1>

<p>
<img src="./images/splashImage.png" width="302" height="210" title="DDMSence" align="right" />
DDMSence (pronounced "<i>dee-dee-em-Essence</i>") is an open source Java API which supports the 
<a href="http://metadata.dod.mil/mdr/irs/DDMS/" target="_new">DoD Discovery Metadata Specification (DDMS)</a>. 
Using <a href="http://xom.nu/" target="_new">XOM</a> and the <a href="http://xerces.apache.org/" target="_new">Xerces</a> Java parser, 
DDMSence can convert an XML DDMS Resource record into a Java object model, or transform existing Java data into valid, well-formed XML, 
HTML, and Text records. DDMSence also performs logical validation of rules which are listed in the DDMS documentation but not coded into 
the DDMS schema (such as the validation of latitude/longitude values).</p>

<p>DDMS components created with this library maintain an underlying XOM element structure, which provides latent avenues for future 
improvement, based on XOM's built-in support of XPath, XInclude, and XSLT. However, you do not need to have any experience with XOM to use
this library.</p>

<p>DDMSence comes with a full set of JUnit tests, and code management was aided by <a href="http://www.eclemma.org/" target="_new">EclEmma</a> 
and <a href="http://findbugs.sourceforge.net/" target="_new">FindBugs</a>. It is being released under the GNU Lesser General Public License 
(<a href="license.jsp">LGPL</a>).</p>

<h1>Current Release</h1>

<p><u>04/01/2010</u>: <a href="downloads.jsp">Version 1.0.0</a> is now available for download. This stable version is the first official public 
release.</p>

<ul>
	<li>The core layers of the DDMS 3.0 Pre-Release specification are fully implemented.</li>
	<li>Validation of DDMS components supports both warnings and errors.</li>
	<li>ICISM security attributes are validated against their Controlled Vocabulary Enumerations, as defined by the Intelligence Community.</li>
	<li>Three sample applications and accompanying <a href="documentation.jsp#samples">tutorials</a> provide an introduction to the library.</li>
</ul>

<h1>On the Horizon</h1>

<p>Here are some areas which I would like to improve in future releases:</p>
<ul>
	<li>Supporting DDMS 2.0 (and possibly earlier versions).</li>
	<li>Adding support for the extensible portions of DDMS components (Person, Organization, Service, Unknown, keyword, category, and the Resource itself).</li>
	<li>Implementing the security constraints found in the IC's "XML Data Encoding Specification for Information Security Marking Metadata" document.</li>
</ul>

<p>Please follow the "<a href="http://code.google.com/p/ddmsence/issues/list">Issue Tracking</a>" link in the menu above to see a complete list
 of enhancements under consideration.</p>
			
<h1>About the Author</h1>

<p><img src="./images/BU.jpg" width="73" height="100" title="BU" align="left" />
<b>Brian Uri!</b> is a software engineer at <a href="http://www.fgm.com/" target="_new">FGM, Inc.</a> in Reston, Virginia. He has been a member
of the <a href="http://metadata.dod.mil/" target="_new">DoD Metadata Registry</a> team since 2004, and is currently a
technical lead on that project. DDMSence grew out of the author's desire to gain more practical XML Schema experience, to write something useful
which had never been done before, and an unusually large amount of free time.</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>