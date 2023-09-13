package itstudies.au.edu.groupsmsv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditSendTo extends AppCompatActivity {

    /***
     * When this activity is created, it gets the intent that called it,
     * and takes the phone number info from the main activity. Then it
     * turns the text box into something that can be parsed. The button,
     * when pressed, checks if the string is a valid phone number, by
     * checking if it is an integer, then checks if it is equal to 10
     * characters in length. If this is true, it creates a new intent
     * to go to the main activity and adds the new phone number to it
     * so the main activity can update too.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_send_to);

        // Get Intent handler
        Intent editIntent;
        EditText etPhone;
        editIntent = this.getIntent();
        String phoneNo;
        phoneNo = editIntent.getStringExtra("CURRENT_PHONE");
        etPhone = (EditText) this.findViewById(R.id.etPhone);
        etPhone.setText(phoneNo);

        // Event Handler for Done Button
        Button btnDone = (Button) this.findViewById(R.id.btnEditSendToDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Integer.parseInt(etPhone.getText().toString());
                    // redundant code but it works fur now
//                    int phone = Integer.valueOf(etPhone.getText().toString());
                    if (etPhone.getText().toString().length() == 10) {
                        Intent intent = new Intent();
                        intent.putExtra("NEW_PHONE", ((EditText) findViewById(R.id.etPhone)).getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(EditSendTo.this, "Phone number is not legal.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(EditSendTo.this, "Phone number is not valid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}