package de.viadee.context.testplan;

import java.util.ArrayList;
import java.util.List;

import de.viadee.context.Block;
import de.viadee.context.BlockContextTupel;
import de.viadee.context.Context;
import de.viadee.context.TestPlan;

public class StaticContextTPG implements TestPlanGenerator {

	private final Context context;

	public StaticContextTPG(Context context) {
		this.context = context;
	}

	public List<TestPlan> generate(List<Block> list) {
		List<TestPlan> result = new ArrayList<TestPlan>();
		TestPlan tp = new TestPlan();
		for (Block block : list) {
			tp.addBlockContextTupel(new BlockContextTupel(block, context));
		}
		tp.setName("All Blocks " + context);
		result.add(tp);
		return result;
	}

}
