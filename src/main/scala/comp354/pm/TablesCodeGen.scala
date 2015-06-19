package comp354.pm

import slick.codegen.SourceCodeGenerator

object TablesCodeGen extends App {
  slick.codegen.SourceCodeGenerator.main(
    Array("slick.driver.SQLiteDriver", "org.sqlite.JDBC", "jdbc:sqlite:project.db", "src/main/scala", "comp354.pm.model"))
}