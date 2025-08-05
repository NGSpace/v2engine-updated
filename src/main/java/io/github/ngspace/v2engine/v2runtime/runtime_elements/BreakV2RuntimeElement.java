package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.compilers.utils.CompileState;

public class BreakV2RuntimeElement extends AV2RuntimeElement {
	@Override public boolean execute(CompileState compileState, StringBuilder builder) throws CompileException {
		builder.setLength(0);
		return false;
	}
}
