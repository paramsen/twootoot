package se.amsen.par.twootoot.source.twitter;

import android.util.Pair;

import se.amsen.par.testlib.testlib.UnitTestUtil;

/**
 * @author params on 03/11/15
 */
public class SharedStorageSourceTest extends UnitTestUtil {
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