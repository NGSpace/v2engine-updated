package io.github.ngspace.v2engine.v2runtime.values.modifiable;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class V2DynamicVar extends AV2Value {
	private V2Runtime runtime;

	public V2DynamicVar(String value, V2Runtime runtime, int line, int charpos) {
		super(line, charpos, value.toLowerCase(), runtime.compiler);
		this.runtime = runtime;
	}
	
	@Override public Object get() throws CompileException {
		return runtime.getVariable(value);
	}
	
	@Override public boolean hasValue() {
		return !(runtime.getScoped(value)==null&&compiler.get(value)==null);
	}

	@Override public void setValue(AV2Compiler compiler, Object value) throws CompileException {
		compiler.put(this.value, value);
	}
	
	@Override public boolean isConstant() throws CompileException {return false;}

	@Override public double asDouble() throws CompileException {
		if (!hasValue()) throw new CompileException('"' + value + "\" has no set value!",line,charpos);
		return super.asDouble();
	}
}
