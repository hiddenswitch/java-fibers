package com.hiddenswitch.fibers.gradle.tests;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;

public class ShouldBeInstrumented {
	public void test() throws SuspendExecution, InterruptedException {
		Fiber.sleep(10);
	}
}
