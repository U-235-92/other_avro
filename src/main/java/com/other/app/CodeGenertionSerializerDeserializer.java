package com.other.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.other.avro.User;

public class CodeGenertionSerializerDeserializer {

	private static final String AVRO_FILE_PATH = "src/main/resources/avro/code-generate-users.avro";
	private static final Logger LOGGER = Logger.getLogger(CodeGenertionSerializerDeserializer.class.getName());
	
	public void serialize() throws IOException {
		DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(datumWriter);
		List<User> users = getUserList();
		dataFileWriter.create(users.get(0).getSchema(), new File(AVRO_FILE_PATH));
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

	public void deserialize() throws IOException {
		DatumReader<User> datumReader = new SpecificDatumReader<>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(new File(AVRO_FILE_PATH), datumReader);
		while(dataFileReader.hasNext()) {
			User user = dataFileReader.next();
			LOGGER.info(user.toString());
		}
		LOGGER.info("[CodeGenertionSerializerDeserializer] All the users deserialized!");
	}

}
