import java.util.Timer;
import java.util.TimerTask;

public final class FallingPlatform extends GameObject implements Collidable{
	private final static int collapse_rate = 350;
	private final Platform[] submodels;
	private boolean collapsing;
	private static final float[] size = {71, 39};
	private final int components;          //number of components for the platform
	
	FallingPlatform (float[] pos) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 1000, false, false, null, new float[]{size[0] * 6, size[1]});
		components = 6;
		submodels = new Platform[6];
		collapsing = false;
		for (int i = 0; i < 6; i++) {
			submodels[i] = new Platform(new float[]{pos[0] + 71 * i, pos[1]}, "images/platform_falling.png", size);
		}
	}
	
	
	FallingPlatform (float[] pos, int num) {
		/*
		 * Different constructor for boss platform init, allows for better control over size.
		 */
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 1000, false, false, null, new float[]{size[0] * num, size[1]});
		components = num;
		submodels = new Platform[num];
		collapsing = false;
		for (int i = 0; i < num; i++) {
			submodels[i] = new Platform(new float[]{pos[0] + 71 * i, pos[1]}, "images/platform_falling.png", size);
		}
	}
	
	public Platform[] getSubmodels () {
		return submodels;
	}
	
	@Override
	public void collide (GameObject other) {
		if (other instanceof Hero)
			collapse();
	}
	
	public void collapse () {
		if (collapsing)
			return;
		collapsing = true;
		Timer t = new Timer();
		this.remove();
		t.schedule(
			new TimerTask(){
				private int count = 0;
				
				@Override
				public void run () {
					submodels[count++].setG(true);
					if (count == components)
						t.cancel();
				}
			}
			, 0, collapse_rate);
	}
	
	public boolean getCollapsing () {
		return this.collapsing;
	}
}