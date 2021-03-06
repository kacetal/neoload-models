package com.neotys.neoload.model.v3.project.scenario;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.scenario.Scenario;


public class ScenarioTest {
	@Test
	public void constants() {
		assertEquals("sla_profile", Scenario.SLA_PROFILE);
		assertEquals("populations", Scenario.POPULATIONS);
		assertEquals("apm_configuration", Scenario.APM);
		assertEquals("excluded_urls", Scenario.EXCLUDED_URLS);
		assertEquals("rendezvous_policies", Scenario.RENDEZVOUS_POLICIES);
		assertEquals("monitoring", Scenario.MONITORING);
		assertEquals("store_variables_for_raw_data", Scenario.STORE_VARIABLES);
	}
}
