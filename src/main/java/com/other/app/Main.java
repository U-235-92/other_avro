package com.other.app;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		CodeGenertionSerializerDeserializer codeGenertionSerializerDeserializer = new CodeGenertionSerializerDeserializer();
		codeGenertionSerializerDeserializer.serialize();
		codeGenertionSerializerDeserializer.deserialize();
		
		NoCodeGenertionSerializerDeserializer noCodeGenertionSerializerDeserializer = new NoCodeGenertionSerializerDeserializer();
		noCodeGenertionSerializerDeserializer.serialize();
		noCodeGenertionSerializerDeserializer.deserialize();
	}

}
