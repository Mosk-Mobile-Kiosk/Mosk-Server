package team.mosk.api.server.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
public class BusinessCheckRequest {
    //필수가 아닌 값들은 ""로 대체 가능
    private String b_no ="";        //필수
    private String start_dt = "";   //필수
    private String p_nm = "";       //필수
    private String p_nm2 = "";
    private String b_nm = "";
    private String corp_no = "";
    private String b_sector = "";
    private String b_type = "";

    public BusinessCheckRequest(String b_no, String start_dt, String p_nm) {
        this.b_no = b_no;
        this.start_dt = start_dt;
        this.p_nm = p_nm;
    }

    public String getB_no() {
        return b_no;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public String getP_nm() {
        return p_nm;
    }

    public void setB_no(String b_no) {
        this.b_no = b_no;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public void setP_nm(String p_nm) {
        this.p_nm = p_nm;
    }
}
