package application;

import box2DTemplate.Box;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Laser {
	
	public Rectangle rectangle;
	public Pane root;
	private static final int speed = 1;
	
	public Laser(int lowestPoint, Pane root) {
		
		this.rectangle = new Rectangle(0, 0, 7, lowestPoint);
		this.root = root;
		this.rectangle.setFill(Color.rgb(255, 0, 0, 0.6));
		this.rectangle.setStrokeWidth(0);
		this.root.getChildren().add(0, this.rectangle);
		
	}
	
	public void updatePosition() {
		
		this.rectangle.setTranslateX(this.rectangle.getTranslateX() + speed);
		
		if (this.rectangle.getTranslateX() > this.root.getTranslateX() + 120)
			Main.translateNodes(-speed);
		
	}
	
	public boolean ifPlayerBurnt(Player player) {
		
		for (Box box : player.person.bodyParts) {
			
			Shape intersection = Shape.intersect(this.rectangle, box.getRectangle());
			
			if (intersection.getBoundsInLocal().getWidth() != -1)
				return true;
			
		}
		
		return false;
		
	}
	
	public void resetLaser() {
		
		this.rectangle.setTranslateX(0);
		Main.resetTranslation();
		
	}

}
