public class Weapon implements Collectable{
	private int level;
	private String sprite;
	private float[] size;
	private final int cat;
	
	Weapon(int _cat){
		level = 1;
		cat = _cat;
		switch (_cat){
			case 0 -> {sprite = "images/weapon_knife.png"; size = new float[]{69,16};}
			case 1 -> {sprite = "images/weapon_shuriken.png"; size = new float[]{30,30};}
		}
	}
	
	@Override
	public boolean equals (Object obj) {
		if(obj.getClass() != Weapon.class) return false;
		return ((Weapon)obj).cat == cat;
	}
	
	public float[] getSize () {
		return size;
	}
	
	public String getSprite () {
		return sprite;
	}
	
	public void get_collected (Hero hero) {
		//todo
	}
	
	public void use (Hero hero) {
		float[] heropos = hero.getPos();
		FlyingWeapon p = new FlyingWeapon(new float[]{heropos[0]+25, heropos[1]},new float[]{12,0},0,this);
		// TODO: add item can be polished to process ID internally
		hero.getCurrent_game().add_item(p.getID(), p);
		if(level >= 2){
			FlyingWeapon q = new FlyingWeapon(new float[]{heropos[0]+25, heropos[1]-25},new float[]{11.5F,-0.5F},-7,this);
			hero.getCurrent_game().add_item(q.getID(), q);
		}
		if(level >= 3){
			FlyingWeapon r = new FlyingWeapon(new float[]{heropos[0]+25, heropos[1]+25},new float[]{11.5F,0.5F},7,this);
			hero.getCurrent_game().add_item(r.getID(), r);
		}
	}
	
	public void upgrade () {
		level++;
	}
}

