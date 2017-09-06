package com.qamam;

import org.json.JSONObject;

public class PipelineResult {
    private String ciScore, infrastructureScore, environmentsScore, qaScore, codingPracticesScore,
            monitoringScore, resilienceScore, generalPipelineScore, rawData;

    private CI ci;
    private Infrastructure infrastructure;
    private Environments environments;
    private QA qa;
    private CodingPractices codingPractices;
    private Monitoring monitoring;
    private Resilience resilience;
    private GeneralPipeline generalPipeline;


    public void setCiScore(String ciScore) {
        this.ciScore = ciScore;
    }

    public void setInfrastructureScore(String infrastructureScore) {
        this.infrastructureScore = infrastructureScore;
    }

    public void setEnvironmentsScore(String environmentsScore) {
        this.environmentsScore = environmentsScore;
    }

    public void setQaScore(String qaScore) {
        this.qaScore = qaScore;
    }

    public void setCodingPracticesScore(String codingPracticesScore) {
        this.codingPracticesScore = codingPracticesScore;
    }

    public void setMonitoringScore(String monitoringScore) {
        this.monitoringScore = monitoringScore;
    }

    public void setResilienceScore(String resilienceScore) {
        this.resilienceScore = resilienceScore;
    }

    public void setGeneralPipelineScore(String generalPipelineScore) {
        this.generalPipelineScore = generalPipelineScore;
    }

    public void setCi(String rawData) {
        CI ci = new CI();
        JSONObject json = new JSONObject(rawData);
        String ciQuestions;

        try {
            ciQuestions = json.get("ci").toString();

            JSONObject ciJson = new JSONObject(ciQuestions);

            ci.setQuestion1(ciJson.getBoolean("question1"));
            ci.setQuestion2(ciJson.getBoolean("question2"));
            ci.setQuestion3(ciJson.getBoolean("question3"));
            ci.setQuestion4(ciJson.getBoolean("question4"));
            ci.setQuestion5(ciJson.getBoolean("question5"));
            ci.setQuestion6(ciJson.getBoolean("question6"));
            ci.setQuestion7(ciJson.getBoolean("question7"));
        }
        catch(Exception ex){
            ciQuestions = "";
        }

        this.ci = ci;
    }

    public void setInfrastructure(String rawData) {
        Infrastructure infrastructure = new Infrastructure();
        JSONObject json = new JSONObject(rawData);
        String infrastructureQuestions;

        try {
            infrastructureQuestions = json.get("infrastructure").toString();

            JSONObject infrastructureJson = new JSONObject(infrastructureQuestions);

            infrastructure.setQuestion1(infrastructureJson.getBoolean("question1"));
            infrastructure.setQuestion2(infrastructureJson.getBoolean("question2"));
            infrastructure.setQuestion3(infrastructureJson.getBoolean("question3"));
            infrastructure.setQuestion4(infrastructureJson.getBoolean("question4"));
            infrastructure.setQuestion5(infrastructureJson.getBoolean("question5"));
        }
        catch(Exception ex){
            infrastructureQuestions = "";
        }

        this.infrastructure = infrastructure;
    }

    public void setEnvironments(String rawData) {
        Environments environments = new Environments();
        JSONObject json = new JSONObject(rawData);
        String environmentsQuestions;

        try {
            environmentsQuestions = json.get("pipeline-environments").toString();

            JSONObject environmentsJson = new JSONObject(environmentsQuestions);

            environments.setQuestion1(environmentsJson.getBoolean("question1"));
            environments.setQuestion2(environmentsJson.getBoolean("question2"));
            environments.setQuestion3(environmentsJson.getBoolean("question3"));
            environments.setQuestion4(environmentsJson.getBoolean("question4"));
        }
        catch(Exception ex){
            environmentsQuestions = "";
        }

        this.environments = environments;

    }

    public void setQa(String rawData) {
        QA qa = new QA();
        JSONObject json = new JSONObject(rawData);
        String qaQuestions;

        try {
            qaQuestions = json.get("qa").toString();

            JSONObject qaJson = new JSONObject(qaQuestions);

            qa.setQuestion1(qaJson.getBoolean("question1"));
            qa.setQuestion2(qaJson.getBoolean("question2"));
            qa.setQuestion3(qaJson.getBoolean("question3"));
            qa.setQuestion4(qaJson.getBoolean("question4"));
            qa.setQuestion5(qaJson.getString("question5"));
            qa.setQuestion6(qaJson.getBoolean("question6"));
            qa.setQuestion7(qaJson.getBoolean("question7"));
            qa.setQuestion8(qaJson.getBoolean("question8"));
            qa.setQuestion9(qaJson.getBoolean("question9"));
            qa.setQuestion10(qaJson.getBoolean("question10"));
        }
        catch(Exception ex){
            qaQuestions = "";
        }

        this.qa = qa;
    }

    public void setCodingPractices(String rawData) {
        CodingPractices codingPractices = new CodingPractices();
        JSONObject json = new JSONObject(rawData);
        String codingPracticesQuestions;

        try {
            codingPracticesQuestions = json.get("pipeline-code").toString();

            JSONObject codingPracticesJson = new JSONObject(codingPracticesQuestions);

            codingPractices.setQuestion1(codingPracticesJson.getBoolean("question1"));
            codingPractices.setQuestion2(codingPracticesJson.getBoolean("question2"));
            codingPractices.setQuestion3(codingPracticesJson.getBoolean("question3"));
            codingPractices.setQuestion4(codingPracticesJson.getBoolean("question4"));
            codingPractices.setQuestion5(codingPracticesJson.getBoolean("question5"));
        }
        catch(Exception ex){
            codingPracticesQuestions = "";
        }

        this.codingPractices = codingPractices;
    }

    public void setMonitoring(String rawData) {
        Monitoring monitoring = new Monitoring();
        JSONObject json = new JSONObject(rawData);
        String monitoringQuestions;

        try {
            monitoringQuestions = json.get("pipeline-monitoring").toString();

            JSONObject monitoringJson = new JSONObject(monitoringQuestions);

            monitoring.setQuestion1(monitoringJson.getBoolean("question1"));
            monitoring.setQuestion2(monitoringJson.getBoolean("question2"));
            monitoring.setQuestion3(monitoringJson.getBoolean("question3"));
            monitoring.setQuestion4(monitoringJson.getBoolean("question4"));
            monitoring.setQuestion5(monitoringJson.getBoolean("question5"));
        }
        catch(Exception ex){
            monitoringQuestions = "";
        }

        this.monitoring = monitoring;
    }

    public void setResilience(String rawData) {
        Resilience resilience = new Resilience();
        JSONObject json = new JSONObject(rawData);
        String resilienceQuestions;

        try {
            resilienceQuestions = json.get("pipeline-resilience").toString();

            JSONObject resilienceJson = new JSONObject(resilienceQuestions);

            resilience.setQuestion1(resilienceJson.getBoolean("question1"));
            resilience.setQuestion2(resilienceJson.getBoolean("question2"));
            resilience.setQuestion3(resilienceJson.getBoolean("question3"));
        }
        catch(Exception ex){
            resilienceQuestions = "";
        }

        this.resilience = resilience;
    }

    public void setGeneralPipeline(String rawData) {
        GeneralPipeline generalPipeline = new GeneralPipeline();
        JSONObject json = new JSONObject(rawData);
        String generalPipelineQuestions;

        try {
            generalPipelineQuestions = json.get("pipeline-general").toString();

            JSONObject generalPipelineJson = new JSONObject(generalPipelineQuestions);

            generalPipeline.setQuestion1(generalPipelineJson.getBoolean("question1"));
            generalPipeline.setQuestion2(generalPipelineJson.getBoolean("question2"));
            generalPipeline.setQuestion3(generalPipelineJson.getBoolean("question3"));
        }
        catch(Exception ex){
            generalPipelineQuestions = "";
        }

        this.generalPipeline = generalPipeline;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
