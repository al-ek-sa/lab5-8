package edu.itmo.piikt.client.webSocket;

import com.fasterxml.jackson.databind.JsonNode;

public sealed interface CollectionUpdate {
	record Add(JsonNode worker) implements CollectionUpdate {
	}
	record Update(JsonNode worker) implements CollectionUpdate {
	}
	record Remove(String id) implements CollectionUpdate {
	}
	record Clear() implements CollectionUpdate {
	}
	record FullSync(JsonNode workers) implements CollectionUpdate {
	}
}
