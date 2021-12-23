public abstract class Orc extends GameObject{
    private int hit_endurance;
    private boolean is_alive;
    private int coin_drop;
    private String[] dialogues;
    private float size;
    private String sprite;
    private GameInstance current_game;

    private static final double CallOutProbability = 0.0005;


    //TODO: other inits
    Orc(float[] position, float mass, String spritee, float[] size, int HP, GameInstance instance){
        //todo constructor
        super(position, new float[]{0,0}, new float[]{0,0}, mass, true, true, spritee, size);
        this.current_game = instance;
        this.hit_endurance = HP;
    }



    public void die(Hero hero){
        //todo
    }

    public void get_hit_by_weapon(FlyingWeapon weapon){
        //todo: get weapon damage and subtract it from HP
    }

    public void is_alive(){
        //todo
    }

    public void smash_hero(Hero hero){
        //todo
    }
    public void call_out(){
        //todo
        System.out.println("CALLED OUT!");
    }

    @Override
    public void refresh(){
        super.refresh();
        //call out dialogue, probability 0.05
        if(Math.random() < CallOutProbability){
            call_out();
        }

    }

}
