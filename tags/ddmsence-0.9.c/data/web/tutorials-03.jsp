<html>
<head>
	<title>DDMSence: Tutorial #3 (Escape)</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="An open source Java library for the DoD Discovery Metadata Specification (DDMS)">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<h1>Tutorial #3: <u>Escape</u></h1>

<p><u>Escape</u> is a tool that loads multiple DDMS Resource files and then exposes various statistics about them through the 
<a href="http://code.google.com/apis/visualization/documentation/gallery.html" target="_new">Google Visualization API</a>. Charts in this sample
application are limited to non-interactive visualizations, but provide the foundation for possible uses of the Flash-based Google charts: it would
be possible to plot Temporal Coverage on an Annotated Timeline, or Geospatial Coverage on Google Maps. While the first two sample applications were 
designed to teach developers how DDMSence works, the intent of this one is to provide brainstorming ideas for leveraging DDMS in other contexts.</p>

<p>This application would be better suited as a web application, but I have implemented it with Swing, to minimize the amount of overhead required
to run it.</p>

<h3>Getting Started</h3>

<p>You do not need to have any experience with Google Visualization to use this application -- just be aware that it is a web-based tool which accepts 
a formatted URL and renders some sort of chart.</p>	

<p><u>Escape</u> can be run from the command line with the class, <b>buri.ddmsence.samples.Escape</b>. The application does not accept any
command line parameters. A network connection is needed to connect to Google (which also means that this application will not run in a SIPRNET environment).</p>

<p>Please see "<a href="documentation.jsp#started">Getting Started</a>" section for classpath details and command line syntax.</p> 

<h3>Walkthrough</h3>

<p>When the application first opens, it will search the <b>data/sample/</b> directory for any XML files and try to convert them all into Resource
objects. You might see an error message appear in your console, because I have included an invalid resource file (aptly named <b>InvalidResource.xml</b>)
in the directory, which is used in the <u>Essentials</u> application. You can safely ignore this message, because <u>Escape</u> will ignore any files it could not convert.</p>

<p>With a collection of Resources in hand, <u>Escape</u> examines the data in each Resource and builds frequency distribution charts based on various metrics. The
chart that appears initially shows the distribution of MIME Types in the resources (the ddms:mimeType element nested inside of ddms:format).</p>

<img src="../images/escape-01.png" width="400" height="264" title="First Screen" />
<p class="figure">Figure 1. The MIME Types Distribution</p>

<p>The text field below the chart contains the URL used to generate it. You can paste this URL into your web browser to view the same graph.</p>

<p>Now, let's take a look at the source code in <b>/src/samples/buri/ddmsence/samples/Escape.java</b> to see how this was accomplished. 
The important lines are found in the <b>buildMimeTypeGraph()</b> method:</p>

<div class="example"><pre>Distribution distribution = new Distribution();
for (Resource resource : getResources()) {
   // Check any records that have a format (mimeType is required if format is present)
   if (resource.getFormat() != null) {
      String mimeType = resource.getFormat().getMimeType();
      distribution.incrementCount(mimeType);
   }			
}
return (buildPieGraphURL("DDMS%20MimeType%20Distribution", distribution, PIE_GRAPH_3D));</pre></div>
<p class="figure">Figure 2. MIME Type source code</p>

<p>The code traverses all of the Format components to maintain a count of each MIME Type. (A simple data class, called <b>Distribution</b>, is
used to encapsulate the counts). Once the counts are generated, the data is sent to the <b>buildPieGraphURL()</b> method to render as a
Pie Chart in Google-specific terms.</p>

<p>As you can see from the code, a DDMS Resource object can be traversed in a standard way. Each of the top-level components in a Resource record
(as well as its attributes) has a getter method, and deeper components can be extracted from those top-level components. If the DDMS schema allows
0 or 1 elements to appear, the getter will return a single DDMS component. If the DDMS schema allows 0 to many elements appear, the getter will return
a List of DDMS components. The name of the getter will be a capitalized version of the DDMS element name (such as <b>getVirtualCoverage</b> for <b>ddms:virtualCoverage</b>).</p>

<p>Next, let's click on the "Keyword" tab to see a pie graph of keyword usage.</p>

<img src="../images/escape-02.png" width="400" height="266" title="Second Screen" />
<p class="figure">Figure 3. The Keyword Distribution</p>
 
<div class="example"><pre>Distribution distribution = new Distribution();
for (Resource resource : getResources()) {
   // Check any records that have a keyword (subjectCoverage is required)
   if (!resource.getSubjectCoverage().getKeywords().isEmpty()) {
      List<Keyword> keywords = resource.getSubjectCoverage().getKeywords();
      // Record the counts for each keyword's usage
      for (Keyword keyword : keywords) {
         // Split multiword keywords.
         String[] splitValues = keyword.getValue().split(" ");
         for (int i = 0; i < splitValues.length; i++) {
            distribution.incrementCount(splitValues[i]);
         }
      }
   }
}</pre></div>
<p class="figure">Figure 4. Keyword source code</p>

<p>The source code for this statistic is functionally identical to the source code for MIME Types. We are locating the Subject Coverage component
in the Resource and then traversing its complete list of Keywords. The only minor difference here is that multi-word Keyword values are being split
up into single words (to make the visualization more exciting).</p> 

<p>The last visualization can be seen in the "Dates" tab.</p>

<img src="../images/escape-03.png" width="400" height="265" title="Third Screen" />
<p class="figure">Figure 5. The Dates Distribution</p>

<p>The source code for this visualization can be found in the <b>buildDateGraph()</b> method. Dates can appear in a DDMS Resource in multiple locations:</p>

<ul>
	<li>There are four date attributes in the ddms:dates element, which apply to the resource being described.</li>
	<li>If any ddms:temporalCoverage elements are defined, each one may have a date range defined for starting and ending.</li>
	<li>The resource record itself has a createDate attribute.</li>
</ul>

<p>For this visualization, all date locations are checked, and then tranformed into xs:year values (the Java pattern "YYYY"). The distribution
is then tracked as it was in the previous two examples. A more useful graph might show just expiration dates or time periods -- I added the additional
dates to provide more examples of traversing a Resource, and to make the visualization more exciting.</p>

<p>If you would like to add new Resources to the dataset, simply copy your XML files into the <b>data/samples/</b> directory, or use the <u>Escort</u> wizard
application to generate Resources.</p>

<h3>Conclusion</h3>

<p>In this tutorial, you have seen how to traverse a valid DDMS Resource in Java, and how the Java representations can be used as a gateway to apply
DDMS concepts in unexpected contexts.</p>

<p>This is the final tutorial. If you have any suggestions for additional tutorials or sample applications, please contact me in one of the ways mentioned
under <a href="documentation.jsp#feedback">Feedback</a>!</p> 

<p>
	<a href="tutorials-01.jsp">Tutorial #1: Essentials</a><br />
	<a href="tutorials-02.jsp">Tutorial #2: Escort</a><br />
	Tutorial #3: Escape <span class="notify">(you are here)</span><br />
	<a href="documentation.jsp#samples">Back to Samples Documentation</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>