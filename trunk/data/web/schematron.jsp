<html>
<head>
	<title>DDMSence: Schematron Implementation for DDMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<p align="right"><b>Last Update:</b> 6/4/2011 at 13:02</p>

<a name="top"></a><h1>Schematron Implementation for DDMS</h1>

<p>This document is an attempt to map some of the more complex DDMS conditions to a Schematron file. Where an XML Schema can be used 
to validate correctness and syntax, a Schematron file can more easily address rules related to content and dependencies between 
various elements and attributes. The rules I identify here may duplicate some of the checks that DDMSence already does.</p> 

<p>Schematron rules must be placed in a valid schema template which identifies any XML namespaces that the XML instance might employ.</p>

<pre class="brush: xml">&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;iso:schema   
   xmlns="http://purl.oclc.org/dsdl/schematron"
   xmlns:iso="http://purl.oclc.org/dsdl/schematron"&gt;
   
   &lt;iso:title&gt;Test ISO Schematron File for DDMSence (DDMS 3.0)&lt;/iso:title&gt;
   &lt;iso:ns prefix='ddms' uri='http://metadata.dod.mil/mdr/ns/DDMS/3.0/' /&gt;
   &lt;iso:ns prefix='ICISM' uri='urn:us:gov:ic:ism' /&gt;
   &lt;iso:ns prefix='gml' uri='http://www.opengis.net/gml/3.2' /&gt;
   &lt;iso:ns prefix='xlink' uri='http://www.w3.org/1999/xlink' /&gt;

   &lt;!-- Patterns go here. --&gt;
&lt;/iso:schema&gt;</pre>   

<p>This document is heavily under construction, and I will work on it as I have time. When it is "finalized", I'll post a notification 
on the front page, as well as the discussion area. I am just getting started with XPath, so suggestions on simplifying the syntax
of these rules are always appreciated.</p>

<h4>Bounding Box Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="BoundingBox_Constraints"&gt;
    &lt;iso:rule context="/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:boundingBox"&gt;
       &lt;iso:assert test="ddms:WestBL &amp;gt;= -180 and ddms:WestBL &amp;lt;= 180"&gt;
          WestBL must be between -180 and 180 degrees.
       &lt;/iso:assert&gt;
       &lt;iso:assert test="ddms:EastBL &amp;gt;= -180 and ddms:EastBL &amp;lt;= 180"&gt;
          EastBL must be between -180 and 180 degrees.
       &lt;/iso:assert&gt;
       &lt;iso:assert test="ddms:SouthBL &amp;gt;= -90 and ddms:SouthBL &amp;lt;= 90"&gt;
          SouthBL must be between -90 and 90 degrees.
       &lt;/iso:assert&gt;
       &lt;iso:assert test="ddms:NorthBL &amp;gt;= -90 and ddms:EastBL &amp;lt;= 90"&gt;
          NorthBL must be between -90 and 90 degrees.
       &lt;/iso:assert&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Dates Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Dates_Constraints"&gt;
    &lt;iso:rule context="/ddms:dates"&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          The ddms:dates element does not have any date attributes.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Extent Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Extent_Constraints"&gt;
    &lt;iso:rule context="/ddms:format/ddms:Media/ddms:extent"&gt;
       &lt;iso:assert test="not(@ddms:value) or (@ddms:qualifier and @ddms:value)"&gt;
          If a ddms:extent element has a value, it must also have a qualifier.
       &lt;/iso:assert&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          The ddms:extent element does not have any attributes.
       &lt;/iso:report&gt;
       &lt;iso:report test="not(@ddms:value) and @ddms:qualifier"&gt;
          The ddms:extent element has a qualifier but no value.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>GeographicIdentifier Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="GeographicIdentifier_Constraints"&gt;
    &lt;iso:rule context="//ddms:geographicIdentifier"&gt;
       &lt;iso:assert test="(ddms:facilityIdentifier and count(*) = 1) or not(ddms:facilityIdentifier)"&gt;
          A ddms:facilityIdentifier element cannot be used with any sibling elements.
       &lt;/iso:assert&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>GeospatialCoverage Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="GeospatialCoverage_Constraints"&gt;
    &lt;iso:rule context="/ddms:geospatialCoverage/ddms:GeospatialExtent"&gt;
       &lt;iso:assert test="(ddms:geographicIdentifier/ddms:facilityIdentifier and count(*) = 1) or not(ddms:geographicIdentifier/ddms:facilityIdentifier)"&gt;
          A ddms:geospatialCoverage element which contains a facilityIdentifier-based geographicIdentifier cannot contain any other child elements.
       &lt;/iso:assert&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Language Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Language_Constraints"&gt;
    &lt;iso:rule context="/ddms:language"&gt;
       &lt;iso:assert test="not(@ddms:value) or (@ddms:qualifier and @ddms:value)"&gt;
          If a ddms:language element has a value, it must also have a qualifier.
       &lt;/iso:assert&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          A ddms:language element does not have any attributes.
       &lt;/iso:report&gt;
       &lt;iso:report test="not(@ddms:value) and @ddms:qualifier"&gt;
          A ddms:language element has a qualifier but no value.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Point Constraints</h4>
<pre>Assert: If the nested gml:pos has an srsName, it matches the srsName of this Point.</pre>

<h4>Polygon Constraints</h4>
<pre>Assert: If the nested gml:pos elements have srsNames, they match the srsName of this Polygon.
Assert: The first and last gml:pos coordinates must be identical (a closed polygon).</pre>

<h4>Position Constraints</h4>
<pre>Assert: The first coordinate is a valid latitude.
Assert: The second coordinate is a valid longitude.</pre>

<h4>PostalAddress Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="PostalAddress_Constraints"&gt;
    &lt;iso:rule context="//ddms:postalAddress"&gt;
      &lt;iso:assert test="(ddms:state and not(ddms:province)) or (not(ddms:state) and ddms:province)"&gt;
          A ddms:postalAddress can have either a state or a province, but not both.
       &lt;/iso:assert&gt;   
       &lt;iso:report test="count(*) = 0"&gt;
          A ddms:postalAddress element does not have any child elements.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Source Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Source_Constraints"&gt;
    &lt;iso:rule context="/ddms:source"&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          A ddms:source element does not have any attributes.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>SRSAttributes Constraints</h4>
<pre>Assert: If the srsName is omitted, the axisLabels must be omitted.
Assert: If the axisLabels are omitted, the uomLabels must be omitted.</pre>

<h4>SubjectCoverage Constraints</h4>

<pre>Report: 1 or more keywords have the same value.
Report: 1 or more categories have the same value.</pre>

<h4>Type Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Type_Constraints"&gt;
    &lt;iso:rule context="/ddms:type"&gt;
       &lt;iso:assert test="not(@ddms:value) or (@ddms:qualifier and @ddms:value)"&gt;
          If a ddms:type element has a value, it must also have a qualifier.
       &lt;/iso:assert&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          A ddms:type element does not have any attributes.
       &lt;/iso:report&gt;
       &lt;iso:report test="not(@ddms:value) and @ddms:qualifier"&gt;
          A ddms:type element has a qualifier but no value.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Vertical Extent Constraints</h4>

<pre>Assert: If a MinVerticalExtent has unitOfMeasure or datum set, its values match the parent (verticalExtent) attribute values of the same name.
Assert: If a MaxVerticalExtent has unitOfMeasure or datum set, its values match the parent (verticalExtent) attribute values of the same name.</pre>

<h4>VirtualCoverage Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="VirtualCoverage_Constraints"&gt;
    &lt;iso:rule context="/ddms:virtualCoverage"&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          A ddms:virtualCoverage element does not have any attributes.
       &lt;/iso:report&gt;
       &lt;iso:report test="@ddms:address and not(@ddms:protocol)"&gt;
          A ddms:virtualCoverage element has an address but no protocol.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#explorations">Back to Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>