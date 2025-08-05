package io.github.ngspace.v2engine.compilers;

import java.util.Arrays;

import io.github.ngspace.v2engine.compilers.abstractions.AV2Compiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;
import io.github.ngspace.v2engine.utils.HudderUtils;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.BreakV2RuntimeElement;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.ForV2RuntimeElement;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.IfV2RuntimeElement;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.ReturnV2RuntimeElement;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.VariableV2RuntimeElement;
import io.github.ngspace.v2engine.v2runtime.runtime_elements.WhileV2RuntimeElement;

public class V2EngineCompiler extends AV2Compiler {
	public static final int TEXT_STATE = 0;
	public static final int VARIABLE_STATE = 1;
	public static final int HASHTAG_STATE = 2;

	@Override public V2Runtime buildRuntime(String text, CharPosition charPosition, String filename, V2Runtime scope)
			throws CompileException {
		V2Runtime runtime = new V2Runtime(this, scope);
		
		StringBuilder elemBuilder = new StringBuilder();

		int savedind = 0;
		
		byte compileState = TEXT_STATE;

		for (int ind = 0;ind<text.length();ind++) {
			char c = text.charAt(ind);
			switch (compileState) {
				case TEXT_STATE: {
					switch (c) {
						case ' ','\t','\n','\r': break;
						case '#':
							compileState = HASHTAG_STATE;
							savedind = ind;
							break;
						default: 
							compileState = VARIABLE_STATE;
							ind--;
							savedind = ind;
							break;
					}
					break;
				}
				case VARIABLE_STATE: {
					if (c=='"') {
						char prevchar = '\\';
						for (;ind<text.length();ind++) {
							c = text.charAt(ind);
							if (prevchar!='\\'&&c=='"')
								break;
							elemBuilder.append(c);
							prevchar = c;
						}
					}
					if (c==';') {
						var pos = getPosition(charPosition, savedind, text);
						var str = elemBuilder.toString();
						if ("break".equalsIgnoreCase(str.trim())) {
							runtime.addRuntimeElement(new BreakV2RuntimeElement());
						} else if (str.trim().startsWith("return ")) {
							runtime.addRuntimeElement(new ReturnV2RuntimeElement(str.trim().substring(7), this,
									runtime, pos.line, pos.charpos));
						} else if (str.trim().startsWith("import ")) {
							runtime.importFile(str.trim().substring(7), pos.line, pos.charpos);
						} else {
							runtime.addRuntimeElement(new VariableV2RuntimeElement(str, this,
									runtime, pos.line, pos.charpos));
						}
						elemBuilder.setLength(0);
						compileState = TEXT_STATE;
						break;
					}
					elemBuilder.append(c);
					break;
				}
				case HASHTAG_STATE: {
					/**
					 * 0x0 - condition
					 * 0x1 - if
					 * 0x2 - while
					 * 0x3 - def (functions and methods)
					 * 0x5 - class //Not implemented
					 */
					byte command = 0x0;
					compileState = TEXT_STATE;
					for (;ind<text.length();ind++) {
						if ((c = text.charAt(ind))=='\n') break;
						if (command==0) {
							if(c==' '&&elemBuilder.toString().equals("while")) {command=0x2;}
							else if(c==' '&&elemBuilder.toString().equals("if")) {command=0x1;}
							else if(c==' '&&elemBuilder.toString().equals("def")){command=0x3;}
							else if(c==' '&&elemBuilder.toString().equals("for")){command=0x4;}
							if (command!=0x0) {elemBuilder.setLength(0);continue;}
						}
						elemBuilder.append(c);
					}
					String cond = elemBuilder.toString();
					elemBuilder.setLength(0);
					StringBuilder instructions = new StringBuilder();
					
					if (ind+1<text.length()&&(text.charAt(ind+1)=='\t'||text.charAt(ind+1)==' ')) {
						
						ind++;
						String initalIndent = checkIndentation(text,ind);
						
						for (;ind<text.length();ind++) {
							if (ind+1<text.length()) {
								String indent = checkIndentation(text,ind);
								if (indent.startsWith(initalIndent)) {
									ind+=initalIndent.length();
									for (;ind<text.length();ind++) {
										c = text.charAt(ind);
										instructions.append(c);
										if (c=='\n') break;
									}
								} else {
									break;
								}
							}
							
						}
						ind--;
					}
					String cmds = instructions.toString();
					CharPosition pos = getPosition(charPosition, savedind+1, "\n"+text);
					
					switch (command) {
						case 0x4: {
							String[] split = cond.split(" in ", 2);
							String variablename = split[0];
							String value = split[1];
							elemBuilder.setLength(0);
							runtime.addRuntimeElement(new ForV2RuntimeElement(variablename,value,cmds,this,
									runtime,pos,filename));
							break;
						}
						case 0x3: {
							String[] builder = HudderUtils.processParemeters(cond);
							String name = builder[0];
							String[] args = Arrays.copyOfRange(builder, 1, builder.length);
							defineFunctionOrMethod(cmds,args,name,pos,filename);
							elemBuilder.setLength(0);
							break;
						}
						case 0x2: {
							runtime.addRuntimeElement(new WhileV2RuntimeElement(cond, cmds, this, runtime,
									getPosition(charPosition, savedind+1, "\n"+text),filename));
							break;
						}
						default://0x0 or 0x1
							runtime.addRuntimeElement(new IfV2RuntimeElement(cond, cmds, this, runtime,
									getPosition(charPosition, savedind+1, "\n"+text),filename));
							break;
					}
					break;
				}
				default: throw new CompileException("Unknown compile state: " + compileState);
			}
		}
		
		if (compileState!=0) throw new CompileException(getCompilerErrorMessage(compileState));
		
		return runtime;
	}

	private String checkIndentation(String text, int index) {
		StringBuilder b = new StringBuilder();
		for (;index<text.length();index++) {
			char c = text.charAt(index);
			if (!(c==' '||c=='\t')) break;
			b.append(c);
		}
		return b.toString();
	}

	public String getCompilerErrorMessage(int compileState) {
		StringBuilder strb = new StringBuilder();
		strb.append(switch(compileState) {
			case VARIABLE_STATE -> "Expected ';'";
			case HASHTAG_STATE -> "Expected end of HASHTAG_STATE";
			default -> "An unknown error has occurred";
		});
		return strb.toString();
	}
}
