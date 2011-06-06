<html>
<head>
	<title>DDMSence: Schematron Implementation for DDMS</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<p align="right"><b>Last Update:</b> 6/6/2011 at 17:30</p>

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

<p><a href="documentation.jsp#feedback">Feedback</a> on this document is appreciated!</p>

<h4>Bounding Box Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="BoundingBox_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:boundingBox"&gt;
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
    &lt;iso:rule context="//ddms:Resource/ddms:dates"&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          The ddms:dates element does not have any date attributes.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Extent Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Extent_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:format/ddms:Media/ddms:extent"&gt;
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
    &lt;iso:rule context="//ddms:Resource//ddms:geographicIdentifier"&gt;
       &lt;iso:assert test="(ddms:facilityIdentifier and count(*) = 1) or not(ddms:facilityIdentifier)"&gt;
          A ddms:facilityIdentifier element cannot be used with any sibling elements.
       &lt;/iso:assert&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>GeospatialCoverage Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="GeospatialCoverage_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:geospatialCoverage/ddms:GeospatialExtent"&gt;
       &lt;iso:assert test="(ddms:geographicIdentifier/ddms:facilityIdentifier and count(*) = 1) or not(ddms:geographicIdentifier/ddms:facilityIdentifier)"&gt;
          A ddms:geospatialCoverage element which contains a facilityIdentifier-based geographicIdentifier cannot contain any other child elements.
       &lt;/iso:assert&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Language Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Language_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:language"&gt;
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

<pre class="brush: xml">&lt;iso:pattern id="Point_Constraints"&gt;
   &lt;iso:rule context="//gml:Point"&gt;
      &lt;iso:assert test="not(gml:pos/@srsName) or (@srsName = gml:pos/@srsName)"&gt;
         If a srsName attribute appears on a gml:pos element, it should have the same value as the srsName attribute on the enclosing gml:Point element.
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@srsName or not(@axisLabels)"&gt;
         If the srsName attribute on a gml:Point element is omitted, the axisLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@axisLabels or not(@uomLabels)"&gt;
         If the axisLabels attribute on a gml:Point element is omitted, the uomLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Polygon Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Polygon_Constraints"&gt;
   &lt;iso:rule context="//gml:Polygon/gml:exterior/gml:LinearRing"&gt;
      &lt;iso:assert test="not(gml:pos/@srsName) or (gml:pos/@srsName = ../../@srsName)"&gt;
         If a srsName attribute appears on a gml:pos element, it should have the same value as the srsName attribute on the enclosing gml:Polygon element.
      &lt;/iso:assert&gt;
      &lt;iso:assert test="gml:pos[1] = gml:pos[last()]"&gt;
         The first and last gml:pos elements in a gml:Polygon must be identical, to outline an enclosed shape.
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@srsName or not(@axisLabels)"&gt;
         If the srsName attribute on a gml:Point element is omitted, the axisLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@axisLabels or not(@uomLabels)"&gt;
         If the axisLabels attribute on a gml:Point element is omitted, the uomLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Position Constraints</h4>

<pre class="brush: xml">&lt;!-- This rule employs the XPath 2.0 function, tokenize(). DDMSence currently does Schematron validation with XSLT 1.0 --&gt;
&lt;iso:pattern id="Position_Constraints"&gt;
   &lt;iso:rule context="//gml:pos"&gt;
      &lt;iso:let name="firstCoord" value="number(tokenize(text(), ' ')[1])"/&gt;
      &lt;iso:let name="secondCoord" value="number(tokenize(text(), ' ')[2])"/&gt;
      &lt;iso:assert test="$firstCoord &amp;gt;= -90 and $firstCoord &amp;lt;= 90"&gt;
         The first coordinate in a gml:pos element must be between -90 and 90 degrees.
      &lt;/iso:assert&gt;
      &lt;iso:assert test="$secondCoord &amp;gt;= -180 and $secondCoord &amp;lt;= 180"&gt;
         The second coordinate in a gml:pos element must be between -180 and 180 degrees.
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@srsName or not(@axisLabels)"&gt;
         If the srsName attribute on a gml:Point element is omitted, the axisLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
      &lt;iso:assert test="@axisLabels or not(@uomLabels)"&gt;
         If the axisLabels attribute on a gml:Point element is omitted, the uomLabels attribute must be omitted as well. 
      &lt;/iso:assert&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>PostalAddress Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="PostalAddress_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource//ddms:postalAddress"&gt;
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
    &lt;iso:rule context="//ddms:Resource/ddms:source"&gt;
       &lt;iso:report test="count(@*) = 0"&gt;
          A ddms:source element does not have any attributes.
       &lt;/iso:report&gt;
    &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>SubjectCoverage Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="SubjectCoverage_Constraints"&gt;
   &lt;iso:rule context="//ddms:Resource/ddms:subjectCoverage/ddms:Subject"&gt;
      &lt;iso:report test="ddms:keyword[./@ddms:value = preceding-sibling::ddms:keyword/@ddms:value]"&gt;
         The ddms:subjectCoverage element contains duplicate keywords.
      &lt;/iso:report&gt;
      &lt;iso:report   test="ddms:category[./@ddms:qualifier = preceding-sibling::ddms:category/@ddms:qualifier and ./@ddms:code = preceding-sibling::ddms:category/@ddms:code]"&gt;
         The ddms:subjectCoverage element contains duplicate categories.
      &lt;/iso:report&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>Type Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="Type_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:type"&gt;
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

<pre class="brush: xml">&lt;iso:pattern id="VerticalExtent_Constraints"&gt;
   &lt;iso:rule context="//ddms:Resource/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:verticalExtent"&gt;
      &lt;iso:let name="parentUOM" value="@ddms:unitOfMeasure"/&gt;
      &lt;iso:let name="minUOM" value="ddms:MinVerticalExtent/@ddms:unitOfMeasure"/&gt;
      &lt;iso:let name="maxUOM" value="ddms:MaxVerticalExtent/@ddms:unitOfMeasure"/&gt;
      &lt;iso:let name="parentDatum" value="@ddms:datum"/&gt;
      &lt;iso:let name="minDatum" value="ddms:MinVerticalExtent/@ddms:datum"/&gt;
      &lt;iso:let name="maxDatum" value="ddms:MaxVerticalExtent/@ddms:datum"/&gt;         
      &lt;iso:assert test="not($parentUOM) or not($minUOM) or ($parentUOM = $minUOM)"&gt;
         If a ddms:unitOfMeasure attribute appears on both the ddms:verticalExtent and ddms:MinVerticalExtent elements, it should have the same value in both locations. 
      &lt;/iso:assert&gt;
      &lt;iso:assert test="not($parentUOM) or not($maxUOM) or ($parentUOM = $maxUOM)"&gt;
         If a ddms:unitOfMeasure attribute appears on both the ddms:verticalExtent and ddms:MaxVerticalExtent elements, it should have the same value in both locations. 
      &lt;/iso:assert&gt;         
      &lt;iso:assert test="not($parentDatum) or not($minDatum) or ($parentDatum = $minDatum)"&gt;
         If a ddms:datum attribute appears on both the ddms:verticalExtent and ddms:MinVerticalExtent elements, it should have the same value in both locations. 
      &lt;/iso:assert&gt;
      &lt;iso:assert test="not($parentDatum) or not($maxDatum) or ($parentDatum = $maxDatum)"&gt;
         If a ddms:datum attribute appears on both the ddms:verticalExtent and ddms:MaxVerticalExtent elements, it should have the same value in both locations. 
      &lt;/iso:assert&gt;
   &lt;/iso:rule&gt;
&lt;/iso:pattern&gt;</pre>

<h4>VirtualCoverage Constraints</h4>

<pre class="brush: xml">&lt;iso:pattern id="VirtualCoverage_Constraints"&gt;
    &lt;iso:rule context="//ddms:Resource/ddms:virtualCoverage"&gt;
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