-------------------------------------------------------------------
-             Block-Based Context Sensitive Testing               -
-                          Version  1.0.0                         -
-------------------------------------------------------------------
Build:
The maven goal "package" is responsible to build the JAR file of the solution.

Usage:
1. Add resulting JAR to an Android Testing Project

2. Adjust AndroidManifest.xml of the application under test
	Add the following lines for changing the mobile internet context:
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>

3. Implement a class extending ContextMutationTestCase for the block based context sensitive test case
	override the method getBlocks() using anonymous inner classes of the class Block

4. Generate tests into a JUnit test suite using a MutationTestSuite together with the implemented class and a TestPlanGenerator

5. Run test suite like any 'normal' JUnit test suite for Android (in Eclipse: Run As -> Android JUnit Test)

-------------------------------------------------------------------
