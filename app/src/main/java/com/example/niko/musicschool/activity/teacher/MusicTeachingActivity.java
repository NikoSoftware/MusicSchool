package com.example.niko.musicschool.activity.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.adapter.teacher.MusicTeacherListAdapter;
import com.example.niko.musicschool.control.appbar.AppbarIconController;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.TeacherModel;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by niko on 2017/3/26.
 */

public class MusicTeachingActivity extends BaseActivity {
    private AppbarIconController mAppbar;
    private LRecyclerView mLRecyclerView;
    private MusicTeacherListAdapter mAdapter;
    private int mPage = 1;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_music_teaching);
    }

    @Override
    protected void initViews() {
        mAppbar = new AppbarIconController(mContext, findViewById(R.id.appbar));
        mLRecyclerView = (LRecyclerView)findViewById(R.id.lrecycler_view);
    }

    @Override
    protected void setupViews() {

        mAppbar.init(R.drawable.selector_btn_back, "课程列表", R.drawable.selector_btn_search);
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAppbar.setOnIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(mContext, SearchActivity.class));
            }
        });



       /*  TeacherModel teacherModel = new TeacherModel();
        final CourseModel courseModel = new CourseModel();
       teacherModel.setTname("张泛美");
        teacherModel.setTage(28);
        teacherModel.setTeducation("中央美术学院");
        teacherModel.setTphone("18190745250");
        teacherModel.setTsex("女");
        teacherModel.setTimg("http://img.hairbobo.com/uploadimg/14/12/04/141204192436287.jpg");
        teacherModel.setTsubject("口笛");
        teacherModel.setTteachage(3);
        teacherModel.setTbrief("中央小学口笛教师，从事口笛教学3年");


        courseModel.setCdate("2017-06-07");
        courseModel.setCimg("http://img.hairbobo.com/uploadimg/14/12/04/141204192436287.jpg");
        courseModel.setCname("口笛初级教学");
        courseModel.setCsubject("口笛");
        courseModel.setCteacher(teacherModel);
        courseModel.setCtime("9:00-11:00");
        courseModel.setCprice(360);

        teacherModel.save(new SaveListener<String>(){

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    courseModel.save(new SaveListener<String>(){

                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                Toast.makeText(mContext,"创建成",Toast.LENGTH_SHORT).show();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    BmobQuery<TeacherModel> query = new BmobQuery<TeacherModel>();
        query.getObject("19284aac7d", new QueryListener<TeacherModel>() {

            @Override
            public void done(TeacherModel object, BmobException e) {
                if(e==null){
                    courseModel.setCteacher(object);
                    courseModel.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e1) {
                            if(e1==null){
                                Toast.makeText(mContext,"创建成",Toast.LENGTH_SHORT).show();
                            }else{

                                Log.i("bmob","失败："+e1.getMessage()+","+e1.getErrorCode());
                            }
                        }
                    });

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });*/

        setCustomDialog();
        mAdapter = new MusicTeacherListAdapter(mContext);

        final LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
       // mLRecyclerView.setPullRefreshEnabled(true);
        queryCourseList();
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryCourseList();
            }
        });


    }

    public void queryCourseList(){

        BmobQuery<CourseModel> query = new BmobQuery<CourseModel>();
        query.include("cteacher");
        query.order("-createdAt");
        query.findObjects(new FindListener<CourseModel>() {

            @Override
            public void done(List<CourseModel> CourseModels, BmobException e) {
                mLRecyclerView.refreshComplete(mPage);
                closeCustomDialog();
                if (e == null) {
                    mAdapter.setData(CourseModels);
                    closeCustomDialog();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });
    }



}
