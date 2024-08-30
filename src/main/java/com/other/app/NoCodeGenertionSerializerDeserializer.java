package com.other.app;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

public class NoCodeGenertionSerializerDeserializer {

	private static final String AVRO_FILE_PATH = "src/main/resources/avro/no-code-generate-users.avro";
	private static final String SCHEMA_FILE_PATH = "src/main/resources/avro/user-schema.avsc";
	private static final Logger LOGGER = Logger.getLogger(CodeGenertionSerializerDeserializer.class.getName());

	public void serialize() throws IOException {
		Schema schema = new Schema.Parser().parse(new File(SCHEMA_FILE_PATH));
		GenericRecord user1 = new GenericData.Record(schema);
		user1.put("name", "Alyssa");
		user1.put("favorite_number", 256);
		GenericRecord user2 = new GenericData.Record(schema);
		user2.put("name", "Ben");
		user2.put("favorite_number", 7);
		user2.put("favorite_color", "red");
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		dataFileWriter.create(schema, new File(AVRO_FILE_PATH));
		dataFileWriter.append(user1);
		dataFileWriter.append(user2);
		dataFileWriter.close();
		LOGGER.info("[NoCodeGenertionSerializerDeserializer] All the users serialized!");
	}

	public void deserialize() throws IOException {
		Schema schema = new Schema.Parser().parse(new File(SCHEMA_FILE_PATH));
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File(AVRO_FILE_PATH), datumReader);
		while (dataFileReader.hasNext()) {
			GenericRecord user = dataFileReader.next();
			LOGGER.info(user.toString());
		}
		LOGGER.info("[NoCodeGenertionSerializerDeserializer] All the users deserialized!");
	}
}
