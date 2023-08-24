package neuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Layer {
	
	private int numNodes;
	private ArrayList<Float> values;
	
	private static final Random random = new Random();
	
	public Layer(int numNodes) {
		
		this.numNodes = numNodes;
		this.values = new ArrayList<Float>();
		this.randomize();
		
	}
	
	public Layer(Layer copy) {
		
		this(copy.numNodes);
		
		for (int i = 0; i < copy.numNodes; i += 1)
			this.values.set(i, copy.values.get(i));
		
	}
	
	public ArrayList<Float> getNodeValueList() {
		return this.values;
	}
	
	public int getLength() {
		return this.numNodes;
	}
	
	public void randomize() {
		
		for (int i = 0; i < this.numNodes; i += 1)
			this.values.add(random.nextFloat() * 2f - 1f);
		
	}
	
	@Override
	public String toString() {
		return super.toString() + ": " + this.values.toString();
	}
	
}
