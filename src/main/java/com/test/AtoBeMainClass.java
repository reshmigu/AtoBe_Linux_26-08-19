package com.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
/*import org.uncommons.reportng.HTMLReporter;
*/
	public class AtoBeMainClass {
		private static final Logger LOGGER = LogManager.getLogger(AtoBeMainClass.class);

	  public static void main(String[] args) {

	    if (args.length != 0) {

	      String argData = args[0];
/*	      HTMLReporter tla = new HTMLReporter();
*/	      JUnitXMLReporter jux = new JUnitXMLReporter();
	      XmlSuite suite = new XmlSuite();
	      suite.setName("TestSuite");
	      XmlTest test = new XmlTest(suite);
	      test.setName("Automation Testing");
	      List<XmlClass> classes = new ArrayList<>();

	      if (argData.equals("DRY_RUN")) {

	        classes.add(new XmlClass("com.test.DryRun"));

	      } else if (argData.equals("FULL_RUN")) {

	        classes.add(new XmlClass("com.test.FullRun"));

	      } else if (argData.equals("RUN")) {

	        classes.add(new XmlClass("com.test.Run"));

	      } else {
	      	LOGGER.info("Wrong argument.");
	      }


	      test.setXmlClasses(classes);
	      List<XmlSuite> suites = new ArrayList<>();
	      suites.add(suite);
	      TestNG tng = new TestNG();
/*	      tng.addListener(tla);
*/	      tng.addListener(jux);
	      tng.setXmlSuites(suites);
	      tng.run();

	    } else {
	    	LOGGER.info("Please pass an argument to run a test.");
	    }


	  }
	}

	
	

