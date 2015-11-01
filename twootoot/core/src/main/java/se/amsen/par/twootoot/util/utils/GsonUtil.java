package se.amsen.par.twootoot.util.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import se.amsen.par.twootoot.util.annotation.Exclude;

/**
 * Static Gson utils
 *
 * @author params on 29/10/15
 */
public class GsonUtil {
	public static Gson gson;

	static {
		gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
	}

	public static JsonObject merge(JsonObject in1, JsonObject in2) {
		JsonObject out = new JsonObject();

		for(Map.Entry<String, JsonElement> entry : in1.entrySet()) {
			out.add(entry.getKey(), entry.getValue());
		}

		for(Map.Entry<String, JsonElement> entry : in2.entrySet()) {
			out.add(entry.getKey(), entry.getValue());
		}

		return out;
	}

	/**
	 * Shallow lexicographical sort of entries in JsonObject
	 */
	public static JsonObject lexSort(JsonObject in) {
		JsonObject out = new JsonObject();
		List<String> keys = new ArrayList<>(in.entrySet().size());

		for (Map.Entry<String, JsonElement> entry : in.entrySet()) {
			keys.add(entry.getKey());
		}

		Collections.sort(keys);

		for(String key : keys) {
			out.add(key, in.get(key));
		}

		return out;
	}


	/**
	 * Util for retrieving a deep copy of T for type E
	 */
	public static <T, E> E deepCopy(T t, Class<E> type) {
		return gson.fromJson(gson.toJson(t), type);
	}

	/**
	 * ExclusionStrategy for Gson, excludes everything annotated with @Exclude, more declarative
	 * than using transient keyword.
	 */
	private static class AnnotationExclusionStrategy implements ExclusionStrategy {
		public boolean shouldSkipClass(Class<?> clazz) {
			return clazz.getAnnotation(Exclude.class) != null;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(Exclude.class) != null;
		}
	}
}
