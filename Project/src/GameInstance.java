import java.io.Serial;
import java.io.Serializable;
import java.util.*;

//TODO: improve resurrection method to take into account that the first jump can be on fallingplatform, otherwise it
// just spawns without any buffer.

public class GameInstance implements Serializable{
	private Hero hero;
	private LinkedHashMap<UUID, GameObject> gamemap;
	private int coin_count;
	private float panCam;
	
	@Serial
	private static final long serialVersionUID = 30;
	
	// STATE VARIABLES
	private boolean resurrection;  // has the hero resurrected yet?
	private boolean won;
	private static final int RESURRECTIONCCOST = 10;      //cost for resurrection
	
	// DESIGN VARIABLES
	private static final int BOSSPLATFORM = 22;        //boss platform's index in the platform array
	private static final int BOSSPLATFORMCOMPONENTS = 15;  //size of boss' platform;
	private static final float[] PLATFORMSIZE = {480, 300};
	
	// RATES
	private static final double GREENORCPROB = 0.6;
	private static final double ORCPLACEPROB = 0.5;
	private static final double CHESTPROB = 1;
	private static final double WEAPONPROB = 0.7;
	private static final double COINPROB = 0.2;
	private static final double DOUBLECOINPROB = 0.4;
	private static final double FALLINGPROB = 0.2;
	
	// OFFSETS
	private static final float[] ORCOFFSET = {160, -100};
	private static final float[] CHESTOFFSET = {250, -55};
	private static final float[] COINOFFSET = {55, -40};
	
	// VARIANCES
	private static final float[] PLATFORMVARIANCE = {60, 100};
	private static final float[] ORCVARIANCE = {50, 100};
	private static final float[] COINVARIANCE = {50, 0};
	private static final float[] CHESTVARIANCE = {35, 0};
	
	GameInstance () {
		resurrection = false;
		won = false;
		init_gamemap();
	}
	
	// GETTERS / SETTERS
	public Hero getHero () {
		return hero;
	}
	
	public float getPanCam () {
		return panCam;
	}
	
	public void setPanCam (float panCam) {
		this.panCam = panCam;
	}
	
	public void add_coins (int _coin_count) {
		coin_count += _coin_count;
	}
	
	public int getCoin_count () {
		return coin_count;
	}
	
	public LinkedHashMap<UUID, GameObject> get_gameMap () {
		return gamemap;
	}
	
	public void win () {
		won = true;
	}
	
	public boolean isWon () {
		return won;
	}
	
	private static float genrand () {
		return (float)(Math.random() - 0.5);
	}
	
	private static float[] apply_offset (float[] start, float[] offset, float[] variance) {
		float[] del = new float[]{variance[0] * genrand(), variance[1] * genrand()};
		return new float[]{start[0] + offset[0] + del[0], start[1] + offset[1] + del[1]};
	}
	
	private void init_gamemap () {
		gamemap = new LinkedHashMap<>(150); // I want hashmap, but also hero should be first object
		hero = new Hero(this);
		register(hero);
		
		// MAKE STARTING PLATFORM
		Platform p0 = new Platform(new float[]{0, 300}, 0);
		register(p0);
		
		for (int i = 1; i < BOSSPLATFORM; i++) {
			
			// starting of the platform - all objects are placed relative to this
			float[] START = apply_offset(new float[]{PLATFORMSIZE[0] * i, PLATFORMSIZE[1]}, new float[]{0, 0},
				PLATFORMVARIANCE);
			
			// SET PLATFORM
			if (Math.random() < FALLINGPROB) {
				FallingPlatform p = new FallingPlatform(START);
				register(p);
				for (Platform m : p.getSubmodels()) {
					register(m);
				}
				continue;
			} else {
				Platform p = new Platform(START, (int)(Math.random() * 3));
				register(p);
			}
			
			// ADD ORCS
			if (Math.random() < ORCPLACEPROB) {
				float[] orcPos = apply_offset(START, ORCOFFSET, ORCVARIANCE);
				
				// Decide Orc Type
				Orc orc = (Math.random() < GREENORCPROB) ?
					new GreenOrc(orcPos, this) : new RedOrc(orcPos, this);
				register(orc);
			}
			
			// ADD COINS
			if (Math.random() < COINPROB) {
				Coin c1 = new Coin(apply_offset(START, COINOFFSET, COINVARIANCE));
				register(c1);
				
				if (Math.random() < DOUBLECOINPROB) {
					Coin c2 = new Coin(apply_offset(START, new float[]{COINOFFSET[0] + 35, COINOFFSET[1]}, COINVARIANCE));
					register(c2);
				}
			}
			
			// ADD CHESTS
			float[] chestPos = apply_offset(START, CHESTOFFSET, CHESTVARIANCE);
			Chest c;
			if (Math.random() < CHESTPROB) {
				if (Math.random() < WEAPONPROB) {
					c = new WeaponChest(chestPos);
				} else {
					c = new CoinChest(chestPos);
				}
				register(c);
			}
		}
		addBossAndPlatforms();
	}
	
	private void addBossAndPlatforms () {
		float p_del_x = PLATFORMVARIANCE[0] * genrand();
		float p_del_y = PLATFORMVARIANCE[1] * genrand();
		float xlocation = PLATFORMSIZE[0] * (BOSSPLATFORM) + p_del_x;
		float ylocation = PLATFORMSIZE[1] + p_del_y;
		FallingPlatform p1 = new FallingPlatform(new float[]{xlocation, ylocation}, BOSSPLATFORMCOMPONENTS);
		register(p1);
		for (Platform m : p1.getSubmodels()) {
			register(m);
		}
		float[] orcPos = {xlocation + 71 * (BOSSPLATFORMCOMPONENTS / 2F), (float)(-50 + 50 * Math.random())};
		GameObject boss = new BossGreenOrc(orcPos, this);
		register(boss);
	}
	
	// REGISTRATION AND DEREGISTRATION OF OBJECTS
	public void deregister (GameObject object) {
		gamemap.remove(object.getID());
	}
	
	public void register (GameObject object) {
		gamemap.put(object.getID(), object);
		GameController.add_object(object);
	}
	
	// METHODS RELATED TO RESURRECTION
	public int resurrect () {
		/*
		 * Method for resurrection: -1 for coins less than cost, 0 for success, and 1 for no platform being available
		 * TODO: this is functional, but O(n)
		 */
		if (getCoin_count() < RESURRECTIONCCOST) return -1;
		double XPos = hero.getPos()[0];
		
		//find an appropriate platform
		List<GameObject> map = new ArrayList<>(this.get_gameMap().values());
		
		//iterating over map
		Iterator<GameObject> iter = map.iterator();
		
		double viableXPos = 0;
		
		boolean falling = false;
		
		// TODO: would be cleaner with a for-each loop, although flexing iterator DP is kinda cool.
		while (iter.hasNext()) {
			GameObject iterating = iter.next();
			
			if ((iterating instanceof Platform || (iterating instanceof FallingPlatform
				&& !((FallingPlatform)iterating).getCollapsing()))
				&& iterating.getPos()[0] > XPos && iterating.getVel()[1] < 0.1) {
				// TODO: doesnt work because of the way fallingplatform is configured. could be fixed but im not too sure.
				//it is a Platform!
				viableXPos = iterating.getPos()[0];
				break;
			}
			if (iterating instanceof FallingPlatform && ((FallingPlatform)iterating).getCollapsing()) {
				falling = true;
			}
			
		}
		
		viableXPos = Math.ceil(viableXPos);
		
		//some basic stuff to ensure it doesn't break the game
		if (viableXPos == 0) {
			return 1;
		}
		
		if (falling) viableXPos += 200;
		
		hero.clearMoveCount();
		hero.setPos((float)viableXPos, 20);
		hero.set_vel(0, 0);
		hero.set_vel(1, 0);
		
		// TODO dont need to be a separate method imo
		setResurrection();
		resurrectionDeduction();
		
		return 0;
	}
	
	private void resurrectionDeduction () {
		this.add_coins(-RESURRECTIONCCOST);
	}
	
	private void setResurrection () {
		resurrection = true;
		this.hero.resurrect();
	}
	
	private boolean hasResurrected () {
		return resurrection;
	}
	
	public boolean canResurrect () {
		return !hasResurrected();
	}
}
