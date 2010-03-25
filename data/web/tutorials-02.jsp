<html>
<head>
	<title>DDMSence: Tutorial #2 (Escort)</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Tutorial #2: <u>Escort</u></h1>

<p><u>Escort</u> is a step-by-step wizard for building a DDMS Resource from scratch, and then saving it to a file. The source code for this application
shows an example of how the Java object model can be built with basic data types (possibly mapped from a database table or some other pre-existing entity).</p>

<p>I have implemented this wizard as a series of textual prompts, to avoid the overhead of having a full-fledged MVC Swing application (or implementing it as a web application and
requiring a server to run). It is not as flashy, but this should make it easier to focus on the important sections of the source code. There are also
a few artificial limitations on the some elements:</p>

<ul>
	<li>The wizard will not ask for the ICISM "additional" attributes on any component.</li>
	<li>The wizard only allows 1 name, 1 phone number, and 1 email address per producer entity.</li>
	<li>The wizard builds SubjectCoverage components from keywords only, not categories.</li>
	<li>The wizard only allows GeospatialCoverage components based on FacilityIdentifiers.</li>
	<li>The wizard only allows a RelatedResources component to contain 1 RelatedResource, and that Resource only contains 1 Link.</li>
</ul>

<p>There are no good reasons for these omissions, other than to make the wizard a little simpler. I may remove these limitations in the future.</p>

<h3>Getting Started</h3>

<p><u>Escort</u> can be run from the command line with the class, <b>buri.ddmsence.samples.Escort</b>. The application does not accept any
command line parameters.</p>

<p>Please see "<a href="documentation.jsp#started">Getting Started</a>" section for classpath details and command line syntax.</p> 

<h3>Walkthrough</h3>

<p>When you run <u>Escort</u> you will see a simple text screen:</p>

<div class="example"><pre>Escort: a DDMSence Sample

This program allows you to build a DDMS resource from scratch.
If you do not know how to answer a question, a suggested valid answer is provided in square brackets.
However, this is not a default value (hitting Enter will answer the question with an empty string).

Starting a new ddms:Resource...

At least 1 ddms:identifier is required.
Please enter the qualifier [URI]:</pre></div>
<p class="figure">Figure 1. Starting the Wizard</p>

<p>The wizard will walk you through each top-level component of a DDMS Resource. Each component you create must be valid before you can proceed to the
next one. This process could be lengthy, so I suggest that you skip any optional components on your first run-through.</p>

<p>First, let's try creating an invalid Identifier. The DDMS specification states that the qualifier must be a valid URI. Type in "<b>:::::</b>" as
a qualifier and <b>test</b> as a value.</p>

<div class="example"><pre>Please enter the qualifier [URI]: :::::
Please enter the value [testValue]: test
Validating...
Invalid URI: Expected scheme name at index 0: :::::
Please enter the qualifier [URI]:</pre></div>
<p class="figure">Figure 2. Outsmarting the Wizard</p>

<p>Because the qualifier was an invalid URI, the wizard printed out the error message and then restarted the loop to get your input values.</p>

<p>Now, let's take a look at the source code in <b>/src/samples/buri/ddmsence/samples/Escort.java</b> to see how this was accomplished. 
The important lines are found in the <b>buildIdentifier()</b> method:</p>

<div class="example"><pre>String qualifier = readString("the qualifier [URI]");
String value = readString("the value [testValue]");
try {
   println("Validating...");
   Identifier component = new Identifier(qualifier, value);
   println("This component is valid.");
   return (component);
}
catch (InvalidDDMSException e) {
	println(e.getMessage());
}</pre></div>
<p class="figure">Figure 3. Source code to build an Identifier</p>

<p>The example code above sits in a loop which keeps asking you for values until a valid Identifier can be created. It then returns the valid
Identifier back to the main wizard method, <b>run()</b>, and proceeds to the next top-level component. There is a <b>build<i>Component</i>()</b>
method for each top-level component, so you can see how each one is built. (You can also see how a component is built by loading an XML file into the <u>Essentials</u> application
and viewing its generated Java code).</p>

<p>Let's use the wizard to create a valid Resource. You should be able to follow the prompts to the end, but if not, the output below is one possible
road to a valid Resource.</p>

<div class="example"><pre>Please enter the qualifier [URI]: URI
Please enter the value [testValue]: testValue
Validating...
This component is valid.
Add another ddms:identifier? [Y/N]: n

At least 1 ddms:title is required.
Please enter the title text [testTitle]: My New Resource
Please enter the title classification [U]: U
Please enter the title's ownerProducers as a space-delimited string [USA AUS]: USA
Validating...
This component is valid.
Add another ddms:title? [Y/N]: n

ddms:subtitle is optional.
Add an optional ddms:subtitle? [Y/N]: n

ddms:description is optional.
Add the optional ddms:description? [Y/N]: n

ddms:language is optional.
Add an optional ddms:language? [Y/N]: n

ddms:dates is optional.
Add the optional ddms:dates? [Y/N]: n

ddms:rights is optional.
Add the optional ddms:rights? [Y/N]: n

ddms:source is optional.
Add an optional ddms:source? [Y/N]: n

ddms:type is optional.
Add an optional ddms:type? [Y/N]: n

At least one producer (creator, publisher, contributor, pointOfContact) is required.
Sample Limitation: This wizard only allows one name, phone number, and email per producer.
Please enter the producer type [creator]: creator
Please enter the entity type [Organization]: Person
Please enter the entity name [John]: Brian
Please enter the entity phone [703-885-1000]: 
Please enter the entity email [ddms@fgm.com]: ddmsence@urizone.net
Please enter the producer classification [U]: U
Please enter the producer's ownerProducers as a space-delimited string [USA AUS]: USA
Please enter the Person surname [Smith]: Uri
Please enter the Person userID [123]: 
Please enter the Person affiliation [DISA]: 
Validating...
This component is valid.
Add another producer? [Y/N]: n

ddms:format is optional.
Add the optional ddms:format? [Y/N]: n

ddms:subjectCoverage is required.
Sample Limitation: This wizard only supports keywords, not categories.
Please enter the keywords as a space-delimited string [ddms xml]: ddmsence ddms xml
Please enter the subject classification [U]: U
Please enter the subject's ownerProducers as a space-delimited string [USA AUS]: USA
Validating...
This component is valid.

ddms:virtualCoverage is optional.
Add an optional ddms:virtualCoverage? [Y/N]: n

ddms:temporalCoverage is optional.
Add an optional ddms:temporalCoverage? [Y/N]: n

ddms:geospatialCoverage is optional.
Add an optional ddms:geospatialCoverage? [Y/N]: n

ddms:relatedResources is optional.
Add an optional ddms:relatedResources? [Y/N]: n

ddms:security is required.
Please enter the classification [U]: U
Please enter the ownerProducers as a space-delimited string [USA AUS]: USA
Validating...
This component is valid.

You are now done with top-level components. All that remains is the Resource attributes.
Does this tag set the classification for the resource as a whole? [Y/N]: Y
Please enter Resource createDate [2010-03-24]: 2010-03-24
Please enter the Resource DESVersion [2]: 2
Please enter the Resource classification [U]: U
Please enter the Resource's ownerProducers as a space-delimited string [USA AUS]: USA
Validating...
The DDMS Resource is valid!</pre></div>
<p class="figure">Figure 4. Successful run of the Escort Wizard</p>

<p>The final step is to save your valid Resource as an XML file. Enter a filename, and the Resource will be saved in the <b>data/sample/</b> directory.</p>

<div class="example"><pre>This Resource will be saved as XML in the data/sample/ directory.
Please enter a filename: myNewResource.xml
File saved at F:\projects\DDMSence\data\sample\myNewResource.xml .

You can now open your saved file with the Essentials application.
</pre></div>
<p class="figure">Figure 5. Saving the File</p>

<p>Once the file is saved, you can open it with the <u>Essentials</u> application to view the Resource in different formats. You can also use the wizard
to generate additional data files for the <u>Escape</U> application.</p>

<h3>Conclusion</h3>

<p>In this tutorial, you have seen how DDMS Resources can be built from scratch. You have also seen further examples of component validation.</p>

<p>The next tutorial, covering the Escape application, will show how a DDMS Resource can be traversed and used in other contexts.</p>

<p>
	<a href="tutorials-01.jsp">Tutorial #1: Essentials</a><br />
	Tutorial #2: Escort <span class="notify">(you are here)</span><br />
	<a href="tutorials-03.jsp">Tutorial #3: Escape</a><br />
	<a href="documentation.jsp#samples">Back to Samples Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>