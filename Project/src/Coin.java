import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;


public final class Coin extends GameObject implements Collectable, Collidable{
	private final Duration frames;
	private final int value;
	private boolean collected;
	
	Coin (float[] pos) {
		super(pos, new float[]{0F, 0F}, new float[]{0F, 0F}, 1, false, false, "images/coin.png", new float[]{30, 30});
		frames = new Duration(500);
		value = 1;
		collected = false;
	}
	
	Coin (float[] pos, int val) {
		super(pos, new float[]{0F, 0F}, new float[]{0F, 0F}, 1, false, false, "images/coin.png", new float[]{30, 30});
		frames = new Duration(500);
		value = val;
		collected = false;
	}
	
	public void get_collected (Hero hero) {
		assert (!collected);
		collected = true;
		
		//creates new ScaleTransition animation and sets node accordingly
		FadeTransition fade = new FadeTransition(frames);
		fade.setNode(this.getModel());
		fade.setFromValue(1);
		fade.setToValue(0);
		
		ScaleTransition scale = new ScaleTransition(frames);
		scale.setNode(this.getModel());
		scale.setByX(1.5);
		scale.setByY(1.5);
		
		fade.setOnFinished(e->this.remove());
		
		scale.play();
		fade.play();
	}
	
	@Override
	public void collide (GameObject obj) {
		if (!(obj instanceof Hero) || collected)
			return;
		get_collected((Hero)obj);
		((Hero)obj).add_coins(this.value);
	}
}
