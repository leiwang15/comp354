package comp354.pm

import org.junit.runner.RunWith
import org.specs2._
import org.specs2.runner.JUnitRunner
import comp354.pm.Model._
  

/**
 * 
 * This specification can be executed with: mvn test
 *
 * For more information on how to write or run specifications, please visit: 
 *   http://etorreborre.github.com/specs2/guide/org.specs2.guide.Runners.html
 *
 */
@RunWith(classOf[JUnitRunner])
class CreateProjectSpecs extends mutable.Specification { 
  "A normal flow for creating projects should" >> {
    "Login to the program using your username and password" >> {
      val (first, last, role) = ("test", "testson", "manager")
      user = new User(first, last, role)
      todo
    }
    "Verify that the account logging in is a manager account" >> todo
    "Enter the name of the new project you wish to create" >> todo
    "Validate the name of the new project" >> todo
    "Assign team members to the project" >> todo
    "Confirm the creation of the project has persisted" >> todo
  }
  "An alternative flow for creating projects should" >> {
    "Not commit an action when the user chooses to cancel" >> todo
    "Resume when the user chooses not to cancel" >> todo
  }
  
  var user:User = null
  var project:Project = null
}
@RunWith(classOf[JUnitRunner])
class DeleteProjectSpecs extends mutable.Specification { 
  "A normal flow for deleting projects should" >> {
    "Login to the program using your username and password" >> {
      val (first, last, role) = ("test", "testson", "manager")
      user = new User(first, last, role)
      todo
    }
    "Verify that the account logging in is a manager account" >> todo
    "Enter the name of the new project you wish to delete" >> todo
    "Validate the name of the project to delete" >> todo
    "Confirm the deletion of the project has persisted" >> todo
  }
  "An alternative flow for deleting projects should" >> {
    "Not commit an action when the user chooses to cancel" >> todo
    "Resume when the user chooses not to cancel" >> todo
  }
  
  var user:User = null
  var project:Project = null
}
@RunWith(classOf[JUnitRunner])
class BusinessRulesSpecs extends mutable.Specification { 
  "BR1 should" >> {
    "Check that the username and password must be linked to an account made for the program" >> todo
    "Check that the username and password fields are not empty" >> todo
  }
  "BR2 should" >> {
    "Check that the manager services are accessed by manager roled users" >> todo
  }
  "BR3 should" >> {
    "Check that the project name doesn't already exist" >> todo
  }
  "BR4 should" >> {
    "Check that the project name is valid" >> todo
    "Check that the project name is not empty" >> todo
  }
}
