package neuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class NodeConnections {
	
	private Layer entryLayer;
	private Layer exitLayer;
	private ArrayList<ArrayList<Float>> weights;
	private static final Random random = new Random();
	
	public NodeConnections(Layer entryLayer, Layer exitLayer) {
		
		this.entryLayer = entryLayer;
		this.exitLayer = exitLayer;
		this.weights = new ArrayList<ArrayList<Float>>();
		this.randomize();
		
	}
	
	public NodeConnections(Layer entryLayer, Layer exitLayer, NodeConnections connections) {
		
		this(entryLayer, exitLayer);

		int count = 0;
		ArrayList<Float> floatArrayFromCopy = new ArrayList<Float>();
		
		for (ArrayList<Float> array : this.weights) {
			
			floatArrayFromCopy = connections.getWeights().get(count);
			
			for (int i = 0; i < array.size(); i += 1)
				array.set(i, floatArrayFromCopy.get(i).floatValue());
			
			count += 1;
			
		}
		
	}
	
	public void mutateWeights(int mutationRate) {
		
		for (ArrayList<Float> array : this.weights) {
			
			for (int i = 0; i < array.size(); i += 1) {
				
				int num = random.nextInt(99) + 1;

				if (num <= mutationRate)
					array.set(i, (float) Math.round(random.nextFloat() * 2f - 1f));
				
			}
			
		}

	}
	
	private void randomize() {
		
		for (int i = 0; i < this.exitLayer.getLength(); i += 1) {
			
			ArrayList<Float> array = new ArrayList<Float>();
			this.weights.add(array);
			
			for (int j = 0; j < this.entryLayer.getLength(); j += 1)
				array.add(random.nextFloat() * 2f - 1f);
			
		}
		
	}
	
	public ArrayList<ArrayList<Float>> getWeights() {
		return this.weights;
	}
	
	@Override
	public String toString() {
		return super.toString() + ": " + this.weights.toString();
	}

}
