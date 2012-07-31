package de.viadee.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import de.viadee.context.testplan.TestPlanGenerator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import android.util.Log;

public class MutationTestSuite extends TestSuite {

	private static final String TAG = "MutationTestSuite";
	private Class<? extends TestCase> testCaseClass;

	public MutationTestSuite mutate(final Class<? extends TestCase> testCaseClass) {
		this.testCaseClass = testCaseClass;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public TestSuite with(TestPlanGenerator generator) {
		setName(testCaseClass.getName());

		final Collection<TestCase> testCaseInstances = new ArrayList<TestCase>();
		try {
			Constructor<?> constr = testCaseClass.getConstructor(new Class[] { int.class, TestPlanGenerator.class });

			int i = 0;
			ContextMutationTestCase test = (ContextMutationTestCase) constr.newInstance(new Object[] { i, generator });
			while (test.hasMoreTestPlans()) {
				test.setName(((TestPlan) test.getTestPlans().get(i)).getName());
				testCaseInstances.add(test);
				test = (ContextMutationTestCase) constr.newInstance(new Object[] { ++i, generator });
			}
		} catch (final IllegalArgumentException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		} catch (InstantiationException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		} catch (SecurityException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			return warning(testCaseInstances, e);
		}

		for (final TestCase testCase : testCaseInstances) {
			addTest(testCase);
		}
		return this;
	}

	private TestSuite warning(final Collection<TestCase> testCaseInstances, final Exception e) {
		TestSuite result = new TestSuite();
		result.addTest(new TestCase(e.getMessage()) {

			@SuppressWarnings("unused")
			public void testWarning() {
				assertTrue(e.getMessage(), false);
			}
		});
		return result;
	}

}
