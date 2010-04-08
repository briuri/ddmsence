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
requiring a server to run). It is not as flashy, but this should make it easier to focus on the important sections of the source code.</p>

<p>The only limitations on this application are that it will not ask you for the "Additional" ICISM security attributes, 
such as "SCIcontrols" or "SARIdentifier", the SRS Attributes on individual gml:pos elements, or any custom components in the Extensible Layer. 
There is no good reason for these limitations, other than to make the wizard a little shorter.</p>

<h3>Getting Started</h3>

<p><u>Escort</u> can be run from the command line with the class, <code>buri.ddmsence.samples.Escort</code>. The application does not accept any
command line parameters.</p>

<p>Please see the "<a href="documentation.jsp#started">Getting Started</a>" section for classpath details and command line syntax.</p> 

<h3>Walkthrough</h3>

<p>When you run <u>Escort</u> you will see a simple text screen:</p>

<div class="example"><pre>Escort: a DDMSence Sample

This program allows you to build a DDMS 3.0 resource from scratch.
If you do not know how to answer a question, a suggested valid answer is provided in square brackets.
However, this is not a default value (hitting Enter will answer the question with an empty string).

In FAST mode, Escort will only ask you to create top-level components which are required for a valid Resource.
In COMPLETE mode, Escort will let you create all of the top-level components.
Would you like to run in FAST mode? [Y/N]:</pre></div>
<p class="figure">Figure 1. Starting the Wizard</p>

<p>The wizard will walk you through each top-level component of a DDMS Resource. Each component you create must be valid before you can proceed to the
next one. This process could be lengthy, so I suggest that you do your first run-through in FAST mode.</p>

<p>First, let's try creating an invalid Identifier. The DDMS specification states that the qualifier must be a valid URI. Type in "<code>:::::</code>" as
a qualifier and "<code>test</code>" as a value.</p>

<div class="example"><pre>=== ddms:identifier (at least 1 required) ===
Please enter the qualifier [testQualifier]: :::::
Please enter the value [testValue]: test
[ERROR] /ddms:identifier: Invalid URI (Expected scheme name at index 0: :::::)
Please enter the qualifier [testQualifier]:</pre></div>
<p class="figure">Figure 2. Outsmarting the Wizard</p>

<p>Because the qualifier was an invalid URI, the wizard printed out the error message and then restarted the loop to get your input values. The value, 
"<code>/ddms:identifier</code>" tells you which component was causing a problem and can be retrieved via <code>getLocator()</code> on the <code>InvalidDDMSException</code>. The format
of the locator will be an XPath string, but should be enough to help you locate the offending component even if you have no XPath experience. (Skilled XPath
developers will notice that <code>ddms:identifier</code> seems to be the root node in the string -- this is because it had not been added to the parent Resource at the time
of the exception).</p>

<p>Now, let's take a look at the source code in <code>/src/samples/buri/ddmsence/samples/Escort.java</code> to see how this was accomplished. The <code>run()</code>
method defines the control flow for the program, and uses the helper method, <code>inputLoop()</code>, to ask for user input until a valid component can be created:</p>


<div class="example"><pre>printHead("ddms:identifier (at least 1 required)");
getTopLevelComponents().add(inputLoop(Identifier.class));
while (!onlyRequiredComponents && confirm("Add another ddms:identifier?")) {
   getTopLevelComponents().add(inputLoop(Identifier.class));	
}</pre></div>
<p class="figure">Figure 3. Excerpt from the run() method</p>
		
<div class="example"><pre>private IDDMSComponent inputLoop(Class theClass) throws IOException {
   IDDMSComponent component = null;
   while (component == null) {
      try {
         component = BUILDERS.get(theClass).build();
      }
      catch (InvalidDDMSException e) {
         printError(e);
      }
   }
   return (component);
}</pre></div>
<p class="figure">Figure 4. Source code to continuously loop until a valid component is created</p>

<p>An anonymous implementation of IComponentBuilder is created (in the <u>Escort</u> constructor) for each DDMS Component class, and each Builder
is responsible for one top-level component. For example, here is the definition of the Builder that builds Identifiers:</p>

<div class="example"><pre>BUILDERS.put(Identifier.class, new IComponentBuilder() {
   public IDDMSComponent build() throws IOException, InvalidDDMSException {
      String qualifier = readString("the qualifier [testQualifier]");
      String value = readString("the value [testValue]");
      return (new Identifier(qualifier, value));
   }		
});</pre></div>
<p class="figure">Figure 5. An anonymous builder that can build Identifiers from user input</p>

<p>As soon as <code>inputLoop()</code> receives a valid component from a Builder, it returns that component to the main wizard method, <code>run()</code>. The main
wizard saves the component in a list and moves on to the next component type. You can examine the constructor to see the code needed to build each type of
component. You can also see similar code by loading an XML file into the <u>Essentials</u> application and viewing its generated Java code.</p>

<p>Let's use the wizard to create a valid Resource. You should be able to follow the prompts to the end, but if not, the output below is one possible
road to a valid Resource.</p>

<div class="example"><pre>=== ddms:identifier (at least 1 required) ===
Please enter the qualifier [testQualifier]: testQualifier
Please enter the value [testValue]: testValue

=== ddms:title (at least 1 required) ===
Please enter the title text [testTitle]: testTitle
Please enter the title classification [U]: U
Please enter the title's ownerProducers as a space-delimited string [USA]: USA

=== Producers: creator, publisher, contributor, and pointOfContact (at least 1 required) ===
Please enter the producer type [creator]: creator
Please enter the entity type [Organization]: Organization
Please enter the number of names this producer has [1]: 1
Please enter the number of phone numbers this producer has [0]: 1
Please enter the number of email addresses this producer has [0]: 1
Please enter entity name #1 [testName1]: DISA
Please enter entity phone number #1 [testPhone1]: 703-885-1000
Please enter entity email #1 [testEmail1]: disa@disa.disa
Please enter the producer classification [U]: U
Please enter the producer's ownerProducers as a space-delimited string [USA]: USA

=== ddms:subjectCoverage (exactly 1 required) ===
Please enter the number of keywords to include [1]: 2
Please enter the number of categories to include [0]: 0
* Keyword #1
Please enter the keyword value [testValue]: ddms
* Keyword #2
Please enter the keyword value [testValue]: xml
Please enter the subject classification [U]: U
Please enter the subject's ownerProducers as a space-delimited string [USA]: USA

=== ddms:security (exactly 1 required) ===
Please enter the classification [U]: U
Please enter the ownerProducers as a space-delimited string [USA]: USA

=== ddms:Resource Attributes (all required) ===
Does this tag set the classification for the resource as a whole? [Y/N]: Y
Please enter Resource createDate [2010-03-24]: 2010-03-24
Please enter the Resource DESVersion [2]: 2
Please enter the Resource classification [U]: U
Please enter the Resource's ownerProducers as a space-delimited string [USA]: USA
The DDMS Resource is valid!
No warnings were recorded.</pre></div>
<p class="figure">Figure 6. Successful run of the Escort Wizard</p>

<p>DDMSence stores warning messages on each component for conditions that aren't necessarily invalid. Calling <code>getValidationWarnings()</code> on any component will return
the messages of that component and any subcomponents. In this run-through, no warnings were recorded. We will try an example with warnings later.</p>

<p>The final step is to save your valid Resource as an XML file. Enter a filename, and the Resource will be saved in the <code>data/sample/</code> directory.</p>

<div class="example"><pre>=== Saving the Resource ===
Would you like to save this file? [Y/N]: y
This Resource will be saved as XML in the data/sample/ directory.
Please enter a filename: myResource.xml
File saved at "C:\ddmsence-bin-1.1.0\data\sample\myResource.xml".

You can now open your saved file with the Essentials application.
The Escort wizard is now finished.</pre></div>
<p class="figure">Figure 7. Saving the File</p>

<p>Once the file is saved, you can open it with the <u>Essentials</u> application to view the Resource in different formats. You can also use the wizard
to generate additional data files for the <u>Escape</U> application.</p>

<p>If you were to run <u>Escort</u> again in COMPLETE mode, and then create an empty <code>ddms:dates</code> component, you would see a warning message when the Resource
was generated:</p>

<div class="example"><pre>=== ddms:dates (only 1 allowed) ===
Include this component? [Y/N]: y
Please enter the created date [2010]: 
Please enter the posted date [2010]: 
Please enter the validTil date [2010]: 
Please enter the infoCutOff date [2010]:

<i>[...]</i>

The DDMS Resource is valid!
[WARNING] /ddms:Resource/ddms:dates: A completely empty ddms:dates element was found.
</pre></div>
<p class="figure">Figure 8. Triggering a Warning Condition</p>

<p>As you can see, the locator information on warnings is in the same format as the information on errors. Because parent components claim the warnings of their children,
a more detailed locator can be created. In this case, calling <code>getValidationWarnings()</code> on the Resource shows the full path of "<code>/ddms:Resource/ddms:dates</code>". If 
you called <code>getValidationWarnings()</code> on the Dates component itself, the locator would be "<code>/ddms:dates</code>".</p>

<p>If a parent-child hierarchy has some DDMS elements which are not <a href="documentation.jsp#design">implemented as Java objects</a>, the locator string will
include every element in the hierarchy. For example, a warning in a <code>ddms:medium</code> element will have a locator value of "<code>/ddms:Resource/ddms:format/ddms:Media/ddms:medium</code>"
even though <code>ddms:Media</code> is not an implemented component (the medium is a property on the Format object in the Java implementation).</p>
  
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