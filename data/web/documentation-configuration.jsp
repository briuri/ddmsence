<html>
<head>
	<title>DDMSence: Power Tip - Configurable Properties</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<a name="top"></a><h1>Power Tip: Configurable Properties</h1>

<p>DDMSence exposes some properties, such as the namespace prefixes used for each XML namespace, so they can be configured by the end user. For example, if you are 
building components from scratch, and you wish to use "ic" as a namespace prefix for the Intelligence Community namespace
instead of "ISM", you would set the "ism.prefix" property with a custom value of <code>ic</code>.</p>

<pre class="brush: java">PropertyReader.setProperty("ism.prefix", "ic");</pre>
<p class="figure">Figure 1. Command to change a configurable property.</p>

<p>Only the subset of properties listed below can be set programmatically. Attempts to change other DDMSence properties will result in an exception.</p>
	
<table>
<tr><th>Property Name</th><th>Description</th><th>Default Value</th></tr>
<tr><td>ddms.prefix</td><td>Default DDMS prefix used when generating components from scratch</td><td><code>ddms</code></td></tr>
<tr><td>gml.prefix</td><td>Default GML prefix used when generating components from scratch</td><td><code>gml</code></td></tr>
<tr><td>ism.prefix</td><td>Default ISM prefix used when generating components from scratch</td><td><code>ISM</code></td></tr>
<tr><td>ntk.prefix</td><td>Default NTK prefix used when generating components from scratch</td><td><code>ntk</code></td></tr>
<tr><td>sample.data</td><td>Default data directory used by sample applications</td><td><code>data/sample/</code></td></tr>
<tr><td>xlink.prefix</td><td>Default XLink prefix used when generating components from scratch</td><td><code>xlink</code></td></tr>
<tr><td>xml.transform.TransformerFactory</td><td>XSLT Engine class name, for Schematron validation<td><code>net.sf.saxon.TransformerFactoryImpl</code></td></tr>
</table>
<p class="figure">Table 1. Configurable Properties</p>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>