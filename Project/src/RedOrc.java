public class RedOrc extends Orc{
    private static final String loc1 = "images/orc_red_angry.png";
    private static final String loc2 = "images/orc_red_ring.png";
    private static final int HP = 4;

    //TODO: other inits in orc abstract
    RedOrc(float[] position, GameInstance instance){
        super(position, 8, loc1, new float[]{60,60}, HP, instance);
    }
}
