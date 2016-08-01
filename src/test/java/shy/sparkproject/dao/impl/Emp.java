package shy.sparkproject.dao.impl;

import java.util.Date;

/**
 * EMPNO     int(11)
 * ENAME     varchar(10)
 * JOB       varchar(9)
 * MGR       int(11)
 * HIREDATE  date
 * SAL       double(7,2)
 * COMM      double(7,2)
 * DEPNO     int(4)
 * Created by Shy on 2016/6/28.
 */
public class Emp {

    private Integer empno;
    private String ENAME;
    private String JOB;
    private Integer MGR;
    private Date HIREDATE;
    private Double SAL;
    private Double COMM;
    private Integer DEPNO;

    public Integer getDEPNO() {
        return DEPNO;
    }

    public void setDEPNO(Integer DEPNO) {
        this.DEPNO = DEPNO;
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getENAME() {
        return ENAME;
    }

    public void setENAME(String ENAME) {
        this.ENAME = ENAME;
    }

    public String getJOB() {
        return JOB;
    }

    public void setJOB(String JOB) {
        this.JOB = JOB;
    }

    public Integer getMGR() {
        return MGR;
    }

    public void setMGR(Integer MGR) {
        this.MGR = MGR;
    }

    public Date getHIREDATE() {
        return HIREDATE;
    }

    public void setHIREDATE(Date HIREDATE) {
        this.HIREDATE = HIREDATE;
    }

    public Double getSAL() {
        return SAL;
    }

    public void setSAL(Double SAL) {
        this.SAL = SAL;
    }

    public Double getCOMM() {
        return COMM;
    }

    public void setCOMM(Double COMM) {
        this.COMM = COMM;
    }

    public Emp(Integer empno, String ENAME, String JOB, Integer MGR, Date HIREDATE, Double SAL, Double COMM, Integer DEPNO) {
        this.empno = empno;
        this.ENAME = ENAME;
        this.JOB = JOB;
        this.MGR = MGR;
        this.HIREDATE = HIREDATE;
        this.SAL = SAL;
        this.COMM = COMM;
        this.DEPNO = DEPNO;
    }

    public Emp() {
    }

    @Override
    public String toString() {
        return "Emp{" +
                "empno=" + empno +
                ", ENAME='" + ENAME + '\'' +
                ", JOB='" + JOB + '\'' +
                ", MGR=" + MGR +
                ", HIREDATE=" + HIREDATE +
                ", SAL=" + SAL +
                ", COMM=" + COMM +
                ", DEPNO=" + DEPNO +
                '}';
    }
}
