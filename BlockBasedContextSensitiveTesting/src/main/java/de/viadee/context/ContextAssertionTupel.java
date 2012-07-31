package de.viadee.context;

public class ContextAssertionTupel {

	private final Context context;
	private final Assertion assertion;

	public ContextAssertionTupel(final Context context, final Assertion assertion) {
		super();
		this.context = context;
		this.assertion = assertion;
	}

	protected Context getContext() {
		return context;
	}

	protected Assertion getAssertion() {
		return assertion;
	}

}
