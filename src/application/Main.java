package application;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

import box2DTemplate.Box;
import box2DTemplate.Box2DHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	public static int width = 1382;
	public static int height = 704;
	public static Box2DHelper world;
	public static Pane root;
	public static Timeline timeline = new Timeline();
	public static Label generations;
	public static Label maxFitness;
	public static Label mutationRate;
	public static Box platform;
	public static Population population;

	@Override
	public void start(Stage window) {
		
		window = new Stage();
		root = new Pane();
		root.setBackground(Background.EMPTY);
		
		Scene scene = new Scene(root, width, height, Color.gray(0.25));
		window.setTitle("Walker");
		window.setScene(scene);
		window.setMaximized(true);
		
		world = new Box2DHelper(width, height);
		world.createWorld();
		world.createMouseJoint(root);
		world.listenForCollisions(Main.class);
		this.createPlatform();
		this.createLabels();
		
		population = new Population(50, world, root);
		Laser laser = new Laser(height - 50, root);
		generations.setText("Generation: " + population.getGeneration());
		mutationRate.setText("Mutation Rate: " + population.getMutationRate() + "%");
		maxFitness.setText("Max Fitness Achieved: " + Math.round(population.getMaxFitness()));
		
		EventHandler<ActionEvent> stepEvent = new EventHandler<>() {

            private int count = 0;

            @Override
            public void handle(ActionEvent event) {

                population.runGenomes();
                world.stepAndDisplay();
                population.removeFailures();
                laser.updatePosition();

                for (Player player : population.players)
                    if (laser.ifPlayerBurnt(player))
                        population.evaluateAndDeletePlayer(player);

                if (population.players.isEmpty() && this.count % 200 == 0) {

                    laser.resetLaser();
                    population.resetPopulation();
                    generations.setText("Generation: " + population.getGeneration());
                    mutationRate.setText("Mutation Rate: " + population.getMutationRate() + "%");
                    maxFitness.setText("Max Fitness Achieved: " + Math.round(population.getMaxFitness()));
                    population.resetMaxFitness();
                    this.count = 0;

                } else
                    this.count += 1;

            }

        };
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(world.getTimeStep()).divide(1.5), stepEvent, null, null));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		scene.setOnKeyPressed(event -> {
			
			if (event.getCode() == KeyCode.RIGHT) {
				
				root.setTranslateX(root.getTranslateX() - 15);
				generations.setTranslateX(generations.getTranslateX() + 15);
				maxFitness.setTranslateX(maxFitness.getTranslateX() + 15);
				mutationRate.setTranslateX(mutationRate.getTranslateX() + 15);
				
			}
			
			else if (event.getCode() == KeyCode.LEFT) {
				
				root.setTranslateX(root.getTranslateX() + 15);
				generations.setTranslateX(generations.getTranslateX() - 15);
				maxFitness.setTranslateX(maxFitness.getTranslateX() - 15);
				mutationRate.setTranslateX(mutationRate.getTranslateX() - 15);

			}
			
			else if (event.getCode() == KeyCode.SPACE) {
				
				System.out.println("Generation: " + population.getGeneration());
				System.out.print(population.bestPerformer);
				
			}
			
		});
		
		window.setOnCloseRequest(closeEvent -> {
			
			timeline.stop();
			Platform.exit();
			System.exit(0);
			
		});
		
		window.show();
		
	}
	
	public void beginContact(Contact contact) {
			
		try {
			
			Object bodyA = contact.getFixtureA().getBody().getUserData();
			Object bodyB = contact.getFixtureB().getBody().getUserData();
			
			if (bodyA instanceof Box boxA && bodyB instanceof Box boxB) {

				if (boxA.getMainBody().equals(Main.world.getGroundBody()) || boxB.getMainBody().equals(Main.world.getGroundBody())) {
					
					if (boxA.getName().equals(Person.DoNotTouch))
						Main.population.deletePlayer(boxA);					
					
					else if (boxB.getName().equals(Person.DoNotTouch))
						Main.population.deletePlayer(boxB);
				
				}
				
			}
			
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void createPlatform() {
		
		platform = new Box(width * 20, height - 30, width * 40, 50, BodyType.STATIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5), "Platform", world, root);
		platform.getRectangle().setFill(new ImagePattern(new Image("ground.png"),0, 0, 70, platform.getPixelHeight(), false));
		platform.getRectangle().setStrokeWidth(0);
		world.setGroundBody(platform.getMainBody());
		
	}
	
	private void createLabels() {
		
		Font font = Font.font("Regular", FontWeight.BOLD, 30);
		Color textColor = Color.rgb(255, 255, 50);
		
		generations = new Label();
		generations.setFont(font);
		generations.setTextFill(textColor);
		generations.setLayoutX(500);
		generations.setLayoutY(15);
		
		maxFitness = new Label();
		maxFitness.setFont(font);
		maxFitness.setTextFill(textColor);
		maxFitness.setLayoutX(width - 500);
		maxFitness.setLayoutY(15);
		
		mutationRate = new Label();
		mutationRate.setFont(font);
		mutationRate.setTextFill(textColor);
		mutationRate.setLayoutX(width - 500);
		mutationRate.setLayoutY(60);
		
		root.getChildren().addAll(generations, maxFitness, mutationRate);
		
	}
	
	public static void translateNodes(double speed) {
		
		root.setTranslateX(root.getTranslateX() + speed);
		generations.setTranslateX(generations.getTranslateX() - speed);
		maxFitness.setTranslateX(maxFitness.getTranslateX() - speed);
		mutationRate.setTranslateX(mutationRate.getTranslateX() - speed);
		
	}
	
	public static void resetTranslation() {
		
		root.setTranslateX(0);
		generations.setTranslateX(0);
		maxFitness.setTranslateX(0);
		mutationRate.setTranslateX(0);
		
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
