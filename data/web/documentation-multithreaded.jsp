<html>
<head>
	<title>Power Tip - Thread Safety| DDMSence</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="The open-source Java library for the DoD Discovery Metadata Specification (DDMS)">
	<meta name="viewport" content="width=device-width,initial-scale=1">
</head>
<body>
<%@ include file="../shared/header.jspf" %>

<a name="top"></a><h1>Power Tip: Thread Safety</h1>

<p>Thread safety can be evaluated from two perspectives:</p>
<ol>
	<li>Can multiple Threads create unique instances of a class without unexpected side effects or a shared global state?</li>
	<li>Can a unique instance of a class be shared and manipulated by more than one Thread simultaneously?</li>
</ol>
<p>Starting in DDMSence v2.5.0, every class has been implemented in a way that prevents unexpected side effects or shared global state, 
but only a subset of classes can have their instances shared safely among Threads. This table provides a summary of the relative 
thread safety of every custom class in DDMSence. The class categories are discussed in-depth below.</p>

<table>
<tr><th>Class Category</th><th>Free of Side Effects?</th><th>Shareable Across Threads?</th></tr>
<tr><td><b>immutable</b></td><td>Yes</td><td>Yes</td></tr>
<tr><td><b>stateless utility</b></td><td>Yes</td><td>Yes</td></tr>
<tr><td><b>synchronized</b></td><td>Yes</td><td>Yes</td></tr>
<tr><td><b>thread-localized</b></td><td>Yes</td><td>Not Applicable</td></tr>
<tr><td><b>unsafe</b></td><td>Yes</td><td>No</td></tr>
</table>
<p class="figure">Table 1. Thread Safety</p>

<h3>Class Categories</h3>

<h4>Immutable Classes</h4>

<p>Immutable classes are read-only and cannot change after instantiation. They can be instantiated in multiple Threads without side effects, 
and can also be shared by more than one Thread simultaneously. Whenever one of these classes exposes a "get" accessor with a mutable return 
value (such as an XMLGregorianCalendar, an ArrayList, a XOM Element, or a XOM Attribute), the returned object will always be a new copy, 
detached from the original. Classes in this category include:</p>

<ul>
	<li>All DDMS component classes (classes which implement the IDDMSComponent interface) and their superclasses</li>
	<li>All attribute group classes (classes which extend from AbstractAttributeGroup) and their superclass</li>
	<li>All interfaces (classes which have a capital letter "I" as a class name prefix)</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/ddms/OutputFormat.html">OutputFormat</a> enumeration</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/ddms/ValidationMessage.html">ValidationMessage</a> class</li>
</ul>

<h4>Stateless Utility Classes</h4>

<p>These classes are simple sets of stateless utility methods, whose output depends solely on input parameters. These functional methods 
can be called from multiple Threads without side effects, and are implicitly shared by all Threads. The classes may perform some private 
caching for performance reasons, but it is done in a thread-safe way.</p>

<ul>
	<li>The <a href="/docs/index.html?buri/ddmsence/ddms/security/ism/ISMVocabulary.html">ISMVocabulary</a> class</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/util/Util.html">Util</a> class</li>
</ul>

<h4>Synchronized Classes</h4>

<p>Although these classes might contain mutable fields, access to those fields is protected with the Java synchronized keyword. 
They can be instantiated in multiple Threads without side effects. They can also be shared by more than one Thread simultaneously, 
but are generally intended for read-only, informational purposes.</p>

<ul>
	<li>All custom exception classes (classes which extend from java.lang.Exception)</li>
</ul>

<h4>Thread-Localized Classes</h4>

<p>Although these classes might contain mutable fields, access to those fields is protected with Java ThreadLocal wrappers, 
ensuring that each running Thread has its own copy. These localized classes can be instantiated in multiple Threads without side effects, 
but by their nature, cannot be shared by more than one Thread simultaneously.</p>

<ul>
	<li>The <a href="/docs/index.html?buri/ddmsence/util/DDMSVersion.html">DDMSVersion</a> class</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/util/PropertyReader.html">PropertyReader</a> class</li>
</ul>

<h4>Unsafe Classes</h4>

<p>These classes are mutable and may change after instantiation, or have dependencies on other mutable classes. 
They can be instantiated in multiple Threads without side effects, but cannot be shared by more than one Thread 
simultaneously.</p>

<ul>
	<li>All DDMS component builder classes (classes which implement the IBuilder interface) and their superclasses</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/util/DDMSReader.html">DDMSReader</a> class</li>
	<li>The <a href="/docs/index.html?buri/ddmsence/util/LazyList.html">LazyList</a> class</li>
</ul>

<h3>Multithreaded Use Cases</h3>

<p>DDMSence is primarily designed to support multithreaded use cases where Threads have no dependency on each other. 
Multiple Threads can be parsing, validating, building, or transforming DDMS components, with the expectation that no 
two Threads are operating on the same components at the same time.</p>

<p>As an example, consider an input set of millions of DDMS 2.0 XML records. You wish to use DDMSence to transform them 
all up to DDMS 4.1. This operation has 3 discrete steps:</p>

<ol>
	<li>Parse the old XML file and set up a mutable Builder.</li>
	<li>Transform the record using Builder method calls.</li>
	<li>Save the record as a DDMS 4.1 XML file.</li>
</ol>

<p>A basic approach would be to perform all 3 steps within a single Thread, but load-balance the millions of records across 
many Threads. Each Thread owns a separate batch of records, and is guaranteed to run without any dependency on 
(or interference from) other Threads.</p>

<img src="./images/multithreaded-1.png" width="540" height="213" title="Basic Approach" class="diagram" />
<p class="figure">Figure 1. The Basic Approach</p>

<p>A more advanced approach would be to assign the responsibility of each step to a different Thread. By maintaining a 
collection of partially completed objects, the outputs of one Thread become the inputs of the next Thread. You might 
consider this approach to isolate and parallelize one of the steps for some reason, such as managing I/O bottlenecks.</p>

<img src="./images/multithreaded-2.png" width="800" height="166" title="More Advanced Approach" class="diagram" />
<p class="figure">Figure 2. The More Advanced Approach</p>

<ol>
	<li>Thread #1 parses the old XML file and sets up the initial mutable Builder.</li>
	<li>Thread #2 listens to some data source for Builders in need of transformation. When one becomes available, this 
		Thread transforms the record to DDMS 4.1 and commits it as an immutable Resource.</li>
	<li>Thread #3 listens to some data source for finished Resources. When one becomes available, this Thread saves it to disk.</li>
</ol>

<p>Although the Threads perform sequentially on a single metacard, they are not directly dependent on each other (assuming that the 
collection of partially completed objects exists in some sort of publish/subscribe architecture). The different representations 
of the XML file (Resource and Builder) are shared between Threads, but the unsafe classes (the Builders) are only operated on by 
a single Thread.</p>

<p>In the advanced approach, you could parallelize Thread #2 responsibilities as long as each copy of Thread #2 was working on a 
separate Builder instance. Multiple Threads should never manipulate the same Builder instance at the same time.</p>

<p>
	<a href="#top">Back to Top</a><br>
	<a href="documentation.jsp#tips">Back to Power Tips</a>
</p>

<div class="clear"></div>
<%@ include file="../shared/footer.jspf" %>
</body>
</html>