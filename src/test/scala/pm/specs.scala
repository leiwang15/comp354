package pm

import org.junit.runner.RunWith
import org.specs2.Specification
import org.specs2.runner.JUnitRunner
  

/**
 * 
 * This specification can be executed with: scala -cp <your classpath=""> ${package}.SpecsTest
 * Or using maven: mvn test
 *
 * For more information on how to write or run specifications, please visit: 
 *   http://etorreborre.github.com/specs2/guide/org.specs2.guide.Runners.html
 *
 */
@RunWith(classOf[JUnitRunner])
class specs extends Specification { def is = s2"""

This is the Project Specification for the COMP 354 Project.

 Use case for Create
1)  Log in to the program using your username and password. $login
2)  Verify that the account logging in is a manager account. $isManager
3)  Enter the name of the new project you wish to create. $nameProject
4)  Validate the name of the new project. $validateProjectName
5)  Assign team members to the project. $assignMembers
6)  Use case complete

Alternative clause: The user name and passwords do not match $badLogin
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account. 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty $emptyLogin
2b.1) the program will ask the user to enter a username and a password in the fields 
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account $badRoleLogin
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation $cancel
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation $dontCancel
*a.1) the program will resume from the step where the cancel was attempted.

Alternative clause:  if the name already exists $duplicate
4a.1) the program will tell the user that there is a project that already has that name and it cannot be used.
4a.2) the program will then continue from use case step 3

Alternative clause:  if the name is invalid (empty or otherwise) $badName
4a.1) the program will tell the user that there is a project that the project name is invalid and cannot be used.
4a.2) the program will then continue from use case step 3

Use case for Delete
1)  Log in to the program using your username and password
2)  Verify that the account logging in is a manager account.
3)  Enter the name of the new project you wish to delete.
4)  Confirm that the project is the correct one $confirmProject
5)  Validate the deletion $confirmDeleteWizard
6)  Project is deleted. $delete
7)  Use case complete


Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account

2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative clause: the user is not a manager of the project
5a.1) the program will tell the user that they do not have permission to delete the project
5a.2) the program will resume from use case step 3

Alternative clause: The user decided to click no at the confirmation of deletion $cancelDeleteWizard
4a.1) the program resumes from use case step 3.

Use case for edit
1)  Log in to the program using your username and password
2)  Verify that the account logging in is a manager account.
3)  Enter the name of the new project you wish to edit.
4)  Validate the name of the project
5)  Edit the project that you chose. $edit
6)  Use case complete

Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative clause:  the user is not a manager of the current project $permissions
5a.1) the program will tell the user that they do not have permission to alter this project
5a.2) the program will resume at use case step 3.

US 2: As a project manager, I want to associate multiple interdependent activities with my project.   
Use case where you are creating a node 
1)  Log in to the program using your username and password
2)  Verify that the account
3)  The user selects a project that they want to work on
4)  Verify that the user has permission to work on the project
5)  The user creates a node
6)  The user specifies all of the requirements within the node
7)  Verify
8)  Use Case complete.

Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative case: the user clicks a project that they are not allowed to work on
4a.1) the program will tell the user that they do not have permission to work on that project and will ask the user to choose a new project.
4a.2) the use case resumes from step 4.

Alternative case: the user leaves the node empty
5a.1) the program will tell the user that there is a minimum amount of data that needs to be entered into the node before it can be created
5a.2) the use case resumes from step 6

Alternative case: the user creates a project where there is no start date or end date
10a.1) the program tells the user that there is no start date or end date that has been established
10a.2 the use case resumes from step 9.

Use case where you are creating a link between nodes 
1)  Log in to the program using your username and password
2)  Verify that the account
3)  The user selects a project that they want to work on
4)  Verify that the user has permission to work on the project
5)  The user creates a link between 2 or more nodes
6)  Verify links
7)  Use Case complete.

Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative case: the user clicks a project that they are not allowed to work on
4a.1) the program will tell the user that they do not have permission to work on that project and will ask the user to choose a new project.
4a.2) the use case resumes from step 4.

Alternative case: the user chooses no nodes to link
6a.1)  the program tells the user that they have not selected a valid node to link.
6a.2) the use case resumes from step 5.

Alternative case: the user try’s to link a node with something other than a node
6b.1) the program tells the user that they have attempted to link a node with an invalid object.
6b.2) the program resumes from step 5.

Use case where you are editing node
1)  Log in to the program using your username and password
2)  Verify that the account
3)  The user selects a project that they want to work on
4)  Verify that the user has permission to work on the project
5)  The user selects a node.
6)  Verify that selected object is a node
7)  The user edits contents of the node
8)  Verify the changes
9)  Use Case complete.

Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative case: the user has selected to edit something other than a node
6a.1) the program tells the user that they have attempted to edit an invalid object.
6a.2) the program resumes from use case step 5.

Alternative case: The user changes the start date of the node to a time before the start date of the first node in the project.
8a.1) the program tells the user that there cannot be a node that has a start date before the start date of the project
8a.2) the program will ask the user if they would like to make this node the new start node.
8a.3) get confirmation.
8a.4) this node is now the new start node.

Alternative case: The user chooses no when asked to confirm their decision about changing the start node
8a.2.1) the changes are reverted
8a.2.1) the program resumes at use case step 7.

Alternative case: The user changes the end date of the node to a time after the end date of the last node in the project.
8b.1) the program tells the user that there cannot be a node that has an end date after the end date of the project
8b.2) the program will ask the user if they would like to make this node the new end node.
8b.3) get confirmation.
8b.4) this node is now the new end node.

Alternative case: The user chooses no when asked to confirm their decision about changing the end node
8b.2.1) the changes are reverted
8b.2.2) the program resumes at use case step 7.

Alternative case: the user tried to put invalid characters within the nodes
8c.1) the program tells the user that they have not entered a valid input
8c.2) the program resumes from use case 7

Alternative case: the user changes all values within the node to empty
8d.1) the program tells the user that they have set the node to be empty
8d.2) the program asks the user if they would like to delete the node
8d.3) the program gets confirmation.
8d.4) the node is deleted.

Alternative case: the user chooses no when asked to delete the node
8d.3.1) the changes are reverted
8d.3.2) The program resumes from use case step 7.

US 3: As a project manager, I want to assign resources (project members) and project-relevant properties to my activities.
Use case of add project member 
1)  Log in to the program using your username and password
2)  Verify that the account
3)  The user selects a project that they want to work on
4)  Verify that the user has permission to work on the project
5)  The user chooses an activity
6)  Verify
7)  The user adds team members to the activity
8)  Verify that the user can be added to this activity
9)  Use Case complete.

Alternative clause: The user name and passwords do not match
2a.1) if the username and the passwords do not match an account then the program will ask you to enter a valid account 
2a.2) the program resumes from use case step 1

Alternative clause: the user name or password field is empty
2b.1) the program will ask the user to enter a username and a password in the fields
2b.2) the program will then resume from step 1.

Alternative clause: the account that will be logged in is not a manager account
2c.1) the program tells the user that the account that is being logged on is not the correct type of account
2c.2) the program then resumes from use case step 1

Alternative clause: The user chooses to cancel the operation
*.1) the program will ask for confirmation
*.2) the operation cancels and the program returns to the home screen the use case then fails.

Alternative clause: The user chooses not to cancel the operation
*a.1) the program will resume from the step where the cancel was attempted.

Alternative case: the user clicks a project that they are not allowed to work on
4a.1) the program will tell the user that they do not have permission to work on that project and will ask the user to choose a new project.
4a.2) the use case resumes from step 4.

Alternative case: the user chooses a node that cannot have people added to it
6a.1) the program tells the user that they are not allowed to assign team members to that node
6a.2) the program resumes from step 5.

Alternative case: the user tries to add a member who is not part of the project
8a.1) the program will tell the user that the person they are trying to enter is not part of the project.
8a.2) the use case then resumes form step 7.

Alternative case: The user tries to add no one
8b.1) the program will tell the user that they have not entered a valid team member.
8b.2) the use case then resumes form step 7.

Alternative case: The user tries to add someone who is already assigned to another project at the same time
8c.1) the program tells the user that they are unable to join this activity due to them being on another at the same time.
8c.2) the use case then resumes at step 7.
$ok
                                 """

  def login = failure
  def isManager = failure
  def nameProject = failure
  def validateProjectName = failure
  def assignMembers = failure
  def badLogin = failure
  def emptyLogin = failure
  def badRoleLogin = failure
  def cancel = failure
  def dontCancel = failure
  def duplicate = failure
  def badName = failure
  def confirmProject = failure
  def confirmDeleteWizard = failure
  def delete = failure
  def cancelDeleteWizard = failure
  def edit = failure
  def permissions = failure
}
