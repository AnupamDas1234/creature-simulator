package neuralNetwork;

import java.util.ArrayList;

public class Genome {
	
	private Layer inputLayer;
	private Layer outputLayer;
	private Layer hiddenLayer;
	private NodeConnections inputToHiddenConnections;
	private NodeConnections hiddenToOutputConnections;
	private static final float biasValue = 0;
	
	public Genome(int numInputNodes, int numHiddenNodes, int numOutputNodes) {
		
		this.inputLayer = new Layer(numInputNodes);
		this.hiddenLayer = new Layer(numHiddenNodes);
		this.outputLayer = new Layer(numOutputNodes);
		this.inputToHiddenConnections = new NodeConnections(inputLayer, hiddenLayer);
		this.hiddenToOutputConnections = new NodeConnections(hiddenLayer, outputLayer);
		
	}
	
	public Genome(Genome copy) {
		
		this.inputLayer = new Layer(copy.inputLayer);
		this.hiddenLayer = new Layer(copy.hiddenLayer);
		this.outputLayer = new Layer(copy.outputLayer);
		this.inputToHiddenConnections = new NodeConnections(this.inputLayer, this.hiddenLayer, copy.inputToHiddenConnections);
		this.hiddenToOutputConnections = new NodeConnections(this.hiddenLayer, this.outputLayer, copy.hiddenToOutputConnections);
		
	}
	
	public ArrayList<Float> feedForward(float... inputs) {
		
		if (inputs.length != this.inputLayer.getLength()) {
			
			System.err.println("Wrong number of inputs. You have entered " + inputs.length + " but you should have entered " + this.inputLayer.getLength());
			return null;
			
		}
				
		for (int i = 0; i < inputs.length; i += 1)
			this.inputLayer.getNodeValueList().set(i, inputs[i]);
		
		this.executeLayerJump(this.inputLayer, this.hiddenLayer, this.inputToHiddenConnections);
		this.executeLayerJump(this.hiddenLayer, this.outputLayer, this.hiddenToOutputConnections);
		
		return this.outputLayer.getNodeValueList();
		
	}

	private void executeLayerJump(Layer input, Layer output, NodeConnections weightsInputToOutput) {
		
		int count = 0;

		for (ArrayList<Float> weightArray : weightsInputToOutput.getWeights()) {
			
			float calculatedValue = this.getArrayMultiplicationSum(weightArray, input.getNodeValueList()) + biasValue;
			output.getNodeValueList().set(count, this.activationFunction(calculatedValue));
			count += 1;
			
		}
		
	}
	
	private float getArrayMultiplicationSum(ArrayList<Float> array1, ArrayList<Float> array2) {

		if (array1.size() != array2.size()) {
			
			System.err.println("The size of the arrays must be equal");
			return 0;
			
		}
		
		ArrayList<Float> result = new ArrayList<Float>();

		for (int i = 0; i < array1.size(); i += 1)
			result.add(array1.get(i) * array2.get(i));
	
		float sum = 0;
	
		for (Float value : result)
			sum += value;
	
		return sum;
		
	}
	
	private float activationFunction(float x) {
		// return (float) (Math.tanh(x));
		
		// if (x > 0.4f)
		// 	return 1;
		
		// else if (x < -0.4f)
		// 	return -1;
		
		// else
		// 	return 0;

		if (x > 0.0f)
			return 1;

		else
			return -1;
		
	}
	
	public void mutateConnections(int mutationRate) {
	
		this.inputToHiddenConnections.mutateWeights(mutationRate);
		this.hiddenToOutputConnections.mutateWeights(mutationRate);
	
	}
	
	@Override
	public String toString() {
		
		String result = super.toString() + "\n";
		result += this.inputLayer.toString() + "\n";
		result += this.inputToHiddenConnections.toString() + "\n";
		result += this.hiddenLayer.toString() + "\n";
		result += this.hiddenToOutputConnections.toString() + "\n";
		result += this.outputLayer.toString() + "\n\n";
		return result;
		
	}
	
}
