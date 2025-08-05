package io.github.ngspace.v2engine.v2runtime.functions;

import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class TestFunction implements IV2Function {

	@Override public Object execute(V2Runtime runtime, String functionName, AV2Value[] args, int line, int charpos)
			throws CompileException {
		return args[0].asString() + args[1].asString();
	}
	
}
