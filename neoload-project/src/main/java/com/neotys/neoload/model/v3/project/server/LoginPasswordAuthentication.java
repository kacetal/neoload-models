package com.neotys.neoload.model.v3.project.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface LoginPasswordAuthentication extends Authentication {

	String BASIC_AUTHENTICATION 		= "basic_authentication";
	String NEGOTIATE_AUTHENTICATION 	= "negotiate_authentication";
	String NTLM_AUTHENTICATION 			= "ntlm_authentication";

	String LOGIN = "login";
	String PASSWORD = "password";

	@JsonProperty(LOGIN)
	@RequiredCheck(groups = {NeoLoad.class})
	String getLogin();

	@JsonProperty(PASSWORD)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPassword();
}
