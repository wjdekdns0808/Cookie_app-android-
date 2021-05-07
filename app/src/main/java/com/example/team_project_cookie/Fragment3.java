package com.example.team_project_cookie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */public class Fragment3 extends Fragment {

    private static Intent intent;
    private static myrefArrayList arr_ref = new myrefArrayList();
    ViewGroup rootView;


    public static MyrefAdapter adapter_ref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    GridView gv_ref;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    private FloatingActionButton fab1, fab2, fab3;

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
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_my_refrigerator, container, false);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_colse);

        fab1 = (FloatingActionButton)rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)rootView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)rootView.findViewById(R.id.fab3);

        CoordinatorLayout cl = (CoordinatorLayout)rootView.findViewById(R.id.layout1);
        cl.setOnClickListener(ls);

        gv_ref = (GridView) rootView.findViewById(R.id.gv_ref);
        if(!(arr_ref.ral==null)){
            arr_ref.ral.clear();
            arr_ref.ral2.clear();
            arr_ref.category.clear();
        }

        HashMap my_material_tmep;

        for (Object key : ((Get_User_Info) Get_User_Info.context_main).my_material_map.keySet()) {
            my_material_tmep = ((Get_User_Info) Get_User_Info.context_main).my_material_map.get(key);
            for (Object key_value : my_material_tmep.keySet()) {
                arr_ref.ral.add(key_value.toString());
                arr_ref.ral2.add(my_material_tmep.get(key_value).toString());
                arr_ref.category.add(key.toString());
            }
        }



        adapter_ref = new MyrefAdapter(getActivity(), R.layout.customlist_ref, R.id.tv_item1, arr_ref.ral, arr_ref.ral2);
        gv_ref.setAdapter(adapter_ref);
        gv_ref.setOnItemLongClickListener(longls);
        fab1.setOnClickListener(ls);
        fab2.setOnClickListener(ls);
        fab3.setOnClickListener(ls);

        return rootView;
    }

    AdapterView.OnItemLongClickListener longls = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList dialog_list = new ArrayList();
            dialog_list.add("수정");
            dialog_list.add("삭제");
            dialog_list.add("취소");
            ArrayAdapter dialog_ad = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,dialog_list);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setAdapter(dialog_ad, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        EditText et = new EditText(getContext());

                        new AlertDialog.Builder(getContext()).setTitle("수정하기").setMessage("수정할 값을 입력해주세요").setView(et)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Realtime_base_edit rDB = new Realtime_base_edit();


                                        String count = et.getText().toString();
                                        rDB.upload_data("User/"+Login_user.login_user+"/재료/"+arr_ref.category.get(position)+"/"+arr_ref.ral.get(position),count);
                                        arr_ref.ral2.add(position,count);
                                        adapter_ref.notifyDataSetChanged();
                                        ((Get_User_Info) Get_User_Info.context_main).my_material_all_map.put(arr_ref.ral.get(position),count);
                                    }
                                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }else if(which==1){
                        new AlertDialog.Builder(getContext()).setTitle("삭제하기").setMessage("삭제 하시겠습니까?")
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Realtime_base_edit rDB = new Realtime_base_edit();
                                        rDB.delete_data("User/"+Login_user.login_user+"/재료/"+arr_ref.category.get(position),arr_ref.ral.get(position));
                                        arr_ref.ral.remove(position);
                                        arr_ref.ral2.remove(position);
                                        arr_ref.category.remove(position);
                                        adapter_ref.notifyDataSetChanged();
                                        ((Get_User_Info) Get_User_Info.context_main).my_material_all_map.remove(arr_ref.ral.get(position));
                                    }
                                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }else{
                        return;
                    }
                }
            }).show();

            return false;
        }
    };
    static void additem(String item1 , String item2,String category){
        for(int i =0 ; i<arr_ref.ral.size();i++){
            if(arr_ref.ral.get(i).equals(item1)){
                arr_ref.ral2.set(i,String.valueOf(Integer.parseInt(arr_ref.ral2.get(i))+Integer.parseInt(item2)));
                return;
            }
        }
        arr_ref.category.add(category);
        arr_ref.ral.add(item1);
        arr_ref.ral2.add(item2);
    }
    View.OnClickListener ls = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.fab1:
                    anim();
                    break;

                case R.id.fab2:

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/"));
                    startActivity(intent);
                    anim();
                    break;
                case R.id.fab3:

                    intent = new Intent(getActivity(),AddproductActivity.class);
                    startActivityForResult(intent,1);
                    anim();

                    break;
            }
        }
    };


    public void anim(){
        if(isFabOpen){
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        }else {
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }


    }

}
class MyrefAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    myArrayList arr = new myArrayList();
    Get_Storage_Image get_img = new Get_Storage_Image();

    public MyrefAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects, ArrayList<String> objects2) {
        super(context, resource, textViewResourceId, objects);

        arr.al = objects;
        arr.al2 = objects2;
        inflater = LayoutInflater.from(context);
        arr.al = objects;
        arr.al2 = objects2;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.customlist_ref, null);
        }

        ImageView iv = (ImageView) view.findViewById(R.id.image);
        TextView tv = (TextView) view.findViewById(R.id.tv_item1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_item2);
        tv.setText(arr.al.get(position));


        if (arr.al2.get(position).equals("0"))
        tv2.setText(get_material_unit(arr.al.get(position)));
        else
        tv2.setText(arr.al2.get(position)+get_material_unit(arr.al.get(position)));

        HashMap<String,String> material_img_path= new HashMap<String,String>();
        material_img_path = ((Get_Sever_Data)Get_Sever_Data.context_main).material_all;
        String img_path = "";
        if (material_img_path.containsKey(arr.al.get(position))){//material 이미지 경로 구하기
            img_path = material_img_path.get(arr.al.get(position));
        }

        get_img.read_image(view.getContext(),img_path,iv);//냉장고 material 이미지 등록

        return view;
    }

    String get_material_unit (String material)
    {
        String material_unit="";
        HashMap material_category = new HashMap();
        HashMap<String,String> material_hash;
        material_category = ((Get_Sever_Data)Get_Sever_Data.context_main).material_category;
        String material_class = "";

        for (Object category : material_category.keySet()) {
            material_hash = new HashMap();
            material_hash = (HashMap)material_category.get(category);

            if (material_hash.containsKey(material)) {
                material_class = category.toString();

                if (material_class.equals("소스")) {
                    return "-";
                } else if (material_class.equals("과일")) {
                    return "개";
                } else if (material_class.equals("수산물")) {
                    return "개";
                } else if (material_class.equals("육류")) {
                    return "g";
                } else if (material_class.equals("채소")) {
                    return "개";
                }else if (material_class.equals("기타")) {
                    return "-";
                }
            }
        }


        return material_unit;
    }
}

