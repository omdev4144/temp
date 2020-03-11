package com.connectCouchbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class CouchbaseConnectionService {

	private static Logger logger = LoggerFactory.getLogger(CouchbaseConnectionService.class);
	private static Properties prop = new Properties();
	private static InputStream propStream = CouchbaseConnectionService.class.getResourceAsStream("/config.properties");
	
	private static CouchbaseConnectionService couchbaseConnectionService = null;
	private CouchbaseEnvironment env = null;
	private Cluster cluster;
	private Bucket bucket;
	
	public CouchbaseConnectionService() {
		
		try {
			env = DefaultCouchbaseEnvironment.builder().connectTimeout(10000).build();
			prop.load(propStream);
			cluster = CouchbaseCluster.create(env, prop.getProperty("url"));
			bucket = cluster.openBucket("dpas_data",prop.getProperty("password"));
			prop.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		logger.info("Couchbase connection to bucket opened");
	}
	
	public static CouchbaseConnectionService getInstance() {
		if (couchbaseConnectionService == null) {
		couchbaseConnectionService = new CouchbaseConnectionService();
		}
		return couchbaseConnectionService;
	}
	
	public static boolean closeConnection() {
		logger.info("Closing Couchbase Bucket Connection");
		return couchbaseConnectionService.bucket.close();
	}
	
	public CouchbaseEnvironment getEnv() {
		return env;
	}

	public static void setEnv(CouchbaseEnvironment env) {
		couchbaseConnectionService.env = env;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public static void setCluster(Cluster cluster) {
		couchbaseConnectionService.cluster = cluster;
	}

	public Bucket getBucket() {
		return bucket;
	}

	public static void setBucket(Bucket bucket) {
		couchbaseConnectionService.bucket = bucket;
	}

}

