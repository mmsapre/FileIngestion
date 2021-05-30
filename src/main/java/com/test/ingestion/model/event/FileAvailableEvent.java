package com.test.ingestion.model.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileAvailableEvent implements Serializable {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("fileUri")
    private String fileUri;

    @JsonProperty("templateId")
    private String templateId;

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("fileUri")
    public String getFileUri() {
        return fileUri;
    }

    @JsonProperty("fileUri")
    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    @JsonProperty("templateId")
    public String getTemplateId() {
        return templateId;
    }

    @JsonProperty("templateId")
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
