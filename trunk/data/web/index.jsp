<html>
<head>
	<title>DDMSence: An open source Java library for DDMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="DDMSence,DDMS,Metadata,Discovery,Java,DoD,XML,DDMEssence,DDMSense,Essence" />

	<!-- DDMS Resource Record --> 	
	<meta name="resource.resourceElement" content="true" />
	<meta name="resource.createDate" content="2010-01-21" />
	<meta name="resource.ism.DESVersion" content="5" />
	<meta name="resource.ntk.DESVersion" content="5" />
	<meta name="resource.classification" content="U" />
	<meta name="resource.ownerProducer" content="USA" />
	<meta name="identifier.qualifier" content="URI" />
	<meta name="identifier.value" content="urn:buri:ddmsence:website" />
	<meta name="title" content="DDMSence Home Page" />
	<meta name="title.classification" content="U" />
	<meta name="title.ownerProducer" content="USA" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)" />
	<meta name="description.classification" content="U" />
	<meta name="description.ownerProducer" content="USA" />
	<meta name="language.qualifier" content="http://purl.org/dc/elements/1.1/language" />
	<meta name="language.value" content="en" />
	<meta name="dates.created" content="2010-03-13" />
	<meta name="dates.posted" content="2003-03-13" />
	<meta name="dates.approvedOn" content="2010-04-01" />
	<meta name="rights.privacyAct" content="false" />
	<meta name="rights.intellectualProperty" content="true" />
	<meta name="rights.copyright" content="true" />
	<meta name="type.qualifier" content="DCMITYPE" />
	<meta name="type.value" content="http://purl.org/dc/dcmitype/Text" />	
	<meta name="creator.entityType" content="person" />
	<meta name="creator.name" content="Brian Uri" />
	<meta name="creator.name" content="Brian" />
	<meta name="creator.surname" content="Uri" />
	<meta name="creator.classification" content="U" />
	<meta name="creator.ownerProducer" content="USA" />
	<meta name="publisher.entityType" content="person" />
	<meta name="publisher.name" content="Brian Uri" />
	<meta name="publisher.name" content="Brian" />
	<meta name="publisher.surname" content="Uri" />
	<meta name="publisher.classification" content="U" />
	<meta name="publisher.ownerProducer" content="USA" />
	<meta name="pointOfContact.entityType" content="person" />
	<meta name="pointOfContact.name" content="Brian Uri" />
	<meta name="pointOfContact.name" content="Brian" />
	<meta name="pointOfContact.surname" content="Uri" />
	<meta name="format.media" content="text/html" />
	<meta name="format.medium" content="digital" />
	<meta name="subjectCoverage.keyword" content="DDMSence" />
	<meta name="subjectCoverage.keyword" content="DDMS" />
	<meta name="subjectCoverage.keyword" content="Metadata" />
	<meta name="subjectCoverage.keyword" content="Discovery" />
	<meta name="subjectCoverage.keyword" content="Java" />
	<meta name="subjectCoverage.keyword" content="DoD" />
	<meta name="subjectCoverage.keyword" content="XML" />
	<meta name="subjectCoverage.keyword" content="DDMEssence" />
	<meta name="subjectCoverage.keyword" content="DDMSense" />
	<meta name="subjectCoverage.keyword" content="Essence" />
	<meta name="virtualCoverage.address" content="http://ddmsence.urizone.net/" />
	<meta name="virtualCoverage.networkProtocol" content="URL" />
	<meta name="temporalCoverage.name" content="Unknown" />
	<meta name="temporalCoverage.start" content="2010-03-24T12:00:00Z" />
	<meta name="temporalCoverage.end" content="Not Applicable" />
	<meta name="ddms.generator" content="DDMSence 2.0.0" />
	<meta name="ddms.version" content="4.0.1" />
		
	<script type="text/javascript" src="./shared/jquery-1.6.4.min.js"></script>
	<script type="text/javascript">

	// Collapsible subsection code
	$(document).ready(function() {
		$("div.divExpand").click(
			function() {
				$(this).hide("fast");
				$(this).next().show("fast");
			});
		});
	</script>
</head>
<body>
<%@ include file="../shared/header.jspf" %>
<div class="newsFeed">
	<b><u>Recent News</u></b><br /><br />
	<div class="newsUpdate"><u>12/01/2011</u>: <a href="downloads.jsp">v2.0.0</a> released, adding support for DDMS 4.0.1.</div>
	<div class="newsUpdate"><u>10/20/2011</u>: I gave a briefing on DDMSence at the <a href="https://metadata.ces.mil/mdr/mdwgDocuments.htm">DoD Metadata Working Group Meeting</a> on October 20 in Columbia, MD.</div>
	<div class="newsUpdate"><u>09/25/2011</u>: Development on v2.0.0 is complete! v2.0.0 will support DDMS 4.0.1, and will be released once the "Public Release" versions of IC-ISM and IC-NTK are available to be bundled with DDMSence.</div>
	<div class="newsUpdate"><u>07/17/2011</u>: v1.11.0 released, adding <a href="documentation-schematron.jsp">support for XSLT2-based Schematron validation</a>.</div>
	<div class="divExpand feed"><a href="#" onClick="return false;">more...</a></div>	
	<div id="oldNews" class="divHidden">
		<div class="newsUpdate"><u>07/05/2011</u>: v1.10.0 released, adding support for DDMS 3.1.</div>
		<div class="newsUpdate"><u>06/06/2011</u>: <a href="schematron.jsp">Schematron Implementation for DDMS</a> published.</div>
		<div class="newsUpdate"><u>06/02/2011</u>: v1.9.1 released. Example code for <a href="validator.uri">DDMS Validator</a> and <a href="builder.uri">DDMS Builder</a> posted.</div>
		<div class="newsUpdate"><u>05/24/2011</u>: v1.9.0 released.</div>
		<div class="newsUpdate"><u>05/13/2011</u>: v1.8.0 released, introducing the Component Builder framework.</div>
		<div class="newsUpdate"><u>03/10/2011</u>: v1.7.2 released.</div>
		<div class="newsUpdate"><u>11/26/2010</u>: <a href="relationalTables.jsp">Relational Database Model for DDMS</a> published.</div>
		<div class="newsUpdate"><u>11/16/2010</u>: v1.7.1 released.</div>
		<div class="newsUpdate"><u>09/18/2010</u>: v1.7.0 released.</div>	
		<div class="newsUpdate"><u>09/07/2010</u>: v1.6.0 released.</div>
		<div class="newsUpdate"><u>09/01/2010</u>: <a href="validator.uri">DDMS validator</a> released.</div>
		<div class="newsUpdate"><u>07/07/2010</u>: v1.5.1 released.</div>
		<div class="newsUpdate"><u>07/05/2010</u>: v1.5.0 released.</div>
		<div class="newsUpdate"><u>06/08/2010</u>: v1.4.0 released.</div>
		<div class="newsUpdate"><u>05/14/2010</u>: v1.3.2 released.</div>
		<div class="newsUpdate"><u>05/11/2010</u>: Added table of supported XSLT Engines to Schematron Validation Power Tip.</div>
		<div class="newsUpdate"><u>05/09/2010</u>: v1.3.1 released.</div>
		<div class="newsUpdate"><u>05/07/2010</u>: v1.3.0 released.</div>
		<div class="newsUpdate"><u>04/24/2010</u>: v1.2.1 released.</div>
		<div class="newsUpdate"><u>04/21/2010</u>: v1.2.0 released.</div>
		<div class="newsUpdate"><u>04/08/2010</u>: v1.1.0 released.</div>
		<div class="newsUpdate"><u>04/01/2010</u>: v1.0.0 released. DDMSence information posted on the
			<a href="https://metadata.ces.mil/mdr/topic.htm?topicId=4662" target="_new">DoD Metadata Registry Community Forum</a> and on 
			<a href="http://freshmeat.net/projects/ddmsence" target="_new">freshmeat</a>.</div>
	<!-- 
		<div class="newsUpdate"><u>03/29/2010</u>: v0.9.d released.</div>
		<div class="newsUpdate"><u>03/25/2010</u>: v0.9.c released.</div>
		<div class="newsUpdate"><u>03/24/2010</u>: v0.9.b released.</div>
	-->
	</div><br /><br />
	
	<div class="feed">New Release Feed: <a href="http://code.google.com/feeds/p/ddmsence/downloads/basic"><img border="0" src="./images/atom.png" width="14" height="14" title="Atom Feed" /></a></div>	
	<div class="feed">SVN Code Feed: <a href="http://code.google.com/feeds/p/ddmsence/svnchanges/basic"><img border="0" src="./images/atom.png" width="14" height="14" title="Atom Feed" /></a></div>
	<div class="feed">Issue Tracker Feed: <a href="http://code.google.com/feeds/p/ddmsence/issueupdates/basic"><img border="0" src="./images/atom.png" width="14" height="14" title="Atom Feed" /></a></div>
	
</div>

<h1>DDMSence v@ddmsence.version@</h1>

<p>
DDMSence (pronounced "<i>dee-dee-em-<font color="#660000">Essence</font></i>") is an open source Java API which supports the 
<a href="http://metadata.ces.mil/mdr/irs/DDMS/" target="_new">DoD Discovery Metadata Specification (DDMS)</a>. 
It transforms XML DDMS Resource records into a Java object model (using XOM and Xerces), allowing them to be manipulated or traversed within the context of a Java environment. 
</p>

<ul>
	<li>Provides a complete implementation of the DDMS specification (including the Extensible Layer) using DDMS 2.0, 3.0, 3.1, or 4.0.1.</li>
	<li>Java object model can be created from existing XML files, or built up from scratch with basic Java data types. Components automatically transform into valid, well-formed XML, HTML, and Text records.</li>
	<li>Validates resources against the complete specification, not just the schemas. Integrated with the Controlled Vocabulary Enumerations for ISM attributes, as defined by the Intelligence Community, and supports Schematron-based custom validation rules.</li>
	<li>Three sample applications and accompanying <a href="documentation.jsp#started">tutorials</a> provide an introduction to the library,
		and <a href="documentation.jsp#tips">Power Tips</a> are available for more experienced users.</li>
</ul>
<h2>Suggested Use Cases</h2>

<ul>
<li>Reading existing DDMS resources, possibly as part of a query or search service</li>
<li>Creating new DDMS resources from scratch, possibly as part of a web-based wizard</li>
<li>Editing existing DDMS resources</li>
<li>Transforming older DDMS resources to the latest version of DDMS</li>
<li>Applying custom Community of Interest (COI) constraints to a DDMS resource with Schematron</li>
</ul>

<p>DDMSence comes with a complete set of JUnit tests, and is being released under the GNU Lesser General Public License (<a href="license.jsp">LGPL</a>).</p>
		
<img src="./images/chart.png" width="720" height="280" title="DDMSence Flow Chart"  />

<h1>About the Author</h1>

<p><img src="./images/BU.jpg" width="73" height="100" title="BU" class="bordered" align="left" />
<b><a href="http://www.linkedin.com/in/urizone">Brian Uri!</a></b> is a Technical Lead at <a href="http://www.fgm.com/" target="_new">FGM, Inc.</a> 
in Reston, Virginia. He has been involved in the development and support of the <a href="http://metadata.ces.mil/" target="_new">DoD Metadata Registry</a>
and other enterprise services since the inception of the Net-Centric Enterprise Services (NCES) program in 2005. He currently supports the Defense
Information Systems Agency (DISA) in its role as the Technical Agent for Universal Core (UCore). DDMSence was conceived in January 2010 and
grew out of Brian's desire to gain more practical XML Schema experience, to write something useful which had never been done before, and an 
unusually large amount of free time.</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>