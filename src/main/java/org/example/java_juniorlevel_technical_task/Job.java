package org.example.java_juniorlevel_technical_task;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String jobPageUrl;

    @Column(length = 500)
    private String positionName;

    @Column(length = 500)
    private String organizationUrl;

    @Column(length = 500)
    private String logoUrl;

    @Column(length = 500)
    private String organizationTitle;

    @Column(length = 500)
    private String laborFunction;

    @Column(length = 500)
    private String location;

    @Column
    private Instant postedDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String tagNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobPageUrl() {
        return jobPageUrl;
    }

    public void setJobPageUrl(String jobPageUrl) {
        this.jobPageUrl = jobPageUrl;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getOrganizationUrl() {
        return organizationUrl;
    }

    public void setOrganizationUrl(String organizationUrl) {
        this.organizationUrl = organizationUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    public String getLaborFunction() {
        return laborFunction;
    }

    public void setLaborFunction(String laborFunction) {
        this.laborFunction = laborFunction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Instant postedDate) {
        this.postedDate = postedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    public Job() {
    }
}

