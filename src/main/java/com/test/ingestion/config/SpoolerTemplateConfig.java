package com.test.ingestion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "file", ignoreUnknownFields = true)
public class SpoolerTemplateConfig {

    private String defaultFilePath;
    private Map<String, ReferenceFileConfig> templateConfig = new HashMap<String, ReferenceFileConfig>();

    public String getDefaultFilePath() {
        return defaultFilePath;
    }

    public void setDefaultFilePath(String defaultFilePath) {
        this.defaultFilePath = defaultFilePath;
    }

    public Map<String, ReferenceFileConfig> getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfg(Map<String, ReferenceFileConfig> templateConfg) {
        this.templateConfig = templateConfg;
    }

    public static class ReferenceFileConfig {

        private String prefifxIdentifier;
        private String delimiter;
        private boolean useHeader;
        private boolean useTemplate;
        private String template;
        private String partition;
        public String getPrefifxIdentifier() {
            return prefifxIdentifier;
        }

        public void setPrefifxIdentifier(String prefifxIdentifier) {
            this.prefifxIdentifier = prefifxIdentifier;
        }

        public boolean isUseTemplate() {
            return useTemplate;
        }

        public void setUseTemplate(boolean useTemplate) {
            this.useTemplate = useTemplate;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public boolean isUseHeader() {
            return useHeader;
        }

        public void setUseHeader(boolean useHeader) {
            this.useHeader = useHeader;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        public String getPartition() {
            return partition;
        }

        public void setPartition(String partition) {
            this.partition = partition;
        }
    }
}
