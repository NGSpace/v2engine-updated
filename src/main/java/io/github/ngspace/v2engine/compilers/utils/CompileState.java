package io.github.ngspace.v2engine.compilers.utils;

public class CompileState {
	
	public boolean hasBroken = false;
	public Object returnValue;
	public boolean hasReturned;
	
	public void setReturnValue(Object value) {hasReturned = true;returnValue = value;}
	
}
