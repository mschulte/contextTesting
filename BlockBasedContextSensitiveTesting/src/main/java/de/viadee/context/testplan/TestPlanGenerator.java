package de.viadee.context.testplan;

import java.util.List;

import de.viadee.context.Block;
import de.viadee.context.TestPlan;

public interface TestPlanGenerator {

	public List<TestPlan> generate(final List<Block> list);

}
