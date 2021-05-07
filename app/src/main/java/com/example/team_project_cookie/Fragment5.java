package com.example.team_project_cookie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Fragment5 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Intent intent;

    TextView user_name;
    ImageView user_picture;
    String user_code;
    String img_path;


    public Fragment5() {
        // Required empty public constructor
    }

    public static Fragment5 newInstance(String param1, String param2) {
        Fragment5 fragment = new Fragment5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Context mContext;
    ListView listView;
    String[] mypageMenu = {"회원정보 수정", "나의 리뷰", "고객센터", "버전정보", "로그아웃", "회원탈퇴"};
    HomeMenu activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        activity = (HomeMenu)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_fragment5, container, false);
        user_name = (TextView)rootView.findViewById(R.id.profile_user_name);
        user_name.setText(((Get_User_Info)Get_User_Info.context_main).user_name);

        user_picture = (ImageView)rootView.findViewById(R.id.iv_profile_picture);

        Get_Storage_Image get_img = new Get_Storage_Image();

        user_code = ((Get_User_Info)Get_User_Info.context_main).user_code;
        img_path = "profile/"+user_code;
        get_img.read_image(mContext, img_path, user_picture);


        listView = (ListView)rootView.findViewById(R.id.mypage_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, mypageMenu);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    dialog_profile_edit();
                } else if(position == 1) {
                    dialog_review();
                } else if(position == 2){
                    dialog_ask();
                } else if(position == 3) {
                    dialog_version();
                } else if(position == 4) {
                    dialog_logout();
                } else if(position == 5) {
                    dialog_delete();
                }
            }
        });

        return rootView;
    }



    private void dialog_profile_edit() {
        intent = new Intent(getActivity(),User_Profile_Edit.class);
        intent.putExtra("user_name",user_name.getText());
        intent.putExtra("user_code",user_code);
        startActivityForResult(intent,1);
    }

    public void dialog_review(){
        ArrayList<Review_data> review_list = new ArrayList<>();
        HashMap recipe_map= ((Get_Recipe_List)Get_Recipe_List.context_main).reviedata;

        HashMap tempMap;
        int count=1;
        review_list.clear();
        for(Object uid_key: recipe_map.keySet()){
            tempMap = (HashMap)recipe_map.get(uid_key);
            if (tempMap.get("uid").toString().equals(Login_user.login_user)) {
                Review_data review_data = new Review_data();
                review_data.setdata(tempMap.get("food_name").toString(), tempMap.get("text").toString(), Float.parseFloat(String.valueOf(tempMap.get("star"))), tempMap.get("date").toString(),tempMap.get("uid").toString(),tempMap.get("food_name").toString(),tempMap.get("food_class").toString(),uid_key.toString());
                review_list.add(review_data);

            }
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("내가 쓴 리뷰");
        ListAdapter listAdapter = new ListAdapter(getActivity(), R.layout.review_layout, review_list);

        alertBuilder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Review_data select_review = review_list.get(which);
                int list_index = which;
                new AlertDialog.Builder(mContext)
                        .setTitle("삭제").setMessage("리뷰를 삭제 하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Recipe/"
                                        + select_review.food_class + "/" + select_review.food_name + "/리뷰/").child(select_review.push_uid).removeValue();
                                review_list.remove(list_index);
                                listAdapter.notifyDataSetChanged();
                                ((Get_Recipe_List)Get_Recipe_List.context_main).reviedata.remove(select_review.push_uid);
                            }
                        }).show();
            }
        }).show();
        alertBuilder.create().show();
    }

    private void dialog_ask(){

    }

    public void dialog_logout() {
        new AlertDialog.Builder(mContext)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(activity, Login_Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((Login_Activity)Login_Activity.context_main).ch.setChecked(false);
                        startActivity(i);
                        activity.finish();
                    }
                })
                .show();
    }

    public void dialog_version() {
        new AlertDialog.Builder(mContext)
                .setTitle("버전정보").setMessage("버전정보: test")
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public void dialog_delete() {
        new AlertDialog.Builder(mContext)
                .setTitle("회원탈퇴").setMessage("회원탈퇴 하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(mContext,"계정이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                String id_code = Login_user.login_user;
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference table_user = database.getReference("User");
                                table_user.child(id_code).removeValue();
                                ((Login_Activity)Login_Activity.context_main).ch.setChecked(false);
                                Intent i = new Intent(activity, Login_Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                activity.finish();
                            }
                        });

                    }
                })
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Uri image_uri = data.getData();
                if (image_uri != null) {
                    Upload_Storage_Image upload_storage_image = new Upload_Storage_Image();
                    upload_storage_image.input_image(image_uri, img_path);
                    user_picture.setImageURI(image_uri);
                }
                ((Get_User_Info)Get_User_Info.context_main).user_name = data.getStringExtra("user_name");
                user_name.setText(data.getStringExtra("user_name"));
                Realtime_base_edit db_edit = new Realtime_base_edit();
                db_edit.upload_data("User/"+user_code+"/name",data.getStringExtra("user_name"));

            }
        }
    }
}