package itstudies.au.edu.groupsmsv1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String CLASS_TAG = "MainActivity";
    private String message = "";
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvMessageDetails = findViewById(R.id.tvMessageDetails);
        tvMessageDetails.setBackgroundColor(Color.GREEN);
        tvMessageDetails.setMovementMethod(new ScrollingMovementMethod());

        message = "No Message Set";
        phone = "No Number Set";

        setSummary();

        ActivityResultContract simpleRawIntentContract;
        HandleActivityResultForMessage handleActivityResultForMessage;
        HandleActivityResultForSendTo handleActivityResultForSendTo;
        ActivityResultLauncher launchEditMessageActivity;
        ActivityResultLauncher launchEditSendToActivity;

        simpleRawIntentContract = new ActivityResultContracts.StartActivityForResult();
        handleActivityResultForMessage = new HandleActivityResultForMessage();
        handleActivityResultForSendTo = new HandleActivityResultForSendTo();

        //Instantiating our launchActivity object for EditMessage
        launchEditMessageActivity = registerForActivityResult(simpleRawIntentContract, handleActivityResultForMessage);

        //Instantiating launchActivity for EditSendTo
        launchEditSendToActivity = registerForActivityResult(simpleRawIntentContract, handleActivityResultForSendTo);

        // Edit Message Button
        Button btnEditMessage;
        btnEditMessage = (Button) this.findViewById(R.id.btnEditMessage);
        ActivityResultLauncher finalLaunchEditMessageActivity = launchEditMessageActivity;
        btnEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent;
                Activity sourceActivity;
                Class destinationClass;

                sourceActivity = MainActivity.this;
                destinationClass = EditMessage.class;

                editIntent = new Intent(sourceActivity, destinationClass);
                editIntent.putExtra("CURRENT_MESSAGE", message);

//                MainActivity.this.startActivity(editIntent);
                launchEditMessageActivity.launch(editIntent);
            }
        });

        // Edit Phone Button
        /***
         * When the Send To button is clicked, it creates an intent with a destination
         * of the EditSendTo class, adds the phone number information to it, and sends
         * it to the EditSendTo activity. It does this through an ActivityResultCallback
         * method, so when the other activity calls the finish() method, it can handle
         * the information on this activity as well.
         */
        Button btnEditSendTo;
        btnEditSendTo = (Button) this.findViewById(R.id.btnEditSendTo);
        btnEditSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent;
                Activity sourceActivity;
                Class destinationClass;

                sourceActivity = MainActivity.this;
                destinationClass = EditSendTo.class;

                editIntent = new Intent(sourceActivity, destinationClass);
                editIntent.putExtra("CURRENT_PHONE", phone);

//                MainActivity.this.startActivity(editIntent);
                launchEditSendToActivity.launch(editIntent);
            }
        });

        /***
         * When the Send button is clicked, it sends the text to the phone number.
         */
        Button btnSend;
        btnSend = (Button) this.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent to " + phone, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setSummary() {
        StringBuilder summary;
        summary = new StringBuilder("Sending To:\n");
        summary.append(phone);
        summary.append("\n\nMessage:\n");
        summary.append(message);
        TextView tvMessageDetails = (TextView) findViewById(R.id.tvMessageDetails);
        tvMessageDetails.setText(summary);
    }

    public class HandleActivityResultForMessage implements ActivityResultCallback {
        public void onActivityResult(Object dataIn) {
            ActivityResult result;
            result = (ActivityResult)dataIn;
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String newMessage = (String) (data.getStringExtra("NEW_MESSAGE"));
                message = newMessage;
                setSummary();
            }
        }
    }

    /***
     * This takes the new number created by the EditSendTo activity to
     * re-write the old phone number. It checks if the activity succeeded, and
     * then calls setSummary again to replace it with new info.
     */
    public class HandleActivityResultForSendTo implements ActivityResultCallback {
        public void onActivityResult(Object dataIn) {
            ActivityResult result;
            result = (ActivityResult)dataIn;
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String newPhone = (String) (data.getStringExtra("NEW_PHONE"));
                phone = newPhone;
                setSummary();
            }
        }
    }
}