package de.viadee.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import de.viadee.context.changer.Contexter;
import de.viadee.context.testplan.TestPlanGenerator;

public abstract class ContextMutationTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

	private Contexter mContexter;
	private Solo solo;
	private final int currentTestPlanIndex;
	public List<TestPlan> testPlans;
	private TestPlanGenerator generator;

	public ContextMutationTestCase(final Class<T> activity, final int testPlanNumber, final TestPlanGenerator generator) {
		super(activity);
		this.currentTestPlanIndex = testPlanNumber;
		this.generator = generator;
		getTestPlans();
	}

	@Override
	public void setUp() {
		mContexter = new Contexter(getInstrumentation());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	// only one test to execute
	public void executeTestPlan() throws Exception {
		TestPlan testPlan = testPlans.get(currentTestPlanIndex);
		testPlan.setContexter(getContexter());
		testPlan.execute();
		phone().finishOpenedActivities();
	}

	protected abstract List<Block> getBlocks();

	public List<TestPlan> getTestPlans() {
		this.testPlans = generator.generate(getBlocks());
		return this.testPlans;
	}

	public Contexter getContexter() {
		return mContexter;
	}

	public Solo phone() {
		return solo;
	}

	public boolean hasMoreTestPlans() {
		return (currentTestPlanIndex < testPlans.size());
	}

	@Override
	protected void runTest() throws Throwable {
		String fName = "executeTestPlan";
		assertNotNull(fName);
		Method method = null;
		try {
			// use getMethod to get all public inherited
			// methods. getDeclaredMethods returns all
			// methods of this class but excludes the
			// inherited ones.
			method = getClass().getMethod(fName, (Class[]) null);
		} catch (NoSuchMethodException e) {
			fail("Method \"" + fName + "\" not found");
		}

		if (!Modifier.isPublic(method.getModifiers())) {
			fail("Method \"" + fName + "\" should be public");
		}

		runMethod(method, 1);
	}

	private void runMethod(final Method runMethod, final int tolerance) throws Throwable {
		Throwable exception = null;

		int runCount = 0;
		do {
			try {
				runMethod.invoke(this, (Object[]) null);
				exception = null;
			} catch (InvocationTargetException e) {
				e.fillInStackTrace();
				exception = e.getTargetException();
			} catch (IllegalAccessException e) {
				e.fillInStackTrace();
				exception = e;
			} finally {
				runCount++;
			}
		} while ((runCount < tolerance) && (exception != null));

		if (exception != null) {
			throw exception;
		}
	}

	public void abortCurrentTestPlan() throws StopTestPlanExecutionException {
		throw new StopTestPlanExecutionException();
	}

}
