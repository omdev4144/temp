package temp.omdev;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;

public class UpdateDocument {

	private static String documentId = "temp_123";
	private static String bucketName = "schema";
	private static String fileName = "allHabitPlan.json";

	public static void main(String... args) {

		if (args.length != 0) {
			switch (args.length) {

			case 1:
				bucketName = args[0];
				break;
			case 2:
				bucketName = args[0];
				fileName = args[1];
				documentId = args[1].split(".json", 2)[0];
				break;
			default:
				bucketName = args[0];
				fileName = args[1];
				documentId = args[2];
			}
		}
		Properties prop = new Properties();
		InputStream propStream = UpdateDocument.class.getResourceAsStream("/config.properties");
		InputStream inputStream = UpdateDocument.class.getResourceAsStream("/" + fileName);

		JSONParser jsonParser = new JSONParser();
		Logger logger = Logger.getLogger(UpdateDocument.class.getName());

		try {

			String data = convertInputStreamToString(inputStream);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
			prop.load(propStream);
			Cluster cluster = Cluster.connect(prop.getProperty("url"), prop.getProperty("username"),
					prop.getProperty("password"));
			Bucket bucket = cluster.bucket(bucketName);
			Collection collection = bucket.defaultCollection();
			GetResult getResult = collection.get(documentId);
			JsonObject content = getResult.contentAsObject();
			collection.upsert(documentId + "_backup", content);
			collection.upsert(documentId, jsonObject);
		} catch (IOException | NullPointerException e) {
			logger.log(Level.WARNING, ErrorMessage.FILE_NOT_FOUND_ERROR.getMessage());
		} catch (ParseException e) {
			logger.log(Level.WARNING, ErrorMessage.PARSE_ERROR.getMessage());
		} catch (DocumentNotFoundException e) {
			logger.log(Level.WARNING, ErrorMessage.DOCUMENT_NOT_FOUND_ERROR.getMessage());
		} catch (Exception e) {
			logger.log(Level.WARNING, ErrorMessage.DATABASE_ERROR.getMessage());
		}

	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}

		return result.toString(StandardCharsets.UTF_8.name());

	}
}