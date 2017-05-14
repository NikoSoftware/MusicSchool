package com.example.niko.musicschool.JsonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko on 2016/9/23.
 */
public class SubjectLevelModel {


    /**
     * status : 0
     * msg : 获取成功
     * data : [{"id":"1","grade_specialty_id":"1","name":"钢琴一级","icon":"http://music.baooo.cn/upload/grade_icon1.png"},{"id":"2","grade_specialty_id":"1","name":"钢琴二级","icon":"http://music.baooo.cn/upload/grade_icon1.png"},{"id":"3","grade_specialty_id":"1","name":"钢琴三级","icon":"http://music.baooo.cn/upload/grade_icon1.png"}]
     * token : 362ba3efcd969a8a0b712b3095fe1c9a
     */

    private String status;
    private String msg;
    private String token;
    private String json = "[{\"id\":\"1\",\"grade_specialty_id\":\"1\",\"name\":\"一级\",\"icon\":\"http://music.baooo.cn/upload/grade_icon1.png\"},{\"id\":\"2\",\"grade_specialty_id\":\"1\",\"name\":\"二级\",\"icon\":\"http://music.baooo.cn/upload/grade_icon1.png\"},{\"id\":\"3\",\"grade_specialty_id\":\"1\",\"name\":\"三级\",\"icon\":\"http://music.baooo.cn/upload/grade_icon1.png\"}]";

    /**
     * id : 1
     * grade_specialty_id : 1
     * name : 钢琴一级
     * icon : http://music.baooo.cn/upload/grade_icon1.png
     */

    public SubjectLevelModel(){

        parseJson(json);
    }

    private void parseJson(String json) {

        JSONObject object =null;
        JSONArray objectArray =null;
        try {
             objectArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i <objectArray.length() ; i++) {
            try {
                object = objectArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            DataBean dataBean  = new DataBean();

            dataBean.id = object.optString("id");
            dataBean.grade_specialty_id = object.optString("grade_specialty_id");
            dataBean.name = object.optString("name");
            dataBean.icon = object.optString("icon");

            data.add(dataBean);

        }


    }


    private List<DataBean> data = new ArrayList<DataBean>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String grade_specialty_id;
        private String name;
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGrade_specialty_id() {
            return grade_specialty_id;
        }

        public void setGrade_specialty_id(String grade_specialty_id) {
            this.grade_specialty_id = grade_specialty_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
