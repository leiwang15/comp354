package comp354.pm.model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.SQLiteDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(Activity.schema, ActivityAssign.schema, ActivityPre.schema, Project.schema, ProjectAssign.schema, User.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Activity
   *  @param activityid Database column ActivityID SqlType(INTEGER)
   *  @param projectId Database column Project_ID SqlType(INTEGER)
   *  @param name Database column Name SqlType(TEXT)
   *  @param desc Database column Desc SqlType(TEXT)
   *  @param duration Database column Duration SqlType(INT)
   *  @param progress Database column Progress SqlType(INT), Default(Some(0))
   *  @param finished Database column Finished SqlType(INT), Default(Some(0)) */
  case class ActivityRow(activityid: Option[Int], projectId: Int, name: String, desc: String, duration: Int, progress: Option[Int] = Some(0), finished: Option[Int] = Some(0))
  /** GetResult implicit for fetching ActivityRow objects using plain SQL queries */
  implicit def GetResultActivityRow(implicit e0: GR[Option[Int]], e1: GR[Int], e2: GR[String]): GR[ActivityRow] = GR{
    prs => import prs._
    ActivityRow.tupled((<<?[Int], <<[Int], <<[String], <<[String], <<[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table Activity. Objects of this class serve as prototypes for rows in queries. */
  class Activity(_tableTag: Tag) extends Table[ActivityRow](_tableTag, "Activity") {
    def * = (activityid, projectId, name, desc, duration, progress, finished) <> (ActivityRow.tupled, ActivityRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (activityid, Rep.Some(projectId), Rep.Some(name), Rep.Some(desc), Rep.Some(duration), progress, finished).shaped.<>({r=>import r._; _2.map(_=> ActivityRow.tupled((_1, _2.get, _3.get, _4.get, _5.get, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ActivityID SqlType(INTEGER) */
    val activityid: Rep[Option[Int]] = column[Option[Int]]("ActivityID", O.PrimaryKey, O.AutoInc)
    /** Database column Project_ID SqlType(INTEGER) */
    val projectId: Rep[Int] = column[Int]("Project_ID")
    /** Database column Name SqlType(TEXT) */
    val name: Rep[String] = column[String]("Name")
    /** Database column Desc SqlType(TEXT) */
    val desc: Rep[String] = column[String]("Desc")
    /** Database column Duration SqlType(INT) */
    val duration: Rep[Int] = column[Int]("Duration")
    /** Database column Progress SqlType(INT), Default(Some(0)) */
    val progress: Rep[Option[Int]] = column[Option[Int]]("Progress", O.Default(Some(0)))
    /** Database column Finished SqlType(INT), Default(Some(0)) */
    val finished: Rep[Option[Int]] = column[Option[Int]]("Finished", O.Default(Some(0)))

    /** Foreign key referencing Project (database name Project_FK_1) */
    lazy val projectFk = foreignKey("Project_FK_1", Rep.Some(projectId), Project)(r => r.projectid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Activity */
  lazy val Activity = new TableQuery(tag => new Activity(tag))

  /** Entity class storing rows of table ActivityAssign
   *  @param aaId Database column AA_ID SqlType(INTEGER)
   *  @param activityId Database column Activity_ID SqlType(INTEGER)
   *  @param userId Database column User_ID SqlType(INTEGER) */
  case class ActivityAssignRow(aaId: Option[Int], activityId: Int, userId: Int)
  /** GetResult implicit for fetching ActivityAssignRow objects using plain SQL queries */
  implicit def GetResultActivityAssignRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[ActivityAssignRow] = GR{
    prs => import prs._
    ActivityAssignRow.tupled((<<?[Int], <<[Int], <<[Int]))
  }
  /** Table description of table Activity_Assign. Objects of this class serve as prototypes for rows in queries. */
  class ActivityAssign(_tableTag: Tag) extends Table[ActivityAssignRow](_tableTag, "Activity_Assign") {
    def * = (aaId, activityId, userId) <> (ActivityAssignRow.tupled, ActivityAssignRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (aaId, Rep.Some(activityId), Rep.Some(userId)).shaped.<>({r=>import r._; _2.map(_=> ActivityAssignRow.tupled((_1, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column AA_ID SqlType(INTEGER) */
    val aaId: Rep[Option[Int]] = column[Option[Int]]("AA_ID", O.PrimaryKey, O.AutoInc)
    /** Database column Activity_ID SqlType(INTEGER) */
    val activityId: Rep[Int] = column[Int]("Activity_ID")
    /** Database column User_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("User_ID")

    /** Foreign key referencing Activity (database name Activity_FK_1) */
    lazy val activityFk = foreignKey("Activity_FK_1", Rep.Some(activityId), Activity)(r => r.activityid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name User_FK_2) */
    lazy val userFk = foreignKey("User_FK_2", Rep.Some(userId), User)(r => r.userid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ActivityAssign */
  lazy val ActivityAssign = new TableQuery(tag => new ActivityAssign(tag))

  /** Entity class storing rows of table ActivityPre
   *  @param apId Database column AP_ID SqlType(INTEGER)
   *  @param activityId1 Database column Activity_ID1 SqlType(INTEGER)
   *  @param activityId2 Database column Activity_ID2 SqlType(INTEGER) */
  case class ActivityPreRow(apId: Option[Int], activityId1: Int, activityId2: Int)
  /** GetResult implicit for fetching ActivityPreRow objects using plain SQL queries */
  implicit def GetResultActivityPreRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[ActivityPreRow] = GR{
    prs => import prs._
    ActivityPreRow.tupled((<<?[Int], <<[Int], <<[Int]))
  }
  /** Table description of table Activity_Pre. Objects of this class serve as prototypes for rows in queries. */
  class ActivityPre(_tableTag: Tag) extends Table[ActivityPreRow](_tableTag, "Activity_Pre") {
    def * = (apId, activityId1, activityId2) <> (ActivityPreRow.tupled, ActivityPreRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (apId, Rep.Some(activityId1), Rep.Some(activityId2)).shaped.<>({r=>import r._; _2.map(_=> ActivityPreRow.tupled((_1, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column AP_ID SqlType(INTEGER) */
    val apId: Rep[Option[Int]] = column[Option[Int]]("AP_ID", O.PrimaryKey, O.AutoInc)
    /** Database column Activity_ID1 SqlType(INTEGER) */
    val activityId1: Rep[Int] = column[Int]("Activity_ID1")
    /** Database column Activity_ID2 SqlType(INTEGER) */
    val activityId2: Rep[Int] = column[Int]("Activity_ID2")

    /** Foreign key referencing Activity (database name Activity_FK_1) */
    lazy val activityFk = foreignKey("Activity_FK_1", (Rep.Some(activityId2), Rep.Some(activityId1)), Activity)(r => (r.activityid, r.activityid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ActivityPre */
  lazy val ActivityPre = new TableQuery(tag => new ActivityPre(tag))

  /** Entity class storing rows of table Project
   *  @param projectid Database column ProjectID SqlType(INTEGER)
   *  @param name Database column Name SqlType(TEXT)
   *  @param desc Database column Desc SqlType(TEXT)
   *  @param startdate Database column StartDate SqlType(DATE)
   *  @param enddate Database column EndDate SqlType(DATE)
   *  @param finished Database column Finished SqlType(INT), Default(Some(0)) */
  case class ProjectRow(projectid: Option[Int], name: String, desc: String, startdate: java.sql.Date, enddate: java.sql.Date, finished: Option[Int] = Some(0))
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Option[Int]], e1: GR[String], e2: GR[java.sql.Date]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<?[Int], <<[String], <<[String], <<[java.sql.Date], <<[java.sql.Date], <<?[Int]))
  }
  /** Table description of table Project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends Table[ProjectRow](_tableTag, "Project") {
    def * = (projectid, name, desc, startdate, enddate, finished) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (projectid, Rep.Some(name), Rep.Some(desc), Rep.Some(startdate), Rep.Some(enddate), finished).shaped.<>({r=>import r._; _2.map(_=> ProjectRow.tupled((_1, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ProjectID SqlType(INTEGER) */
    val projectid: Rep[Option[Int]] = column[Option[Int]]("ProjectID", O.PrimaryKey, O.AutoInc)
    /** Database column Name SqlType(TEXT) */
    val name: Rep[String] = column[String]("Name")
    /** Database column Desc SqlType(TEXT) */
    val desc: Rep[String] = column[String]("Desc")
    /** Database column StartDate SqlType(DATE) */
    val startdate: Rep[java.sql.Date] = column[java.sql.Date]("StartDate")
    /** Database column EndDate SqlType(DATE) */
    val enddate: Rep[java.sql.Date] = column[java.sql.Date]("EndDate")
    /** Database column Finished SqlType(INT), Default(Some(0)) */
    val finished: Rep[Option[Int]] = column[Option[Int]]("Finished", O.Default(Some(0)))
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table ProjectAssign
   *  @param paId Database column PA_ID SqlType(INTEGER)
   *  @param projectId Database column Project_ID SqlType(INTEGER)
   *  @param userId Database column User_ID SqlType(INTEGER) */
  case class ProjectAssignRow(paId: Option[Int], projectId: Int, userId: Int)
  /** GetResult implicit for fetching ProjectAssignRow objects using plain SQL queries */
  implicit def GetResultProjectAssignRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[ProjectAssignRow] = GR{
    prs => import prs._
    ProjectAssignRow.tupled((<<?[Int], <<[Int], <<[Int]))
  }
  /** Table description of table Project_Assign. Objects of this class serve as prototypes for rows in queries. */
  class ProjectAssign(_tableTag: Tag) extends Table[ProjectAssignRow](_tableTag, "Project_Assign") {
    def * = (paId, projectId, userId) <> (ProjectAssignRow.tupled, ProjectAssignRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (paId, Rep.Some(projectId), Rep.Some(userId)).shaped.<>({r=>import r._; _2.map(_=> ProjectAssignRow.tupled((_1, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PA_ID SqlType(INTEGER) */
    val paId: Rep[Option[Int]] = column[Option[Int]]("PA_ID", O.PrimaryKey, O.AutoInc)
    /** Database column Project_ID SqlType(INTEGER) */
    val projectId: Rep[Int] = column[Int]("Project_ID")
    /** Database column User_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("User_ID")

    /** Foreign key referencing Project (database name Project_FK_1) */
    lazy val projectFk = foreignKey("Project_FK_1", Rep.Some(projectId), Project)(r => r.projectid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name User_FK_2) */
    lazy val userFk = foreignKey("User_FK_2", Rep.Some(userId), User)(r => r.userid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ProjectAssign */
  lazy val ProjectAssign = new TableQuery(tag => new ProjectAssign(tag))

  /** Entity class storing rows of table User
   *  @param userid Database column UserID SqlType(INTEGER)
   *  @param firstname Database column FirstName SqlType(TEXT)
   *  @param lastname Database column LastName SqlType(TEXT)
   *  @param role Database column Role SqlType(TEXT)
   *  @param username Database column UserName SqlType(TEXT)
   *  @param password Database column Password SqlType(TEXT) */
  case class UserRow(userid: Option[Int], firstname: String, lastname: String, role: String, username: String, password: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Option[Int]], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<?[Int], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table User. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "User") {
    def * = (userid, firstname, lastname, role, username, password) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userid, Rep.Some(firstname), Rep.Some(lastname), Rep.Some(role), Rep.Some(username), Rep.Some(password)).shaped.<>({r=>import r._; _2.map(_=> UserRow.tupled((_1, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column UserID SqlType(INTEGER) */
    val userid: Rep[Option[Int]] = column[Option[Int]]("UserID", O.PrimaryKey, O.AutoInc)
    /** Database column FirstName SqlType(TEXT) */
    val firstname: Rep[String] = column[String]("FirstName")
    /** Database column LastName SqlType(TEXT) */
    val lastname: Rep[String] = column[String]("LastName")
    /** Database column Role SqlType(TEXT) */
    val role: Rep[String] = column[String]("Role")
    /** Database column UserName SqlType(TEXT) */
    val username: Rep[String] = column[String]("UserName")
    /** Database column Password SqlType(TEXT) */
    val password: Rep[String] = column[String]("Password")
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
