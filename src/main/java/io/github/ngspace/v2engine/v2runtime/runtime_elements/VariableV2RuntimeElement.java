package io.github.ngspace.v2engine.v2runtime.runtime_elements;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.compilers.utils.CompileState;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class VariableV2RuntimeElement extends AV2RuntimeElement {
	
	final AV2Value value;
	final AV2Compiler compiler;
	
	public VariableV2RuntimeElement(String value, AV2Compiler compiler, V2Runtime runtime, int line, int charpos) throws CompileException {
		this.compiler = compiler;
		this.value = compiler.getV2Value(runtime, value, line, charpos);
	}

	@Override public boolean execute(CompileState meta, StringBuilder builder) throws CompileException {
		Object val = value.get();
		if (val instanceof Number num&&num.doubleValue()==num.longValue()) val = num.longValue();
		builder.append(val);
		return true;
	}
}