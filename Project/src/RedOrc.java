public final class RedOrc extends Orc{
	private static final String loc1 = "images/orc_red_angry.png";
	private static final String loc2 = "images/orc_red_ring.png";
	private static final int HP = 10;
	
	RedOrc (float[] position) {
		super(position, (float)(5 + 4 * Math.random()), (Math.random() > 0.5) ? loc1 : loc2, new float[]{60, 60}, HP
		);
	}
	
	@Override
	public void call_out () {
		//has four options
		//won't taunt if isTaunting is set
		if (isTaunting) return;
		
		this.isTaunting = true;
		int promptNumber = (int)(1 + Math.random() * 4);
		String fileno = "images/orc_prompts/normal/normal_prompt_" + promptNumber + ".png";
		
		GameController.getGameInstance().register(new Shout(fileno, this));
	}
	
	@Override
	public void call_out_death () {
		if (isTaunting) return;
		
		this.isTaunting = true;
		int promptNumber = (int)(1 + Math.random() * 3);
		String fileno = "images/orc_prompts/hero_death/death_prompt_" + promptNumber + ".png";
		
		GameController.getGameInstance().register(new Shout(fileno, this));
	}
	
}
