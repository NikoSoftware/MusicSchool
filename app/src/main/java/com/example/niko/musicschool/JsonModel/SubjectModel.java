package com.example.niko.musicschool.JsonModel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko on 2016/9/7.
 */
public class SubjectModel {


    /**
     * status : 0
     * msg : 获取成功
     * data : [{"id":"1","name":"钢琴","icon":"http://music.baooo.cn/upload/grade_icon1.png","sort":"0"},{"id":"2","name":"小提琴","icon":"http://music.baooo.cn/upload/grade_icon2.png","sort":"0"},{"id":"3","name":"电子琴","icon":"http://music.baooo.cn/upload/grade_icon3.png","sort":"0"}]
     */

    private String status;
    private String msg;
    private String json = "[{\"id\":\"1\",\"name\":\"钢琴\",\"icon\":\"http://music.baooo.cn/upload/grade_icon1.png\",\"sort\":\"0\"},{\"id\":\"2\",\"name\":\"小提琴\",\"icon\":\"http://music.baooo.cn/upload/grade_icon2.png\",\"sort\":\"0\"},{\"id\":\"3\",\"name\":\"电子琴\",\"icon\":\"http://music.baooo.cn/upload/grade_icon3.png\",\"sort\":\"0\"}]";

    /**
     * id : 1
     * name : 钢琴
     * icon : http://music.baooo.cn/upload/grade_icon1.png
     * sort : 0
     */

    public SubjectModel(){

        parseJson(json);
    }

    private void parseJson(String json) {

            JSONObject object = null;
        JSONArray objectArray = null;
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
            DataBean dataBean= new DataBean();

                dataBean.id =object.optString("id");
                dataBean.name =object.optString("name");
                dataBean.icon =object.optString("icon");
                dataBean.sort =object.optString("sort");

                data.add(dataBean);
            }



    }


    private List<DataBean> data =new ArrayList<>();

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        private String id;
        private String name;
        private String icon;
        private String sort;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.icon);
            dest.writeString(this.sort);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.icon = in.readString();
            this.sort = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
