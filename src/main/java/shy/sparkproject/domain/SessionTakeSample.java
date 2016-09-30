package shy.sparkproject.domain;

import java.io.Serializable;

/**
 * Created by AnonYmous_shY on 2016/8/24.
 */
public class SessionTakeSample implements Serializable {

    private Integer task_id;
    private String session_id;
    private String start_time;
    private String end_time;
    private String search_keywords;
    private String click_category_ids;
    private String click_product_ids;

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getSearch_keywords() {
        return search_keywords;
    }

    public void setSearch_keywords(String search_keywords) {
        this.search_keywords = search_keywords;
    }

    public String getClick_category_ids() {
        return click_category_ids;
    }

    public void setClick_category_ids(String click_category_ids) {
        this.click_category_ids = click_category_ids;
    }

    public String getClick_product_ids() {
        return click_product_ids;
    }

    public void setClick_product_ids(String click_product_ids) {
        this.click_product_ids = click_product_ids;
    }

    public SessionTakeSample(Integer task_id, String session_id, String start_time, String end_time, String search_keywords, String click_category_ids, String click_product_ids) {
        this.task_id = task_id;
        this.session_id = session_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.search_keywords = search_keywords;
        this.click_category_ids = click_category_ids;
        this.click_product_ids = click_product_ids;
    }

    @Override
    public String toString() {
        return "SessionTakeSample{" +
                "task_id=" + task_id +
                ", session_id='" + session_id + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", search_keywords='" + search_keywords + '\'' +
                ", click_category_ids='" + click_category_ids + '\'' +
                ", click_product_ids='" + click_product_ids + '\'' +
                '}';
    }
}
