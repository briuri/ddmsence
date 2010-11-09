<html>
<head>
	<title>DDMSence: DDMS Table Model</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>DDMS Table Model</h1>

<div class="toc">
	<b><u>Table of Contents</u></b>
	<li><a href="#tables-notes">General Notes</a></li>
	<li><a href="#tables-primary">Primary and Shared Components</a></li>
	<li><a href="#tables-format">The Format Layer</a></li>
	<li><a href="#tables-resource">The Resource Layer</a></li>
	<li><a href="#tables-security">The Security Layer</a></li>
	<li><a href="#tables-summary">The Summary Layer</a></li>
	<li><a href="#tables-extensible">The Extensible Layer</a></li>
</div>

<p>This document is an attempt to map the DDMS specification to a relational database model. At the moment, it is very incomplete and I am working on it as time permits. 
The intent of this mapping is to be comprehensive first and pragmatic second -- the full scope of DDMS will be modeled, but some design decisions may be 
made for simplicity, such as modeling lists of values as a delimited string value.</p>

<p>Although direct-to-table persistence mapping will probably not be a feature in any version of DDMSence, this table model may be useful when integrating DDMSence
with an existing persistence framework like Hibernate or the Oracle XML SQL Utility (XSU).</p> 

<p>The last time I worked on this document (and remembered to update the date) was on <b>11/09/2010 at 18:51</b>.</p>
<div class="clear"></div>
<pre>
TODO:
   Primary and Shared Components:
      Resource
   Summary Layer:
      GeospatialCoverage
         GeographicIdentifier
            CountryCode
            FacilityIdentifier
         BoundingBox
         BoundingGeometry
            GmlPoint
               GmlPosition
            GmlPolygon
               GmlPosition
         PostalAddress
            CountryCode
         VerticalExtent
      RelatedResources
         RelatedResource
            Link
      SubjectCoverage
         Category
         Keyword
      TemporalCoverage
      VirtualCoverage
</pre>

<a name="tables-notes"></a><h4>General Notes</h4> 
<ul>
<li>Child elements and attributes will have links back to their parents, but not in the reverse direction. This key is allowed to have an initial <code>&lt;NULL&gt;</code> value, to support a bottom-up approach to building DDMS resources from scratch.</li>
<li>If a table column is a character string and a value is not provided, an empty string should be favored instead of <code>&lt;NULL&gt;</code>.</li>
<li>The intent of the tables is to model the resource data, not schema data. XML namespaces and other schema constructs are not necessarily modeled.</li>
<li>Reference tables (i.e. the four types of producers, or the valid names of ICISM security attributes) are not included here. Columns which have string values for these constants could just as easily have foreign keys to a reference table.</li>
<li>Most validation constraints are omitted from this model, since it is assumed that a validating library like DDMSence would be placed in front of the tables.</li>
<li>Character string lengths are fairly arbitrary, although the numbers I chose are relatively reasonable for the types of data the fields contain. URI fields are set at 2048 characters to match Internet Explorer URL restrictions.</li>
<li>The pipe character, <b>|</b>, is suggested as a delimiter for columns containing lists in string form.</li> 
</ul>

<a name="tables-primary"></a><h4>Primary and Shared Components</h4>

<a name="ddmsSecurityAttributes"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSecurityAttributes</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table contains any <a href="/docs/buri/ddmsence/ddms/security/SecurityAttributes.html">ICISM security attributes</a> affixed to a
			DDMS element. Each table row represents one attribute, and an element may link to 0-to-many rows in this table, via the <code>parentId</code> 
			column. Because only certain elements require a <code>classification</code> and <code>ownerProducers</code>, no constraints enforce this 
			condition here. In DDMS 3.0, acceptable parents	would include
			<a href="#ddmsDescription">Description</a>,	<a href="#ddms">GeospatialCoverage</a>, <a href="#ddmsProducer">Organization</a>,
			<a href="#ddmsProducer">Person</a>, <a href="#ddmsRelatedResources">RelatedResources</a>, <a href="#ddmsResource">Resource</a>,
			<a href="#ddmsSecurity">Security</a>, <a href="#ddmsProducer">Service</a>, <a href="#ddmsSource">Source</a>,
			<a href="#ddmsSubjectCoverage">SubjectCoverage</a>, <a href="#ddmsSubtitle">Subtitle</a>, <a href="#ddmsTemporalCoverage">TemporalCoverage</a>,
			<a href="#ddmsTitle">Title</a>, <a href="#ddmsProducer">Unknown</a>, or <a href="#ddmsVirtualCoverage">VirtualCoverage</a>. In DDMS 2.0,
			acceptable parents would include
			<a href="#ddmsDescription">Description</a>,	<a href="#ddmsOrganization">Organization</a>, <a href="#ddmsPerson">Person</a>, 
			<a href="#ddmsRelatedResources">RelatedResources</a>, <a href="#ddmsResource">Resource</a>, <a href="#ddmsSecurity">Security</a>, 
			<a href="#ddmsService">Service</a>, <a href="#ddmsSubtitle">Subtitle</a>, <a href="#ddmsTitle">Title</a>, or <a href="#ddmsUnknown">Unknown</a>.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">name</td><td class="relRules">char(256), not null</td><td>the unique attribute name, i.e. "classification" or "SCIcontrols"</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the attribute value as a string</td>
	</tr>
</table>

<a name="ddmsSrsAttributes"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSrsAttributes</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table contains the <a href="/docs/buri/ddmsence/ddms/summary/SRSAttributes.html">SRS attributes</a> affixed to GML elements, including
			<a href="#ddmsGmlPoint">Point</a>, <a href="#ddmsGmlPolygon">Polygon</a>, or <a href="#ddmsGmlPosition">Position</a>. Unlike the ICISM Security
			Attributes table, where each row is an attribute, the rows in this table represent a complete set of SRS information for a single element. 
			An element may link to 0-or-1 rows in this table, via the <code>parentId</code> column. Because the required/optional status of each attribute
			varies based on the parent, no constraints enforce any rules here. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">parentId</td><td class="relRules">number</td><td>foreign key to the parent component of this attribute</td>
	</tr>
	<tr class="relRow">
		<td class="relField">srsName</td><td class="relRules">char(2048)</td><td>the URI-based SRS name, optional on Positions, but required on Points and Polygons</td>
	</tr>
	<tr class="relRow">
		<td class="relField">srsDimension</td><td class="relRules">number</td><td>a positive integer dimension</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">axisLabels</td><td class="relRules">char(2048)</td><td>an ordered list of axes labels, as a space-delimited list of NCNames</td>
	</tr>
	<tr class="relRow">
		<td class="relField">uomLabels</td><td class="relRules">char(2048)</td><td>an ordered list of unit-of-measure labels for the axes, as a space-delimited list of NCNames</td>
	</tr>		
</table>

<a name="tables-format"></a><h4>The Format Layer</h4>

<a name="ddmsFormat"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsFormat</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/format/Format.html">ddms:format</a>
			element, which is a top-level component. This element contains 0-1 references to the <a href="#ddmsMediaExtent">ddmsMediaExtent</a> table. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">mimeType</td><td class="relRules">char(256), not null</td><td>the mime type, exactly 1 required</td>
	</tr>
	<tr class="relRow">
		<td class="relField">medium</td><td class="relRules">char(256)</td><td>the medium, 0-1 optional</td>
	</tr>
</table>

<ul><a name="ddmsMediaExtent"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsMediaExtent</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/format/MediaExtent.html">ddms:extent</a>
			element nested in ddms:format. A qualifier is required when a value is present, but this constraint
			is not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">formatId</td><td class="relRules">number</td><td>foreign key to the parent ddms:format element</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table></ul>

<a name="tables-resource"></a><h4>The Resource Layer</h4>

<a name="ddmsDates"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsDates</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Dates.html">ddms:dates</a>
			element, which is a top-level component. Dates are stored in string format, to ensure that a date value is
			retrieved in the same XML date format that it was entered in.			
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">created</td><td class="relRules">char(64)</td><td>the creation date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">posted</td><td class="relRules">char(64)</td><td>the posting date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">validTil</td><td class="relRules">char(64)</td><td>the expiration date</td>
	</tr>
	<tr class="relRow">
		<td class="relField">infoCutOff</td><td class="relRules">char(64)</td><td>the info cutoff date</td>
	</tr>
</table>

<a name="ddmsIdentifier"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsIdentifier</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Identifier.html">ddms:identifier</a>
			element, which is a top-level component. Every DDMS resource will have at least 1 row in this table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
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
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Language.html">ddms:language</a>
			element, which is a top-level component. A qualifier is required when a value is present, but this constraint is not enforced here. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
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
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Organization.html">ddms:Organization</a>,
			<a href="/docs/buri/ddmsence/ddms/resource/Person.html">ddms:Person</a>,
			<a href="/docs/buri/ddmsence/ddms/resource/Service.html">ddms:Service</a>, and
			<a href="/docs/buri/ddmsence/ddms/resource/Unknown.html">ddms:Unknown</a> elements. It is consistent with the DDMSence
			approach of <a href="documentation.jsp#design">flattening the producer hierarchy</a>, and each row in this table is a "producer entity that fulfills
			some role". In the DDMS schema, the hierarchy is modelled as "a producer role that is filled by some entity". Rows in this table may be associated with
			rows in the <a href="#ddmsSecurityAttributes">ddmsSecurityAttributes</a> table.<br /><br />
			All four producer entities share similar characteristics (at least one name, and optional phone numbers and email addresses), so I have modelled them
			in a single producer table, rather than a separate table for each producer entity type. The latter approach is equally viable.<br /><br />
			This modeling also assumes that there is no reuse of producers between various roles. So, while the person named "Brian Uri" might have a creator role and a contributor role,
			and while Brian's details might be identical in each XML element, each set of details would have a separate row in this table.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">producerType</td><td class="relRules">char(24)</td><td>the role being filled by this producer, i.e. "creator", "contributor", "pointOfContact", or "publisher"</td>
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
		<td class="relField">surname</td><td class="relRules">char(256)</td><td>This is a Person-specific column, containing a surname. Exactly one surname is required for Person records.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">userId</td><td class="relRules">char(64)</td><td>This is a Person-specific column, containing an ID for a user.</td>
	</tr>	
	<tr class="relRow">
		<td class="relField">affiliation</td><td class="relRules">char(256)</td><td>This is a Person-specific column, containing an organizational affiliation for a user. Optional.</td>
	</tr>	
</table>

<a name="ddmsRights"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsRights</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Rights.html">ddms:rights</a>
			element, which is a top-level component. All attributes have a default value of "false". 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
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
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Source.html">ddms:source</a>
			element, which is a top-level component.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
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
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Subtitle.html">ddms:subtitle</a>
			element, which is an optional top-level component. It will be associated with rows in the <a href="#ddmsSecurityAttributes">ddmsSecurityAttributes</a>
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
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the subtitle of the resource</td>
	</tr>
</table>

<a name="ddmsTitle"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsTitle</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Title.html">ddms:title</a>
			element, which is a top-level component. Every DDMS resource will have at least 1 row in this table.
			It will be associated with rows in the <a href="#ddmsSecurityAttributes">ddmsSecurityAttributes</a>
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
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the title of the resource</td>
	</tr>
</table>

<a name="ddmsType"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsType</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/resource/Type.html">ddms:type</a>
			element, which is a top-level component. A qualifier is required when a value is present, but this constraint is not enforced here. 
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">resourceId</td><td class="relRules">number</td><td>foreign key to the parent DDMS resource</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table>

<a name="tables-security"></a><h4>The Security Layer</h4>

<a name="ddmsSecurity"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsSecurity</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/security/Security.html">ddms:security</a>
			element, which is a top-level component. It will be associated with rows in the <a href="#ddmsSecurityAttributes">ddmsSecurityAttributes</a>
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
		<td class="relField">excludeFromRollup</td><td class="relRules">boolean</td><td>has a fixed value of "true" in DDMS 3.0. This attribute does not exist in DDMS 2.0, so its value there is irrelevant (a DDMS 2.0 Security object will return a <code>null</code> value for this attribute).</td>
	</tr>
</table>

<a name="tables-summary"></a><h4>The Summary Layer</h4>

<a name="ddmsDescription"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsDescription</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the <a href="/docs/buri/ddmsence/ddms/summary/Description.html">ddms:description</a>
			element, which is a top-level component. Every DDMS resource will have at least 1 row in this table.
			It will be associated with rows in the <a href="#ddmsSecurityAttributes">ddmsSecurityAttributes</a>
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
		<td class="relField">value</td><td class="relRules">char(2048)</td><td>the description of the resource</td>
	</tr>
</table>

<a name="tables-extensible"></a><h4>The Extensible Layer</h4>

<a name="ddmsExtensible"></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsExtensible</th>
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

<!--
<a name=""></a><table class="rel">
	<tr>
		<th class="relName" colspan="3">TABLENAME</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			DETAILS
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField"></td><td class="relRules"></td><td></td>
	</tr>
</table>
-->

<p>
	<a href="documentation.jsp#samples">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>