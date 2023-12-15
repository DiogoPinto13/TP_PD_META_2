package isec.pd.meta2.Server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
/*
-- Tabela 'eventos'
CREATE TABLE IF NOT EXISTS eventos (
  idevento INTEGER PRIMARY KEY AUTOINCREMENT,
  designacao TEXT NOT NULL,
  place TEXT NOT NULL,
  datetime TEXT NOT NULL
);

-- Tabela 'utilizadores'
CREATE TABLE IF NOT EXISTS utilizadores (
  idutilizador TEXT NOT NULL PRIMARY KEY,
  username TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  nome TEXT NOT NULL
);

-- Tabela 'codigos_registo'
CREATE TABLE IF NOT EXISTS codigos_registo (
  idcodigo_registo INTEGER PRIMARY KEY AUTOINCREMENT,
  codigo INTEGER NOT NULL,
  idevento INTEGER NOT NULL,
  FOREIGN KEY (idevento) REFERENCES eventos (idevento)
);

-- Tabela 'eventos_utilizadores'
CREATE TABLE IF NOT EXISTS eventos_utilizadores (
  idevento INTEGER NOT NULL,
  idutilizador TEXT NOT NULL,
  PRIMARY KEY (idevento, idutilizador),
  FOREIGN KEY (idevento) REFERENCES eventos (idevento),
  FOREIGN KEY (idutilizador) REFERENCES utilizadores (idutilizador)
);

 */


/*
CREATE SCHEMA IF NOT EXISTS `ClassCodes` DEFAULT CHARACTER SET utf8 ;
USE `ClassCodes` ;

CREATE TABLE IF NOT EXISTS `ClassCodes`.`eventos` (
  `idevento` INT NOT NULL AUTO_INCREMENT,
  `designacao` VARCHAR(255) NOT NULL,
  `place` VARCHAR(255) NOT NULL,
  `datetime` DATETIME NOT NULL,
  PRIMARY KEY (`idevento`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `ClassCodes`.`utilizadores` (
  `idutilizador` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idutilizador`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `idutilizador_UNIQUE` (`idutilizador` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ClassCodes`.`codigos_registo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ClassCodes`.`codigos_registo` (
  `idcodigo_registo` INT NOT NULL AUTO_INCREMENT,
  `codigo` INT NOT NULL,
  `idevento` INT NOT NULL,
  PRIMARY KEY (`idcodigo_registo`, `idevento`),
  INDEX `fk_codigos_registo_eventos1_idx` (`idevento` ASC),
  CONSTRAINT `fk_codigos_registo_eventos1`
    FOREIGN KEY (`idevento`)
    REFERENCES `ClassCodes`.`eventos` (`idevento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ClassCodes`.`eventos_utilizadores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ClassCodes`.`eventos_utilizadores` (
  `idevento` INT NOT NULL,
  `idutilizador` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idevento`, `idutilizador`),
  INDEX `fk_eventos_has_utilizadores_utilizadores1_idx` (`idutilizador` ASC),
  INDEX `fk_eventos_has_utilizadores_eventos_idx` (`idevento` ASC),
  CONSTRAINT `fk_eventos_has_utilizadores_eventos`
    FOREIGN KEY (`idevento`)
    REFERENCES `ClassCodes`.`eventos` (`idevento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_eventos_has_utilizadores_utilizadores1`
    FOREIGN KEY (`idutilizador`)
    REFERENCES `ClassCodes`.`utilizadores` (`idutilizador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

*/

public class DatabaseManager {
    private static String url = "jdbc:sqlite:./";
    private static String databaseFile = "tp.db";

    public static String getDatabaseFileName() {return databaseFile;}

    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * A method to create a connection to the database
     */
    public static boolean connect(String filePath) {
        Connection conn = null;
        try {
            String filePathDB = "./" + filePath + "/" + databaseFile;
            Path path = Paths.get(filePathDB);
            if(!Files.exists(path)){
                url = url + filePath + "/" + databaseFile;
                createNewDatabase();
                createNewTable();
                fillDatabase();
            }
            else
                url = url + filePath + "/" + databaseFile;
            conn = DriverManager.getConnection(DatabaseManager.url);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    public static void createNewTable() {
        //eventos, utilizadores, codigos_registo, eventos_utilizadores

        String eventos = "CREATE TABLE IF NOT EXISTS eventos (\n" +
                "  idevento INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  designacao TEXT NOT NULL,\n" +
                "  place TEXT NOT NULL,\n" +
                "  horaInicio DATETIME  NOT NULL,\n" +
                "  horaFim DATETIME NOT NULL\n" +
                ");";
        String utilizadores = "CREATE TABLE IF NOT EXISTS utilizadores (\n" +
                "  idutilizador TEXT NOT NULL UNIQUE,\n" +
                "  username TEXT NOT NULL PRIMARY KEY,\n" +
                "  password TEXT NOT NULL,\n" +
                "  nome TEXT NOT NULL,\n" +
                "  isAdmin BOOLEAN NOT NULL\n" +
                ");";
        String codigos_registo = "CREATE TABLE IF NOT EXISTS codigos_registo (\n" +
                "  idcodigo_registo INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  codigo INTEGER NOT NULL,\n" +
                "  duracao INTEGER NOT NULL,\n" +
                "  idevento INTEGER NOT NULL,\n" +
                "  horaRegisto DATETIME NOT NULL,\n" +
                "  FOREIGN KEY (idevento) REFERENCES eventos (idevento)\n" +
                ");";
        String eventos_utilizadores = "CREATE TABLE IF NOT EXISTS eventos_utilizadores (\n" +
                "  idevento INTEGER NOT NULL,\n" +
                "  username TEXT NOT NULL,\n" +
                "  PRIMARY KEY (idevento, username),\n" +
                "  FOREIGN KEY (idevento) REFERENCES eventos (idevento),\n" +
                "  FOREIGN KEY (username) REFERENCES utilizadores (username)\n" +
                ");";
        String versao = "CREATE TABLE IF NOT EXISTS versao (\n" +
                "idversao INTEGER PRIMARY KEY AUTOINCREMENT,\n"  +
                "versao INTEGER NOT NULL\n" +
                ");";
        String iniciaVersao = "INSERT INTO versao(versao) VALUES(0);";
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.execute(eventos);
            stmt.execute(utilizadores);
            stmt.execute(codigos_registo);
            stmt.execute(eventos_utilizadores);
            stmt.execute(versao);
            stmt.execute(iniciaVersao);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function is meant to clear all tables from the database
     */
    public static void clearDatabase(){
        //there is no DROP DATABASE, so we have to drop each table manually...
        String eventos = "DROP TABLE IF EXISTS eventos;";
        String utilizadores ="DROP TABLE IF EXISTS utilizadores;";
        String codigos_registo = "DROP TABLE IF EXISTS codigos_registo;";
        String eventos_utilizadores = "DROP TABLE IF EXISTS eventos_utilizadores;";
        //eventos, utilizadores, codigos_registo, eventos_utilizadores
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            //stmt.execute("DROP TABLE IF EXISTS warehouses");
            conn.setAutoCommit(true);
            stmt.execute(eventos);
            stmt.execute(utilizadores);
            stmt.execute(codigos_registo);
            stmt.execute(eventos_utilizadores);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void fillDatabase(){

        String user1 = "INSERT INTO utilizadores (idutilizador, username, password, nome, isAdmin) VALUES ('202013653', 'a2020133653@isec.pt', 'password12345', 'diogo', false);";
        String user2 = "INSERT INTO utilizadores(idutilizador, username, password, nome, isAdmin) VALUES ('2021146924', 'a2021146924@isec.pt', 'stammPassword', 'stamm', false);";
        String user3 = "INSERT INTO utilizadores(idutilizador, username, password, nome, isAdmin) VALUES ('2021ana', 'a2021ana@isec.pt', 'anaPassword', 'Ana', false);";
        String user4 = "INSERT INTO utilizadores (idutilizador, username, password, nome, isAdmin) VALUES ('admin12345', 'admin', 'admin', 'administrator', true);";
        String user5 = "INSERT INTO utilizadores (idutilizador, username, password, nome, isAdmin) VALUES ('teste12345', 'test', 'test', 'teste', false);";


        String event1 = "INSERT INTO eventos (designacao, place, horaInicio, horaFim) VALUES ('Aula PD T1', 'ISEC', '2019-12-12 12:12:12', '2019-12-12 14:12:12');";
        String event2 = "INSERT INTO eventos (designacao, place, horaInicio, horaFim) VALUES ('Aula PD P1', 'ISEC', '2021-12-12 12:12:12', '2021-12-12 15:12:12');";
        String event3 = "INSERT INTO eventos(designacao, place, horaInicio, horaFim) VALUES ('Aula ED T1', 'DEIS', '2021-12-12 12:12:12', '2021-12-12 14:12:12');";

        String relation1 = "INSERT INTO eventos_utilizadores (idevento, username) VALUES (1, 'a2020133653@isec.pt');";
        String relation2 = "INSERT INTO eventos_utilizadores (idevento, username) VALUES (2, 'a2020133653@isec.pt');";
        String relation3 = "INSERT INTO eventos_utilizadores (idevento, username) VALUES (1, 'a2021146924@isec.pt');";

        //String codeEvent1 = "INSERT INTO codigos_registo (codigo, duracao, idevento, horaRegisto) VALUES(12345, 60, 1, '2021-12-12 12:12:12');";

        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.execute(user1);
            stmt.execute(user2);
            stmt.execute(user3);
            stmt.execute(user4);
            stmt.execute(user5);
            stmt.execute(event1);
            stmt.execute(event2);
            stmt.execute(event3);
            stmt.execute(relation1);
            stmt.execute(relation2);
            stmt.execute(relation3);
            //stmt.execute(codeEvent1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void fillDatabase1(){
        String user = "INSERT INTO utilizadores (idutilizador, username, password, nome, isAdmin) VALUES ('admin12345', 'admin', 'admin', 'administrator', true);";

        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.execute(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void testUser(){
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT password FROM utilizadores")) {
            while(rs.next()){
                System.out.println(
                        //rs.getInt("idutilizador") + "," +
                          //      rs.getString("username")   + "," +
                                rs.getString("password")   + ",");
                            //    rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("error while executing the query: " + e.getMessage());
        }
    }
    /**
     * this function receives a string, which will be the query we want to execute and returns
     * a result set
     * @param query
     * @return ResultSet
     */
    public static synchronized ResultSet executeQuery(String query) throws SQLException {
        Connection conn = DriverManager.getConnection(DatabaseManager.url);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }
    /**
     * this function receives a string, which will be the query we want to execute, and returns
     * if the operation either succeeded or not
     * @param query query
     * @return Boolean if success
     */
    public static synchronized boolean executeUpdate(String query) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            boolean result = stmt.execute(query);
            int rows = stmt.getUpdateCount();
            if(rows > 0 )
                updateVersion();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("error while executing the update: " + e.getMessage());
        }
        return false;
    }
    /**
     * this function is mean to update the version of the database when either an insert or update its called
     * @return if success
     */
    public static synchronized void updateVersion(){
        try (Connection conn = DriverManager.getConnection(DatabaseManager.url);
             Statement stmt = conn.createStatement()) {
            stmt.execute("UPDATE versao SET versao = versao + 1 WHERE idversao = 1");
            //UpdateClients.update();
        } catch (SQLException e) {
            System.out.println("error while executing the update: " + e.getMessage());
        }
    }

    public static synchronized int getDatabaseVersion(){
        try(ResultSet rs = executeQuery("select versao from versao where idversao = 1")){
            return rs.getInt("versao");
        }
        catch (SQLException e){
            System.out.println("Error while getting database version.");
        }
        return -1;
    }
}