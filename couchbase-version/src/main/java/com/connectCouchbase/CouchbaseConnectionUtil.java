package com.connectCouchbase;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

public class CouchbaseConnectionUtil {
	
	private static Logger logger = LoggerFactory.getLogger(CouchbaseConnectionUtil.class);
	
	private static CouchbaseConnectionService couchbaseService = CouchbaseConnectionService.getInstance();
	private static final String BUCKET_NAME = "dpas_data";
	
	public static List<JsonObject> queryByType(String type) {
				
		N1qlQuery whereQuery = N1qlQuery.simple("SELECT * FROM " + BUCKET_NAME + " WHERE type_= '" + type.trim() + "' LIMIT 1");
		
		//System.out.println(whereQuery.n1ql());
		N1qlQueryResult queryResult = couchbaseService.getBucket().query(whereQuery);
		
		if (queryResult == null)
			return null;
		
		List<JsonObject> resultList = new ArrayList<>();
		System.out.println(resultList);
		
		for (N1qlQueryRow result : queryResult) {
			resultList.add((JsonObject) result.value().get(BUCKET_NAME));
		}
		logger.info("Documents returned in array");
		return resultList;
	}	
	
	public static void main(String[] args) {

		System.out.println(queryByType("user"));
		CouchbaseConnectionService.closeConnection();
	}
}
