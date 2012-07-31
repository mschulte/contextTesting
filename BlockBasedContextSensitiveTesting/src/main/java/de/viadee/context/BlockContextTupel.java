package de.viadee.context;

public class BlockContextTupel {

	public final Block block;
	public final Context context;

	public BlockContextTupel(final Block block, final Context context) {
		this.block = block;
		this.context = context;
	}

	public Block getBlock() {
		return block;
	}

	public Context getContext() {
		return context;
	}

}
