package io.github.ngspace.v2engine.v2runtime.values;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;

public interface IV2VariableParser {
	public AV2Value parse(V2Runtime runtime, String valuee, AV2Compiler comp, int line, int charpos)
			throws CompileException;
}
