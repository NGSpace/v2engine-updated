package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.abstractions.ATextCompiler.CharPosition;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.compilers.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class ForV2RuntimeElement extends AV2RuntimeElement {

	private String variablename;
	private AV2Value condition;
	
	public ForV2RuntimeElement(String variablename, String value, String instructions,
			AV2Compiler compiler, V2Runtime parentRuntime, CharPosition charPosition, String filename)
					throws CompileException {
		this.variablename = variablename;
		this.condition = compiler.getV2Value(parentRuntime, value, charPosition.line, charPosition.charpos);
		this.nestedRuntime = compiler.buildRuntime(instructions, new CharPosition(charPosition.line, 1),
				filename, parentRuntime);
	}
	
	@Override
	public boolean execute(CompileState compileState, StringBuilder builder) throws CompileException {
		if (condition.get() instanceof Iterable<?> iterable) {
			for (Object val : iterable) {
				nestedRuntime.putScoped(variablename, val);
				CompileState res = nestedRuntime.execute();
				if (res.hasBroken) break;
				if (res.hasReturned) compileState.setReturnValue(res.returnValue);
			}
		}
		return true;
	}
}