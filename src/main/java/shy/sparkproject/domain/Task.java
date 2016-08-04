package shy.sparkproject.domain;

import java.io.Serializable;

/**
 * Created by Shy on 2016/6/29.
 */
public class Task implements Serializable{

    private Integer task_Id;
    private String task_Name;
    private String create_Time;
    private String start_Time;
    private String finish_Time;
    private String task_Type;
    private String task_Status;
    private String task_Param;

    public Integer getTask_Id() {
        return task_Id;
    }

    public void setTask_Id(Integer task_Id) {
        this.task_Id = task_Id;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public String getCreate_Time() {
        return create_Time;
    }

    public void setCreate_Time(String create_Time) {
        this.create_Time = create_Time;
    }

    public String getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(String start_Time) {
        this.start_Time = start_Time;
    }

    public String getFinish_Time() {
        return finish_Time;
    }

    public void setFinish_Time(String finish_Time) {
        this.finish_Time = finish_Time;
    }

    public String getTask_Type() {
        return task_Type;
    }

    public void setTask_Type(String task_Type) {
        this.task_Type = task_Type;
    }

    public String getTask_Status() {
        return task_Status;
    }

    public void setTask_Status(String task_Status) {
        this.task_Status = task_Status;
    }

    public String getTask_Param() {
        return task_Param;
    }

    public void setTask_Param(String task_Param) {
        this.task_Param = task_Param;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_Id=" + task_Id +
                ", task_Name='" + task_Name + '\'' +
                ", create_Time='" + create_Time + '\'' +
                ", start_Time='" + start_Time + '\'' +
                ", finish_Time='" + finish_Time + '\'' +
                ", task_Type='" + task_Type + '\'' +
                ", task_Status='" + task_Status + '\'' +
                ", task_Param='" + task_Param + '\'' +
                '}';
    }
}
