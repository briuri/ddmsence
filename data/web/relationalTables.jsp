<html>
<head>
	<title>DDMSence: DDMS Table Model</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<p align="right"><b>Last Update:</b> 11/26/2010 at 12:21</p>

<h1>DDMS Table Model</h1>

<p>This document is an attempt to map the DDMS specification to a relational database model. The intent of this mapping is to be comprehensive first 
and pragmatic second -- the full scope of DDMS will be modeled, but some design decisions may be made for simplicity, such as modeling lists of values 
as a delimited string value. Although direct-to-table persistence mapping will probably not be a feature in any version of DDMSence, this table model 
may be useful when integrating DDMSence with an existing persistence framework like Hibernate or the Oracle XML SQL Utility (XSU).</p> 
<p>At this point, all of the DDMS components have been modelled. I am in the process of reviewing and revising the tables before I publicize 
this document.</p>

<a name="tables-notes"></a><h4>General Notes</h4> 
<ul>
<li>Column names generally match the XML element name, which is consistent with DDMSence's object model. There are a few minor differences 
	(such as using "timePeriodName" in a temporalCoverage element to avoid confusing it with the plain XML element name).</li>
<li>The unique sequenced <code>id</code> value which is the primary key in each table should be unique across <i>all</i> tables. This will allow foreign key
	references in child components without requiring the child table to know what kind of parent it has. All <code>id</code> columns could also contain 
	generated UUID values instead of sequenced integers.</li>
<li>Child elements will have links back to their parents, but not in the reverse direction. This key is allowed to have an initial 
	<code>&lt;NULL&gt;</code> value, to support a bottom-up approach to building DDMS resources from scratch.</li>
<li>If a table column is a character string and a value is not provided, an empty string should be favored instead of <code>&lt;NULL&gt;</code>.</li>
<li>The intent of the tables is to model the resource data, not schema data. XML namespaces and other schema constructs are not necessarily modeled.</li>
<li>Reference tables (i.e. the four types of producers, or the valid names of ICISM security attributes) are not included here. Columns which have string 
	values for these constants could just as easily have foreign keys to a reference table.</li>
<li>Most validation constraints are omitted from this model, since it is assumed that a validating library like DDMSence would be placed in front of the tables.</li>
<li>Character string lengths are fairly arbitrary, although the numbers I chose are relatively reasonable for the types of data the fields contain. 
	URI fields are set at 2048 characters to match Internet Explorer URL restrictions.</li>
<li>The pipe character, <b>|</b>, is suggested as a delimiter for columns containing lists in string form.</li>
</ul>

<h4>Table Overview</h4>
<table>
	<tr>
		<td colspan="5">
			<a href="#tables-primary">Primary DDMS Record</a>
			<ul>
				<li><a href="#ddmsResource">ddmsResource</a></li>
			</ul>
		</td>
	</tr><tr>
		<td class="relOverviewLayer">
			<a href="#tables-resource">Resource Layer</a>
			<ul>
				<li><a href="#ddmsDates">ddmsDates</a></li>
				<li><a href="#ddmsIdentifier">ddmsIdentifier</a></li>
				<li><a href="#ddmsLanguage">ddmsLanguage</a></li>
				<li><a href="#ddmsProducer">ddmsProducer</a></li>
				<li><a href="#ddmsRights">ddmsRights</a></li>
				<li><a href="#ddmsSource">ddmsSource</a></li>
				<li><a href="#ddmsSubtitle">ddmsSubtitle</a></li>
				<li><a href="#ddmsTitle">ddmsTitle</a></li>
				<li><a href="#ddmsType">ddmsType</a></li>				
			</ul>
					
			<a href="#tables-format">Format Layer</a>
			<ul>
				<li><a href="#ddmsFormat">ddmsFormat</a></li>
				<li><a href="#ddmsMediaExtent">ddmsMediaExtent</a></li>
			</ul>
		</td><td class="relOverviewLayer">
			<a href="#tables-summary">Summary Layer</a>
			<ul>
				<li><a href="#ddmsBoundingBox">ddmsBoundingBox</a></li>
				<li><a href="#ddmsBoundingGeometry">ddmsBoundingGeometry</a></li>
				<li><a href="#ddmsCategory">ddmsCategory</a></li>
				<li><a href="#ddmsCountryCode">ddmsCountryCode</a></li>
				<li><a href="#ddmsDescription">ddmsDescription</a></li>
				<li><a href="#ddmsFacilityIdentifier">ddmsFacilityIdentifier</a></li>
				<li><a href="#ddmsGeographicIdentifier">ddmsGeographicIdentifier</a></li>
				<li><a href="#ddmsGeospatialCoverage">ddmsGeospatialCoverage</a></li>
				<li><a href="#ddmsGmlPoint">ddmsGmlPoint</a></li>
				<li><a href="#ddmsGmlPolygon">ddmsGmlPolygon</a></li>
				<li><a href="#ddmsGmlPosition">ddmsGmlPosition</a></li>
				<li><a href="#ddmsKeyword">ddmsKeyword</a></li>
				<li><a href="#ddmsLink">ddmsLink</a></li>
				<li><a href="#ddmsPostalAddress">ddmsPostalAddress</a></li>
				<li><a href="#ddmsRelatedResource">ddmsRelatedResource</a></li>
				<li><a href="#ddmsRelatedResources">ddmsRelatedResources</a></li>
				<li><a href="#ddmsSrsAttributes">ddmsSrsAttributes</a></li>
				<li><a href="#ddmsSubjectCoverage">ddmsSubjectCoverage</a></li>
				<li><a href="#ddmsTemporalCoverage">ddmsTemporalCoverage</a></li>
				<li><a href="#ddmsVerticalExtent">ddmsVerticalExtent</a></li>
				<li><a href="#ddmsVirtualCoverage">ddmsVirtualCoverage</a></li>
			</ul>		
			<a href="#tables-security">Security Layer</a>
			<ul>
				<li><a href="#ddmsSecurity">ddmsSecurity</a></li>
				<li><a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a></li>
			</ul>			
				
		</td><td class="relOverviewLayer">
			<a href="#tables-extensible">Extensible Layer</a>
			<ul>
				<li><a href="#ddmsExtensibleAttribute">ddmsExtensibleAttribute</a></li>
				<li><a href="#ddmsExtensibleElement">ddmsExtensibleElement</a></li>
			</ul>			
		</td>
	</tr>
</table>

<div class="clear"></div>

<a name="tables-primary"></a><h4>Primary DDMS Record</h4>

<a name="ddmsResource"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsResource</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#ResourceHeader"><code>ddms:Resource</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/Resource.html">Resource</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsDates">ddmsDates</a>, 
			<a href="#ddmsDescription">ddmsDescription</a>, 
			<a href="#ddmsExtensibleElement">ddmsExtensibleElement</a>, 
			<a href="#ddmsFormat">ddmsFormat</a>, 
			<a href="#ddmsGeospatialCoverage">ddmsGeospatialCoverage</a>, 
			<a href="#ddmsIdentifier">ddmsIdentifier</a>, 
			<a href="#ddmsLanguage">ddmsLanguage</a>, 
			<a href="#ddmsProducer">ddmsProducer</a>, 
			<a href="#ddmsRelatedResources">ddmsRelatedResources</a>, 
			<a href="#ddmsRights">ddmsRights</a>, 
			<a href="#ddmsSecurity">ddmsSecurity</a>, 
			<a href="#ddmsSource">ddmsSource</a>, 
			<a href="#ddmsSubjectCoverage">ddmsSubjectCoverage</a>, 
			<a href="#ddmsSubtitle">ddmsSubtitle</a>, 
			<a href="#ddmsTemporalCoverage">ddmsTemporalCoverage</a>, 
			<a href="#ddmsTitle">ddmsTitle</a>, 
			<a href="#ddmsType">ddmsType</a>, and
			<a href="#ddmsVirtualCoverage">ddmsVirtualCoverage</a> 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsExtensibleAttribute">ddmsExtensibleAttribute</a> (DDMS 3.0), and
			<a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> (DDMS 3.0)
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Please see the documentation on <a href="documentation.jsp#tips-extensible">Extensible Attributes on a Resource</a> to 
			understand the ambiguity problem associated with modelling security attributes as extensible attributes.	
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row. This value is a foreign key in all child component tables.</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceElement</td><td class="relRules">boolean</td><td>Whether this tag sets the classification for the XML file as a whole (required, 
			starting in DDMS 3.0)</td>
	</tr>
	<tr class="relRow">
		<td class="relField">createDate</td><td class="relRules">char(32)</td><td>the creation date (dates are stored in string format to ensure
		that the date value is retrieved in the same <code>xsd:date</code> that it was entered in).</td>
	</tr>
	<tr class="relRow">
		<td class="relField">desVersion</td><td class="relRules">char(8)</td><td>the version of the Digital Encryption Schema used.</td>
	</tr>
	<tr class="relRow">
		<td class="relField">ddmsVersion</td><td class="relRules">char(8)</td><td>the version of DDMS used.</td>
	</tr>
</table>

<a name="tables-resource"></a><h4>The Resource Layer</h4>

<a name="ddmsDates"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsDates</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Date"><code>ddms:dates</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Dates.html">Dates</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Dates are stored in string format, to ensure that a date value is retrieved in the same XML date format that it was entered in.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">created</td><td class="relRules">char(32)</td><td>the creation date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">posted</td><td class="relRules">char(32)</td><td>the posting date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">validTil</td><td class="relRules">char(32)</td><td>the expiration date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">infoCutOff</td><td class="relRules">char(32)</td><td>the info cutoff date</td>
	</tr>
</table>

<a name="ddmsIdentifier"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsIdentifier</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Identifier"><code>ddms:identifier</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Identifier.html">Identifier</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Constraints on the qualifier and value are not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048), not null</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256), not null</td><td>the value</td>
	</tr>	
</table>

<a name="ddmsLanguage"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsLanguage</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Language"><code>ddms:language</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Language.html">Language.html</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Constraints on the qualifier and value are not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table>

<a name="ddmsProducer"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsProducer</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Organization"><code>ddms:Organization</code></a>,
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Person"><code>ddms:Person</code></a>,
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Service"><code>ddms:Service</code></a>, and
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Unknown"><code>ddms:Unknown</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Organization.html">Organization</a>,
			<a href="/docs/buri/ddmsence/ddms/resource/Person.html">Person</a>,
			<a href="/docs/buri/ddmsence/ddms/resource/Service.html">Service</a>, and
			<a href="/docs/buri/ddmsence/ddms/resource/Unknown.html">Unknown</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			This table is consistent with the DDMSence approach of <a href="documentation.jsp#design">flattening the producer hierarchy</a>, and each row 
			is a "producer entity that fulfills some role". In the DDMS schema, the hierarchy is modelled as "a producer role that is filled by some entity".<br><br>
			All four producer entities share similar characteristics (at least one name, and optional phone numbers and email addresses), so 
			they are grouped into a single table, rather than a separate table for each producer entity type. The latter approach is equally viable.<br /><br />
			This modeling also assumes that there is no reuse of producers between various roles. So, while the person named "Brian Uri" might have a creator 
			role and a contributor role, and while Brian's details might be identical in each XML element, each set of details would have a separate 
			row in this table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">producerType</td><td class="relRules">char(24)</td><td>the role being filled by this producer, i.e. "creator", "contributor", "pointOfContact", 
			or "publisher"</td>
	</tr>
	<tr class="relRow">
		<td class="relField">entityType</td><td class="relRules">char(24)</td><td>the type of this producer, i.e. "Organization", "Person", "Service" or "Unknown"</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">name</td><td class="relRules">char(256), not null</td><td>a delimited string-list of names for this producer. At least one is required.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">phone</td><td class="relRules">char(256)</td><td>a delimited string-list of phone numbers for this producer. Optional.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">email</td><td class="relRules">char(2048)</td><td>a delimited string-list of email addresses for this producer. Optional.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">surname</td><td class="relRules">char(256)</td><td>This is a Person-specific column, containing a surname. Exactly one surname is required for 
			Person records.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">userId</td><td class="relRules">char(64)</td><td>This is a Person-specific column, containing an ID for a user.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">affiliation</td><td class="relRules">char(256)</td><td>This is a Person-specific column, containing an organizational affiliation for a user. 
			Optional.</td>
	</tr>	
</table>

<a name="ddmsRights"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsRights</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Rights"><code>ddms:rights</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Rights.html">Rights</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">privacyAct</td><td class="relRules">boolean, default to "false"</td><td>whether protected by the Privacy Act</td>
	</tr>
	<tr class="relRow">
		<td class="relField">intellectualProperty</td><td class="relRules">boolean, default to "false"</td><td>whether the resource has an intellectual property rights owner</td>
	</tr>
	<tr class="relRow">
		<td class="relField">copyright</td><td class="relRules">boolean, default to "false"</td><td>whether the resource has a copyright owner</td>
	</tr>	
</table>

<a name="ddmsSource"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSource</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Source"><code>ddms:source</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Source.html">Source</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048), not null</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256), not null</td><td>the value</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">schemaQualifier</td><td class="relRules">char(64), not null</td><td>the value</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">schemaHref</td><td class="relRules">char(2048), not null</td><td>the value</td>
	</tr>	
</table>

<a name="ddmsSubtitle"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSubtitle</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Subtitle"><code>ddms:subtitle</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Subtitle.html">Subtitle</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the subtitle of the resource</td>
	</tr>
</table>

<a name="ddmsTitle"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsTitle</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Title"><code>ddms:title</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Title.html">Title</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the title of the resource</td>
	</tr>
</table>

<a name="ddmsType"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsType</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Type"><code>ddms:type</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/resource/Type.html">Type</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Constraints on the qualifier and value are not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table>




<a name="tables-format"></a><h4>The Format Layer</h4>

<a name="ddmsFormat"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsFormat</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Format"><code>ddms:format</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/format/Format.html">Format</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsMediaExtent">ddmsMediaExtent</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">mimeType</td><td class="relRules">char(64), not null</td><td>the mime type, exactly 1 required</td>
	</tr>
	<tr class="relRow">
		<td class="relField">medium</td><td class="relRules">char(64)</td><td>the medium, 0-1 optional</td>
	</tr>
</table>

<a name="ddmsMediaExtent"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsMediaExtent</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#ddms:extent"><code>ddms:extent</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/format/MediaExtent.html">MediaExtent</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			A qualifier is required when a value is present, but this constraint is not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">formatId</td><td class="relRules">integer</td><td>foreign key to the parent ddms:format element</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table>




<a name="tables-summary"></a><h4>The Summary Layer</h4>

<a name="ddmsBoundingBox"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsBoundingBox</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#GeographicBoundingBox"><code>ddms:boundingBox</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/BoundingBox.html">BoundingBox</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Longitude and latitude values may be negative, and will fall in a range of -180.0 to 180.0 for longitudes, and -90.0 to 90.0 for latitudes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent geospatialCoverage element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">WestBL</td><td class="relRules">double</td><td>westbound longitude</td>
	</tr>
	<tr class="relRow">
		<td class="relField">EastBL</td><td class="relRules">double</td><td>eastbound longitude</td>
	</tr>
	<tr class="relRow">
		<td class="relField">SouthBL</td><td class="relRules">double</td><td>northbound latitude</td>
	</tr>
	<tr class="relRow">
		<td class="relField">NorthBL</td><td class="relRules">double</td><td>southbound latitude</td>
	</tr>
</table>

<a name="ddmsBoundingGeometry"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsBoundingGeometry</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#GeographicBoundingGeometry"><code>ddms:boundingGeometry</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/BoundingGeometry.html">Bounding Geometry</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsGmlPoint">ddmsGmlPoint</a> or
			<a href="#ddmsGmlPolygon">ddmsGmlPolygon</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent geospatialCoverage row of this attribute</td>
	</tr>	
</table>

<a name="ddmsCategory"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsCategory</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Subject"><code>ddms:category</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/Category.html">Category</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsExtensibleAttribute">ddmsExtensibleAttribute</a> (DDMS 3.0)
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			No other notes.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent subjectCoverage element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">code</td><td class="relRules">char(2048)</td><td>the machine-readable representation of the category</td>
	</tr>
	<tr class="relRow">
		<td class="relField">label</td><td class="relRules">char(2048), not null</td><td>the human-readable representation of the category</td>
	</tr>		
</table>

<a name="ddmsCountryCode"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsCountryCode</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#CountryCode"><code>ddms:countryCode</code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/CountryCode.html">CountryCode</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			A country code might appear in a <code>ddms:geographicIdentifier</code> or <code>ddms:postalAddress</code> element.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048), not null</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256), not null</td><td>the value</td>
	</tr>
</table>

<!-- ############################################################################################################################################# -->
<!-- ############################################################################################################################################# -->
<!-- ############################################################################################################################################# -->
<div style="background-color: #cdd3e5"><i>The information below this point has not yet been reviewed.</i><ul>
	<li>Standardizing hyperlinks and labels so readers can immediately tell whether a link will take them to the DDMS specification, the DDMSence API documentation, or another table in this document.</li>
	<li>Converting the textual descriptions of foreign keys between tables into an explicit row or column below the details.</li>
	<li>Reviewing column sizes and standardizing the format of the details for each column.</li>
	<li>Consistency of using char or number for double values (especially lat/lon).</li>
	<li>Consistent use of <code>code</code> tags to identify column names vs. element names</li>
</ul></div>
<!-- ############################################################################################################################################# -->
<!-- ############################################################################################################################################# -->
<!-- ############################################################################################################################################# -->

<a name="ddmsDescription"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsDescription</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Description.html">ddms:description</a>
			element, which is a top-level component. Every DDMS resource will have at least 1 row in this table.
			It will be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
			table. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the description of the resource</td>
	</tr>
</table>

<a name="ddmsFacilityIdentifier"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsFacilityIdentifier</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/FacilityIdentifier.html">ddms:facilityIdentifier</a>
			element, which is nested in a ddms:geographicIdentifier element.
		</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">beNumber</td><td class="relRules">char(64), not null</td><td>unique identifier for a facility</td>
	</tr>
	<tr class="relRow">
		<td class="relField">osuffix</td><td class="relRules">char(64), not null</td><td>used in conjunction with the beNumber to identify a facility</td>
	</tr>	
</table>

<a name="ddmsGeographicIdentifier"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsGeographicIdentifier</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/GeographicIdentifier.html">ddms:geographicIdentifier</a>
			element, which is nested in a ddms:geospatialCoverage element. A GeographicIdentifier consists of a list of names and regions,
			and a reference to either a <a href="#ddmsCountryCode">CountryCode</a> or a <a href="#ddmsFacilityIdentifier">FacilityIdentifier</a>. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent geospatialCoverage element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">name</td><td class="relRules">char(256), not null</td><td>a delimited string-list of names for this identifier</td>
	</tr>
	<tr class="relRow">
		<td class="relField">region</td><td class="relRules">char(256), not null</td><td>a delimited string-list of region names for this identifier</td>
	</tr>			
</table>

<a name="ddmsGeospatialCoverage"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsGeospatialCoverage</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/GeospatialCoverage.html">ddms:geospatialCoverage</a>
			element, which is a top-level component. This element will have a reference to one row from 
			<a href="#ddmsGeographicIdentifier">GeographicIdentifier</a>,
			<a href="#ddmsBoundingBox">BoundingBox</a>,
			<a href="#ddmsBoundingGeometry">BoundingGeometry</a>,
			<a href="#ddmsPostalAddress">PostalAddress</a>, or
			<a href="#ddmsVerticalExtent">VerticalExtent</a>.			 
			There must be at least one reference to a <a href="#ddmsRelatedResource">RelatedResource</a>.
			Rows in this table may be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> table in DDMS 3.0.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">integer</td><td>foreign key to the parent DDMS resource</td>
	</tr>
</table>

<a name="ddmsGmlPoint"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsGmlPoint</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Point.html">gml:Point</a>
			element, which is nested in a ddms:boundingGeometry element. 
			Rows in this table will be associated with a row in the <a href="#ddmsSrsAttributes">ddmsSrsAttributes</a> table
			and a row in the <a href="#ddmsGmlPosition">ddmsGmlPosition</a> table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent boundingGeometry element of this attribute</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">gmlId</td><td class="relRules">char(64), not null</td><td>a unique ID for the point</td>
	</tr>	
</table>

<a name="ddmsGmlPolygon"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsGmlPolygon</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Polygon.html">gml:Polygon</a>
			element, which is nested in a ddms:boundingGeometry element. 
			Rows in this table will be associated with a row in the <a href="#ddmsSrsAttributes">ddmsSrsAttributes</a> table
			and multiple rows in the <a href="#ddmsGmlPosition">ddmsGmlPosition</a> table. Because a polygon is comprised of ordered
			positions that create an enclosed area, it is assumed that positions are ordered according to their sequenced <code>id</code> value. As
			a closed shape, the first and last positions should be identical, but no validation is done on this constraint here. The last position
			should definitely be a separate row in the ddmsGmlPosition table.
		</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent boundingGeometry element of this attribute</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">gmlId</td><td class="relRules">char(64), not null</td><td>a unique ID for the polygon</td>
	</tr>		
</table>

<a name="ddmsGmlPosition"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsGmlPosition</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Position.html">gml:pos</a>
			element, which is nested in a gml:Point or gml:Polygon element. 
			Rows in this table may be associated with a row in the <a href="#ddmsSrsAttributes">ddmsSrsAttributes</a> table.
		</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">integer, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">integer</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">latitude</td><td class="relRules">double, not null</td><td>first coordinate</td>
	</tr>
	<tr class="relRow">
		<td class="relField">longitude</td><td class="relRules">double, not null</td><td>second coordinate</td>
	</tr>
	<tr class="relRow">
		<td class="relField">heightAboveEllipsoid</td><td class="relRules">number</td><td>optional third coordinate</td>
	</tr>
</table>

<a name="ddmsKeyword"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsKeyword</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Keyword.html">ddms:keyword</a>
			element, which is nested in ddms:subjectCoverage. It may be associated with rows in the 
			<a href="#ddmsExtensibleAttribute">ddmsExtensibleAttribute</a> table in DDMS 3.0.			
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent subjectCoverage element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the keyword itself</td>
	</tr>	
</table>

<a name="ddmsLink"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsLink</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Link.html">ddms:link</a>
			element, which is contained inside of a RelatedResource. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField"></td><td class="relRules"></td><td></td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent RelatedResource element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">type</td><td class="relRules">char(64), not null</td><td>the link type has a fixed value of "locator". It is being modelled in case
			this changes in the future.</td>
	</tr>
	<tr class="relRow">
		<td class="relField">href</td><td class="relRules">char(2048), not null</td><td>the URL to the target resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">role</td><td class="relRules">char(2048)</td><td>a URI reference describing the role of the link</td>
	</tr>
	<tr class="relRow">
		<td class="relField">title</td><td class="relRules">char(2048)</td><td>a human-readable title</td>
	</tr>
	<tr class="relRow">
		<td class="relField">label</td><td class="relRules">char(2048)</td><td>a name for the link, for use by an XLink arc-type element</td>
	</tr>
</table>

<a name="ddmsPostalAddress"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsPostalAddress</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/PostalAddress.html">ddms:postalAddress</a>
			element, which is nested in a ddms:geospatialCoverage element. It will have either a state or a province, and may reference
			a <a href="ddmsCountryCode">countryCode</a>.
		</td>
	</tr>		
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent geospatialCoverage row of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">street</td><td class="relRules">char(2048)</td><td>a delimited string-list of street addresses.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">city</td><td class="relRules">char(64)</td><td>a city</td>
	</tr>
	<tr class="relRow">
		<td class="relField">state</td><td class="relRules">char(64)</td><td>the state code, if within a country with states</td>
	</tr>
	<tr class="relRow">
		<td class="relField">province</td><td class="relRules">char(64)</td><td>the province code, if within a country with provinces</td>
	</tr>
	<tr class="relRow">
		<td class="relField">postalCode</td><td class="relRules">char(64)</td><td>the postal code of the address</td>
	</tr> 	
</table>

<a name="ddmsRelatedResource"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsRelatedResource</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/RelatedResource.html">ddms:RelatedResource</a>
			element, which is nested in ddms:RelatedResources elements. The parent element must have at least one RelatedResource,
			and the RelatedResource must have at least one defined <a href="#ddmsLink">Link</a>.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent RelatedResources element</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048), not null</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256), not null</td><td>the value which describes the RelatedResource</td>
	</tr>	
</table>

<a name="ddmsRelatedResources"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsRelatedResources</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/RelatedResources.html">ddms:relatedResources</a>
			element, which is a top-level component. There must be at least one reference to a <a href="#ddmsRelatedResource">RelatedResource</a>.
			Rows in this table may be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">relationship</td><td class="relRules">char(2048), not null</td><td>a URI representing the relationship between the
			resource being described and these related resources</td>
	</tr>
	<tr class="relRow">
		<td class="relField">direction</td><td class="relRules">char(64)</td><td>the direction of the relationship, which must have a value of 
			"inbound", "outbound", or "bidirectional"</td>
	</tr>	
</table>

<a name="ddmsSrsAttributes"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSrsAttributes</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			GML SRS Attributes
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/SRSAttributes.html">SRSAttributes</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augments:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsGmlPoint">ddmsGmlPoint</a>,
			<a href="#ddmsGmlPolygon">ddmsGmlPolygon</a>, and
			<a href="#ddmsGmlPosition">ddmsGmlPosition</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Unlike the ICISM Security Attributes table, where each row is an attribute, the rows in this table represent a complete set of 
			SRS information for a single element. Because the required/optional status of each attribute varies based on the parent, 
			no constraints enforce any rules here. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">srsName</td><td class="relRules">char(2048)</td><td>the URI-based SRS name, optional on Positions, 
			but required on Points and Polygons</td>
	</tr>
	<tr class="relRow">
		<td class="relField">srsDimension</td><td class="relRules">number</td><td>a positive integer dimension</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">axisLabels</td><td class="relRules">char(2048)</td><td>an ordered list of axes labels, as a 
			space-delimited list of NCNames</td>
	</tr>
	<tr class="relRow">
		<td class="relField">uomLabels</td><td class="relRules">char(2048)</td><td>an ordered list of unit-of-measure labels for 
			the axes, as a space-delimited list of NCNames</td>
	</tr>		
</table>

<a name="ddmsSubjectCoverage"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSubjectCoverage</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/SubjectCoverage.html">ddms:subjectCoverage</a>
			element, which is a required top-level component. 
			It may be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> table in DDMS 3.0.<br><br>
			On its own, this table is not very interesting -- it is merely a cross-reference table between a DDMS Resource and any nested
			<a href="#ddmsCategory">categories</a> or <a href="ddmsKeyword">keywords</a>.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
</table>

<a name="ddmsTemporalCoverage"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsTemporalCoverage</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/TemporalCoverage.html">ddms:temporalCoverage</a>
			element, which is an optional top-level component. 
			It may be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> table in DDMS 3.0. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">timePeriodName</td><td class="relRules">char(64)</td><td>the name of the time period</td>
	</tr>
	<tr class="relRow">
		<td class="relField">start</td><td class="relRules">char(64)</td><td>the start date string in a valid XML date format, or one of the strings, "Not Applicable" or 
			"Unknown".</td>
	</tr>
	<tr class="relRow">
		<td class="relField">end</td><td class="relRules">char(64)</td><td>the end date string in a valid XML date format, or one of the strings, "Not Applicable" or 
			"Unknown".</td>
	</tr>	
</table>

<a name="ddmsVerticalExtent"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsVerticalExtent</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/VerticalExtent.html">ddms:verticalExtent</a>
			element, which is nested in a ddms:geospatialCoverage element. As described in the class description, DDMSence requires
			the top-level unitOfMeasure and datum attributes to be consistent on both the parent component and the two extents, so this
			information need only be stored once for a table row.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent geospatialCoverage row of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">unitOfMeasure</td><td class="relRules">char(64), not null</td><td>should be one of Meter, Kilometer, Foot, StatuteMile, NauticalMile, Fathom, Inch</td>
	</tr>
	<tr class="relRow">
		<td class="relField">datum</td><td class="relRules">char(64), not null</td><td>should be one of MSL, AGL, or HAE</td>
	</tr>
	<tr class="relRow">
		<td class="relField">minVerticalExtent</td><td class="relRules">char(64), not null</td><td>a decimal number (modelled as a string) representing the minimum extent</td>
	</tr>
	<tr class="relRow">
		<td class="relField">maxVerticalExtent</td><td class="relRules">char(64), not null</td><td>a decimal number (modelled as a string) representing the maximum extent</td>
	</tr>	
</table>

<a name="ddmsVirtualCoverage"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsVirtualCoverage</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/summary/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/VirtualCoverage.html">ddms:virtualCoverage</a>
			element, which is an optional top-level component. 
			It may be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a> table in DDMS 3.0. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">address</td><td class="relRules">char(2048)</td><td>a network address</td>
	</tr>
	<tr class="relRow">
		<td class="relField">protocol</td><td class="relRules">char(64)</td><td>a network protocol for data transfer</td>
	</tr>
</table>




<a name="tables-security"></a><h4>The Security Layer</h4>

<a name="ddmsSecurity"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSecurity</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/security/.html"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>		
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/security/Security.html">ddms:security</a>
			element, which is a top-level component. It will be associated with rows in the <a href="#ddmsSecurityAttribute">ddmsSecurityAttribute</a>
			table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">excludeFromRollup</td><td class="relRules">boolean</td><td>has a fixed value of "true" in DDMS 3.0. This attribute does not exist in DDMS 2.0, 
			so its value there is irrelevant (a DDMS 2.0 Security object will return a <code>null</code> value for this attribute).</td>
	</tr>
</table>

<a name="ddmsSecurityAttribute"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSecurityAttribute</th>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			ICISM Security Attributes
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/security/SecurityAttributes.html">SecurityAttributes</a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			No other tables.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augments:</td>
		<td class="relInfo" colspan="2">
			<a href="#ddmsDescription">ddmsDescription</a>,
			<a href="#ddmsGeospatialCoverage">ddmsGeospatialCoverage</a> (DDMS 3.0 only),
			<a href="#ddmsProducer">ddmsProducer</a>,
			<a href="#ddmsRelatedResources">ddmsRelatedResources</a>,
			<a href="#ddmsResource">ddmsResource</a>,
			<a href="#ddmsSecurity">ddmsSecurity</a>, 
			<a href="#ddmsSource">ddmsSource</a> (DDMS 3.0 only),
			<a href="#ddmsSubjectCoverage">ddmsSubjectCoverage</a> (DDMS 3.0 only), 
			<a href="#ddmsSubtitle">ddmsSubtitle</a>, 
			<a href="#ddmsTemporalCoverage">ddmsTemporalCoverage</a> (DDMS 3.0 only),
			<a href="#ddmsTitle">ddmsTitle</a>, and
			<a href="#ddmsVirtualCoverage">ddmsVirtualCoverage</a> (DDMS 3.0 only)
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
			Each row in this table represents a single ICISM attribute, because the "complete" set of attributes is subject to change, and most values are optional.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">name</td><td class="relRules">char(64), not null</td><td>the unique attribute name, i.e. "classification" or "SCIcontrols"</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the attribute value as a string</td>
	</tr>
</table>




<a name="tables-extensible"></a><h4>The Extensible Layer</h4>

<a name="ddmsExtensibleAttribute"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsExtensibleAttribute</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table contains any <a href="/docs/buri/ddmsence/ddms/extensible/ExtensibleAttributes.html">custom attributes</a> affixed to a
			DDMS element. Each table row represents one attribute, and an element may link to 0-to-many rows in this table, via the <code>parentId</code> 
			column. In DDMS 3.0, acceptable parents would include
			<a href="#ddmsProducer">Organization</a>, <a href="#ddmsProducer">Person</a>, <a href="#ddmsProducer">Service</a>, <a href="#ddmsProducer">Unknown</a>, 
			<a href="#ddmsCategory">Category</a>, <a href="#ddmsKeyword">Keyword</a>, or the <a href="#ddmsResource">Resource</a> itself.
			
			In DDMS 2.0, extensible attributes can only decorate <a href="#ddmsProducer">Organization</a>, <a href="#ddmsProducer">Person</a>, 
			<a href="#ddmsProducer">Service</a>, or the <a href="#ddmsResource">Resource</a> itself.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qname</td><td class="relRules">char(256), not null</td><td>the qualified name of the attribute, i.e. "opensearch:relevance"</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the attribute value as a string</td>
	</tr>
</table>

<a name="ddmsExtensibleElement"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsExtensibleElement</th>
	</tr>
		<tr class="relRow">
		<td class="relHeader">In DDMS:</td>
		<td class="relName" colspan="2">
			<a href="http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#"><code></code></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">In DDMSence:</td>
		<td class="relName" colspan="2">	
			<a href="/docs/buri/ddmsence/ddms/"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Parent Of:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Augmented By:</td>
		<td class="relInfo" colspan="2">
			<a href="#"></a>
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader">Additional Notes:</td>
		<td class="relInfo" colspan="2">
		</td>
	</tr>
	<tr class="relRow">
		<td class="relHeader" colspan="3">Columns:</td>
	</tr>	
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/extensible/ExtensibleElement.html">custom elements</a> 
			which can appear at the top-level of the DDMS resource. The table merely stores the XML of the element (DDMSence does 
			not dig into these elements either). In DDMS 3.0, 0-to-many of these might appear on a DDMS resource. Each row in this table 
			should map to one top-level extensible element.	Also note that the XML namespaces may have been defined higher up, so the XML 
			fragment may not be correct on its own.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">xml</td><td class="relRules">blob</td><td>the raw XML text of the extensible element</td>
	</tr>
</table>

<p>
	<a href="documentation.jsp#explorations">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>