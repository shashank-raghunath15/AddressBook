/**
 * ContractDao class deals with elasticsearch api for data access and modification
 */
package com.eaiti.addressBook.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eaiti.addressBook.AddressBook;
import com.eaiti.addressBook.exception.InitializationException;
import com.eaiti.addressBook.util.DBUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Shashank Raghunath
 * 
 */
public class ContactDao {

	private RestHighLevelClient client;
	private static final String DOC = "contact";
	private static final String DOC_ID = "name";
	public static final Logger LOGGER = LoggerFactory.getLogger(AddressBook.class);
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public ContactDao() throws InitializationException {
		client = DBUtil.getClient();
		if (client == null) {
			throw new InitializationException("ElasticSearch Client is null");
		}
	}

	public Contact getContactByName(String name)
			throws ElasticsearchStatusException, JsonParseException, JsonMappingException, IOException {
		GetRequest getRequest = new GetRequest(DOC, DOC_ID, name);
		GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
		if (response.isExists())
			return mapper.readValue(response.getSourceAsString(), Contact.class);
		return null;
	}

	public List<Contact> getContactByQuery(int size, int offset, String query) throws IOException {
		SearchRequest request = new SearchRequest();
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(new QueryStringQueryBuilder(query));
		builder.size(size);
		builder.from(offset);
		request.source(builder);
		SearchResponse response = client.search(request, RequestOptions.DEFAULT);

		SearchHits hits = response.getHits();
		if (hits.totalHits == 0) {
			return null;
		}
		List<Contact> result = new ArrayList<>();
		for (SearchHit hit : hits) {
			result.add(mapper.readValue(hit.getSourceAsString(), Contact.class));
		}
		return result;
	}

	public String saveContact(Contact contact) throws IOException {
		IndexRequest indexRequest = new IndexRequest(DOC, DOC_ID, contact.getName());
		indexRequest.source(mapper.writeValueAsString(contact), XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
		return indexResponse.status().toString();
	}

	public String updateContact(Contact contact, String name)
			throws JsonParseException, JsonMappingException, IOException {
		UpdateRequest updateRequest = new UpdateRequest(DOC, DOC_ID, name).doc(mapper.writeValueAsString(contact),
				XContentType.JSON);
		UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
		return response.status().toString();
	}

	public String deleteContact(String name) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(DOC, DOC_ID, name);
		DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
		return response.status().toString();
	}
}
