package application;

import box2DTemplate.Box;
import box2DTemplate.Box2DHelper;
import javafx.scene.layout.Pane;
import neuralNetwork.Genome;

public class Player {
	
	public Person person;
	public Genome genome;
	private boolean isDestroyed;
	
	public Player(Box2DHelper world, Pane root) {
		
		this.person = new Person(world, root);
		this.genome = new Genome(Person.neuralNetwork[0], Person.neuralNetwork[1], Person.neuralNetwork[2]);
		this.isDestroyed = false;
		
	}
	
	public Player(Genome genome, Box2DHelper world, Pane root) {
		
		this.person = new Person(world, root);
		this.genome = new Genome(genome);
		this.isDestroyed = false;
		
	}
	
	public void runGenome() {
		
		if (this.isDestroyed) {
			
			System.err.println("This player object has been destroyed: " + this.toString());
			return;
			
		}
		
		this.person.setOutputs(this.genome.feedForward(this.person.getInputs()));
		
	}
	
	public float calculateFitness() {		
		return this.person.getProgress() - Person.startingXPosition;
	}
	
	public boolean ifBoxBelongsToPlayer(Box box) {
		
		for (Box bodyPart : this.person.bodyParts)
			if (bodyPart.equals(box))
				return true;
		
		return false;
		
	}
	
	public void destroy() {
		
		this.person.destroy();
		this.person = null;
		this.isDestroyed = true;

	}
	
}
