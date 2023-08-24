package application;

import java.util.ArrayList;
import java.util.Iterator;

import box2DTemplate.Box;
import box2DTemplate.Box2DHelper;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import neuralNetwork.Genome;

public class Population {
	
	private int generation;
	private int size;
	public ArrayList<Player> players;
	public ArrayList<Player> failures;
	public Genome bestPerformer;
	private float maxFitness;
	private int mutationRate;
	private Box2DHelper world;
	private Pane root;
	
	public Population(int size, Box2DHelper world, Pane root) {
		
		this.generation = 1;
		this.size = size;
		this.players = new ArrayList<Player>();
		this.failures = new ArrayList<Player>();
		this.maxFitness = 0;
		this.mutationRate = 100;
		this.world = world;
		this.root = root;
		
		for (int i = 0; i < this.size; i += 1)
			this.players.add(new Player(this.world, this.root));

		this.bestPerformer = new Genome(this.players.get(0).genome);
		
	}
	
	public int getGeneration() {
		return this.generation;
	}
	
	public float getMaxFitness() {
		return this.maxFitness;
	}
	
	public void resetMaxFitness() {
		this.maxFitness = 0;
	}
	
	public int getMutationRate() {
		return this.mutationRate;
	}
	
	public void runGenomes() {
		
		for (Player player : this.players)
			player.runGenome();
		
	}
	
	public void resetPopulation() {
		
		for (Player player : this.players)
			player.destroy();

		this.players.clear();
		
		this.reduceMutationRate();
		Player player = new Player(this.bestPerformer, this.world, this.root);
		this.players.add(player);
		
		for (Box box : player.person.bodyParts)
			box.getRectangle().setFill(Color.rgb(255, 0, 0, 0.6));

		for (int i = 0; i < this.size - 1; i += 1) {
			
			player = new Player(this.bestPerformer, this.world, this.root);
			this.players.add(player);
			player.genome.mutateConnections(this.mutationRate);
			
		}
		
		this.generation += 1;
		
	}
	
	public void deletePlayer(Box box) {
		
		for (Player player : this.players)
			if (player.ifBoxBelongsToPlayer(box) && !this.failures.contains(player))
				this.failures.add(player);
		
	}
	
	public void evaluateAndDeletePlayer(Player player) {
		
		float playerFitness = player.calculateFitness();
		
		if (playerFitness > this.maxFitness) {
			
			this.maxFitness = playerFitness;
			this.bestPerformer = new Genome(player.genome);
			
		}
		
		if (!this.failures.contains(player))
			this.failures.add(player);
		
	}
	
	public void removeFailures() {
		
		Iterator<Player> iterator = this.failures.iterator();
		
		while (iterator.hasNext()) {
			
			Player failure = iterator.next();
			failure.destroy();
			this.players.remove(failure);
			iterator.remove();
			
		}
		
	}
	
	private void reduceMutationRate() {
		
		if (this.mutationRate > 1)
			this.mutationRate -= 1;
		
		else
			this.mutationRate = 25;
		
	}
	
}
