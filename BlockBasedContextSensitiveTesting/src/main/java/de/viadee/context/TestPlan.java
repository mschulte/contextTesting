package de.viadee.context;

import java.util.ArrayList;

import de.viadee.context.changer.Contexter;

import android.util.Log;

public class TestPlan {

	private Contexter mContexter;
	private String name;

	private final ArrayList<BlockContextTupel> blockContext;

	public TestPlan() {
		this.blockContext = new ArrayList<BlockContextTupel>();
	}

	public void addBlockContextTupel(BlockContextTupel tupel) {
		blockContext.add(tupel);
	}

	public void setContexter(Contexter contexter) {
		mContexter = contexter;
	}

	public void execute() throws Exception {
		Log.d("TestPlan", "execute TestPlan");
		for (BlockContextTupel entry : blockContext) {
			Context context = entry.getContext();
			Block block = entry.getBlock();

			mContexter.establishContext(context);
			try {
				block.operationBlock();
				block.assertion(context);
			} catch (StopTestPlanExecutionException e){
				return;
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
