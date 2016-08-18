package shy.sparkproject.domain;

import java.io.Serializable;

/**
 * Created by AnonYmous_shY on 2016/8/18.
 */
public class SessionAggrRate implements Serializable {

    private Integer task_Id;
    private Long session_Count;
    private Double visit_Length_1s_3s_rate;
    private Double visit_Length_4s_6s_rate;
    private Double visit_Length_7s_9s_rate;
    private Double visit_Length_10s_30s_rate;
    private Double visit_Length_30s_60s_rate;
    private Double visit_Length_1m_3m_rate;
    private Double visit_Length_3m_10m_rate;
    private Double visit_Length_10m_30m_rate;
    private Double visit_Length_30m_rate;
    private Double step_Length_1_3_rate;
    private Double step_Length_4_6_rate;
    private Double step_Length_7_9_rate;
    private Double step_Length_10_30_rate;
    private Double step_Length_30_60_rate;
    private Double step_Length_60_rate;

    public Integer getTask_Id() {
        return task_Id;
    }

    public void setTask_Id(Integer task_Id) {
        this.task_Id = task_Id;
    }

    public Long getSession_Count() {
        return session_Count;
    }

    public void setSession_Count(Long session_Count) {
        this.session_Count = session_Count;
    }

    public Double getVisit_Length_1s_3s_rate() {
        return visit_Length_1s_3s_rate;
    }

    public void setVisit_Length_1s_3s_rate(Double visit_Length_1s_3s_rate) {
        this.visit_Length_1s_3s_rate = visit_Length_1s_3s_rate;
    }

    public Double getVisit_Length_4s_6s_rate() {
        return visit_Length_4s_6s_rate;
    }

    public void setVisit_Length_4s_6s_rate(Double visit_Length_4s_6s_rate) {
        this.visit_Length_4s_6s_rate = visit_Length_4s_6s_rate;
    }

    public Double getVisit_Length_7s_9s_rate() {
        return visit_Length_7s_9s_rate;
    }

    public void setVisit_Length_7s_9s_rate(Double visit_Length_7s_9s_rate) {
        this.visit_Length_7s_9s_rate = visit_Length_7s_9s_rate;
    }

    public Double getVisit_Length_10s_30s_rate() {
        return visit_Length_10s_30s_rate;
    }

    public void setVisit_Length_10s_30s_rate(Double visit_Length_10s_30s_rate) {
        this.visit_Length_10s_30s_rate = visit_Length_10s_30s_rate;
    }

    public Double getVisit_Length_30s_60s_rate() {
        return visit_Length_30s_60s_rate;
    }

    public void setVisit_Length_30s_60s_rate(Double visit_Length_30s_60s_rate) {
        this.visit_Length_30s_60s_rate = visit_Length_30s_60s_rate;
    }

    public Double getVisit_Length_1m_3m_rate() {
        return visit_Length_1m_3m_rate;
    }

    public void setVisit_Length_1m_3m_rate(Double visit_Length_1m_3m_rate) {
        this.visit_Length_1m_3m_rate = visit_Length_1m_3m_rate;
    }

    public Double getVisit_Length_3m_10m_rate() {
        return visit_Length_3m_10m_rate;
    }

    public void setVisit_Length_3m_10m_rate(Double visit_Length_3m_10m_rate) {
        this.visit_Length_3m_10m_rate = visit_Length_3m_10m_rate;
    }

    public Double getVisit_Length_10m_30m_rate() {
        return visit_Length_10m_30m_rate;
    }

    public void setVisit_Length_10m_30m_rate(Double visit_Length_10m_30m_rate) {
        this.visit_Length_10m_30m_rate = visit_Length_10m_30m_rate;
    }

    public Double getVisit_Length_30m_rate() {
        return visit_Length_30m_rate;
    }

    public void setVisit_Length_30m_rate(Double visit_Length_30m_rate) {
        this.visit_Length_30m_rate = visit_Length_30m_rate;
    }

    public Double getStep_Length_1_3_rate() {
        return step_Length_1_3_rate;
    }

    public void setStep_Length_1_3_rate(Double step_Length_1_3_rate) {
        this.step_Length_1_3_rate = step_Length_1_3_rate;
    }

    public Double getStep_Length_4_6_rate() {
        return step_Length_4_6_rate;
    }

    public void setStep_Length_4_6_rate(Double step_Length_4_6_rate) {
        this.step_Length_4_6_rate = step_Length_4_6_rate;
    }

    public Double getStep_Length_7_9_rate() {
        return step_Length_7_9_rate;
    }

    public void setStep_Length_7_9_rate(Double step_Length_7_9_rate) {
        this.step_Length_7_9_rate = step_Length_7_9_rate;
    }

    public Double getStep_Length_10_30_rate() {
        return step_Length_10_30_rate;
    }

    public void setStep_Length_10_30_rate(Double step_Length_10_30_rate) {
        this.step_Length_10_30_rate = step_Length_10_30_rate;
    }

    public Double getStep_Length_30_60_rate() {
        return step_Length_30_60_rate;
    }

    public void setStep_Length_30_60_rate(Double step_Length_30_60_rate) {
        this.step_Length_30_60_rate = step_Length_30_60_rate;
    }

    public Double getStep_Length_60_rate() {
        return step_Length_60_rate;
    }

    public void setStep_Length_60_rate(Double step_Length_60_rate) {
        this.step_Length_60_rate = step_Length_60_rate;
    }

    public SessionAggrRate(Integer task_Id, Long session_Count, Double visit_Length_1s_3s_rate, Double visit_Length_4s_6s_rate, Double visit_Length_7s_9s_rate, Double visit_Length_10s_30s_rate, Double visit_Length_30s_60s_rate, Double visit_Length_1m_3m_rate, Double visit_Length_3m_10m_rate, Double visit_Length_10m_30m_rate, Double visit_Length_30m_rate, Double step_Length_1_3_rate, Double step_Length_4_6_rate, Double step_Length_7_9_rate, Double step_Length_10_30_rate, Double step_Length_30_60_rate, Double step_Length_60_rate) {
        this.task_Id = task_Id;
        this.session_Count = session_Count;
        this.visit_Length_1s_3s_rate = visit_Length_1s_3s_rate;
        this.visit_Length_4s_6s_rate = visit_Length_4s_6s_rate;
        this.visit_Length_7s_9s_rate = visit_Length_7s_9s_rate;
        this.visit_Length_10s_30s_rate = visit_Length_10s_30s_rate;
        this.visit_Length_30s_60s_rate = visit_Length_30s_60s_rate;
        this.visit_Length_1m_3m_rate = visit_Length_1m_3m_rate;
        this.visit_Length_3m_10m_rate = visit_Length_3m_10m_rate;
        this.visit_Length_10m_30m_rate = visit_Length_10m_30m_rate;
        this.visit_Length_30m_rate = visit_Length_30m_rate;
        this.step_Length_1_3_rate = step_Length_1_3_rate;
        this.step_Length_4_6_rate = step_Length_4_6_rate;
        this.step_Length_7_9_rate = step_Length_7_9_rate;
        this.step_Length_10_30_rate = step_Length_10_30_rate;
        this.step_Length_30_60_rate = step_Length_30_60_rate;
        this.step_Length_60_rate = step_Length_60_rate;
    }

    @Override
    public String toString() {
        return "SessionAggrRate{" +
                "task_Id=" + task_Id +
                ", session_Count=" + session_Count +
                ", visit_Length_1s_3s_rate=" + visit_Length_1s_3s_rate +
                ", visit_Length_4s_6s_rate=" + visit_Length_4s_6s_rate +
                ", visit_Length_7s_9s_rate=" + visit_Length_7s_9s_rate +
                ", visit_Length_10s_30s_rate=" + visit_Length_10s_30s_rate +
                ", visit_Length_30s_60s_rate=" + visit_Length_30s_60s_rate +
                ", visit_Length_1m_3m_rate=" + visit_Length_1m_3m_rate +
                ", visit_Length_3m_10m_rate=" + visit_Length_3m_10m_rate +
                ", visit_Length_10m_30m_rate=" + visit_Length_10m_30m_rate +
                ", visit_Length_30m_rate=" + visit_Length_30m_rate +
                ", step_Length_1_3_rate=" + step_Length_1_3_rate +
                ", step_Length_4_6_rate=" + step_Length_4_6_rate +
                ", step_Length_7_9_rate=" + step_Length_7_9_rate +
                ", step_Length_10_30_rate=" + step_Length_10_30_rate +
                ", step_Length_30_60_rate=" + step_Length_30_60_rate +
                ", step_Length_60_rate=" + step_Length_60_rate +
                '}';
    }
}
