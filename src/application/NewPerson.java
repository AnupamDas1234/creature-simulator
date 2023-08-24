package application;

import java.util.ArrayList;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import box2DTemplate.Box;
import box2DTemplate.Box2DHelper;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NewPerson {
	
	public Box body;
	public Box thigh;
	public Box lowerLeg;
	public RevoluteJoint bodyThighJoint;
	public RevoluteJoint kneeJoint;
	public ArrayList<Box> bodyParts;
	public ArrayList<RevoluteJoint> joints;
	public Box2DHelper world;
	
	public static final float startingXPosition = 500;
	public static final float maxMotorSpeed = 100;
	public static final float maxMotorTorque = 200;
	public static final float upperAngle = 60;
	public static final float lowerAngle = -60;
	public static final String DoNotTouch = "Do not touch ground";
	public static final int[] neuralNetwork = new int[] {7, 5, 2};

	public NewPerson(Box2DHelper world, Pane root) {
		
		this.world = world;
		this.body = new Box(startingXPosition, 500, 200, 30, BodyType.DYNAMIC, 0.01f, 1, 0.1f, 0.4f, Color.gray(0.5, 0.4), "body", world, root);
		this.thigh = new Box(startingXPosition, 500, 10, 50, BodyType.DYNAMIC, 0.01f, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "thigh", world, root);
		this.lowerLeg = new Box(startingXPosition, 530, 10, 50, BodyType.DYNAMIC, 0.01f, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "lower Leg", world, root);
		
		RevoluteJointDef hinge = new RevoluteJointDef();
		hinge.bodyA = this.body.getMainBody();
		hinge.bodyB = this.thigh.getMainBody();
		hinge.collideConnected = false;
		hinge.localAnchorA.set(this.body.getWorldWidth() / 2 - this.thigh.getWorldWidth() / 2, -this.body.getWorldHeight() / 2);
		hinge.localAnchorB.set(0, this.thigh.getWorldHeight() / 2 - this.thigh.getWorldWidth() / 2);
		hinge.enableLimit = true;
		hinge.upperAngle = (float) Math.toRadians(upperAngle);
		hinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		hinge.enableMotor = true;
		hinge.maxMotorTorque = maxMotorTorque;
		hinge.motorSpeed = 0;
		this.bodyThighJoint = (RevoluteJoint) world.createJoint(hinge);
		
		RevoluteJointDef kneeHinge = new RevoluteJointDef();
		kneeHinge.bodyA = this.thigh.getMainBody();
		kneeHinge.bodyB = this.lowerLeg.getMainBody();
		kneeHinge.collideConnected = false;
		kneeHinge.localAnchorA.set(0, -this.thigh.getWorldHeight() / 2);
		kneeHinge.localAnchorB.set(0, this.lowerLeg.getWorldHeight() / 2);
		kneeHinge.enableLimit = true;
		kneeHinge.upperAngle = (float) Math.toRadians(upperAngle);
		kneeHinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		kneeHinge.enableMotor = true;
		kneeHinge.maxMotorTorque = maxMotorTorque;
		kneeHinge.motorSpeed = 0;
		this.kneeJoint = (RevoluteJoint) world.createJoint(kneeHinge);
		
		this.collisionFiltering();
		
		this.bodyParts = new ArrayList<Box>();
		this.bodyParts.add(this.body);
		this.bodyParts.add(this.thigh);
		this.bodyParts.add(this.lowerLeg);
		
		this.joints = new ArrayList<RevoluteJoint>();
		this.joints.add(this.bodyThighJoint);
		this.joints.add(this.kneeJoint);
		
		
	}

	private void collisionFiltering() {
		
		this.body.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.thigh.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.lowerLeg.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		
	}
	
	public float getThighAngle() {
		return this.bodyThighJoint.getJointAngle();
	}
	
	public float getThighSpeed() {
		return this.bodyThighJoint.getJointSpeed();
	}
	
	public float getKneeAngle() {
		return this.kneeJoint.getJointAngle();
	}
	
	public float getKneeSpeed() {
		return this.kneeJoint.getJointSpeed();
	}
	
	public float getRotation() {
		return this.body.getMainBody().getAngle();
	}
	
	public boolean ifTouchingGround() {
		
		for (Box box : this.bodyParts)
			for (ContactEdge contactEdge = box.getMainBody().getContactList(); contactEdge != null; contactEdge = contactEdge.next)
				if (contactEdge.other == this.world.getGroundBody() && contactEdge.contact.isTouching())
					return true;
		
		return false;
		
	}
	
	public float getHeightAboveGround() {
		return -this.world.getBodyPixelCoord(this.body.getMainBody()).y + this.world.getBodyPixelCoord(world.getGroundBody()).y;
	}
	

	private static final float map(float value, float istart, float istop, float ostart, float ostop) {
	    return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
	public float[] getInputs() {
		
		float[] inputs = new float[7];
		
		inputs[0] = map(this.getThighAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[1] = map(this.getThighSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[2] = map(this.getKneeAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[3] = map(this.getKneeSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[4] = map((float) (this.getRotation() % Math.toRadians(180)), (float) -Math.toRadians(180), (float) Math.toRadians(180), -1, 1);
		inputs[5] = map(this.ifTouchingGround() ? 1 : 0, 0, 1, -1, 1);
		inputs[6] = map(this.getHeightAboveGround(), 0, Main.height / 2, 1, -1);
		
		return inputs;
		
	}
	
	public void setThighSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.bodyThighJoint.enableMotor(false);
		}
		
		else {
			
			this.bodyThighJoint.enableMotor(true);
			this.bodyThighJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setKneeSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.kneeJoint.enableMotor(false);
		}
		
		else {
			
			this.kneeJoint.enableMotor(true);
			this.kneeJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setOutputs(ArrayList<Float> outputs) {
		
		this.setThighSpeed(outputs.get(0));
		this.setKneeSpeed(outputs.get(1));
		
	}
	
	public void destroy() {
		
		for (RevoluteJoint joint : this.joints)
			this.world.destroyJoint(joint);
		
		for (Box box : this.bodyParts)
			this.world.destroyDisplayableBody(box);
		
	}
	
	public float getProgress() {
		return (float) this.body.getRectangle().getX();
	}

}
