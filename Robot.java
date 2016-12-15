
package org.usfirst.frc.team5496.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team5496.robot.commands.ExampleCommand;
import org.usfirst.frc.team5496.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;

    Joystick myJoystick = new Joystick(0);
	Joystick myJoystick2 = new Joystick(1);
	Talon myTalonLeft1 = new Talon(2);
	Talon myTalonLeft2 =  new Talon(3);
	Talon myTalonRight1 =  new Talon(0);
	Talon myTalonRight2 =  new Talon(1);
	public CANTalon bottomShootTalon = new CANTalon(2);
	public CANTalon topShootTalon = new CANTalon(3);
	public CANTalon OutakeTalon = new CANTalon(0);
	public CANTalon IntakeTalon = new CANTalon(4);
	public CANTalon RampTalon = new CANTalon(1);
	public CANTalon Arm = new CANTalon(5);
	Timer time = new Timer();
	
    DoubleSolenoid  mySolenoid = new DoubleSolenoid(0, 1);
	
	double autoTime;
	boolean armUp = true;
    /**
     * This function is run when the robot is first started ulonap and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 *     Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
	double init_time, curr_time;
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        time.start();
        init_time = Timer.getFPGATimestamp();
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        int flag = 2;
        
        if(flag == 1){
        	autoTime = 0.1;
        }
        else if(flag == 2)
        {
        	autoTime = 3.6;
        }
    }

    /**
     *
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	curr_time = Timer.getFPGATimestamp()-init_time;
        Scheduler.getInstance().run();
        if(curr_time <=  12){
        	myTalonLeft1.set(1);
            myTalonLeft2.set(1);
            myTalonRight1.set(-0.92);
            myTalonRight2.set(-0.92);
            System.out.println(Timer.getMatchTime());      
        }
        else
        {
        	myTalonLeft1.set(0);
            myTalonLeft2.set(0);
            myTalonRight1.set(0);
            myTalonRight2.set(0);
            System.out.println(Timer.getMatchTime()); 
        }
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        System.out.println("Robot running");
       
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        
    	double leftX = myJoystick.getRawAxis(0);
  	  	double leftY = -myJoystick.getRawAxis(1);
  	  //System.out.println(leftX + ", " + leftY); //Leftjoystick
  	 
  	  //right joystick value of X and Y
  	  double rightX = myJoystick.getRawAxis(4);
  	  double rightY = -myJoystick.getRawAxis(5);
  	  //System.out.println(rightX + ", " + rightY);
  	 
  	  //Place to change deadband and the speed of robot
  	 double deadbandx = 0.2;
  	 double deadbandy = 0.1;
  	 double maxSpeedConstantX = 0.3;
  	 double maxSpeedConstantY = 0.5;
  	 
  	 
  	 //Deadband check
  	  if(rightX < deadbandx && rightX > -deadbandx){
  	  
  	   rightX = 0;
  	  }
  	 
  	  if(leftX < deadbandx && leftX > -deadbandx){
  	   leftX = 0;
  	  }
      
  	  if(rightY < deadbandy && rightY > -deadbandy){
  	   rightY = 0;
  	  }
  	 
  	  if(leftY < deadbandy && leftY > -deadbandy){
  	   leftY = 0;
  	  
  	  }
  	  
  	  if(myJoystick.getRawAxis(2) > 0.8 && myJoystick.getRawAxis(3) > 0.8)
  	  {
  		  maxSpeedConstantY = 1;
  	  }
  	  
  	 myTalonLeft1.set(leftY * maxSpeedConstantY);
  	 myTalonLeft2.set(leftY * maxSpeedConstantY);
  	 myTalonRight1.set(-rightY * maxSpeedConstantY * 0.92);
  	 myTalonRight2.set(-rightY * maxSpeedConstantY * 0.92);
	  	 
	  	if(myJoystick.getRawButton(5) && myJoystick.getRawButton(6))
		{
			topShootTalon.set(-1);
			bottomShootTalon.set(0.65);
		}
	  	else
	  	{
	  		topShootTalon.set(0);
			bottomShootTalon.set(0);
	  	}
	  	 
	  	if(myJoystick2.getRawButton(5))
		{
			OutakeTalon.set(0.5);
		//	System.out.println(Timer.getMatchTime());
			
		}
	  	else if(myJoystick2.getRawAxis(2) > 0.8)
		{
			OutakeTalon.set(-0.3);
		}
	  	else
	  	{
	  		OutakeTalon.set(0);
	  	}
	  	
	  	if(myJoystick2.getRawAxis(3) > 0.8){
			IntakeTalon.set(-0.5);
		}
	  	else if(myJoystick2.getRawButton(6)){
	  		IntakeTalon.set(0.5);
	  	}
	  	else
	  	{
	  		IntakeTalon.set(0);
	  	}
	  	//Ramp talon
	  	if (myJoystick2.getRawButton(1) ==true)
	  	{
			mySolenoid.set(DoubleSolenoid.Value.kReverse);
		} 
	  	else 
	  	{
	  		if(myJoystick2.getRawButton(4)== true)
	  		{
	  	   mySolenoid.set(DoubleSolenoid.Value.kForward);
	  		}
	  		}
	  //Latch control 
	  	/*if(myJoystick2.getRawButton(2)){
	  		mySolenoid.set(true);
	  	}
	  	else{
	  		mySolenoid.set(false);
	  	}*/
	  	
	  	//Arm control
	  	//Arm down
	  /*	if(myJoystick2.getRawAxis(1) > 0.2){
	  		Arm.set(0.3);
	  	}
	  	else if(myJoystick2.getRawAxis(1) < -0.2){
	  		Arm.set(-0.8);
	  	}
	  	else{
	  		Arm.set(0);
	  	}       */
	  	
    }    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
