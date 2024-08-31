package com.other.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.EnumSymbol;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

public class NoCodeGenertionSerializerDeserializer {

	private static final String AVRO_FILE_PATH = "src/main/resources/avro/no-code-generate-users.avro";
	private static final String SCHEMA_FILE_PATH = "src/main/resources/avro/user-schema.avsc";
	private static final Logger LOGGER = Logger.getLogger(CodeGenertionSerializerDeserializer.class.getName());

	public void serializeUsers() throws IOException {
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

	public void deserializeUsers() throws IOException {
		Schema schema = new Schema.Parser().parse(new File(SCHEMA_FILE_PATH));
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File(AVRO_FILE_PATH),
				datumReader);
		while (dataFileReader.hasNext()) {
			GenericRecord user = dataFileReader.next();
			LOGGER.info(user.toString());
		}
		LOGGER.info("[NoCodeGenertionSerializerDeserializer] All the users deserialized!");
	}

	public byte[] serializeFigure() throws IOException {
		Schema figureSchema = getFigureSchema();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(figureSchema);
		Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
		GenericRecord triangle = makeTriangleRecord();
		datumWriter.write(triangle, encoder);
		encoder.flush();
		return out.toByteArray();
	}
	
	public void deserializeFigure(byte[] bytes) throws IOException {
		Schema schema = getFigureSchema();
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
		GenericRecord triangle = datumReader.read(null, decoder);
		LOGGER.info(triangle.toString());
	}
	
	private GenericRecord makeTriangleRecord() {
		Schema figureSchema = getFigureSchema();
		Schema figurePointSchema = getFigurePointSchema();
		GenericRecord trianglePointA = new GenericData.Record(figurePointSchema);
		trianglePointA.put("x", 10);
		trianglePointA.put("y", 10);
		GenericRecord trianglePointB = new GenericData.Record(figurePointSchema);
		trianglePointB.put("x", 20);
		trianglePointB.put("y", 10);
		GenericRecord trianglePointC = new GenericData.Record(figurePointSchema);
		trianglePointC.put("x", 10);
		trianglePointC.put("y", 20);
		List<GenericRecord> trianglePoints = List.of(trianglePointA, trianglePointB, trianglePointC);
		EnumSymbol triangleColor = new GenericData.EnumSymbol(getFigureColorSchema(), "BLUE");
		GenericRecord triangle = new GenericData.Record(figureSchema);
		triangle.put("id", 1);
		triangle.put("points", trianglePoints);
		triangle.put("color", triangleColor);
		return triangle;
	}
	
	private Schema getFigureSchema() {
		Schema figure = SchemaBuilder
				.record("Figure")
				.namespace("com.other.app")
				.fields()
				.requiredInt("id")
				.name("points").type(getFigurePointsSchema()).noDefault()
				.name("color").type(getFigureColorSchema()).noDefault()
				.endRecord();
		return figure;
	}
	
	private Schema getFigurePointsSchema() {
		Schema figurePoints = SchemaBuilder
				.array()
				.items(getFigurePointSchema());
		return figurePoints;
	}
	
	private Schema getFigurePointSchema() {
		Schema figurePoint = SchemaBuilder
				.record("Point")
				.namespace("com.other.app")
				.fields()
				.requiredInt("x")
				.requiredInt("y")
				.endRecord();
		return figurePoint;
	}
	
	private Schema getFigureColorSchema() {
		Schema figureColor = SchemaBuilder.enumeration("Color").symbols("RED", "GREEN", "BLUE");
		return figureColor;
	}
}
