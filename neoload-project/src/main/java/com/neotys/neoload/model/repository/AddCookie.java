package com.neotys.neoload.model.repository;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableAddCookie.class)
@Deprecated
public interface AddCookie extends Element {
	String getCookieName();
	String getCookieValue();
	Server getServer();
	Optional<String> getExpires();
	Optional<String> getPath();
}
