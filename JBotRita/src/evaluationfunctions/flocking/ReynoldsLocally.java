package evaluationfunctions.flocking;

import java.util.ArrayList;

import mathutils.Vector2d;
import sensors.RobotSensorWithSources;
import simulation.Simulator;
import simulation.physicalobjects.PhysicalObject;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;
import evolutionaryrobotics.evaluationfunctions.EvaluationFunction;

public class ReynoldsLocally extends EvaluationFunction {
	
	protected double fitnessForAlignment, fitnessForCohesion, fitnessForMovement,fitnessForSeparation;
	
	protected int numberCollisions;

	protected double currentAlignment;
	
	protected double currentCohesion = 0;
	
	@ArgumentsAnnotation(name="cohensionDistance", defaultValue="0.25")	
	protected double cohensionDistance;
	
	@ArgumentsAnnotation(name="separationFactor", defaultValue="1")	
	protected double separationFactor;
	
	protected double currentMovement;
	
	protected Simulator simulator;
	
	protected ArrayList<Robot> robots;

	public ReynoldsLocally(Arguments args) {
		super(args);
		cohensionDistance = args.getArgumentIsDefined("cohensionDistance") ? args
				.getArgumentAsDouble("cohensionDistance") : 0.25;
		separationFactor=args.getArgumentIsDefined("separationFactor") ? args
					.getArgumentAsDouble("separationFactor") : 1.0;
	}
	
	public double getFitness() {
		return fitnessForAlignment/simulator.getTime() + fitnessForCohesion/simulator.getTime()-separationFactor*fitnessForSeparation/simulator.getTime()+fitnessForMovement ;
	}
	
	@Override
	public void update(Simulator simulator) {
		this.simulator=simulator;
		init();
		for(int i = 0 ; i <  robots.size() ; i++) {
			Robot robot=robots.get(i);
			
			alignment(robot);
			cohesion( i,robot);
			separation(robot);

			movement(robot);					

		}
		
		computeSwarmFitnessOfEachRule();
	}
	

	protected void init(){
		numberCollisions=0;
		currentAlignment=0;
		currentCohesion = 0;
		currentMovement=0.0;
		robots = simulator.getRobots();
	}
	
	protected void separation(Robot robot){ 
		if(robot.isInvolvedInCollison()){    
			numberCollisions++;
		}

	}
	
	protected void alignment(Robot robot){
		
		Vector2d robotPosition=robot.getPosition();
		double cos=0;
		double sen=0;
		double numberOfNeighboursInRange=0;
		
		for(int j = 0 ; j < robots.size() ; j++) {  
			Robot neighbour=robots.get(j);

			if(robotPosition.distanceTo(neighbour.getPosition())<=cohensionDistance){				
				double angleOfRobot=neighbour.getOrientation();
				cos+=Math.cos(angleOfRobot);  
				sen+=Math.sin(angleOfRobot);
				numberOfNeighboursInRange++;
				
			}
		}
		if(numberOfNeighboursInRange>1){
			currentAlignment+=Math.sqrt(cos*cos+ sen*sen)/numberOfNeighboursInRange;
		}

	}
	
	protected void cohesion(int i, Robot robot){
		Vector2d robotPosition=robot.getPosition();
		double centerOfMassX=0, centerOfMassY=0,numberOfNeighboursInRange=0;
		for(int j = 0 ; j < robots.size() ; j++) {  
			Vector2d neighbourPosition=robots.get(j).getPosition();
			if(robotPosition.distanceTo(neighbourPosition)<=cohensionDistance){
				if ( i!= j) {
					centerOfMassX+=neighbourPosition.x;
					centerOfMassY+=neighbourPosition.y;
					numberOfNeighboursInRange++;
				}
			}
		}
		Vector2d centerOfMass=new Vector2d(centerOfMassX/numberOfNeighboursInRange, centerOfMassY/numberOfNeighboursInRange);
		double distanceToCenterMass=robotPosition.distanceTo(centerOfMass);
		if(numberOfNeighboursInRange>=1){ //only scores cohesion, if they are within a given distance
			currentCohesion+=1-distanceToCenterMass/cohensionDistance;	
		}	
	}
	
	protected void movement(Robot robot){ //robot getting farther away from the center (0,0)
		double percentageOfDistanceToCenter=robot.getPosition().distanceTo(new Vector2d(0,0))/simulator.getEnvironment().getWidth();
		currentMovement+= percentageOfDistanceToCenter>1? 1:percentageOfDistanceToCenter;
	}
	
	
	protected void computeSwarmFitnessOfEachRule(){
		computeFitnessForAlignment();
		computeFitnessForCohesion();
		computeFitnessForSeparation();
		computeFitnessForMovement();
	}
	
	protected void computeFitnessForAlignment(){
		fitnessForAlignment+= currentAlignment/ robots.size();
	}
	
	protected void computeFitnessForCohesion(){
		fitnessForCohesion+= currentCohesion/ robots.size();
	}
	
	protected void computeFitnessForSeparation(){
		fitnessForSeparation += (double) numberCollisions/ robots.size();
	}
	
	protected void computeFitnessForMovement(){
		double avarage_MovementContribution = currentMovement/ robots.size();
		//if (avarage_MovementContribution > fitnessForMovement)
		fitnessForMovement = avarage_MovementContribution;	
	}
	
	protected boolean contains(PhysicalObject [] neighboursPerceived, Robot neighbour){
		for (PhysicalObject neighbourPerceived: neighboursPerceived){
			if(neighbourPerceived!=null && neighbourPerceived.equals(neighbour)){
				return true;
			}
		}
		return false;
	}
	
	
}