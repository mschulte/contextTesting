package de.viadee.context;

import java.util.ArrayList;

public abstract class Block {

	private ArrayList<ContextAssertionTupel> contextAssertions = new ArrayList<ContextAssertionTupel>();
	private String name;

	public abstract void operationBlock() throws Exception;

	public abstract void defaultAssertionBlock() throws Exception;

	public void assertion(final Context context) throws Exception {
		if (hasAlternativeAssertionForContext(context)) {
			getAlternativeAssertionForContext(context).assertionBlock();
		} else {
			defaultAssertionBlock();
		}
	}

	public void addAlternativeAssertionBlock(final Context context, final Assertion assertion) {
		if (!contextAssertions.contains(context)) {
			contextAssertions.add(new ContextAssertionTupel(context, assertion));
		} else {
			throw new RuntimeException("Assertion already existing for given context, can't add two assertions");
		}
	}

	public Assertion getAlternativeAssertionForContext(final Context context) {
		for (ContextAssertionTupel tupel : contextAssertions) {
			if (tupel.getContext().equals(context)) {
				return tupel.getAssertion();
			}
		}
		return null;
	}

	private boolean hasAlternativeAssertionForContext(final Context context) {
		for (ContextAssertionTupel tupel : contextAssertions) {
			if (tupel.getContext().equals(context)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
