/* Copyright 2010 - 2014 by Brian Uri!
   
   This file is part of DDMSence.
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 3.0 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public 
   License along with DDMSence. If not, see <http://www.gnu.org/licenses/>.

   You can contact the author at ddmsence@urizone.net. The DDMSence
   home page is located at http://ddmsence.urizone.net/
 */
package buri.ddmsence.ddms.security.ism;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.AccessorRunnable;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.DDMSVersionTest.DDMSVersionRunnable;

/**
 * <p> Tests related to the ISM Controlled Vocabularies </p>
 * 
 * @author Brian Uri!
 * @since 0.9.d
 */
public class ISMVocabularyTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public ISMVocabularyTest() {
		super(null);
	}

	@Test
	public void testBadKey() {
		try {
			ISMVocabulary.getEnumerationTokens(DDMSVersion.getCurrentVersion(), "unknownKey");
			fail("Allowed invalid key.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "No controlled vocabulary could be found");
		}
	}

	@Test
	public void testEnumerationTokens() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_ALL_CLASSIFICATIONS, "C"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_ALL_CLASSIFICATIONS, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_OWNER_PRODUCERS, "AUS"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_OWNER_PRODUCERS, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SCI_CONTROLS, "HCS"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SCI_CONTROLS, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FOUO"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_FGI_SOURCE_OPEN, "ABW"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_FGI_SOURCE_OPEN, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, "ABW"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_RELEASABLE_TO, "ABW"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_RELEASABLE_TO, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NON_IC_MARKINGS, "DS"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NON_IC_MARKINGS, "unknown"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DECLASS_EXCEPTION, "25X1"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DECLASS_EXCEPTION, "unknown"));

			if (!version.isAtLeast("3.1")) {
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, "X1"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, "unknown"));
			}

			if (version.isAtLeast("3.1")) {
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_ATOMIC_ENERGY_MARKINGS, "RD"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_ATOMIC_ENERGY_MARKINGS, "unknown"));

				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_COMPLIES_WITH, "DoD5230.24"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_COMPLIES_WITH, "unknown"));

				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISPLAY_ONLY_TO, "ABW"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISPLAY_ONLY_TO, "unknown"));

				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NON_US_CONTROLS, "ATOMAL"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NON_US_CONTROLS, "unknown"));
			}

			if (version.isAtLeast("4.0.1")) {
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NOTICE_TYPE, "DoD-Dist-B"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_NOTICE_TYPE, "unknown"));

				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_POC_TYPE, "DoD-Dist-B"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_POC_TYPE, "unknown"));
			}
		}
	}

	@Test
	public void testEnumerationPatterns() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SCI_CONTROLS, "SI-G-ABCD"));
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SCI_CONTROLS, "SI-G-ABCDE"));

			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SAR_IDENTIFIER, "SAR-ABC"));
			assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SAR_IDENTIFIER, "SAR-AB"));
			
			StringBuffer b = new StringBuffer("SAR-");
			for (int i = 0; i < 11; i++) {
				b.append("0123456789");
			}
			assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_SAR_IDENTIFIER, b.toString()));

			if (!version.isAtLeast("3.1")) {
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-1"));
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-12"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-100"));

				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-1"));
				assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-12"));
				assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-100"));
			}
		}
	}

	@Test
	public void testIsUSMarking() {
		DDMSVersion version = DDMSVersion.getVersionFor("2.0");
		assertTrue(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_US_CLASSIFICATIONS, "TS"));
		assertFalse(ISMVocabulary.enumContains(version, ISMVocabulary.CVE_US_CLASSIFICATIONS, "CTS"));
	}

	@Test
	public void testInvalidMessage() {
		assertEquals("Dog is not a valid enumeration token for this attribute, as specified in Cat.",
			ISMVocabulary.getInvalidMessage("Cat", "Dog"));
	}
	
	@Test
	public void testMultithreaded() throws InterruptedException {
		List<Thread> threads = new ArrayList<Thread>();
		List<AccessorRunnable> runnables = new ArrayList<AccessorRunnable>();
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			runnables.add(new ISMVocabularyRunnable(i));
			threads.add(new Thread(runnables.get(i), runnables.get(i).getThreadName()));
			threads.get(i).start();
		}
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			threads.get(i).join();
		}

		int numFailures = 0;
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			if (!runnables.get(i).getMatch())
				numFailures++;
		}
		if (numFailures > 0) {
			fail(numFailures + " threads contained an invalid enumeration value.");
		}
	}
	
	/**
	 * Helper class to test ISMVocabulary in multiple threads.
	 */
	public static class ISMVocabularyRunnable extends AccessorRunnable {
		private DDMSVersion _randomVersion;
		
		public ISMVocabularyRunnable(int threadNum) {
			super("Thread" + threadNum);
			setRandomVersion(threadNum);
		}
		
		@Override
		public String getExpectedValue() {
			return (String.valueOf(getRandomVersion().getVersion().equals("5.0")));
		}
		
		@Override
		public void callSet() {
			// No set logic needed. Potentially unsafe operation is a single method call.
		}
		
		@Override
		public String callGet() {
			return (String.valueOf(ISMVocabulary.enumContains(getRandomVersion(), ISMVocabulary.CVE_DECLASS_EXCEPTION, "50X1-HUM")));
		}
		
		/**
		 * Accessor for the version tested with this thread.
		 */
		public DDMSVersion getRandomVersion() {
			return _randomVersion;
		}

		/**
		 * Accessor for the version tested with this thread.
		 */
		public void setRandomVersion(int versionIndex) {
			String sVersion = (versionIndex % 2 == 0 ? "2.0" : "5.0");
			_randomVersion = DDMSVersion.getVersionFor(sVersion);
		}
	}
}
