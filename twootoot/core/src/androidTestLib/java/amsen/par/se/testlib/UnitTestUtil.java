package amsen.par.se.testlib;

import android.test.InstrumentationTestCase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;

/**
 * @author params on 03/11/15
 */
public class UnitTestUtil extends InstrumentationTestCase {
	private static final Object lock = new Object();

	@Override protected void setUp() throws Exception {
		super.setUp();

		//Mockito fix for some devices
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
	}

	/**
	 * Unit test timeout for testing async Sources, see TwitterHttpSourceTest for usage
	 */
	public void timeout(final TimeoutCallback timeoutCallback, final TimeUnit unit, final int time) throws InterruptedException {

		new Thread(new Runnable() {
			@Override
			public void run() {
				long timeout = System.currentTimeMillis() + unit.toMillis(time);

				while(System.currentTimeMillis() < timeout && !timeoutCallback.isFinished.get()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}

				timeoutCallback.isFinished.set(true);
				synchronized (lock) {
					lock.notify();
				}
			}
		}).start();

		synchronized (lock) {
			lock.wait();
		}
	}

	public static class TimeoutCallback implements Callback {
		public final AtomicBoolean isFinished = new AtomicBoolean(false);
		public final AtomicReference<Result> ref = new AtomicReference<>(null);
		@Override
		public void onComplete(Result result) {
			synchronized (lock) {
				if(!isFinished.get()) {
					ref.set(result);
					isFinished.set(true);
				}
			}
		}
	}
}
