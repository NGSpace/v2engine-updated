package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.abstractions.ATextCompiler.CharPosition;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.compilers.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class IfV2RuntimeElement extends AV2RuntimeElement {

	private AV2Value condition;

	public IfV2RuntimeElement(String condition, String cmds, AV2Compiler compiler, V2Runtime runtime,
			CharPosition charPosition, String filename) throws CompileException {
		this.nestedRuntime = compiler.buildRuntime(cmds, new CharPosition(charPosition.line, 1), filename, runtime);
		this.condition = compiler.getV2Value(nestedRuntime, condition, charPosition.line, charPosition.charpos);
	}
	
	@Override public boolean execute(CompileState meta, StringBuilder builder) throws CompileException {
		if (condition.asBoolean()) {
			CompileState res = nestedRuntime.execute();
			if (res.hasReturned) meta.setReturnValue(res.returnValue);
			if (res.hasBroken) return false;
		}
		return true;
	}
}
