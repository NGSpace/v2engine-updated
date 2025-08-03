package io.github.ngspace.hudder.compilers.utils.functionandconsumerapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.ngspace.hudder.compilers.abstractions.ATextCompiler;
import io.github.ngspace.hudder.compilers.utils.CompileException;
import io.github.ngspace.hudder.utils.ObjectWrapper;

/**
 * This is my attempt at unifying the Hudder and JavaScript compilers.<br>
 * 
 * Don't use this unless you know what you are doing.
 */
public class FunctionAndConsumerAPI {
	
	static FunctionAndConsumerAPI instance = new FunctionAndConsumerAPI();
	
	HashMap<BindableFunction, String[]> functions = new HashMap<BindableFunction, String[]>();
	HashMap<BindableConsumer, String[]> consumers = new HashMap<BindableConsumer, String[]>();
	
	List<Binder> binders = new ArrayList<Binder>();
	
	
	
	/**
	 * Applies registered consumers and functions to the provided binder as well as any that are added afterwards.
	 * @param binder
	 */
	public void applyFunctionsAndConsumers(Binder binder) {
		for (var cons : consumers.entrySet())
			binder.bindConsumer(cons.getKey(), cons.getValue());
		for (var func : functions.entrySet()) {
			binder.bindFunction(func.getKey(), func.getValue());
		}
		binders.add(binder);
	}
	
	
	
	public void registerFunction(BindableFunction func, String... names) {
		for (var binder : binders)
			binder.bindFunction(func, names);
		functions.put(func, names);
	}



	public void registerConsumer(BindableConsumer func, String... names) {
		for (var binder : binders) 
			binder.bindConsumer(func, names);
		consumers.put(func, names);
	}
	
	

	@FunctionalInterface public interface BindableFunction {
		public Object invoke(ATextCompiler comp, ObjectWrapper... args) throws CompileException;
	}
	@FunctionalInterface public interface BindableConsumer {
		public void invoke(ATextCompiler comp, ObjectWrapper... args) throws CompileException;
	}

	
	
	public interface Binder {
		public void bindConsumer(BindableConsumer cons, String... names);
		public void bindFunction(BindableFunction cons, String... names);
	}
	
	
	
	public static FunctionAndConsumerAPI getInstance() {return instance;}
}
