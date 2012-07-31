package de.viadee.context.testplan;

import java.util.ArrayList;
import java.util.List;

import de.viadee.context.Block;
import de.viadee.context.BlockContextTupel;
import de.viadee.context.Context;
import de.viadee.context.TestPlan;

public class BinaryPermutationTPG implements TestPlanGenerator {

	private final Context standardContext;
	private final Context alterContext;

	public BinaryPermutationTPG(final Context standardContext, final Context alterContext) {
		this.standardContext = standardContext;
		this.alterContext = alterContext;
	}

	public List<TestPlan> generate(final List<Block> list) {
		List<TestPlan> result = new ArrayList<TestPlan>();
		for (Block block : list) {
			if (block.getName() == null) {
				block.setName("Block " + (list.indexOf(block) + 1));
			}
		}

		for (int i = 0; i < list.size()+1; i++) {
			TestPlan tp = new TestPlan();
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
					tp.addBlockContextTupel(new BlockContextTupel(list.get(j), alterContext));
					break;
				} else {
					tp.addBlockContextTupel(new BlockContextTupel(list.get(j), standardContext));
				}
			}
			if (i == 0) {
				String alteredBlockName = list.get(i).getName();
				tp.setName("Execute " + alteredBlockName + " in Context " + alterContext + ", stop afterwards");
			} else if (i == list.size()){
				tp.setName("Execute all blocks in Context "+standardContext);
			} else {
				String alteredBlockName = list.get(i).getName();
				tp.setName("Until " + alteredBlockName + " execute in Context " + standardContext + ", then Context " + alterContext + " and stop after "
						+ alteredBlockName);
			}
			result.add(tp);
		}
		return result;
	}

}
