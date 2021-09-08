package com.hiddenswitch.fibers.gradle.tests;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import io.vertx.core.Handler;

public class ShouldBeInstrumented {
	public void test() throws SuspendExecution, InterruptedException {
		Fiber.sleep(10);
		shouldDetectSuspendable();
		SuspendableHandler v = new SuspendableHandler() {
			@Override
			@Suspendable
			public void handle(Void event) {
				try {
					Fiber.sleep(10);
				} catch (InterruptedException | SuspendExecution e) {
					e.printStackTrace();
				}
			}
		};
		v.handle((Void) null);
	}

	public interface SuspendableHandler extends Handler<Void> {
		@Override
		@Suspendable
		void handle(Void event);
	}

	public void shouldDetectSuspendable() {
		transitive();
	}

	@Suspendable
	public void transitive() {
		try {
			Fiber.sleep(10);
		} catch (InterruptedException | SuspendExecution e) {
			e.printStackTrace();
		}
	}
}
