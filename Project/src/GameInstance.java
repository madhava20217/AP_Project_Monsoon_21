import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable {
    public ArrayList<Helmet> helmet_list;
    private Hero hero;
    private ArrayList<GameObject> gamemap;
    private int coin_count;


    GameInstance(){
        //todo constructor
    }


    public void start(){
        //todo method
    }

    public void stop(){
        //todo method
    }

    public void pause(){
        //todo method
    }

    public void resume(){
        //todo method
    }

    public void exit(){
        //todo method
    }

    public void resurrect(){
        //todo method
    }

    public void game_over(){
        //todo method
    }

    public void win(){
        //todo method
    }
}
