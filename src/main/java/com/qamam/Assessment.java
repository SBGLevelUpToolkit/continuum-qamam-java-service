package com.qamam;

public class Assessment {

    private String teamName;
    private String standards;
    private String metrics;
    private String integration;
    private String stakeholder;
    private String teamManagement;
    private String documentation;
    private String assessmentProcess;
    private String research;
    private String involvement;
    private String repository;
    private String execution;
    private String process;
    private String rawData;
    private String recommendedCapabilities;
    private String capabilitiesToStop;

    public Assessment() {
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public void setInvolvement(String involvement) {
        this.involvement = involvement;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public void setAssessmentProcess(String assessmentProcess) {
        this.assessmentProcess = assessmentProcess;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public void setTeamManagement(String teamManagement) {
        this.teamManagement = teamManagement;
    }

    public void setStakeholder(String stakeholder) {
        this.stakeholder = stakeholder;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setRecommendedCapabilities(String recommendedCapabilities){ this.recommendedCapabilities = recommendedCapabilities;}

    public void setCapabilitiesToStop(String capabilitiesToStop){this.capabilitiesToStop = capabilitiesToStop;}

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public double removeValueFromAssessment(String attribute, double value){
        if(attribute.equals("Standards")){
            return value - Double.parseDouble(this.standards);
        }

        if(attribute.equals("Metrics")){
            return value - Double.parseDouble(this.metrics);
        }

        if(attribute.equals("Integration")){
            return value - Double.parseDouble(this.integration);
        }

        if(attribute.equals("Stakeholder")){
            return value - Double.parseDouble(this.stakeholder);
        }

        if(attribute.equals("TeamManagement")){
            return value - Double.parseDouble(this.teamManagement);
        }

        if(attribute.equals("Documentation")){
            return value - Double.parseDouble(this.documentation);
        }

        if(attribute.equals("AssessmentProcess")){
            return value - Double.parseDouble(this.assessmentProcess);
        }

        if(attribute.equals("Research")){
            return value - Double.parseDouble(this.research);
        }

        if(attribute.equals("Involvement")){
            return value - Double.parseDouble(this.involvement);
        }

        if(attribute.equals("Repository")){
            return value - Double.parseDouble(this.repository);
        }

        if(attribute.equals("Execution")){
            return value - Double.parseDouble(this.execution);
        }

        if(attribute.equals("Process")){
            return value - Double.parseDouble(this.process);
        }

        return 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assessment that = (Assessment) o;

        if (teamName != null ? !teamName.equals(that.teamName) : that.teamName != null) return false;
        if (standards != null ? !standards.equals(that.standards) : that.standards != null) return false;
        if (metrics != null ? !metrics.equals(that.metrics) : that.metrics != null) return false;
        if (integration != null ? !integration.equals(that.integration) : that.integration != null) return false;
        if (stakeholder != null ? !stakeholder.equals(that.stakeholder) : that.stakeholder != null) return false;
        if (teamManagement != null ? !teamManagement.equals(that.teamManagement) : that.teamManagement != null) return false;
        if (documentation != null ? !documentation.equals(that.documentation) : that.documentation != null) return false;
        if (assessmentProcess != null ? !assessmentProcess.equals(that.assessmentProcess) : that.assessmentProcess != null) return false;
        if (research != null ? !research.equals(that.research) : that.research != null) return false;
        if (involvement != null ? !involvement.equals(that.involvement) : that.involvement != null) return false;
        if (repository != null ? !repository.equals(that.repository) : that.repository != null) return false;
        if (execution != null ? !execution.equals(that.execution) : that.execution != null) return false;
        if (process != null ? !process.equals(that.process) : that.process != null) return false;
        return rawData != null ? rawData.equals(that.rawData) : that.rawData == null;
    }

    @Override
    public int hashCode() {
        int result = teamName != null ? teamName.hashCode() : 0;
        result = 31 * result + (standards != null ? standards.hashCode() : 0);
        result = 31 * result + (metrics != null ? metrics.hashCode() : 0);
        result = 31 * result + (integration != null ? integration.hashCode() : 0);
        result = 31 * result + (stakeholder != null ? stakeholder.hashCode() : 0);
        result = 31 * result + (teamManagement != null ? teamManagement.hashCode() : 0);
        result = 31 * result + (documentation != null ? documentation.hashCode() : 0);
        result = 31 * result + (assessmentProcess != null ? assessmentProcess.hashCode() : 0);
        result = 31 * result + (research != null ? research.hashCode() : 0);
        result = 31 * result + (involvement != null ? involvement.hashCode() : 0);
        result = 31 * result + (repository != null ? repository.hashCode() : 0);
        result = 31 * result + (execution != null ? execution.hashCode() : 0);
        result = 31 * result + (process != null ? process.hashCode() : 0);
        result = 31 * result + (rawData != null ? rawData.hashCode() : 0);
        return result;
    }
}
