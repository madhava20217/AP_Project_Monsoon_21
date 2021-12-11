import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GameController{
	public GameController(){
	
	}
	private static ArrayList<GameObject> objects = new ArrayList<>();
	private static Stage stage; // if i keep this non-static the game breaks dont ask me why
	private static Scene pausedGame = null;
	private static AnimationTimer clock;
	private static float panCam;
	
	public static float getPanCam(){
		return panCam;
	}
	public static void panCam (float x){
		panCam += x;
	}
	
	public void setStageVar(Stage _stage){
		stage = _stage;
	}
	
	@FXML
	private void goToPlay(MouseEvent ignored){
		final Label distance;
		Label d;
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PlayScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
			
			objects.add(
				new Hero(scene.lookup("#hero_hitbox"),new float[]{0,-5},new float[]{0,0},2, true, true));
			objects.add(
				new GameObject(scene.lookup("#platform_1_hitbox"),new float[]{0,0},new float[]{0,0},1000,false, true));
			objects.add(
				new GameObject(scene.lookup("#platform_2_hitbox"),new float[]{0,0},new float[]{0,0},1000,false, true));
			objects.add(
				new GameObject(scene.lookup("#platform_3_hitbox"),new float[]{0,0},new float[]{0,0},1000,false, true));
			objects.add(
				new GameObject(scene.lookup("#orc_hitbox"),new float[]{0,-5},new float[]{0,0},10,true, true));
			objects.add(
				new GameObject(scene.lookup("#coin_1_hitbox"),new float[]{0,0},new float[]{0,0},10,false, false));
			objects.add(
				new GameObject(scene.lookup("#coin_2_hitbox"),new float[]{0,0},new float[]{0,0},10,false, false));
			objects.add(
				new GameObject(scene.lookup("#chest_hitbox"),new float[]{0,0},new float[]{0,0},10,false, false));
			
			d = (Label) scene.lookup("#distance");
			assert(objects.get(0).getClass() == Hero.class); // hero needs to be first for collision stuff
		} catch(IOException ignored1) {
			d = null;
		}
		
		distance = d;
		clock = new AnimationTimer(){
			@Override
			public void handle (long l) {
				for (int i = 0; i < objects.size() - 1; i++) {
					for (int j = i + 1; j < objects.size(); j++) {
						float[] overlaps = objects.get(i).getOverlaps(objects.get(j));
						if (overlaps[0] > 0 && overlaps[1] > 0) {
							if (objects.get(i).isTangible() && objects.get(j).isTangible()) {
								objects.get(i).bounce(objects.get(j), 1F, overlaps[0], overlaps[1]);
							}
						}
					}
				}
				
				for (GameObject obj : objects) {
					obj.move();
					if(obj.isRendered()){
						obj.refresh();
					}
				}
				
				assert distance != null;
				distance.setText(String.valueOf(((Hero)objects.get(0)).getDistance()));
			}
		};
		clock.start();
	}
	
	@FXML
	private void reset(MouseEvent ignored){
		assert(clock != null);
		clock.stop();
		objects = new ArrayList<>();
		goToPlay(null);
	}
	
	@FXML
	private void goToOverWin(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/GameOverWin.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void goToOverLose(MouseEvent ignored){
		try{
			FXMLLoader overLose = new FXMLLoader(GameController.class.getResource("templates/GameOverLose.fxml"));
			stage.setScene(new Scene(overLose.load()));
		} catch(IOException ignored1) {
			System.err.println("IOException when overLose screen.");
		}
	}
	
	@FXML
	private void goToPause(MouseEvent ignored){
		try{
			pausedGame = stage.getScene();
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void goResumeFromPause(MouseEvent ignored){
		try{
			/*FXMLLoader loader = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
			Scene scene = new Scene(loader.load());*/
			
			stage.setScene(pausedGame);
		}
		catch (NullPointerException ignored1){
			System.err.println("Null Pointer exception when resuming game");
		}
	}
	
	@FXML
	private void goToMainMenu(MouseEvent ignored){
		try{
			FXMLLoader mainLoad = new FXMLLoader(GameController.class.getResource("templates/Menu.fxml"));
			stage.setScene(new Scene(mainLoad.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException caught when returning to main menu.");
		}
	}
	
	@FXML
	private void goToSaveScreen(MouseEvent ignored){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/SaveScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException when going to save screen");
		}
	}
	@FXML
	private void goToLoadScreen(MouseEvent ignored){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/LoadScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException when going to save screen");
		}
	}
	
	@FXML
	private void exitGame(MouseEvent ignored){
		System.out.println("Thank you for playing!");
		stage.close();
	}
	
	@FXML
	private void move_hero(MouseEvent ignored){
		Hero h = (Hero) objects.get(0);
		// Find hero properly in actual implementation
		h.launch();
	}
}
