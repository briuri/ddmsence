<html>
<head>
	<title>DDMSence: Schematron Implementation for DDMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<p align="right"><b>Last Update:</b> 2/2/2011 at 11:26</p>

<a name="top"></a><h1>Schematron Implementation for DDMS</h1>

<p>This document is an attempt to map some of the more complex DDMS conditions to a Schematron file. Where an XML Schema can be used to validate correctness and syntax,
a Schematron file can more easily address rules related to content and dependencies between various elements and attributes. The rules I identify here
may duplicate some of the checks that DDMSence already does.</p> 

<p>This document is heavily under construction, and I will work on it as I have time. When it is "finalized", I'll post a notification on the front page,
as well as the discussion area.</p>

<pre>
<b>Cases to consider:</b>
ddms:boundingBox
Assert: westBL and eastBL must be between -180 and 180 degrees.
Assert: southBL and northBL must be between -90 and 90 degrees.

ddms:dates
Report: A completely empty ddms:dates element was found.

ddms:extent
Report: A qualifier has been set without an accompanying value attribute.
Report: A completely empty ddms:extent element was found.

ddms:geographicIdentifier
Assert: If facilityIdentifier is used, no other components can exist.

ddms:geospatialCoverage
Assert: If a geographicIdentifer is used and contains a facilityIdentifier, no other subcomponents can be used.

ddms:language
Report: A qualifier has been set without an accompanying value attribute.
Report: Neither a qualifier nor a value was set on this language.

ddms:postalAddress
Assert: Either a state or a province can exist, but not both.
Report: A completely empty postalAddress element was used.

ddms:source
Report: A completely empty ddms:source element was found.

ddms:subjectCoverage
Report: 1 or more keywords have the same value.
Report: 1 or more categories have the same value.

ddms:type
Report: A qualifier has been set without an accompanying value attribute.
Report: Neither a qualifier nor a value was set on this type.

ddms:verticalExtent
Assert: If a MinVerticalExtent has unitOfMeasure or datum set, its values match the parent (verticalExtent) attribute values of the same name.
Assert: If a MaxVerticalExtent has unitOfMeasure or datum set, its values match the parent (verticalExtent) attribute values of the same name.

ddms:virtualCoverage?
Assert: If an address is provided, the protocol is required.
Report: A completely empty ddms:virtualCoverage element was found.

gml:Point
Assert: If the nested gml:pos has an srsName, it matches the srsName of this Point.

gml:Polygon
Assert: If the nested gml:pos elements have srsNames, they match the srsName of this Polygon.
Assert: The first and last gml:pos coordinates must be identical (a closed polygon).

gml:pos
Assert: The first coordinate is a valid latitude.
Assert: The second coordinate is a valid longitude.

SRS attributes on gml elements:
Assert: If the srsName is omitted, the axisLabels must be omitted.
Assert: If the axisLabels are omitted, the uomLabels must be omitted.
</pre>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#explorations">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>