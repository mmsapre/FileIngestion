package com.test.ingestion.util;

public class FileIdGenerator {

    /** The template. */
    private final String template;

    /** The creation time millis. */
    private final long creationTimeMillis;

    /** The last time millis. */
    private long lastTimeMillis;

    /** The discriminator. */
    private long discriminator;

    /**
     * Instantiates a new file id generator.
     *
     * @param template the template
     */
    public FileIdGenerator(final String template){
        this.template=template;
        this.creationTimeMillis = System.currentTimeMillis();
        this.lastTimeMillis = creationTimeMillis;
    }

    /**
     * Creates the fileid.
     *
     * @return the string
     */
    public String createId() {
        String id;
        long now ;
        synchronized(this)
        {
        	now=System.currentTimeMillis();
        }
        if (now == lastTimeMillis) {
            ++discriminator;
        } else {
            discriminator = 0;
        }

        id = String.format("%s-%d-%d", template, creationTimeMillis,discriminator);
        lastTimeMillis = now;

        return id;
    }


}
