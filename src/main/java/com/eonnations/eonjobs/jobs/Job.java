package com.eonnations.eonjobs.jobs;

import com.eonnations.eonjobs.utils.Utils;

public class Job {
    Jobs job;
    double exp;

    public Job(Jobs job, double exp) {
        this.job = job;
        this.exp = exp;
    }

    public Job(Jobs job) {
        this.job = job;
        this.exp = 0.0;
    }

    public Jobs getEnumJob() {
        return job;
    }

    public void addExp(double experience) {
        exp += experience;
    }

    public double getExp() {
        return exp;
    }

    public long getLevel() {
        return Math.round(Utils.getDoubleLevel(exp));
    }

    @Override
    public String toString() {
        return job.name().toLowerCase() + ";" + exp;
    }

    public static Job fromString(String jobString) {
        String[] stringArr = jobString.split(";");
        Jobs enumJob = Jobs.valueOf(stringArr[0].toUpperCase());
        double exp = Double.parseDouble(stringArr[1]);
        return new Job(enumJob, exp);
    }
}

