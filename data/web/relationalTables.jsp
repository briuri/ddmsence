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

<p>This page is an attempt to map the DDMS specification to a relational database model. It is very incomplete, and I will work on it when I have time. The intent of this
mapping is to be comprehensive first and pragmatic second -- the full scope of DDMS will be modelled, but some design decisions may be made for simplicity, such as modelling
lists of values as a delimited string value.</p>

<a name="tables-notes"></a><h4>General Notes</h4> 
<ul>
<li>Each top-level component has a foreign key reference back to the parent DDMS resource. This key may be null, in case components are being generated from scratch and the parent resource is not created until the end.</li>
<li>In general, nested child elements do not have links back to their parents. It is assumed that queries will never need to retrieve ancestors.</li>
<li>If a table column is a character string and a value is not provided, an empty string should be favoured instead of <code>&lt;NULL&gt;</code>.</li>
<li>The intent of the tables is to model the resource data, not schema data. XML namespaces and other schema constructs are not necessarily modelled.</li>
</ul>

<a name="tables-primary"></a><h4>Primary and Shared Components</h4>

<p>Not completed yet.</p>

<a name="tables-format"></a><h4>The Format Layer</h4>

<table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsFormat</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the ddms:format element, which is a top-level component.
			It has an optional reference to the ddmsMediaExtent table.
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
		<td class="relField">extentId</td><td class="relRules">number</td><td>foreign key to the ddmsMediaExtent table, 0-1 optional</td>
	</tr>
	<tr class="relRow">
		<td class="relField">medium</td><td class="relRules">char(256)</td><td>the medium, 0-1 optional</td>
	</tr>
</table>

<table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsMediaExtent</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the ddms:extent element nested in ddms:format. A qualifier is required when a value is present, but this constraint
			is not enforced here.
		</td>
	</tr>
	<tr class="relRow">
		<td class="relField">id</td><td class="relRules">number, not null, sequenced</td><td>primary key of this row</td>
	</tr>
	<tr class="relRow">
		<td class="relField">qualifier</td><td class="relRules">char(2048)</td><td>the qualifier URI</td>
	</tr>
	<tr class="relRow">
		<td class="relField">value</td><td class="relRules">char(256)</td><td>the value</td>
	</tr>
</table>

<a name="tables-resource"></a><h4>The Resource Layer</h4>

<p>Not completed yet.</p>

<a name="tables-security"></a><h4>The Security Layer</h4>

<p>Not completed yet.</p>

<a name="tables-summary"></a><h4>The Summary Layer</h4>

<p>Not completed yet.</p>

<a name="tables-extensible"></a><h4>The Extensible Layer</h4>

<table class="rel">
	<tr>
		<th class="relName" colspan="3">ddmsExtensible</th>
	</tr>
	<tr>
		<td class="relInfo" colspan="3">
			This table maps to the custom elements which can appear at the top-level of the DDMS resource. The table merely stores the XML of the element (DDMSence does not dig into these elements either).
			In DDMS 3.0, 0 to many of these might appear on a DDMS resource. Each row in this table should map to 1 top-level extensible element.
			Also note that the XML namespaces may have been defined higher up, so the XML fragment may not be correct on its own.
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
<table class="rel">
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