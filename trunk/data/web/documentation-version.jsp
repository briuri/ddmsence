<html>
<head>
	<title>DDMSence: Power Tip - Working With Different DDMS Versions</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Power Tip: Working with Different DDMS Versions</h1>

<h2>Using Alternate Versions</h2>
 
<p>DDMSence currently supports four versions of DDMS: 2.0, 3.0, 3.1, and 4.0. When building DDMS components from XML files, the 
DDMSReader class can automatically use the correct version of DDMS based on the XML namespace defined in the file.
When building DDMS components from scratch, the <a href="/docs/index.html?buri/ddmsence/util/DDMSVersion.html">DDMSVersion</a>
class controls the version being used. There is an instance of DDMSVersion for each supported version, and this 
instance contains the specific XML namespaces used for DDMS, GML, NTK, and ISM components.</p>

<div class="clear"></div>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
DDMSVersion version = DDMSVersion.getCurrentVersion();
System.out.println("In DDMS " + version.getVersion() + ", the following namespaces are used: ");
System.out.println("ddms: " + version.getNamespace());
System.out.println("gml: " + version.getGmlNamespace());
System.out.println("ISM: " + version.getIsmNamespace());
System.out.println("Are we using DDMS 2.0? " + DDMSVersion.isCurrentVersion("2.0"));
System.out.println("If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, "
   + "I know we are using DDMS "
   + DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion());

Identifier identifier = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForNamespace(identifier.getNamespace()));

DDMSVersion.setCurrentVersion("3.0");
Identifier identifier2 = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForNamespace(identifier.getNamespace()));</pre>
<p class="figure">Figure 1. Using a different version of DDMS</p>

<pre class="brush: xml">In DDMS 2.0, the following namespaces are used: 
ddms: http://metadata.dod.mil/mdr/ns/DDMS/2.0/
gml: http://www.opengis.net/gml
ISM: urn:us:gov:ic:ism:v2
Are we using DDMS 2.0? true
If I see the namespace, http://metadata.dod.mil/mdr/ns/DDMS/3.0/, I know we are using DDMS 3.0
This identifier was created with DDMS 2.0
This identifier was created with DDMS 3.0</pre>
<p class="figure">Figure 2. Output of the code in Figure 1</p>

<p>Calling <code>DDMSVersion.setCurrentVersion("2.0")</code> will make any components you create from that point on obey DDMS 2.0 
validation rules. The default version if you never call this method is "4.0" (but you should always explicitly set the current version yourself,
because this default changes as new versions of DDMS are released). The version is maintained as a static variable, so this 
is not a thread-safe approach, but I believe that the most common use cases will deal with DDMS components of a single version at a time,
and I wanted the versioning mechanism to be as unobtrusive as possible.</p>

<p>DDMS release 3.0.1 was merely a documentation release which clarified some of the supporting documentation on geospatial elements. Because none of the 
schemas or components themselves were updated, 3.0.1 reuses all of the same technical information from 3.0 (ncluding XML namespaces). DDMSence treats 3.0.1 as an alias 
for DDMS 3.0 -- you can set your DDMS version to 3.0.1, but DDMSence will continue to use DDMS 3.0 artifacts.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("3.0.1");
System.out.println(DDMSVersion.getCurrentVersion().getVersion());
</pre>
<p class="figure">Figure 3. This code will print out "3.0".</p>

<h2>Differences Between Versions</h2>

<p>The validation rules between versions of DDMS are very similar, but there are a few major differences. For example, the Unknown
entity for producers was not introduced until DDMS 3.0, so attempts to create one in DDMS 2.0 will fail.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
List&lt;String&gt; names = new ArrayList&lt;String&gt;();
names.add("Unknown Entity");
Unknown unknown = new Unknown("creator", names, null, null, null);</pre>
<p class="figure">Figure 4. This code will throw an InvalidDDMSException</p>

<p>The tables below identify the key differences between supported versions of DDMS components in DDMSence.</p>
<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 2.0 Notes</th><th width="32%">DDMS 3.0 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/2.0/</code></td><td>Changed to <code>http://metadata.dod.mil/mdr/ns/DDMS/3.0/</code></td></tr>
<tr><td><code>ddms:category/@*</code></td><td>No extensible attributes.</td><td>Supports optional extensible attributes.</td></tr>
<tr><td><code>ddms:geospatialCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:keyword/@*</code></td><td>No extensible attributes.</td><td>Supports optional extensible attributes.</td></tr>
<tr><td><code>ddms:Resource/@ISM:classification<br />ddms:Resource/@ISM:createDate<br />ddms:Resource/@ISM:ownerProducer<br />ddms:Resource/@ISM:resourceElement</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Required.</td></tr>
<tr><td><code>ddms:Resource/@ISM:DESVersion</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Required. DESVersion should be 2.</td></tr>
<tr><td><code>ddms:Resource/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:Resource/*</code></td><td>Supports zero or one extensible elements.</td><td>Supports zero to many extensible elements.</td></tr>
<tr><td><code>ddms:security/@ISM:excludeFromRollup</code></td><td>Cannot exist.</td><td>Required. Value must be "true".</td></tr>
<tr><td><code>ddms:source/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:subjectCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:temporalCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:Unknown</code></td><td>Cannot exist.</td><td>New producer entity introduced.</td></tr>
<tr><td><code>ddms:virtualCoverage/@ISM:*</code></td><td>No security attributes.</td><td>Supports optional security attributes.</td></tr>
</table>
<p class="figure">Table 1. Component changes from DDMS 2.0 to DDMS 3.0</p>
<br /><br />

<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.0 Notes</th><th width="32%">DDMS 3.1 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.0/</code></td><td>Changed to <code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td></tr>
<tr><td><code>ddms:dates/@ddms:approvedOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:Resource/@ISM:compliesWith</td><td>Unsupported, but can exist as extensible attributes.</td><td>Optional.</td></tr>
<tr><td><code>ddms:Resource/@ISM:DESVersion</td><td>Required. DESVersion should be 2.</td><td>Required. DESVersion must be 5.</td></tr>
</table>
<p class="figure">Table 2. Component changes from DDMS 3.0 to DDMS 3.1</p>
<br /><br />

<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.1 Notes</th><th width="32%">DDMS 4.0 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td><td>Changed to <code>urn:us:mil:ces:metadata:ddms:4</code></td></tr>
<tr><td><code>ddms:boundingBox/ddms:westBL<br />ddms:boundingBox/ddms:eastBL<br />ddms:boundingBox/ddms:southBL<br />ddms:boundingBox/ddms:northBL</td><td>Previously called "WestBL", "EastBL", "SouthBL", and "NorthBL".</td><td>Renamed.</td></tr>
<tr><td><code>ddms:category/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:creator/@ddms:POCType<br />ddms:contributor/@ddms:POCType<br />ddms:pointOfContact/@ddms:POCType<br />ddms:publisher/@ddms:POCType</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:dates/@ddms:receivedOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:format/ddms:Media</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:geographicIdentifier/ddms:subDivisionCode</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:geospatialCoverage/ddms:GeospatialExtent</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:geospatialCoverage/@ddms:precedence<br />ddms:geospatialCoverage/@ddms:order</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:keyword/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:link/@xlink:role</td><td>Could be empty.</td><td>Cannot be empty.</td></tr>
<tr><td><code>ddms:link/@xlink:label</td><td>Was an <code>xsd:string</code>.</td><td>Must be an <code>xsd:NCName</code>.</td></tr>
<tr><td><code>ddms:link/@ISM:classification<br />ddms:link/@ISM:ownerProducer</td><td>Cannot exist.</td><td>Required when the link is part of a ddms:revisionRecall element.</td></tr>
<tr><td><code>ddms:link/@ISM:*</td><td>Cannot exist.</td><td>Optional when the link is part of a ddms:revisionRecall element.</td></tr>
<tr><td><code>ddms:organization</td><td>Previously called "Organization"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:organization/@ddms:acronym</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:organization/ddms:subOrganization</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:person</td><td>Previously called "Person"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:relatedResources</td><td>Wraps around ddms:relatedResource elements</td><td>Removed.</td></tr>
<tr><td><code>ddms:relatedResource/@ddms:relationship</td><td>Cannot exist.</td><td>Required.</td></tr>
<tr><td><code>ddms:relatedResource/@ddms:direction</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:relatedResource/@ISM:*</td><td>Cannot exist.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:resource</td><td>Previously called "Resource".</td><td>Renamed.</td></tr>
<tr><td><code>ddms:resource/ddms:metacardInfo</td><td>Cannot exist.</td><td>New required component introduced (contains many other new components).</td></tr>
<tr><td><code>ddms:resource/ddms:resourceManagement</td><td>Cannot exist.</td><td>New optional component introduced (contains many other new components).</td></tr>
<tr><td><code>ddms:resource/ddms:subjectCoverage</td><td>Supports exactly 1 element.</td><td>Supports one to many elements.</td></tr>
<tr><td><code>ddms:resource/@ISM:DESVersion</td><td>Required. DESVersion must be 5.</td><td>Required. DESVersion must be 7.</td></tr>
<tr><td><code>ddms:resource/@ISM:noticeDate<br />ddms:resource/@ISM:noticeReason<br />ddms:resource/@ISM:noticeType<br />ddms:resource/@ISM:unregisteredNoticeType</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Optional.</td></tr>
<tr><td><code>ddms:resource/@NTK:DESVersion</td><td>Unsupported, but can exist as extensible attributes.</td><td>Required. DESVersion must be 5.</td></tr>
<tr><td><code>ddms:security/ddms:noticeList<br />ddms:security/NTK:Access</td><td>Cannot exist.</td><td>Optional (contains many other new components).</td></tr>
<tr><td><code>ddms:service</td><td>Previously called "Service"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:subjectCoverage/ddms:nonStateActor<br />ddms:subjectCoverage/ddms:productionMetric</td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:subjectCoverage/ddms:Subject</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:temporalCoverage/ddms:TimePeriod</td><td>Wraps around child components.</td><td>Removed.</td></tr>
<tr><td><code>ddms:type</td><td>Has no child text.</td><td>Optional child text.</td></tr>
<tr><td><code>ddms:type/@ISM:*</td><td>Cannot exist.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:unknown</td><td>Previously called "Unknown"</td><td>Renamed.</td></tr>
<tr><td><code>ddms:verticalExtent/ddms:minVerticalExtent<br />ddms:verticalExtent/ddms:maxVerticalExtent</td><td>Previously called "MinVerticalExtent" and "MaxVerticalExtent".</td><td>Renamed.</td></tr>
</table>
<p class="figure">Table 3. Component changes from DDMS 3.1 to DDMS 4.0</p>
<br /><br />

<p>The table below lists the complete set of ISM security attributes, and shows which attributes can be used with each version of DDMS.</p>

<table>
<tr><th>Attribute</th><th>DDMS 2.0 (ISM V2-PR)</th><th>DDMS 3.0 (ISM V2)</th><th>DDMS 3.1 (ISM V5)</th><th>DDMS 4.0 (ISM V7)</tr>
<tr><td><code>@ISM:atomicEnergyMarkings</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classification</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classificationReason</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:compilationReason</code></td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:dateOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:declassDate</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassEvent</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassException</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassManualReview</code></td><td>Yes</td><td>No</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:derivativelyClassifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:derivedFrom</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:displayOnlyTo</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:disseminationControls</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceOpen</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceProtected</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonICmarkings</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonUSControls</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:noticeType</code></td><td>No</td><td>No</td><td>No</td><td>Yes</td></tr>
<tr><td><code>@ISM:ownerProducer</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:pocType</code></td><td>No</td><td>No</td><td>No</td><td>Yes</td></tr>
<tr><td><code>@ISM:releasableTo</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SARIdentifier</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SCIcontrols</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:typeOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td></tr></table>
<p class="figure">Table 4. Security Attribute changes from DDMS 2.0 to DDMS 4.0</p>


<p>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>