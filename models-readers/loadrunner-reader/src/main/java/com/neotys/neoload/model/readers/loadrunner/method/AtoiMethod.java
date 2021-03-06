package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.function.ImmutableAtoi;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;

public class AtoiMethod implements LoadRunnerMethod {
	
	private static AtomicInteger counter = new AtomicInteger(0); 

	public AtoiMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);		
		if(method.getParameters() == null || method.getParameters().size()!=1){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have a parameter");
			return Collections.emptyList();
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);	
		final String arg0 = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final List<String> args = ImmutableList.of(arg0);
		final String name = "atoi_" + counter.incrementAndGet();
		return ImmutableList.of(ImmutableAtoi.builder().name(name).args(args).returnValue(MethodUtils.getVariableSyntax(name)).build());
	}	
}	
