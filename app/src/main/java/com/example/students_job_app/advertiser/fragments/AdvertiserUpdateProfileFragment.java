package com.example.students_job_app.advertiser.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.students_job_app.R;
import com.example.students_job_app.model.Advertiser;
import com.example.students_job_app.utils.SharedPrefManager;
import com.example.students_job_app.utils.Urls;
import org.json.JSONException;
import org.json.JSONObject;

public class AdvertiserUpdateProfileFragment extends Fragment {

    Context context;
    EditText  mNameET , mPhoneET, mWebsiteET, mAddressET, mProfField, mYearsOfIncorporation;
    String name, phone, website, address, profField, yearsOfIncorporation;
    Button updateBtn;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public AdvertiserUpdateProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertiser_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameET = view.findViewById(R.id.name);
        mPhoneET= view.findViewById(R.id.phone);
        mWebsiteET= view.findViewById(R.id.website);
        mAddressET = view.findViewById(R.id.location);
        mProfField= view.findViewById(R.id.prof_field);
        mYearsOfIncorporation= view.findViewById(R.id.years_of_corp);
        updateBtn = view.findViewById(R.id.update);

        Advertiser advertiser = SharedPrefManager.getInstance(context).getAdvertiserData();

        mNameET.setText(advertiser.getAdvertiserName());
        mPhoneET.setText(advertiser.getPhone());
        mWebsiteET.setText(advertiser.getWebsite());
        mAddressET.setText(advertiser.getAddress());
        mProfField.setText(advertiser.getProfessional_field());
        mYearsOfIncorporation.setText(advertiser.getYears_of_incorporation());

        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        updateBtn.setOnClickListener(v->{
            updateProfile();
        });
    }

    private void updateProfile() {
        pDialog.show();
        String url = Urls.UPDATE_ADVERTISER;
        updateBtn.setEnabled(false);

        name = mNameET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        website = mWebsiteET.getText().toString().trim();
        address = mAddressET.getText().toString().trim();
        profField = mProfField.getText().toString().trim();
        yearsOfIncorporation = mYearsOfIncorporation.getText().toString().trim();

        String advertiserId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("advertiser_id", advertiserId)
                .addBodyParameter("name", name)
                .addBodyParameter("phone", phone)
                .addBodyParameter("website", website)
                .addBodyParameter("location", address)
                .addBodyParameter("professional_fields", profField)
                .addBodyParameter("years_of_incorporation", yearsOfIncorporation)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User Saved";
                            JSONObject object = obj.getJSONObject("data");

                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                SharedPrefManager.getInstance(context).advertiserUpdate(
                                                object.getString("name"),
                                                object.getString("phone"),
                                                object.getString("website"),
                                                object.getString("address"),
                                                object.getString("professional_field"),
                                                object.getString("years_of_incorporation")
                                );
                            }

                            pDialog.dismiss();
                            updateBtn.setEnabled(true);
                            navController.popBackStack();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("update adv catch", e.getMessage());
                            pDialog.dismiss();
                            updateBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        updateBtn.setEnabled(true);
                        Log.e("updateadverror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("name")) {
                                Toast.makeText(context, data.getJSONArray("advertiser_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(context, data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(context, data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(context, data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("website")) {
                                Toast.makeText(context, data.getJSONArray("website").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("professional_fields")) {
                                Toast.makeText(context, data.getJSONArray("professional_fields").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("address")) {
                                Toast.makeText(context, data.getJSONArray("address").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("professional_field")) {
                                Toast.makeText(context, data.getJSONArray("professional_field").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("years_of_incorporation")) {
                                Toast.makeText(context, data.getJSONArray("years_of_incorporation").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}