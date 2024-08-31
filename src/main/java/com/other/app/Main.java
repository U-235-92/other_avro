package com.other.app;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		CodeGenertionSerializerDeserializer codeGenertionSerializerDeserializer = new CodeGenertionSerializerDeserializer();
//		codeGenertionSerializerDeserializer.serializeUsers();
//		codeGenertionSerializerDeserializer.deserializeUsers();
		codeGenertionSerializerDeserializer.serializeFigure();
		codeGenertionSerializerDeserializer.deserializeFigure();
//		codeGenertionSerializerDeserializer.serializeCar(); //Throw exception because Avro can serialize only Avro objects not POJO
		
		NoCodeGenertionSerializerDeserializer noCodeGenertionSerializerDeserializer = new NoCodeGenertionSerializerDeserializer();
//		noCodeGenertionSerializerDeserializer.serializeUsers();
//		noCodeGenertionSerializerDeserializer.deserializeUsers();
		byte[] bytes = noCodeGenertionSerializerDeserializer.serializeFigure();
		noCodeGenertionSerializerDeserializer.deserializeFigure(bytes);
	}

}
