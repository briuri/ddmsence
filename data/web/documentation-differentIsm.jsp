<html>
<head>
	<title>DDMSence: Power Tip - Using Alternate Versions of ISM</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<a name="top"></a><h1>Power Tip: Using Alternate Versions of ISM</h1>

<p>DDMSence comes bundled with the Public Release versions of the IC Information Security Marking (ISM) schemas, which allows them to be distributed without
caveat on the public Internet. For many developers, this should be sufficient. However, some developers may need to make use of ISM values from higher 
classification versions of ISM (such as the FOUO version). The instructions below show you how to replace the bundled ISM files for one of the supported
versions of DDMS with your own copies.</p> 

<h2>Restrictions</h2>
<img src="./images/ismDirectory.png" width="344" height="327" title="ISM Directory Structure" align="right" border="1"/>
<ol>
<li>These instructions assume that you are updating from a Public Release to a more restricted release, and are NOT changing ISM versions altogether. Please 
remember that each DDMS version identifies one specific supported version of ISM. For example, swapping out ISM.XML V7 Public Release for DDMS 4.0 and 
replacing it with ISM.XML V3 FOUO may have unexpected side effects or simply fail to work.</li>
<li>Out of the box, DDMSence only contains files cleared for Public Release. Using restricted files with DDMSence does not remove your responsibility for
obeying all handling and dissemination instructions on those files. Do not proceed unless you are comfortable with this responsibility.</li>
</ol>

<h2>What You Need</h2>

<ol>
<li>The top-level ISM schema files, <code>IC-ISM.xsd</code> and <code>CVEGeneratedTypes.xsd</code>. These files might be found in your ISM archive at 
<code>ISM7/Schema/ISM/</code>.</li>
<li>The support generated CVE schemas. These files might be found at <code>ISM7/Schema/ISM/CVEGenerated/</code>.</li>
<li>The raw vocabulary files used to create the schemas. These files might be found at <code>ISM7/CVE/ISM/</code>.</li>
</ol>
 
<h2>Instructions</h2>

<ol>
<li>As an example, these instructions show how you would swap ISM files for DDMS 4.0.</li>
<li>First, decide on a classpath-accessible directory where your files will be stored. In this example, let's use <code>C:\tomcat\shared\classes</code>.</li>
<li>DDMSence will expect ISM schema files to be available at <code>schemas/&lt;ddmsVersionNumber&gt;/ISM/</code>, relative to the library. 
In your custom directory, set up the appropriate <code>schemas/&lt;ddmsVersionNumber&gt;/ISM/</code> directory structure, according to the diagram
on the upper right. It should contain the top-level ISM schemas, a subdirectory (<code>CVEGenerated</code>) for the generated schemas,
and a subdirectory (<code>CVE</code>) for the raw vocabulary files.</li>
<li>In this example, there should now be a file at <code>C:\tomcat\shared\classes\schemas\4.0\ISM\CVE\CVEnumISMClassificationAll.xml</code>.</li>
<li>Next, you will need to edit the classpath of your JVM to ensure that this directory structure has a higher priority than the DDMSence JAR file by
adding it first. The example below assumes that your classpath was original stored an the environment variable (<code>DDMSENCE_CLASSPATH</code>), and
places our custom directory first.</li>

<pre class="brush: xml">set DDMSENCE_CLASSPATH=C:\tomcat\shared\classes;%DDMSENCE_CLASSPATH%
java -cp %DDMSENCE_CLASSPATH% buri.ddmsence.samples.Essentials</pre>
<p class="figure">Figure 1. Putting your custom directory at the beginning of your classpath, in Windows/DOS</p>

<li>When you run your program, DDMSence will search your custom directory first, and only turn to the bundled files in the DDMSence JAR 
if it cannot find the custom directory.</li>
<li>To confirm your success, try creating a DDMS XML instance that uses vocabulary values not found in the bundled ISM files. Normally if you tried to open this
instance with the <u>Essentials</u> sample program, it would complain about the vocabulary value. If you have followed these instructions properly, the
instance should load correctly.</li>
</ol>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>