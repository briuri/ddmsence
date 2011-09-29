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
caveat on the public Internet. For many developers, this should be sufficient. However, some developers may need to make use of ISM values from higher classification
versions of ISM (such as the FOUO version). I would ultimately like to support the ability to use custom sets of ISM schemas and vocabularies
as a runtime configuration step -- this future work is captured in <a href="http://code.google.com/p/ddmsence/issues/detail?id=154">Issue #154</a>. In the meantime,
it is still possible to use a different set, but it will require a little more do-it-yourself modification of the DDMSence JAR file.</p>

<p>The instructions below show you how to replace the bundled ISM files with your own copies, based on which DDMSence download you have ("bin" or "src"). 

<h2>Restrictions</h2>

<ol>
<li>These instructions assume that you are updating from a Public Release to a more restricted release, and are NOT changing ISM versions altogether. Please 
remember that each DDMS version identifies one specific supported version of ISM -- for example, swapping out ISM.XML V7 Public Release for DDMS 4.0 and 
replacing it with ISM.XML V3 FOUO may have unexpected side effects or simply fail to work.</li>
<li>Take care to obey all handling and dissemination instructions for your restricted files. For example, if you create a custom DDMSence JAR file that
includes FOUO materials, you will need to treat the JAR file itself as FOUO. It should go without saying that a JAR File with Secret files should only 
exist on SIPRNet!
</li>
</ol>

<p><b><i>Out of the box, DDMSence only contains files cleared for Public Release. Adding restricted files to DDMSence by following these
instructions is a completely optional step, and it is still your responsibility to obey all handling instructions. Do not proceed unless you are comfortable 
with this responsibility.</i></b></p>

 
<h2>Swapping out ISM files in the "src"-flavored download</h2>

<img src="./images/ismDirectory.png" width="344" height="472" title="ISM Directory Structure" align="right"/>
<ol>
<li>In the <code>data</code> directory, there is a <code>schemas</code> directory which contains the schemas for each supported version of DDMS. It should look similar
to the diagram on the right.</li>
<li>Find the <code>ISM</code> subdirectory for the DDMS version you wish to modify. The diagram on the right shows the <code>ISM</code> folder for DDMS 4.0, which
contains the Public Release copy of ISM.XML V7.</li>
<li>Overwrite these files with your own copies. Make sure to copy both your schema files (.xsd) and your Controlled Vocabulary Enumeration files (.xml).</li>
<li>Finally, run the "dist" target of the included <b>build.xml</b> Ant file. This will generate new JAR files containing your schema files, which can be found
under <code>output/jars/</code>.</li>
<li>Give your JAR file a new name to distinguish it from the base DDMSence files and to remind everyone of any security markings, such as <code>ddmsence-ism-fouo-2.0.0.jar</code>.
<li>To confirm your success, try creating a set of SecurityAttributes with the new JAR file, using a vocabulary value that exists in your files but not in the Public Release
files.</li>
</ol>

<h2>Swapping out ISM files in the "bin"-flavored download</h2>

<ol>
<li>Locate the DDMSence JAR file in the <code>lib</code> directory of your downloaded library. In DDMSence 2.0.0, this file will be called <code>ddmsence-2.0.0.jar</code>.</li>
<li>Rename this file to <code>ddmsence.zip</code>. This will allow you to open up the file with your favorite ZIP utility.</li>
<li>Unzip the file in a temporary location and go to the directory where you unzipped it.</li>
<li>In the unzipped archive, you will find a <code>schemas</code> directory which contains the schemas for each supported version of DDMS. It should look similar
to the diagram on the right.</li>
<li>Find the <code>ISM</code> subdirectory for the DDMS version you wish to modify. The diagram on the right shows the <code>ISM</code> folder for DDMS 4.0, which
contains the Public Release copy of ISM.XML V7.</li>
<li>Overwrite these files with your own copies. Make sure to copy both your schema files (.xsd) and your Controlled Vocabulary Enumeration files (.xml).</li>
<li>Use your favorite ZIP utility to rearchive the DDMSence files into a file called (for example) <code>ddmsence-ism-2.0.0.zip</code>.</li>
<li>Open up both the old and the new ZIP files with a ZIP utility and confirm that the directory structure for all of the files in the new ZIP match the directory
structure from the old ZIP. If they do not match, there may be some configuration settings required on your ZIP utility to correctly create the new one.</li>
<li>If the new archive looks correct, give it a new name to distinguish it from the base DDMSence files and to remind everyone of any security markings, such as <code>ddmsence-ism-fouo-2.0.0.jar</code>.
<li>To confirm your success, try creating a set of SecurityAttributes with the new JAR file, using a vocabulary value that exists in your files but not in the Public Release
files.</li>
</ol>



<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>