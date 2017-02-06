package valery.pankov.sourcecode;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Valery on 06.02.2017.
 */

public class DialogWrongURL extends DialogFragment implements DialogInterface.OnClickListener {



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String url = getArguments().getString("url");
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("").setPositiveButton(R.string.yes, this)
                .setMessage(getString(R.string.message_wrong_url)+ "\n"+url);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                i = R.string.yes;
                break;

        }
        if (i > 0)
            System.out.println("Dialog " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.out.println("Dialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        System.out.println("Dialog: onCancel");
    }
}