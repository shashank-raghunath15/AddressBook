/**
 * Database Util Class - connects to ElasticSearch
 */
package com.eaiti.addressBook.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Shashank Raghunath
 *
 */
public class DBUtil {

	private static RestHighLevelClient client;

	private DBUtil() {
	};

	public static RestHighLevelClient getClient() {
		if (client == null) {
			client = new RestHighLevelClient(RestClient
					.builder(new HttpHost(PropertyUtil.DB_HOST, PropertyUtil.DB_PORT, PropertyUtil.DB_PROTOCOL)));
		}
		return client;
	}

	@Override
	protected void finalize() throws Throwable {
		client.close();
	}
}
