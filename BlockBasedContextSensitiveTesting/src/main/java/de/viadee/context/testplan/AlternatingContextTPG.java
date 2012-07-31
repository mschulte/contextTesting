package de.viadee.context.testplan;

import java.util.ArrayList;
import java.util.List;

import de.viadee.context.Block;
import de.viadee.context.BlockContextTupel;
import de.viadee.context.Context;
import de.viadee.context.TestPlan;

public class AlternatingContextTPG implements TestPlanGenerator {

	private final Context secondContext;
	private final Context firstContext;

	public AlternatingContextTPG(final Context firstContext, final Context secondContext) {
		this.firstContext = firstContext;
		this.secondContext = secondContext;
	}

	public List<TestPlan> generate(final List<Block> list) {
		List<TestPlan> result = new ArrayList<TestPlan>();
		for (Block block : list) {
			if (block.getName() == null) {
				block.setName("Block " + (list.indexOf(block) + 1));
			}
		}
		TestPlan tp = new TestPlan();
		StringBuilder testPlanName = new StringBuilder();

		for (Block block : list) {
			if ((list.indexOf(block) % 2) == 0) {
				tp.addBlockContextTupel(new BlockContextTupel(block, firstContext));
				testPlanName.append(block.getName() + "/" + firstContext + " | ");
			} else {
				tp.addBlockContextTupel(new BlockContextTupel(block, secondContext));
				testPlanName.append(block.getName() + "/" + secondContext + " | ");
			}
		}
		String name = testPlanName.toString();
		tp.setName(name);
		result.add(tp);
		return result;
	}

}
