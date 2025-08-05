package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.compilers.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class ReturnV2RuntimeElement extends AV2RuntimeElement {
	
	private AV2Value value;

	public ReturnV2RuntimeElement(String value, AV2Compiler compiler, V2Runtime runtime, int line, int charpos)
			throws CompileException {
		this.value = compiler.getV2Value(runtime, value, line, charpos);
	}
	
	@Override public boolean execute(CompileState compileState, StringBuilder builder) throws CompileException {
		compileState.setReturnValue(value.get());
		return false;
	}
	
	@Override public boolean returnsAValue() {return true;}
}
