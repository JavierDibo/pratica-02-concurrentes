package org.example;

public class PrintJob {
    private final String document;
    private final int jobId;
    private final int arrivalOrder;

    public PrintJob(String document, int jobId, int arrivalOrder) {
        this.document = document;
        this.jobId = jobId;
        this.arrivalOrder = arrivalOrder;
    }

    public String getDocument() {
        return document;
    }

    public int getJobId() {
        return jobId;
    }

    public int getArrivalOrder() {
        return arrivalOrder;
    }

    @Override
    public String toString() {
        return "PrintJob{" +
                "document='" + document + '\'' +
                ", jobId=" + jobId +
                '}';
    }
}

