package com.hiddenswitch.fibers.gradle.test;

import co.paralleluniverse.fibers.Fiber;
import com.hiddenswitch.fibers.gradle.tests.ShouldBeInstrumented;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class InstrumentationTest {
	@Test
	public void testClassInstrumented() throws ExecutionException, InterruptedException {
		var fiber = new Fiber<>(() -> {
			var shouldBeInstrumented = new ShouldBeInstrumented();
			shouldBeInstrumented.test();
		});
		fiber.start();
		fiber.join();
	}
}
