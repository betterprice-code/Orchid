Orchid
======

Orchid is a Tor client implementation and library written in pure Java.  Orchid's strength is that it can be used to Tor-ify Java and JVM applications with near transparency.

It was written from the Tor specification documents, which are available [here](https://www.torproject.org/docs/documentation.html.en#DesignDoc).

Orchid runs on Java 5+ and the Android devices.  

![Orchid](https://subgraph.com/img/orchidlogo1.png)

# USE MAVEN
You can use the project in maven<p> 
You need to add a repository in your `pom.xml`:
	
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
and add dependency:
	
 
```
<dependencies>
	<dependency>
		<groupId>com.github.betterprice-code</groupId>
		<artifactId>lib.orchid</artifactId>
		<version>1.0.4</version>
		<type>jar</type>
	</dependency>
</dependencies>
```