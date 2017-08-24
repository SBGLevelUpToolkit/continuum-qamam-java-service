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

    private static ArrayList<String> getDistinctDetailsForQaMaM(String distinctType, String query) {
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

    private static AssessmentsQaMaM getAssessmentsByDateQaMaM(String dateAssessed, String portfolio, ArrayList<String> previousDates){
        Connection conn = null;
        Statement stmt = null;
        ArrayList<AssessmentQaMaM> assessments = new ArrayList<AssessmentQaMaM>();
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            stmt = conn.createStatement();

            String queryStatement = "SELECT * from QualityMaturityAssessments where portfolio = '" + portfolio + "' AND (dateassessed = '";

            for(String date: previousDates){
                queryStatement += date + "' OR dateassessed = '";
            }

            int length = " OR dateassessed = '".length();
            queryStatement = queryStatement.substring(0, queryStatement.length() - length) + ")";

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            int numberOfRecords = 0;
            double overallTesting = 0;
            double overallTestMetrics = 0;
            double overallQualityAlignment = 0;
            double overallPracticeInnovation = 0;
            double overallToolsArtefacts = 0;

            ArrayList<String> teamObtained = new ArrayList<String>();
            Map<String, String> teamDate = new HashMap<String, String>();
            Map<String, AssessmentQaMaM> teamAssessment = new HashMap<String, AssessmentQaMaM>();

            while (resultSet.next()){
                String teamName = resultSet.getString("teamName");
                String dateOfAssessment = resultSet.getString("dateassessed");
                String portfolioName = resultSet.getString("portfolio");

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date date = format.parse(dateOfAssessment);

                if(teamObtained.contains(teamName)){
                    Date previousDate = format.parse(teamDate.get(teamName));
                    if(previousDate.before(date)){
                        AssessmentQaMaM assessmentToBeRemoved = teamAssessment.get(teamName);
                        assessments.remove(assessmentToBeRemoved);
                        teamObtained.remove(teamName);
                        overallTesting = assessmentToBeRemoved.removeValueFromAssessment("Testing", overallTesting);
                        overallTestMetrics = assessmentToBeRemoved.removeValueFromAssessment("TestMetrics", overallTestMetrics);
                        overallQualityAlignment = assessmentToBeRemoved.removeValueFromAssessment("QualityAlignment", overallQualityAlignment);
                        overallPracticeInnovation = assessmentToBeRemoved.removeValueFromAssessment("PracticeInnovation", overallPracticeInnovation);
                        overallToolsArtefacts = assessmentToBeRemoved.removeValueFromAssessment("ToolsArtefacts", overallToolsArtefacts);
                        numberOfRecords--;
                    }

                }

                if(!teamObtained.contains(teamName) && portfolioName.equals(portfolio)) {
                    AssessmentQaMaM assessment = new AssessmentQaMaM();
                    assessment.setTeamName(teamName);

                    String testing = resultSet.getString("testing");
                    assessment.setTesting(testing);
                    overallTesting += Integer.parseInt(testing);

                    String testMetrics = resultSet.getString("testMetrics");
                    assessment.setTestMetrics(testMetrics);
                    overallTestMetrics += Integer.parseInt(testMetrics);

                    String qualityAlignment = resultSet.getString("qualityAlignment");
                    assessment.setQualityAlignment(qualityAlignment);
                    overallQualityAlignment += Integer.parseInt(qualityAlignment);

                    String practiceInnovation = resultSet.getString("practiceInnovation");
                    assessment.setPracticeInnovation(practiceInnovation);
                    overallPracticeInnovation += Integer.parseInt(practiceInnovation);

                    String toolsArtefacts = resultSet.getString("toolsArtefacts");
                    assessment.setToolsArtefacts(toolsArtefacts);
                    overallToolsArtefacts += Integer.parseInt(toolsArtefacts);

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
            AssessmentQaMaM assessmentOverall = new AssessmentQaMaM();
            assessmentOverall.setTeamName("Average For All The Teams");
            assessmentOverall.setTesting(String.valueOf(overallTesting/numberOfRecords));
            assessmentOverall.setTestMetrics(String.valueOf(overallTestMetrics/numberOfRecords));
            assessmentOverall.setQualityAlignment(String.valueOf(overallQualityAlignment/numberOfRecords));
            assessmentOverall.setPracticeInnovation(String.valueOf(overallPracticeInnovation/numberOfRecords));
            assessmentOverall.setToolsArtefacts(String.valueOf(overallToolsArtefacts/numberOfRecords));
            assessments.add(assessmentOverall);

            return new AssessmentsQaMaM(dateAssessed, portfolio, assessments);
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new AssessmentsQaMaM(dateAssessed, portfolio, new ArrayList<AssessmentQaMaM>());
        }
    }

    private static ArrayList<String> getPreviousAssessmentDatesQaMaM(String currentDateString, ArrayList<String> otherDates){
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

    private static ArrayList<AssessmentsQaMaM> getAssessmentsQaMaM(){

        String portfolioQuery = "SELECT DISTINCT portfolio from QualityMaturityAssessments";
        ArrayList<AssessmentsQaMaM> allAssessmentsDone = new ArrayList<AssessmentsQaMaM>();
        ArrayList<String> assessmentPortfolios = getDistinctDetailsForQaMaM("portfolio", portfolioQuery);
        for(String portfolio: assessmentPortfolios){
            String dateQuery = "SELECT DISTINCT dateassessed from QualityMaturityAssessments WHERE portfolio = '" + portfolio + "'";
            ArrayList<String> assessmentDates = getDistinctDetailsForQaMaM("dateassessed", dateQuery);
            for(String assessmentDate: assessmentDates){
                allAssessmentsDone.add(getAssessmentsByDateQaMaM(assessmentDate, portfolio, getPreviousAssessmentDatesQaMaM(assessmentDate, assessmentDates)));
            }
        }

        return allAssessmentsDone;
    }

    private static AssessmentQaMaM getAssessmentForTeamQaMaM(String teamName){

        createTableIfItDoesNotExistsQaMaM();
        Connection conn = null;
        Statement stmt = null;
        AssessmentQaMaM assessment = new AssessmentQaMaM();
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            stmt = conn.createStatement();
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            String queryStatement = "SELECT * from QualityMaturityAssessments where teamName = '"
                    + teamName + "'";
            ResultSet resultSet = stmt.executeQuery(queryStatement);

            Date previousDate = format.parse("01-01-1990");

            while (resultSet.next()){
                String dateOfAssessment = resultSet.getString("dateassessed");
                Date date = format.parse(dateOfAssessment);

                if(date.after(previousDate)) {
                    assessment.setTeamName(resultSet.getString("teamName"));

                    String testing = resultSet.getString("testing");
                    assessment.setTesting(testing);

                    String testMetrics = resultSet.getString("testMetrics");
                    assessment.setTestMetrics(testMetrics);

                    String qualityAlignment = resultSet.getString("qualityAlignment");
                    assessment.setQualityAlignment(qualityAlignment);

                    String practiceInnovation = resultSet.getString("practiceInnovation");
                    assessment.setPracticeInnovation(practiceInnovation);

                    String toolsArtefacts = resultSet.getString("toolsArtefacts");
                    assessment.setToolsArtefacts(toolsArtefacts);

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

    private static void createDatabaseIfItDoesNotExistsQaMaM(){
        Connection conn = null;
        Statement statement = null;
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[3], dbDetails[1], dbDetails[2]);
            statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS ContinuumAssessment;");
            createTableIfItDoesNotExistsQaMaM();
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
        }
    }

    private static void createTableIfItDoesNotExistsQaMaM(){
        Connection conn = null;
        Statement statement = null;
        String[] dbDetails = getDBDetails();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = conn.createStatement();
            String sqlCreate = "CREATE TABLE IF NOT EXISTS QualityMaturityAssessments"
                    + "  (teamName           VARCHAR(150),"
                    + "   testing            INTEGER,"
                    + "   testMetrics        INTEGER,"
                    + "   qualityAlignment   INTEGER,"
                    + "   practiceInnovation INTEGER,"
                    + "   toolsArtefacts     INTEGER,"
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

    private static String[] getDBDetailsSurvey() {
        try {
            Properties props = new Properties();
            String configFile = System.getProperty("user.dir") + "/config/survey-config.properties";
            InputStream in = new FileInputStream(configFile);
            props.load(in);
            in.close();

            String[] dbDetails = new String[4];
            dbDetails[0] = props.get("DB_URL").toString();
            dbDetails[1] = props.get("DB_USERNAME").toString();
            dbDetails[2] = props.get("DB_PASSWORD").toString();
            dbDetails[3] = props.get("MY_SQL_URL").toString();


            return dbDetails;
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new String[] {"", "", "", ""};
        }
    }

    private static ArrayList<String> getTeamNamesSurvey(){
        String[] dbDetails = getDBDetailsSurvey();
        ArrayList<String> teamNames = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = "SELECT DISTINCT teamName from TeamNames";

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                teamNames.add(resultSet.getString("teamName"));
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getTeamNamesSurvey: " + exception.toString());
        }

        return teamNames;
    }

    private static ArrayList<Surveyee> getAllSurveyees(){
        String[] dbDetails = getDBDetailsSurvey();
        ArrayList<Surveyee> surveyees = new ArrayList<Surveyee>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = "SELECT * from TeamNames";

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                Surveyee surveyee = new Surveyee();
                surveyee.setSurveyeeName(resultSet.getString("bioName"));
                surveyee.setPortfolio(resultSet.getString("portfolio"));
                surveyee.setTeamName(resultSet.getString("teamName"));
                surveyees.add(surveyee);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getAllSurveyees: " + exception.toString());
        }

        return surveyees;
    }

    private static int getNumberOfBioForTeam(String teamName){
        String[] dbDetails = getDBDetailsSurvey();
        int numberOfBIO = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT bioName from TeamNames WHERE teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                numberOfBIO++;
            }

        }
        catch (SQLException exception){
            logger.error("Error Code - sql - getNumberOfBioForTeam: " + exception.toString());
        }
        catch (Exception exception){
            logger.error("Error Code - non-sql - getNumberOfBioForTeam: " + exception.toString());
        }

        return numberOfBIO;
    }

    private static String getPortfolioForTeam(String teamName){
        String[] dbDetails = getDBDetailsSurvey();
        String portfolio = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT portfolio from TeamNames WHERE teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                portfolio = resultSet.getString("portfolio");
            }

        }
        catch (SQLException exception){
            logger.error("Error Code - sql - getPortfolioForTeam: " + exception.toString());
        }
        catch (Exception exception){
            logger.error("Error Code - non-sql - getPortfolioForTeam: " + exception.toString());
        }

        return portfolio;
    }

    private static ArrayList<SurveyResult> getSurveyResultsForTeam(String teamName){
        String[] dbDetails = getDBDetailsSurvey();
        ArrayList<SurveyResult> surveyResults = new ArrayList<SurveyResult>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT * from SurveyResults where teamName = '%s'", teamName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                SurveyResult surveyResult = new SurveyResult();
                surveyResult.setSoftwareScore(resultSet.getString("softwareScore"));
                surveyResult.setAgileCoachingScore(resultSet.getString("agileCoachingScore"));
                surveyResult.setChangeAndReleaseScore(resultSet.getString("changeAndReleaseScore"));
                surveyResult.setQualityEngineeringScore(resultSet.getString("qualityEngineeringScore"));
                surveyResult.setEnterpriseArchitectureScore(resultSet.getString("enterpriseArchitectureScore"));
                surveyResult.setSolutionsArchitectureScore(resultSet.getString("solutionsArchitectureScore"));
                surveyResult.setDataServicesScore(resultSet.getString("dataServicesScore"));
                surveyResult.setBIO(resultSet.getString("bioName"));
                surveyResult.setPeriodOfYear(resultSet.getString("quarter"));
                surveyResult.setTeamName(teamName);
                surveyResults.add(surveyResult);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getSurveyResultsForTeam: " + exception.toString());
        }
        return surveyResults;
    }

    private static SurveyResult getSurveyResultsForTeamAndSurveyee(String teamName, String surveyee, String quarter){
        String[] dbDetails = getDBDetailsSurvey();
        SurveyResult surveyResult = new SurveyResult();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT * from SurveyResults where portfolio = '%s' AND bioName = '%s' AND quarter = '%s'", teamName, surveyee, quarter);

            ResultSet resultSet = stmt.executeQuery(queryStatement);
            while(resultSet.next()){
                surveyResult.setSoftwareScore(resultSet.getString("softwareScore"));
                surveyResult.setAgileCoachingScore(resultSet.getString("agileCoachingScore"));
                surveyResult.setChangeAndReleaseScore(resultSet.getString("changeAndReleaseScore"));
                surveyResult.setQualityEngineeringScore(resultSet.getString("qualityEngineeringScore"));
                surveyResult.setEnterpriseArchitectureScore(resultSet.getString("enterpriseArchitectureScore"));
                surveyResult.setSolutionsArchitectureScore(resultSet.getString("solutionsArchitectureScore"));
                surveyResult.setDataServicesScore(resultSet.getString("dataServicesScore"));
                surveyResult.setBIO(resultSet.getString("bioName"));
                surveyResult.setPeriodOfYear(resultSet.getString("quarter"));
                surveyResult.setRawData(resultSet.getString("rawData"));
                surveyResult.setTeamName(teamName);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getSurveyResultsForTeamAndSurveyee: " + exception.toString());
        }
        return surveyResult;
    }

    private static ArrayList<SurveyResults> getSurveyResults(String teamName){
        ArrayList<SurveyResults> surveyResults = new ArrayList<SurveyResults>();

        if(teamName != null && !teamName.equals("undefined" )){
            SurveyResults teamSurveyResults = new SurveyResults(teamName, getPortfolioForTeam(teamName), getNumberOfBioForTeam(teamName), getSurveyResultsForTeam(teamName));
            surveyResults.add(teamSurveyResults);
        }
        else {
            ArrayList<String> allTeams = getTeamNamesSurvey();

            for (String teamNameRetrieved : allTeams) {
                SurveyResults teamSurveyResults = new SurveyResults(teamNameRetrieved, getPortfolioForTeam(teamNameRetrieved), getNumberOfBioForTeam(teamNameRetrieved), getSurveyResultsForTeam(teamNameRetrieved));
                surveyResults.add(teamSurveyResults);
            }
        }

        return surveyResults;
    }

    private static SurveyResult surveyForSurveyee(String surveyeeName, String teamName){
        String quarter = getQuarter();
        return getSurveyResultsForTeamAndSurveyee(teamName, surveyeeName, quarter);
    }

    private static String getColumnName(String BIOName, String columnName){
        String columnNameRetrieved = "";
        String[] dbDetails = getDBDetailsSurvey();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String queryStatement = String.format("SELECT %s from TeamNames where bioName = '%s'", columnName, BIOName);

            ResultSet resultSet = stmt.executeQuery(queryStatement);

            while(resultSet.next()){
                columnNameRetrieved = resultSet.getString(columnName);
            }
        }
        catch (Exception exception){
            logger.error("Error Code - getColumnName: " + exception.toString());
            return "Error Code: " + exception.toString();
        }
        return columnNameRetrieved;
    }

    private static String updateUserDetails(String surveyeeName, String teamName, String portfolioName){
        String[] dbDetails = getDBDetailsSurvey();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            Statement stmt = conn.createStatement();

            String sql = String.format("REPLACE INTO TeamNames " +
                    "VALUES ('%s','%s', '%s')", surveyeeName, teamName, portfolioName);

            int insertedRecord = stmt.executeUpdate(sql);

            if (insertedRecord > 0) {
                return "Successfully inserted record";
            } else {
                return "Record not inserted";
            }
        }
        catch (SQLException exception){
            logger.error("Error Code - sql - updateUserDetails: " + exception.toString());
            return "Error Code: " + exception.toString();
        }
        catch (Exception exception){
            logger.error("Error Code - non-sql - updateUserDetails: " + exception.toString());
            return "Error Code: " + exception.toString();
        }
    }

    private static String getQuarter(){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentDate);

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if(month <= 2){
            return "Quarter 1 - " + String.valueOf(year);
        }
        else if(month > 2 && month <= 5){
            return "Quarter 2 - " + String.valueOf(year);
        }
        else if(month > 5 && month <= 8){
            return "Quarter 3 - " + String.valueOf(year);
        }
        else{
            return "Quarter 4 - " + String.valueOf(year);
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

        post("/saveTeamData-qamam", new Route() {
            public Object handle(Request request, Response response) throws Exception {

                createDatabaseIfItDoesNotExistsQaMaM();
                Connection conn = null;
                Statement stmt = null;

                String teamName = request.queryParams("teamName");
                String testing = request.queryParams("testing");
                String testMetrics = request.queryParams("test-metrics");
                String qualityAlignment = request.queryParams("quality-alignment");
                String practiceInnovation = request.queryParams("practice-innovation");
                String toolsArtefacts = request.queryParams("tools-artefacts");
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

                    String sql = String.format("REPLACE INTO QualityMaturityAssessments " +
                                    "VALUES ('%s',%s,%s,%s,%s,%s,'%s', '%s', '%s', '%s', '%s')", teamName, testing, testMetrics, qualityAlignment, practiceInnovation,
                            toolsArtefacts, dateOfEvaluation, portfolioName, rawData, recommendedCapabilities, capabilitiesToStop);

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

        get("/assessments-qamam", new Route() {
            public Object handle(Request req, Response res) throws Exception {
                return getAssessmentsQaMaM();
            }
        }, json());


        get("/assessment-qamam", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                logger.info("Request From: " + request.host());
                String teamName = request.queryParams("teamName");
                AssessmentQaMaM teamAssessment = getAssessmentForTeamQaMaM(teamName);
                return teamAssessment;
            }
        }, json());

        post("/saveSurvey", new Route() {
            public Object handle(Request request, Response response) throws Exception {
                JSONObject json = new JSONObject(request.body());

                String BIO, softwareScore, agileCoachingScore, changeAndReleaseScore, qualityEngineeringScore, rawData,
                        enterpriseArchitectureScore, solutionsArchitectureScore, dataServicesScore, selectedPracticeTeam;

                try {
                    BIO = json.get("BIO").toString();
                }
                catch(Exception ex){
                    BIO = "";
                }

                try {
                    softwareScore = json.get("softwareScore").toString();
                }
                catch(Exception ex){
                    softwareScore = "";
                }

                try {
                    agileCoachingScore = json.get("agileCoachingScore").toString();
                }
                catch(Exception ex){
                    agileCoachingScore = "";
                }

                try {
                    changeAndReleaseScore = json.get("changeAndReleaseScore").toString();
                }
                catch(Exception ex){
                    changeAndReleaseScore = "";
                }

                try {
                    qualityEngineeringScore = json.get("qualityEngineeringScore").toString();
                }
                catch(Exception ex){
                    qualityEngineeringScore = "";
                }

                try {
                    enterpriseArchitectureScore = json.get("enterpriseArchitectureScore").toString();
                }
                catch(Exception ex){
                    enterpriseArchitectureScore = "";
                }

                try {
                    solutionsArchitectureScore = json.get("solutionsArchitectureScore").toString();
                }
                catch(Exception ex){
                    solutionsArchitectureScore = "";
                }

                try {
                    dataServicesScore = json.get("dataServicesScore").toString();
                }
                catch(Exception ex){
                    dataServicesScore = "";
                }

                try {
                    rawData = json.get("rawData").toString();
                }
                catch(Exception ex){
                    rawData = "";
                }

                try {
                    selectedPracticeTeam = json.get("selectedPracticeTeam").toString();
                }
                catch(Exception ex){
                    selectedPracticeTeam = "";
                }



                String[] dbDetails = getDBDetailsSurvey();

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
                    Statement stmt = conn.createStatement();

                    String teamName = getColumnName(BIO, "teamName");

                    if(!selectedPracticeTeam.equals("")){
                        teamName = selectedPracticeTeam;
                    }

                    String sql = String.format("REPLACE INTO SurveyResults " +
                                    "VALUES ('%s','%s', '%s', '%s',%s,%s,%s,%s,%s,%s,%s,'%s')", BIO, teamName,
                            getColumnName(BIO, "portfolio"), getQuarter(), softwareScore, agileCoachingScore,
                            changeAndReleaseScore, qualityEngineeringScore, enterpriseArchitectureScore,
                            solutionsArchitectureScore, dataServicesScore, rawData);

                    int insertedRecord = stmt.executeUpdate(sql);

                    if (insertedRecord > 0) {
                        return "Successfully inserted record";
                    } else {
                        return "Record not inserted";
                    }
                }
                catch (SQLException exception){
                    logger.error("Error Code - sql - saveSurvey: " + exception.toString());
                    return "Error Code: " + exception.toString();
                }

            }
        });

        get("/surveys", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                String teamName = request.queryParams("teamName");
                return getSurveyResults(teamName);
            }
        }, json());


        get("/surveyTaken", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                String surveyeeName = request.queryParams("surveyee");
                String teamName = request.queryParams("teamName");
                return surveyForSurveyee(surveyeeName, teamName);
            }
        }, json());


        post("/update", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                String surveyeeName = request.queryParams("surveyee");
                String teamName = request.queryParams("teamName");
                String portfolioName = request.queryParams("portfolio");
                return updateUserDetails(surveyeeName, teamName, portfolioName);
            }
        });


        get("/surveyees", new Route() {
            public Object handle(Request request, Response res) throws Exception {
                return getAllSurveyees();
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
