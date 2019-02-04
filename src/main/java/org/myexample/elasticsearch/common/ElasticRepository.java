package org.myexample.elasticsearch.common;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticRepository {

	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public <T> T save(T t) {
		// t.setId(UUID.randomUUID().toString());
		Map<String, Object> dataMap = objectMapper.convertValue(t, Map.class);
		IndexRequest indexRequest = new IndexRequest(getIndex(t), getType(t), UUID.randomUUID().toString())
				.source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
		return t;
	}

	public <T> T update(T t) {
		return t;
	}

	public <T> T delete(T t) {
		return t;
	}

	public <T> T retrieve(T t, String id) {
		GetRequest getRequest = new GetRequest(getIndex(t), getType(t), id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		String json = getResponse.getSourceAsString();

		Reader reader = new StringReader(json);

		Class c = t.getClass();
		// T returnType = (T) objectMapper.readValue(reader, t.getClass());
		return t;
	}

	public <T> String getIndex(T t) {
		Class c = t.getClass();

		Index index = (Index) c.getAnnotation(Index.class);

		return index.value().toUpperCase();
	}

	public <T> String getType(T t) {
		Class c = t.getClass();

		Type type = (Type) c.getAnnotation(Type.class);

		return type.value().toUpperCase();
	}

}
