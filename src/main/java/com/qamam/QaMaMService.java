package com.qamam;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.qamam.JsonUtil.json;
import static spark.Spark.*;

public class QaMaMService {

    final static Logger logger = LoggerFactory.getLogger(QaMaMService.class);

    private static String[] getDBDetails() {
        try {
            Properties props = new Properties();
            String configFile = System.getProperty("user.dir") + "/config/config.properties";
            InputStream in = new FileInputStream(configFile);
            props.load(in);
            in.close();

            String[] dbDetails = new String[4];
            dbDetails[0] = props.get("QAMAM_DB_URL").toString();
            dbDetails[1] = props.get("QAMAM_DB_USERNAME").toString();
            dbDetails[2] = props.get("QAMAM_DB_PASSWORD").toString();
            dbDetails[3] = props.get("MY_SQL_URL").toString();


            return dbDetails;
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new String[] {"", "", "", ""};
        }
    }

    private static ArrayList<String> getDistinctDetailsFor(String distinctType, String query) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<String> distinctResults = new ArrayList<String>();

        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()){
                distinctResults.add(resultSet.getString(distinctType));
            }
            return distinctResults;
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new ArrayList<String>();
        }
    }

    private static Assessments getAssessmentsByDate(String dateAssessed, String portfolio, ArrayList<String> previousDates){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Assessment> assessments = new ArrayList<Assessment>();
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            stmt = conn.createStatement();

            String queryStatement = "SELECT * from QaMaMAssessmentResults where portfolio = '" + portfolio + "' AND (dateassessed = '";

            for(String date: previousDates){
                queryStatement += date + "' OR dateassessed = '";
            }

            int length = " OR dateassessed = '".length();
            queryStatement = queryStatement.substring(0, queryStatement.length() - length) + ")";

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            int numberOfRecords = 0;
            double overallStandards = 0;
            double overallMetrics = 0;
            double overallIntegration = 0;
            double overallStakeholder = 0;
            double overallTeamManagement = 0;
            double overallDocumentation = 0;
            double overallAssessmentProcess = 0;
            double overallResearch = 0;
            double overallInvolvement = 0;
            double overallRepository = 0;
            double overallExecution = 0;
            double overallProcess = 0;

            ArrayList<String> teamObtained = new ArrayList<String>();
            Map<String, String> teamDate = new HashMap<String, String>();
            Map<String, Assessment> teamAssessment = new HashMap<String, Assessment>();

            while (resultSet.next()){
                String teamName = resultSet.getString("teamName");
                String dateOfAssessment = resultSet.getString("dateassessed");
                String portfolioName = resultSet.getString("portfolio");

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date date = format.parse(dateOfAssessment);

                if(teamObtained.contains(teamName)){
                    Date previousDate = format.parse(teamDate.get(teamName));
                    if(previousDate.before(date)){
                        Assessment assessmentToBeRemoved = teamAssessment.get(teamName);
                        assessments.remove(assessmentToBeRemoved);
                        teamObtained.remove(teamName);
                        overallStandards = assessmentToBeRemoved.removeValueFromAssessment("Standards", overallStandards);
                        overallMetrics = assessmentToBeRemoved.removeValueFromAssessment("Metrics", overallMetrics);
                        overallIntegration = assessmentToBeRemoved.removeValueFromAssessment("Integration", overallIntegration);
                        overallStakeholder = assessmentToBeRemoved.removeValueFromAssessment("Stakeholder", overallStakeholder);
                        overallTeamManagement = assessmentToBeRemoved.removeValueFromAssessment("TeamManagement", overallTeamManagement);
                        overallDocumentation = assessmentToBeRemoved.removeValueFromAssessment("Documentation", overallDocumentation);
                        overallAssessmentProcess = assessmentToBeRemoved.removeValueFromAssessment("AssessmentProcess", overallAssessmentProcess);
                        overallResearch = assessmentToBeRemoved.removeValueFromAssessment("Research", overallResearch);
                        overallInvolvement = assessmentToBeRemoved.removeValueFromAssessment("Involvement", overallInvolvement);
                        overallRepository = assessmentToBeRemoved.removeValueFromAssessment("Repository", overallRepository);
                        overallExecution = assessmentToBeRemoved.removeValueFromAssessment("Execution", overallExecution);
                        overallProcess = assessmentToBeRemoved.removeValueFromAssessment("Process", overallProcess);
                        numberOfRecords--;
                    }

                }

                if(!teamObtained.contains(teamName) && portfolioName.equals(portfolio)) {
                    Assessment assessment = new Assessment();
                    assessment.setTeamName(teamName);

                    String standards = resultSet.getString("standards");
                    assessment.setStandards(standards);
                    overallStandards += Integer.parseInt(standards);

                    String metrics = resultSet.getString("metrics");
                    assessment.setMetrics(metrics);
                    overallMetrics += Integer.parseInt(metrics);

                    String integration = resultSet.getString("integration");
                    assessment.setIntegration(integration);
                    overallIntegration += Integer.parseInt(integration);

                    String stakeholder = resultSet.getString("stakeholder");
                    assessment.setStakeholder(stakeholder);
                    overallStakeholder += Integer.parseInt(stakeholder);

                    String teamManagement = resultSet.getString("teamManagement");
                    assessment.setTeamManagement(teamManagement);
                    overallTeamManagement += Integer.parseInt(teamManagement);

                    String documentation = resultSet.getString("documentation");
                    assessment.setDocumentation(documentation);
                    overallDocumentation += Integer.parseInt(documentation);

                    String assessmentProcess = resultSet.getString("assessmentProcess");
                    assessment.setAssessmentProcess(assessmentProcess);
                    overallAssessmentProcess += Integer.parseInt(assessmentProcess);

                    String research = resultSet.getString("research");
                    assessment.setResearch(research);
                    overallResearch += Integer.parseInt(research);

                    String involvement = resultSet.getString("involvement");
                    assessment.setInvolvement(involvement);
                    overallInvolvement += Integer.parseInt(involvement);

                    String repository = resultSet.getString("repository");
                    assessment.setRepository(repository);
                    overallRepository += Integer.parseInt(repository);

                    String execution = resultSet.getString("execution");
                    assessment.setExecution(execution);
                    overallExecution += Integer.parseInt(execution);

                    String process = resultSet.getString("process");
                    assessment.setProcess(process);
                    overallProcess += Integer.parseInt(process);

                    String recommendedCapabilities = resultSet.getString("recommendedCapabilities");
                    assessment.setRecommendedCapabilities(recommendedCapabilities);

                    String capabilitiesToStop = resultSet.getString("capabilitiesToStop");
                    assessment.setCapabilitiesToStop(capabilitiesToStop);


                    String rawData = resultSet.getString("rawdata");
                    assessment.setRawData(rawData);

                    numberOfRecords++;
                    assessments.add(assessment);
                    teamObtained.add(teamName);
                    teamDate.put(teamName, dateOfAssessment);
                    teamAssessment.put(teamName, assessment);
                }
            }
            Assessment assessmentOverall = new Assessment();
            assessmentOverall.setTeamName("Average For All The Teams");
            assessmentOverall.setStandards(String.valueOf(overallStandards/numberOfRecords));
            assessmentOverall.setMetrics(String.valueOf(overallMetrics/numberOfRecords));
            assessmentOverall.setIntegration(String.valueOf(overallIntegration/numberOfRecords));
            assessmentOverall.setStakeholder(String.valueOf(overallStakeholder/numberOfRecords));
            assessmentOverall.setTeamManagement(String.valueOf(overallTeamManagement/numberOfRecords));
            assessmentOverall.setDocumentation(String.valueOf(overallDocumentation/numberOfRecords));
            assessmentOverall.setAssessmentProcess(String.valueOf(overallAssessmentProcess/numberOfRecords));
            assessmentOverall.setResearch(String.valueOf(overallResearch/numberOfRecords));
            assessmentOverall.setInvolvement(String.valueOf(overallInvolvement/numberOfRecords));
            assessmentOverall.setRepository(String.valueOf(overallRepository/numberOfRecords));
            assessmentOverall.setExecution(String.valueOf(overallExecution/numberOfRecords));
            assessmentOverall.setProcess(String.valueOf(overallProcess/numberOfRecords));
            assessments.add(assessmentOverall);

            return new Assessments(dateAssessed, portfolio, assessments);
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new Assessments(dateAssessed, portfolio, new ArrayList<Assessment>());
        }
    }

    private static ArrayList<String> getPreviousAssessmentDates(String currentDateString, ArrayList<String> otherDates){
        ArrayList<String> previousDates = new ArrayList<String>();

        previousDates.add(currentDateString);

        for(String assessmentDate: otherDates){
            try {
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date date = format.parse(assessmentDate);
                Date currentDate = format.parse(currentDateString);

                if(date.before(currentDate)){
                    previousDates.add(assessmentDate);
                }
            }
            catch (Exception ex){
                logger.error(ex.getMessage());
            }
        }

        return previousDates;
    }

    private static ArrayList<Assessments> getAssessments(){

        String portfolioQuery = "SELECT DISTINCT portfolio from QaMaMAssessmentResults";
        ArrayList<Assessments> allAssessmentsDone = new ArrayList<Assessments>();
        ArrayList<String> assessmentPortfolios = getDistinctDetailsFor("portfolio", portfolioQuery);
        for(String portfolio: assessmentPortfolios){
            String dateQuery = "SELECT DISTINCT dateassessed from QaMaMAssessmentResults WHERE portfolio = '" + portfolio + "'";
            ArrayList<String> assessmentDates = getDistinctDetailsFor("dateassessed", dateQuery);
            for(String assessmentDate: assessmentDates){
                allAssessmentsDone.add(getAssessmentsByDate(assessmentDate, portfolio, getPreviousAssessmentDates(assessmentDate, assessmentDates)));
            }
        }

        return allAssessmentsDone;
    }

    private static Assessment getAssessmentForTeam(String teamName){
        Connection conn = null;
        Statement stmt = null;
        Assessment assessment = new Assessment();
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            stmt = conn.createStatement();
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            String queryStatement = "SELECT * from QaMaMAssessmentResults where teamName = '"
                    + teamName + "'";
            ResultSet resultSet = stmt.executeQuery(queryStatement);

            Date previousDate = format.parse("01-01-1990");

            while (resultSet.next()){
                String dateOfAssessment = resultSet.getString("dateassessed");
                Date date = format.parse(dateOfAssessment);

                if(date.after(previousDate)) {
                    assessment.setTeamName(resultSet.getString("teamName"));

                    String standards = resultSet.getString("standards");
                    assessment.setStandards(standards);

                    String metrics = resultSet.getString("metrics");
                    assessment.setMetrics(metrics);

                    String integration = resultSet.getString("integration");
                    assessment.setIntegration(integration);

                    String stakeholder = resultSet.getString("stakeholder");
                    assessment.setStakeholder(stakeholder);

                    String teamManagement = resultSet.getString("teamManagement");
                    assessment.setTeamManagement(teamManagement);

                    String documentation = resultSet.getString("documentation");
                    assessment.setDocumentation(documentation);

                    String assessmentProcess = resultSet.getString("assessmentProcess");
                    assessment.setAssessmentProcess(assessmentProcess);

                    String research = resultSet.getString("research");
                    assessment.setResearch(research);

                    String involvement = resultSet.getString("involvement");
                    assessment.setInvolvement(involvement);

                    String repository = resultSet.getString("repository");
                    assessment.setRepository(repository);

                    String execution = resultSet.getString("execution");
                    assessment.setExecution(execution);

                    String process = resultSet.getString("process");
                    assessment.setProcess(process);

                    String recommendedCapabilities = resultSet.getString("recommendedCapabilities");
                    assessment.setRecommendedCapabilities(recommendedCapabilities);

                    String capabilitiesToStop = resultSet.getString("capabilitiesToStop");
                    assessment.setCapabilitiesToStop(capabilitiesToStop);

                    previousDate = date;

                    String rawData = resultSet.getString("rawdata");
                    assessment.setRawData(rawData);
                }
            }

            return assessment;
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return assessment;
        }
    }

    private static void createDatabaseIfItDoesNotExists(){
        Connection conn = null;
        Statement statement = null;
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[3], dbDetails[1], dbDetails[2]);
            statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS ContinuumAssessment;");
            createTableIfItDoesNotExists();
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
        }
    }

    private static void createTableIfItDoesNotExists(){
        Connection conn = null;
        Statement statement = null;
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = conn.createStatement();
            String sqlCreate = "CREATE TABLE IF NOT EXISTS QaMaMAssessmentResults"
                    + "  (teamName           VARCHAR(150),"
                    + "   standards          INTEGER,"
                    + "   metrics            INTEGER,"
                    + "   integration        INTEGER,"
                    + "   stakeholder        INTEGER,"
                    + "   teamManagement     INTEGER,"
                    + "   documentation      INTEGER,"
                    + "   assessmentProcess  INTEGER,"
                    + "   research           INTEGER,"
                    + "   involvement        INTEGER,"
                    + "   repository         INTEGER,"
                    + "   execution          INTEGER,"
                    + "   process            INTEGER,"
                    + "   dateassessed       VARCHAR(100),"
                    + "   portfolio          VARCHAR(150),"
                    + "   rawdata            longtext,"
                    + "   recommendedCapabilities            longtext,"
                    + "   capabilitiesToStop                 longtext,"
                    + "   UNIQUE KEY my_unique_key (teamName,dateassessed,portfolio))";

            statement.execute(sqlCreate);
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
        }
    }

    public static void main(String[] args) {

        port(8081);

        post("/saveTeamData", new Route() {
            public Object handle(Request request, Response response) throws Exception {

                createDatabaseIfItDoesNotExists();
                Connection conn = null;
                Statement stmt = null;

                String teamName = request.queryParams("teamName");
                String standards = request.queryParams("standards");
                String metrics = request.queryParams("metrics");
                String integration = request.queryParams("integration");
                String stakeholder = request.queryParams("stakeholder-management");
                String teamManagement = request.queryParams("team-management");
                String documentation = request.queryParams("documentation");
                String assessmentProcess = request.queryParams("assessment-process");
                String research = request.queryParams("research");
                String involvement = request.queryParams("involvement");
                String repository = request.queryParams("repository");
                String execution = request.queryParams("execution");
                String process = request.queryParams("process");
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateOfEvaluation = dateFormat.format(new Date());
                String portfolioName = request.queryParams("portfolioName");
                String rawData = request.queryParams("rawData");

                JSONObject json = new JSONObject(request.body());

                String recommendedCapabilities;
                String capabilitiesToStop;

                try {
                    recommendedCapabilities = json.get("recommendedCapabilities").toString();
                }
                catch(Exception ex){
                    recommendedCapabilities = "";
                }

                try {
                    capabilitiesToStop = json.get("capabilitiesToStop").toString();
                }
                catch(Exception ex){
                    capabilitiesToStop = "";
                }

                String[] dbDetails = getDBDetails();

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
                    stmt = conn.createStatement();

                    String sql = String.format("REPLACE INTO QaMaMAssessmentResults " +
                                    "VALUES ('%s',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'%s', '%s', '%s', '%s', '%s')", teamName, standards, metrics, integration, stakeholder,
                            teamManagement, documentation, assessmentProcess, research, involvement, repository, execution, process, dateOfEvaluation, portfolioName, rawData,
                            recommendedCapabilities, capabilitiesToStop);

                    int insertedRecord = stmt.executeUpdate(sql);

                    if (insertedRecord > 0) {
                        return "Successfully inserted record";
                    } else {
                        return "Record not inserted";
                    }
                }
                catch (SQLException exception){
                    logger.error("Error Code: " + exception.toString());
                    return "Error Code: " + exception.toString();
                }

            }
        });

        get("/assessments", new Route() {
            public Object handle(Request req, Response res) throws Exception {
                return getAssessments();
            }
        }, json());


        get("/assessment", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                logger.info("Request From: " + request.host());
                String teamName = request.queryParams("teamName");
                Assessment teamAssessment = getAssessmentForTeam(teamName);
                return teamAssessment;
            }
        }, json());

        options("/*",
                new Route() {
                    public Object handle(Request request, Response response) throws Exception {

                        String accessControlRequestHeaders = request
                                .headers("Access-Control-Request-Headers");
                        if (accessControlRequestHeaders != null) {
                            response.header("Access-Control-Allow-Headers",
                                    accessControlRequestHeaders);
                        }

                        String accessControlRequestMethod = request
                                .headers("Access-Control-Request-Method");
                        if (accessControlRequestMethod != null) {
                            response.header("Access-Control-Allow-Methods",
                                    accessControlRequestMethod);
                        }

                        return "OK";
                    }
                });

        before(new Filter() {
            public void handle(Request request, Response response) throws Exception {
                response.header("Access-Control-Allow-Origin", "*");
            }
        });
    }
}