package se.amsen.par.twootoot.util.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
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
	public static Gson twitterGson;

	static {
		twitterGson = new GsonBuilder()
				.setExclusionStrategies(new TwitterExclusionStrategy(Exclude.class))
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
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
		return twitterGson.fromJson(twitterGson.toJson(t), type);
	}

	/**
	 * ExclusionStrategy for Gson, excludes everything annotated with @Exclude, much more declarative
	 * than using Java transient keyword.
	 */
	public static class TwitterExclusionStrategy implements ExclusionStrategy {
		Class[] exclude;

		public TwitterExclusionStrategy(Class... exclude) {
			this.exclude = exclude;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			for(Class exclusion : exclude) {
				if(clazz.getAnnotation(exclusion) != null) {
					return true;
				}
			}

			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			for(Class exclusion : exclude) {
				if(f.getAnnotation(exclusion) != null) {
					return true;
				}
			}

			return false;
		}
	}
}
