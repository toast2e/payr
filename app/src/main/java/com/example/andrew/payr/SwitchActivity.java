package com.example.andrew.payr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SwitchActivity extends AppCompatActivity {

    private final String TAG="SwitchActivity";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        // retrieved saved payer and amount
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String savedPayr = sharedPref.getString(getString(R.string.saved_payr), getString(R.string.diana_string));
        float savedAmount = sharedPref.getFloat(getString(R.string.saved_payr_amount), 0F);

        setCurrentlyOwesText(savedPayr, savedAmount);
    }

    private void setCurrentlyOwesText(String ower, float amount)
    {
        // set who owes text
        TextView whoOwes = (TextView) findViewById(R.id.owes_text);
        whoOwes.setText(String.format(getString(R.string.who_owes), ower));
        // set owes amount text
        TextView owesAmount = (TextView)(findViewById(R.id.currently_owes));
        owesAmount.setText(String.format(getString(R.string.owes_amount), amount));
    }

    public void calculatePayr(View view) {
        float savedAmount = sharedPref.getFloat(getString(R.string.saved_payr_amount), 0F);
        EditText amountPaidText = (EditText) findViewById(R.id.amount_paid);
        float amountPaid = Float.parseFloat(amountPaidText.getText().toString());

        String savedPayr = sharedPref.getString(getString(R.string.saved_payr), getString(R.string.diana_string));
        String currentPayr = getCurrentPayr();

        float diff = savedAmount;
        if(currentPayr.equals(savedPayr))
        {
            diff += amountPaid;
        }
        else
        {
            diff -= amountPaid;
        }
        String newPayr = savedPayr;
        if(diff <= 0)
        {
            newPayr = swapPayr(savedPayr);
            diff *= -1;
        }
        setCurrentlyOwesText(newPayr, diff);
        saveAmount(newPayr, diff);
    }

    private String getCurrentPayr()
    {
        RadioGroup group = (RadioGroup) findViewById(R.id.payees);
        RadioButton button = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        return button.getText().toString();
    }

    private String swapPayr(String prevPayer)
    {
        String nextPayr;
        if(prevPayer != null && prevPayer.equals(getString(R.string.andrew_string)))
        {
            return getString(R.string.diana_string);
        }
        else
        {
            return getString(R.string.andrew_string);
        }
    }

    private void saveAmount(String payr, float amount)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_payr), payr);
        editor.putFloat(getString(R.string.saved_payr_amount), amount);
        editor.commit();
    }
}
