#owlbuilder

This is a java-based tool for generating owl files and web pages from an arachadmin (https://github.com/pmidford/arachadmin) database.  The owl files can be loaded into a sesame server, while the web pages can be collected into a web archive (.war) file for installation.  The current arachnologua install uses Tomcat7 as a container for both sesame and the associated web pages (no JSP's though).

## Copyright and License

All original code is copyright 2013-2014 Peter E. Midford

> This tool is available under the terms of the 'MIT' license, a copy of which is included in this repository.

## Contact:
   Peter E. Midford
   peter.midford@gmail.com
   
## Travis build status
[![Build Status](https://travis-ci.org/pmidford/owlbuilder.png?branch=master)](https://travis-ci.org/pmidford/owlbuilder)

### What does this mean?
If the build status is passing, the HEAD revision of the repository has been sucessfully built on a continuous build server so it should build on your system using maven.  Note that the tests are written to pass in the absence of a configured arachadmin database (there is a 'mock' database connection that the tests use in this situation).  

## Building

Use maven (3.0 recommended, 2.x may work) to build.  It was developed under Java 6, but at least builds and tests under Java 7 (using Travis-CI).

## Configuration
This tool assumes you have an arachadmin database installed on a MySQL server.  See the arachadmin project for a tool to populate and maintain
this database.

## Use

