package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;
import android.util.Pair;

/**
 * @author params on 03/11/15
 */
public class SharedStorageSourceTest extends InstrumentationTestCase {
	String key = "mockKey";
	SharedStorageSource<Pair<String, String>> storage;

	@Override protected void setUp() throws Exception {
		super.setUp();
		storage = new SharedStorageSource<>(getInstrumentation().getContext(), key);
		storage.invalidate();
	}

	public void testStoreAndGet() {
		storage.store(key, new Pair<>("first", "second"));

		Pair<String, String> pair = storage.getByKey(key).asSuccess().get();

		assertEquals("First is not correct", "first", pair.first);
		assertEquals("Second is not correct", "second", pair.second);
	}
}