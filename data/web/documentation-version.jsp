<html>
<head>
	<title>DDMSence: Power Tip - Working With Different DDMS Versions</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<a name="top"></a><h1>Power Tip: Working with Different DDMS Versions</h1>

<h2>Using Alternate Versions</h2>

<p>DDMSence currently supports five versions of DDMS: 2.0, 3.0, 3.1, 4.0.1, and 4.1. When loading DDMS components from XML files, the 
DDMSReader class can automatically use the correct version of DDMS based on the XML namespace defined in the file -- you do not need any extra code.</p>

<pre class="brush: java">Resource resource = getReader().getDDMSResource(my41resourceFile);
System.out.println("This metacard was created with DDMS "
   + DDMSVersion.getVersionForNamespace(resource.getNamespace()));</pre>
<p class="figure">Figure 1. Loading resources from XML files</p>

<pre class="brush: xml">This metacard was created with DDMS 4.1</pre>
<p class="figure">Figure 2. Output of the code in Figure 1</p>

<p>When building DDMS components from scratch, the <a href="/docs/index.html?buri/ddmsence/util/DDMSVersion.html">DDMSVersion</a>
class controls the version being used.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
System.out.println("The current version is " + DDMSVersion.getCurrentVersion());
Identifier identifier = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForNamespace(identifier.getNamespace()));
DDMSVersion.setCurrentVersion("3.0");
System.out.println("The current version is " + DDMSVersion.getCurrentVersion());
Identifier identifier2 = new Identifier("http://metadata.dod.mil/mdr/ns/MDR/0.1/MDR.owl#URI",
   "http://www.whitehouse.gov/news/releases/2005/06/20050621.html");
System.out.println("This identifier was created with DDMS "
   + DDMSVersion.getVersionForNamespace(identifier.getNamespace()));</pre>
<p class="figure">Figure 3. Creating Identifiers using different DDMS versions</p>
   
<pre class="brush: xml">The current version is 2.0
This identifier was created with DDMS 2.0
The current version is 3.0
This identifier was created with DDMS 3.0</pre>
<p class="figure">Figure 4. Output of the code in Figure 3</p>
  
<p>There is an instance of DDMSVersion for each supported version, and this instance contains the specific XML namespaces used for DDMS, GML, 
NTK, and ISM components. The NTK namespace is new in DDMS 4.0.1, and will be blank in earlier DDMS versions.</p>

<pre class="brush: java">DDMSVersion version = DDMSVersion.setCurrentVersion("4.1");
System.out.println("In DDMS " + version.getVersion() + ", the following namespaces are used: ");
System.out.println("ddms: " + version.getNamespace());
System.out.println("gml: " + version.getGmlNamespace());
System.out.println("ISM: " + version.getIsmNamespace());
System.out.println("ntk: " + version.getNtkNamespace());
System.out.println("xlink: " + version.getXlinkNamespace());
System.out.println("Are we using DDMS 4.1? " + DDMSVersion.isCurrentVersion("4.1"));
System.out.println("Can we use components that were introduced in DDMS 3.1? " + version.isAtLeast("3.1"));</pre>
<p class="figure">Figure 5. Learning details of the current DDMSVersion</p>

<pre class="brush: xml">In DDMS 4.1, the following namespaces are used: 
ddms: urn:us:mil:ces:metadata:ddms:4
gml: http://www.opengis.net/gml/3.2
ISM: urn:us:gov:ic:ism
ntk: urn:us:gov:ic:ntk
xlink: http://www.w3.org/1999/xlink
Are we using DDMS 4.1? true
Can we use components that were introduced in DDMS 3.1? true</pre>
<p class="figure">Figure 6. Output of the code in Figure 5</p>

<p>Calling <code>DDMSVersion.setCurrentVersion("2.0")</code> will make any components you create from that point on obey DDMS 2.0 
validation rules. The default version if you never call this method is "4.1" (but you should always explicitly set the current version yourself,
because this default changes as new versions of DDMS are released). The version is maintained as a static variable, so this 
is not a thread-safe approach, but I believe that the most common use cases will deal with DDMS components of a single version at a time,
and I wanted the versioning mechanism to be as unobtrusive as possible.</p>

<h2>Differences Between Versions</h2>

<p>The validation rules between versions of DDMS are very similar, but there are a few major differences. For example, the Unknown
entity for producers was not introduced until DDMS 3.0, so attempts to create one in DDMS 2.0 will fail.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("2.0");
List&lt;String&gt; names = Util.getXsListAsList("UnknownEntity"); 
Unknown unknown = new Unknown(names, null, null);</pre>
<p class="figure">Figure 7. This code will throw an InvalidDDMSException</p>

<p>If you have a set of DDMS metacards from an older version of DDMS and wish to transform them to a newer version, you can do so with the <a href="documentation-builders.jsp">Component
Builder</a> framework. Builders allow you to load the old metacard, add any new fields that are required, and save it in the new version.</p>


<h2>DDMS 3.0.1</h2>

<p>DDMS release 3.0.1 was merely a documentation release which clarified some of the supporting documentation on geospatial elements. Because none of the 
schemas or components themselves were updated, 3.0.1 reuses all of the same technical information from 3.0 (including XML namespaces). DDMSence treats 3.0.1 as an alias 
for DDMS 3.0 -- you can set your DDMS version to 3.0.1, but DDMSence will continue to use DDMS 3.0 artifacts.</p>

<pre class="brush: java">DDMSVersion.setCurrentVersion("3.0.1");
System.out.println(DDMSVersion.getCurrentVersion().getVersion());
</pre>
<p class="figure">Figure 8. This code will print out "3.0".</p>

<h2>DDMS 4.0</h2>

<p>DDMS 4.0 was released in September 2011 with an oversight on the technical implementation of the <code>pocType</code> attribute on producer roles. DDMS 4.0
contained a <code>ddms:POCType</code> attribute for this, but it was soon determined by the IC that this would break IRM instances. DDMS 4.0.1 was quickly released a month
later and employs <code>ISM:pocType</code> instead.</p>

<p>Although this change (removing the old attribute and adding a new one) breaks backwards compatibility, the decision was made to reuse the DDMS 4.0
XML namespace, given that the adoption of DDMS 4.0 was assumed to be relatively low. Because DDMS 4.0 is considered to be "broken", I have elected not to
support it in DDMSence.</p>
 
<h2>DDMS 4.0.1</h2>

<p>DDMS 4.0.1 was released in November 2011. It shares the same XML namespace as DDMS 4.1, and unfortunately DDMS offers no mechanism to tell the difference
between 4.0.1 instances and 4.1 instances. Because of this, all instances with the shared XML namespace will be validated against 4.1 schemas. In cases where
new 4.1 elements or attributes are found in the XML instance, DDMSence will provide warnings that the instance might not be parseable by a DDMS 4.0 system.</p>
 
<h2>Tables of Differences</h2>

<p>The tables below identify the key differences between supported versions of DDMS components in DDMSence.</p>

<ul>
	<li><a href="#20to30">DDMS 2.0 to 3.0</a></li>
	<li><a href="#30to31">DDMS 3.0 to 3.1</a></li>
	<li><a href="#31to401">DDMS 3.1 to 4.0.1</a></li>
	<li><a href="#401to41">DDMS 4.0.1 to 4.1</a></li>
	<li><a href="#ismSecurity">ISM Security Attributes</li>
	<li><a href="#ismNotice">ISM Notice Attributes</li>
</ul>
	
<a name="20to30"></a>
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

<a name="30to31"></a>
<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.0 Notes</th><th width="32%">DDMS 3.1 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.0/</code></td><td>Changed to <code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td></tr>
<tr><td><code>ddms:dates/@ddms:approvedOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:Resource/@ISM:compliesWith</td><td>Unsupported, but can exist as extensible attributes.</td><td>Optional.</td></tr>
<tr><td><code>ddms:Resource/@ISM:DESVersion</td><td>Required. DESVersion should be 2.</td><td>Required. DESVersion must be 5.</td></tr>
</table>
<p class="figure">Table 2. Component changes from DDMS 3.0 to DDMS 3.1</p>
<br /><br />

<a name="31to401"></a>
<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 3.1 Notes</th><th width="32%">DDMS 4.0.1 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>http://metadata.dod.mil/mdr/ns/DDMS/3.1/</code></td><td>Changed to <code>urn:us:mil:ces:metadata:ddms:4</code></td></tr>
<tr><td><code>ddms:boundingBox/ddms:westBL<br />ddms:boundingBox/ddms:eastBL<br />ddms:boundingBox/ddms:southBL<br />ddms:boundingBox/ddms:northBL</td><td>Previously called "WestBL", "EastBL", "SouthBL", and "NorthBL".</td><td>Renamed.</td></tr>
<tr><td><code>ddms:category/@ISM:*</td><td>Unsupported, but can exist as extensible attributes.</td><td>Supports optional security attributes.</td></tr>
<tr><td><code>ddms:creator/@ISM:pocType<br />ddms:contributor/@ISM:pocType<br />ddms:pointOfContact/@ISM:pocType<br />ddms:publisher/@ISM:pocType</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
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
<p class="figure">Table 3. Component changes from DDMS 3.1 to DDMS 4.0.1</p>
<br /><br />

<a name="401to41"></a>
<table width="100%">
<tr><th width="36%">Component</th><th width="32%">DDMS 4.0.1 Notes</th><th width="32%">DDMS 4.1 Notes</th></tr>
<tr><td>XML Namespace</td><td><code>urn:us:mil:ces:metadata:ddms:4</code></td><td>Remains the same.</td></tr>
<tr><td><code>ddms:dates/ddms:acquiredOn</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:metacardInfo/ntk:Access</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:nonStateActor/@ddms:qualifier</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:nonStateActor/@ddms:qualifier</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ddms:resource/@ISM:DESVersion</td><td>Required. DESVersion must be 7.</td><td>Required. DESVersion must be 9.</td></tr>
<tr><td><code>ddms:resource/@NTK:DESVersion</td><td>Required. DESVersion must be 5.</td><td>Required. DESVersion must be 7.</td></tr>
<tr><td><code>ddms:resource/@ism:externalNotice</code></td><td>Unsupported, but can exist as extensible attributes.</td><td>Optional.</td></tr>
<tr><td><code>ddms:temporalCoverage/ddms:approximableStart<br />ddms:temporalCoverage/ddms:approximableEnd</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ism:Notice/@ism:externalNotice</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
<tr><td><code>ntk:Access/@ntk:externalReference</code></td><td>Cannot exist.</td><td>Optional.</td></tr>
</table>
<p class="figure">Table 4. Component changes from DDMS 4.0.1 to DDMS 4.1</p>
<br /><br />

<a name="ismSecurity"></a>
<p>The table below lists the complete set of ISM security attributes modeled by the <a href="/docs/index.html?buri/ddmsence/ddms/security/ism/SecurityAttributes.html">SecurityAttributes</a> class, 
and shows which attributes can be used with each version of DDMS.</p>

<table>
<tr><th>Attribute</th><th>DDMS 2.0 (ISM V2-PR)</th><th>DDMS 3.0 (ISM V2)</th><th>DDMS 3.1 (ISM V5)</th><th>DDMS 4.0.1 (ISM V7)</th><th>DDMS 4.1 (ISM V9)</th></tr>
<tr><td><code>@ISM:atomicEnergyMarkings</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classification</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classificationReason</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:classifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:compilationReason</code></td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:dateOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:declassDate</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassEvent</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassException</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:declassManualReview</code></td><td>Yes</td><td>No</td><td>No</td><td>No</td><td>No</td></tr>
<tr><td><code>@ISM:derivativelyClassifiedBy</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:derivedFrom</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:displayOnlyTo</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:disseminationControls</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceOpen</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:FGIsourceProtected</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonICmarkings</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:nonUSControls</code></td><td>No</td><td>No</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:noticeType</code></td><td>No</td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:ownerProducer</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:pocType</code></td><td>No</td><td>No</td><td>No</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:releasableTo</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SARIdentifier</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:SCIcontrols</code></td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:typeOfExemptedSource</code></td><td>Yes</td><td>Yes</td><td>No</td><td>No</td><td>No</td></tr></table>
<p class="figure">Table 5. Security Attribute changes from DDMS 2.0 to DDMS 4.1</p>

<a name="ismNotice"></a>
<p>The table below lists the complete set of ISM notice attributes modeled by the <a href="/docs/index.html?buri/ddmsence/ddms/security/ism/NoticeAttributes.html">NoticeAttributes</a> class, 
and shows which attributes can be used with each version of DDMS. These attributes were first introduced in DDMS 4.0.1.</p>

<table>
<tr><th>Attribute</th><th>DDMS 4.0.1 (ISM V7)</th><th>DDMS 4.1 (ISM V9)</th></tr>
<tr><td><code>@ISM:noticeType</code></td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:noticeReason</code></td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:noticeDate</code></td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:unregisteredNoticeType</code></td><td>Yes</td><td>Yes</td></tr>
<tr><td><code>@ISM:externalNotice</code></td><td>No</td><td>Yes</td></tr></table>

<p class="figure">Table 6. Security Attribute changes from DDMS 4.0.1 to DDMS 4.1</p>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>