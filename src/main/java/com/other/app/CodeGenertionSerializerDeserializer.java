package com.other.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.other.avro.Color;
import com.other.avro.Figure;
import com.other.avro.Point;
import com.other.avro.User;

public class CodeGenertionSerializerDeserializer {

	private static final String AVRO_USER_FILE_PATH = "src/main/resources/avro/code-generate-users.avro";
	private static final String AVRO_FIGURE_FILE_PATH = "src/main/resources/avro/code-generate-figures.avro";
	private static final String AVRO_CAR_FILE_PATH = "src/main/resources/avro/code-generate-cars.avro";
	private static final Logger LOGGER = Logger.getLogger(CodeGenertionSerializerDeserializer.class.getName());
	
	public void serializeUsers() throws IOException {
		DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(datumWriter);
		List<User> users = getUserList();
		dataFileWriter.create(users.get(0).getSchema(), new File(AVRO_USER_FILE_PATH));
		users.forEach(user -> {
			try {
				dataFileWriter.append(user);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		dataFileWriter.close();
		LOGGER.info("[CodeGenertionSerializerDeserializer]: All the users serialized!");
	}

	private List<User> getUserList() {
		List<User> users = new ArrayList<User>();
		User user1 = new User("User1", 512, null);
		User user2 = User.newBuilder().setName("User2").setFavoriteColor("Blue").setFavoriteNumber(null).build();
		User user3 = new User();
		user3.setName("User3");
		user3.setFavoriteNumber(256);
		user3.setFavoriteColor("White");
		users.add(user1);
		users.add(user2);
		users.add(user3);
		return users;
	}

	public void deserializeUsers() throws IOException {
		DatumReader<User> datumReader = new SpecificDatumReader<>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(new File(AVRO_USER_FILE_PATH), datumReader);
		while(dataFileReader.hasNext()) {
			User user = dataFileReader.next();
			LOGGER.info(user.toString());
		}
		LOGGER.info("[CodeGenertionSerializerDeserializer] All the users deserialized!");
	}
	
	public void serializeFigure() throws IOException {
		Figure triangle = Figure.newBuilder()
				.setId(1)
				.setColor(Color.RED)
				.setPoints(List.of(new Point(10, 10), new Point(20, 10), new Point(20, 20)))
				.build();
		DatumWriter<Figure> datumWriter = new SpecificDatumWriter<>(Figure.class);
		DataFileWriter<Figure> dataFileWriter = new DataFileWriter<>(datumWriter);
		dataFileWriter.create(triangle.getSchema(), new File(AVRO_FIGURE_FILE_PATH));
		dataFileWriter.append(triangle);
		dataFileWriter.close();
		LOGGER.info("[CodeGenertionSerializerDeserializer] All the figures serialized!");
	}
	
	public void deserializeFigure() throws IOException {
		DatumReader<Figure> datumReader = new SpecificDatumReader<>(Figure.class);
		DataFileReader<Figure> dataFileReader = new DataFileReader<Figure>(new File(AVRO_FIGURE_FILE_PATH), datumReader);
		while(dataFileReader.hasNext()) {
			Figure figure = dataFileReader.next();
			LOGGER.info(figure.toString());
		}
		LOGGER.info("[CodeGenertionSerializerDeserializer] All the figures deserialized!");
	}

	public void serializeCar() throws IOException {
		Schema carSchema = SchemaBuilder
				.record("Car")
				.namespace("com.other.app")
				.fields()
				.requiredString("name")
				.endRecord();
		Car bmw = new Car("E12");
		DatumWriter<Car> datumWriter = new SpecificDatumWriter<Car>(Car.class);
		DataFileWriter<Car> dataFileWriter = new DataFileWriter<Car>(datumWriter);
		dataFileWriter.create(carSchema, new File(AVRO_CAR_FILE_PATH));
		dataFileWriter.append(bmw);
		dataFileWriter.close();
	}
	
	public void deserializeCar() {
		
	}
}
