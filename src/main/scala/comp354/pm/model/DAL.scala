package comp354.pm

import slick.driver.SQLiteDriver.api._
import comp354.pm.model.Tables._

package object model {
  private val props = new java.util.Properties { setProperty("date_string_format", "yyyy-MM-dd") }
  val db = Database.forURL(url = "jdbc:sqlite:project.db", driver = "org.sqlite.JDBC", prop = props)

  object UserDAO {
    def add(u: UserRow) = db.run((User returning User.map(_.userid) into ((user, id) => user.copy(userid = id))) += u)

    def delete(id: Int) = db.run(filterQuery(id).delete)

    def filterQuery(id: Int) = User.filter { _.userid === id }

    def getUser(username: String, password: String) = db.run(User.filter { x => (x.username === username) && (x.password === password) }.result.headOption)

    def getRoles = db.run(User.map(_.role).result)
  }

  object ActivityDAO {
    def add(u: ActivityRow) = db.run((Activity returning Activity.map(_.activityid) into ((a, id) => a.copy(activityid = id))) += u)

    def assignToUser(a: ActivityRow, u: UserRow) = db.run(ActivityAssign += ActivityAssignRow(None, a.activityid.get, u.userid.get))
  }

  object ProjectDAO {
    def get(u: UserRow) = db.run(ProjectAssign.filter(_.userId === u.userid).flatMap(_.projectFk).result)

    def get(id: Int): concurrent.Future[Seq[ProjectRow]] = db.run(Project.filter(_.projectid === id).result)

    def add(p: ProjectRow) = db.run((Project returning Project.map(_.projectid) into ((p, id) => p.copy(projectid = id))) += p)
  }
}