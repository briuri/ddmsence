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

<a name="tables-notes"></a><h4>General Notes</h4> 
<ul>
<li>Child elements and attributes will have links back to their parents, but not in the reverse direction. This key is allowed to have an initial <code>&lt;NULL&gt;</code> value, to support a bottom-up approach to building DDMS resources from scratch.</li>
<li>If a table column is a character string and a value is not provided, an empty string should be favored instead of <code>&lt;NULL&gt;</code>.</li>
<li>The intent of the tables is to model the resource data, not schema data. XML namespaces and other schema constructs are not necessarily modeled.</li>
<li>Reference tables (i.e. the four types of producers, or the valid names of ICISM security attributes) are not included here. Columns which have string values for these constants could just as easily have foreign keys to a reference table.</li>
</ul>

<a name="tables-primary"></a><h4>Primary and Shared Components</h4>

<pre>
TODO:
	Resource
</pre>

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
			<a href="#ddmsDescription">Description</a>,	<a href="#ddms">GeospatialCoverage</a>, <a href="#ddmsOrganization">Organization</a>,
			<a href="#ddmsPerson">Person</a>, <a href="#ddmsRelatedResources">RelatedResources</a>, <a href="#ddmsResource">Resource</a>,
			<a href="#ddmsSecurity">Security</a>, <a href="#ddmsService">Service</a>, <a href="#ddmsSource">Source</a>,
			<a href="#ddmsSubjectCoverage">SubjectCoverage</a>, <a href="#ddmsSubtitle">Subtitle</a>, <a href="#ddmsTemporalCoverage">TemporalCoverage</a>,
			<a href="#ddmsTitle">Title</a>, <a href="#ddmsUnknown">Unknown</a>, or <a href="#ddmsVirtualCoverage">VirtualCoverage</a>. In DDMS 2.0,
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

<pre>
TODO:
	Dates
	Identifier
	Language
	Organization
	Person
	Rights
	Service
	Source
	Subtitle
	Title
	Type
	Unknown
</pre>

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
		<td class="relField">excludeFromRollup</td><td class="relRules">boolean</td><td>has a fixed value of "" in DDMS 2.0, and "true" in DDMS 3.0.</td>
	</tr>
</table>

<a name="tables-summary"></a><h4>The Summary Layer</h4>

<pre>
TODO:
	Description
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