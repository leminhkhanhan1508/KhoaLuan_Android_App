package com.example.attendence.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.attendence.Model.Request_Change_Avatar;
import com.example.attendence.Model.Request_get_inf_account;
import com.example.attendence.Model.ResponseMessage;
import com.example.attendence.Model.Response_get_inf_account;
import com.example.attendence.R;
import com.example.attendence.Service.AppServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {
    TextView txtUserCode, txtMajor, txtType, txtName, txtEmail;
    String userCode;
    de.hdodenhof.circleimageview.CircleImageView imgAvatar,imgchange_Avatar;
    private static final int REQUEST_IMAGE_CAPTURE = 123;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        imgchange_Avatar=view.findViewById(R.id.imgchange_Avatar);
        txtUserCode = view.findViewById(R.id.txtCodeUser);
        txtMajor = view.findViewById(R.id.txtMajor);
        txtType = view.findViewById(R.id.txt_user_type);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        SharedPreferences prefs = getActivity().getSharedPreferences("Info_User", Context.MODE_PRIVATE);
        userCode = prefs.getString("UserCode", null);
        GetInforAccount(userCode);
        AddEvent();
        return view;
    }

    private void AddEvent() {
        imgchange_Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

//                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(cameraIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = null;
//            Log.d("typeintent",data.getType().toString());
            if (data.getExtras() != null) {

                Bundle extras = data.getExtras();
                if (extras.get("data") != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                }


            } else {
                if(data.getData()!=null)
                {
                    Uri filePath = data.getData();

                    //Getting the Bitmap from Gallery

                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            //Setting the Bitmap to ImageView



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            Request_Change_Avatar request_change_avatar = new Request_Change_Avatar();
            request_change_avatar.setImage(encodedImage);
            request_change_avatar.setUsercode(txtUserCode.getText().toString());

            AppServiceFactory.getInstance();
            Call<ResponseMessage> Change_Avatar = AppServiceFactory.getAppService().Change_Avatar(request_change_avatar);

            final Bitmap finalImageBitmap = imageBitmap;
            Change_Avatar.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    try {
                        if (response.code() == 200) {
                            imgAvatar.setImageBitmap(finalImageBitmap);
                            Toast.makeText(getActivity(), response.body().getMassage(), Toast.LENGTH_LONG).show();

                        } else {
                            Log.d("Exception", "Error");
                        }
                    } catch (Exception exception) {
                        Log.d("Exception", exception.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });
            Log.d("byte Image", encodedImage);
            //TODO: Do somethings with bitmap
        }
    }

    private void getImage(final String urlimage) {
        final Thread threadgetImage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //load Image from listURL
                    String image = "http://192.168.1.108:8000" + urlimage;
                    //create URL of Image
                    URL url = new URL(image);
                    //create bitmap to load Image from URL
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    //add image in listImage
                    final Bitmap img = bitmap;
                    imgAvatar.post(new Runnable() {
                        @Override
                        public void run() {
                            imgAvatar.setImageBitmap(img);
                        }
                    });

                    //use Runnable to change interface of ListImageActivity


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadgetImage.start();
    }

    private void GetInforAccount(String userCode) {

        Request_get_inf_account request_get_inf_account = new Request_get_inf_account();
        request_get_inf_account.setUsercode(userCode);
        AppServiceFactory.getInstance();
        Call<Response_get_inf_account> get_inf_accountCall = AppServiceFactory.getAppService().GetInfoAccount(request_get_inf_account);
        get_inf_accountCall.enqueue(new Callback<Response_get_inf_account>() {
            @Override
            public void onResponse(Call<Response_get_inf_account> call, Response<Response_get_inf_account> response) {
                try {
                    if (response.code() == 200) {
                        txtEmail.setText(response.body().getEmail());
                        txtMajor.setText(response.body().getMajor());
                        txtUserCode.setText(response.body().getCodeUser());
                        txtName.setText(response.body().getName());
                        txtType.setText(response.body().getUserType());
                        getImage(response.body().getUrlAvatar());


                    }
                } catch (Exception exception) {
                    Log.d("Error when call retrofit get information account", exception.toString());
                }
            }

            @Override
            public void onFailure(Call<Response_get_inf_account> call, Throwable t) {
                Log.d("Error when call retrofit get information account", "onFailure");

            }
        });
    }
}