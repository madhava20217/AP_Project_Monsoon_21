import javafx.scene.Node;

public class Hero extends GameObject implements Collidable{

  private Weapon[] available_weapons;
  private Weapon current_weapon;
  private Helmet current_helmet;
  private boolean is_alive;
  private boolean resurrected;
  private GameInstance current_game;
	private int moveCnt;
	private boolean CROSSING;
	private int distance;
  Hero(Node _model, float[] v, float[] a, float m, boolean g, boolean t){
    super(_model, v, a, m, g, t);
		moveCnt = 0;
		CROSSING = false;
  }
	
	public int getDistance () {
		return distance;
	}
	
	public void launch(){
		if(moveCnt >= 2 || (Math.abs(get_vel(0)) > 1)) return;
		moveCnt++;
		set_vel(0, 10);
	}
	
	@Override
	public void move(){
		float[] pos = getPos();
		float[] vel = getVel();
		decelerate(0,0.4F); // air resistance
		
		if (CROSSING) { // after crossing over a co-ordinate
			CROSSING = false;
			if (vel[0] < 9.5) {
				decelerate(0, 20F);
			}
		}
		else if (((vel[0] + pos[0]) % 100 < 50 && pos[0] % 100 > 50) && vel[0] > 0){ // crossing LTR
			if (vel[0] < 9.5) { // with less than launch velocity
				CROSSING = true;
				distance++;
				set_vel(0, 100 - pos[0] % 100); // clip to co-ordinate
			}
		} else if (((vel[0] + pos[0]) % 100 > 50 && pos[0] % 100 < 50) && vel[0] < 0){ // crossing RTL
			CROSSING = true;
			// distance--;  //TODO: check this more properly
			set_vel(0, - pos[0] % 100); // clip to co-ordinate
		}
		super.move();
		Node model = getModel();
		if(model.getTranslateX() > 0){
			GameController.panCam((float)(model.getTranslateX()/3));
		}
	}
	
	@Override
	public void bounce(GameObject other, float e, float x_overlap, float y_overlap){
		// TODO: make this apply for platforms ONLY - use if other instanceOf platform
		moveCnt = 0;
		super.bounce(other, e, x_overlap, y_overlap);
	}
	
  @Override
  public void collide(Collidable other) {
		
  }

  public void add_coins(int coin_count){
      //todo method
  }
  public void die(){
      //todo method
  }
  public Helmet get_helmet(){
      //todo method
      return null;
  }
  public boolean is_alive(){
      return is_alive;
  }
  public void resurrect(){
      //todo method
  }
  public void equip_helmet(Helmet helm){
      //todo method
  }
  public void equip_weapon(Weapon weapon){
      //todo method
  }
}
