package application;

import java.util.ArrayList;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import box2DTemplate.Box;
import box2DTemplate.Box2DHelper;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Person {
	
	public Box body;
	public Box head;
	public Box leftThigh;
	public Box rightThigh;
	public Box leftLowerLeg;
	public Box rightLowerLeg;
	public RevoluteJoint leftBodyThighJoint;
	public RevoluteJoint rightBodyThighJoint;
	public RevoluteJoint leftKneeJoint;
	public RevoluteJoint rightKneeJoint;
	public ArrayList<Box> bodyParts;
	public ArrayList<RevoluteJoint> joints;
	public Box2DHelper world;
	
	public static final float startingXPosition = 500;
	public static final float maxMotorSpeed = 100;
	public static final float maxMotorTorque = 100;
	public static final float upperAngle = 60;
	public static final float lowerAngle = -60;
	public static final String DoNotTouch = "Do not touch ground";
	public static final int[] neuralNetwork = new int[] {11, 20, 4};
	
	public Person(Box2DHelper world, Pane root) {
		
		this.world = world;
		this.head = new Box(startingXPosition, 500, 20, 20, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), DoNotTouch, world, root);
		this.leftThigh = new Box(startingXPosition, 500, 10, 35, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "Left Thigh", world, root);
		this.rightThigh = new Box(startingXPosition, 500, 10, 35, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "Right Thigh", world, root);
		this.leftLowerLeg = new Box(startingXPosition, 530, 10, 35, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "Left Lower Leg", world, root);
		this.rightLowerLeg = new Box(startingXPosition, 530, 10, 35, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), "Right Lower Leg", world, root);
		this.body = new Box(startingXPosition, 500, 50, 50, BodyType.DYNAMIC, 0, 1, 0.5f, 0.4f, Color.gray(0.5, 0.4), DoNotTouch, world, root);
		
		WeldJointDef neck = new WeldJointDef();
		neck.bodyA = this.head.getMainBody();
		neck.bodyB = this.body.getMainBody();
		neck.collideConnected = true;
		neck.dampingRatio = 1;
		neck.frequencyHz = 0;
		neck.localAnchorA.set(0, -this.head.getWorldWidth() / 2);
		neck.localAnchorB.set(0, this.body.getWorldHeight() / 2);
		world.createJoint(neck);
		
		RevoluteJointDef hinge = new RevoluteJointDef();
		hinge.bodyA = this.body.getMainBody();
		hinge.bodyB = this.leftThigh.getMainBody();
		hinge.collideConnected = false;
		hinge.localAnchorA.set(-this.body.getWorldWidth() / 2 + this.leftThigh.getWorldWidth() / 2, -this.body.getWorldHeight() / 2);
		hinge.localAnchorB.set(0, this.leftThigh.getWorldHeight() / 2 - this.leftThigh.getWorldWidth() / 2);
		hinge.enableLimit = true;
		hinge.upperAngle = (float) Math.toRadians(upperAngle);
		hinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		hinge.enableMotor = true;
		hinge.maxMotorTorque = maxMotorTorque;
		hinge.motorSpeed = 0;
		this.leftBodyThighJoint = (RevoluteJoint) world.createJoint(hinge);
		
		hinge = new RevoluteJointDef();
		hinge.bodyA = this.body.getMainBody();
		hinge.bodyB = this.rightThigh.getMainBody();
		hinge.collideConnected = false;
		hinge.localAnchorA.set(this.body.getWorldWidth() / 2 - this.rightThigh.getWorldWidth() / 2, -this.body.getWorldHeight() / 2);
		hinge.localAnchorB.set(0, this.rightThigh.getWorldHeight() / 2 - this.rightThigh.getWorldWidth() / 2);
		hinge.enableLimit = true;
		hinge.upperAngle = (float) Math.toRadians(upperAngle);
		hinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		hinge.enableMotor = true;
		hinge.maxMotorTorque = maxMotorTorque;
		hinge.motorSpeed = 0;
		this.rightBodyThighJoint = (RevoluteJoint) world.createJoint(hinge);
		
		RevoluteJointDef kneeHinge = new RevoluteJointDef();
		kneeHinge.bodyA = this.leftThigh.getMainBody();
		kneeHinge.bodyB = this.leftLowerLeg.getMainBody();
		kneeHinge.collideConnected = false;
		kneeHinge.localAnchorA.set(0, -this.leftThigh.getWorldHeight() / 2);
		kneeHinge.localAnchorB.set(0, this.leftLowerLeg.getWorldHeight() / 2);
		kneeHinge.enableLimit = true;
		kneeHinge.upperAngle = (float) Math.toRadians(upperAngle);
		kneeHinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		kneeHinge.enableMotor = true;
		kneeHinge.maxMotorTorque = maxMotorTorque;
		kneeHinge.motorSpeed = 0;
		this.leftKneeJoint = (RevoluteJoint) world.createJoint(kneeHinge);
		
		kneeHinge = new RevoluteJointDef();
		kneeHinge.bodyA = this.rightThigh.getMainBody();
		kneeHinge.bodyB = this.rightLowerLeg.getMainBody();
		kneeHinge.collideConnected = false;
		kneeHinge.localAnchorA.set(0, -this.rightThigh.getWorldHeight() / 2);
		kneeHinge.localAnchorB.set(0, this.rightLowerLeg.getWorldHeight() / 2);
		kneeHinge.enableLimit = true;
		kneeHinge.upperAngle = (float) Math.toRadians(upperAngle);
		kneeHinge.lowerAngle = (float) Math.toRadians(lowerAngle);
		kneeHinge.enableMotor = true;
		kneeHinge.maxMotorTorque = maxMotorTorque;
		kneeHinge.motorSpeed = 0;
		this.rightKneeJoint = (RevoluteJoint) world.createJoint(kneeHinge);
		
		this.collisionFiltering();
		
		this.bodyParts = new ArrayList<Box>();
		this.bodyParts.add(this.body);
		this.bodyParts.add(this.head);
		this.bodyParts.add(this.leftThigh);
		this.bodyParts.add(this.rightThigh);
		this.bodyParts.add(this.leftLowerLeg);
		this.bodyParts.add(this.rightLowerLeg);
		
		this.joints = new ArrayList<RevoluteJoint>();
		this.joints.add(this.leftBodyThighJoint);
		this.joints.add(this.rightBodyThighJoint);
		this.joints.add(this.leftKneeJoint);
		this.joints.add(this.rightKneeJoint);
				
	}
	
	private void collisionFiltering() {
		
		this.body.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.head.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.leftThigh.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.rightThigh.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.leftLowerLeg.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		this.rightLowerLeg.getMainBody().m_fixtureList.m_filter.groupIndex = -1;
		
	}
	
	public float getLeftBodyThighJointAngle() {
		return this.leftBodyThighJoint.getJointAngle();
	}
	
	public float getLeftBodyThighMotorSpeed() {
		return this.leftBodyThighJoint.getJointSpeed();
	}
	
	public float getRightBodyThighJointAngle() {
		return this.rightBodyThighJoint.getJointAngle();
	}
	
	public float getRightBodyThighMotorSpeed() {
		return this.rightBodyThighJoint.getJointSpeed();
	}
	
	public float getLeftKneeJointAngle() {
		return this.leftKneeJoint.getJointAngle();
	}
	
	public float getLeftKneeMotorSpeed() {
		return this.leftKneeJoint.getJointSpeed();
	}
	
	public float getRightKneeJointAngle() {
		return this.rightKneeJoint.getJointAngle();
	}
	
	public float getRightKneeMotorSpeed() {
		return this.rightKneeJoint.getJointSpeed();
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
		
		float[] inputs = new float[11];
		
		inputs[0] = map(this.getLeftBodyThighJointAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[1] = map(this.getLeftBodyThighMotorSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[2] = map(this.getRightBodyThighJointAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[3] = map(this.getRightBodyThighMotorSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[4] = map(this.getLeftKneeJointAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[5] = map(this.getLeftKneeMotorSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[6] = map(this.getRightKneeJointAngle(), (float) Math.toRadians(-180), (float) Math.toRadians(180), -1, 1);
		inputs[7] = map(this.getRightKneeMotorSpeed(), -Person.maxMotorSpeed, Person.maxMotorSpeed, -1, 1);
		inputs[8] = map((float) (this.getRotation() % Math.toRadians(180)), (float) -Math.toRadians(180), (float) Math.toRadians(180), -1, 1);
		inputs[9] = map(this.ifTouchingGround() ? 1 : 0, 0, 1, -1, 1);
		inputs[10] = map(this.getHeightAboveGround(), 0, Main.height / 2, 1, -1);
		
		return inputs;
		
	}
	
	public void setLeftBodyThighJointSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.leftBodyThighJoint.enableMotor(false);
		}
		
		else {
			
			this.leftBodyThighJoint.enableMotor(true);
			this.leftBodyThighJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setRightBodyThighJointSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.rightBodyThighJoint.enableMotor(false);
		}
		
		else {
			
			this.rightBodyThighJoint.enableMotor(true);
			this.rightBodyThighJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setLeftKneeJointSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.leftKneeJoint.enableMotor(false);
		}
		
		else {
			
			this.leftKneeJoint.enableMotor(true);
			this.leftKneeJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setRightKneeJointSpeed(float rawMotorSpeed) {
		
		float motorSpeedRounded = (float) Math.round(rawMotorSpeed * 1000.0f) / 1000.0f;
		
		if (motorSpeedRounded == 0) {
			this.rightKneeJoint.enableMotor(false);
		}
		
		else {
			
			this.rightKneeJoint.enableMotor(true);
			this.rightKneeJoint.setMotorSpeed(motorSpeedRounded * maxMotorSpeed);
			
		}
		
	}
	
	public void setOutputs(ArrayList<Float> outputs) {
		
		this.setLeftBodyThighJointSpeed(outputs.get(0));
		this.setRightBodyThighJointSpeed(outputs.get(1));
		this.setLeftKneeJointSpeed(outputs.get(2));
		this.setRightKneeJointSpeed(outputs.get(3));
		
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
