package vztrack.gls.com.vztrack_user.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.ContactAdapter;
import vztrack.gls.com.vztrack_user.beans.ContactBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.PostData;

public class ContactsActivity extends AppCompatActivity {
    ArrayList<ContactBean> contacts;
    ArrayList<ContactBean> listContacts;
    ContactAdapter adapter;
    ListView contactList;
    TextView addToInvBtn;
    LinearLayout loadingView;
    EditText filter;
    int intValue=0;
//    int intValue=0;
    Context context;
    String abc;
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        context=ContactsActivity.this;
        Intent mIntent = getIntent();
        intValue= mIntent.getIntExtra("frompreapproval", 0);

        TextView title=findViewById(R.id.title);
        title.setText("Choose Contacts");

      ImageView backpress=findViewById(R.id.backpress);
        backpress.setOnClickListener(v->onBackPressed());
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listContacts = new ArrayList<>();
        contactList = findViewById(R.id.contactList);
        addToInvBtn = findViewById(R.id.addToInvBtn);
        filter = findViewById(R.id.filter);
        contacts = new ArrayList<>();
        adapter = new ContactAdapter(this, R.layout.contact, listContacts,intValue);
        contactList.setAdapter(adapter);
        loadingView = findViewById(R.id.loadingView);
        new GetContacts(this).execute();
        filter.addTextChangedListener(watcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public ArrayList<ContactBean> getContacts() {
        ArrayList<ContactBean> contacts = new ArrayList<>();
        String phoneNumber = null, name = null;


        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    phoneNumber = cursor.getString(numberIndex);
                    phoneNumber = phoneNumber.replace(" ", "");
                    phoneNumber = phoneNumber.replace("-", "");
                    if (phoneNumber.length() >= 10) {
                        phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
                        boolean added = false;
                        for (ContactBean bean : contacts) {
                            if (bean.getMobileNo().equals(phoneNumber)) {
                                added = true;
                                break;
                            }
                        }
                        if (!added && phoneNumber.trim().length() == 10) {
                            ContactBean contact = new ContactBean();
                            contact.setContactName(name);
                            contact.setMobileNo(phoneNumber);
                            contacts.add(contact);
                        }
                    }
                }
            } finally {
                cursor.close();
            }
        }


//        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//        String _ID = ContactsContract.Contacts._ID;
//        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//
//        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
//        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
//
//        ContentResolver contentResolver = ContactsActivity.this.getContentResolver();
//        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, DISPLAY_NAME);
//
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
//                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
//                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
//                if (hasPhoneNumber > 0) {
//                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
//                    while(phoneCursor.moveToNext()){
//                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
//                        phoneNumber = phoneNumber.replace(" ", "");
//                        phoneNumber = phoneNumber.replace("-", "");
//                        if(phoneNumber.length() >=10) {
//                            phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
//                            boolean added = false;
//                            for(ContactBean bean: contacts){
//                                if(bean.getMobileNo().equals(phoneNumber)){
//                                    added = true;
//                                    break;
//                                }
//                            }
//                            if(!added && phoneNumber.trim().length() == 10) {
//                                ContactBean contact = new ContactBean();
//                                contact.setContactName(name);
//                                contact.setMobileNo(phoneNumber);
//                                contacts.add(contact);
//                            }
//                        }
//                    }
//                    phoneCursor.close();
//                }
//            }
        //}
        cursor.close();
        return contacts;
    }

    public void setSelected(ContactBean contactBean, boolean flag) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getMobileNo().equals(contactBean.getMobileNo())) {
                contacts.get(i).setSelected(flag);
                break;
            }
        }
        setBtnLabel();
    }

    public void setBtnLabel() {
        int cnt = getSelectedCount();
        if (cnt == 0) {
            addToInvBtn.setVisibility(View.GONE);
        } else {
            addToInvBtn.setText("Add " + cnt + " contact(s) to invitation");
            addToInvBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSelectedCount() > 0) {
            //New Pop up alert
            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
            TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

            TextView btnegative = dialogView.findViewById(R.id.btnegative);
            TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

            txtalertheading.setText("Go back?");
            txtalertsubheading.setText("Selected contacts are not added to invitation.");

            btnegative.setVisibility(View.VISIBLE);


            final android.app.AlertDialog b = dialogBuilder.create();
            b.setCanceledOnTouchOutside(false);
            b.setCancelable(false);
            b.show();

            btnpositive.setText("Yes");
            btnegative.setText("No");
            btnpositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.dismiss();
                    goBack();
                }
            });
            btnegative.setOnClickListener(v1 -> b.dismiss());
            //end of alert

//            AlertDialog.Builder confirm = new AlertDialog.Builder(this)
//                    .setTitle("Go back?")
//                    .setMessage("Selected contacts are not added to invitation.")
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            goBack();
//                        }
//                    })
//                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            confirm.show();
        } else {
            goBack();
        }
    }

    public void goBack() {
        super.onBackPressed();
    }

    public int getSelectedCount() {
        int cnt = 0;
        for (ContactBean contact : contacts) {
            if (contact.isSelected()) {
                cnt++;
            }
        }
        return cnt;
    }

    public void addContactsToInvitation(View v) {
        InvitationActivity.selectedContacts = getSelectedContacts();
        setResult(RESULT_OK, null);
        finish();
    }

    public class GetContacts extends AsyncTask {
        ContactsActivity context;
        ArrayList<ContactBean> contacts;

        public GetContacts(ContactsActivity context) {
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            contacts = getContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            showContacts(contacts);
        }
    }

    public void showContacts(ArrayList<ContactBean> contacts) {
        this.contacts.addAll(contacts);
        listContacts.addAll(contacts);
        adapter.notifyDataSetChanged();
        loadingView.setVisibility(View.GONE);
        filter.setEnabled(true);
    }

    public ArrayList<ContactBean> getSelectedContacts() {
        ArrayList<ContactBean> selected = new ArrayList<>();
        for (ContactBean contactBean : contacts) {
            if (contactBean.isSelected()) {
                selected.add(contactBean);
            }
        }
        return selected;
    }

    public void clearFilter(View v) {
        filter.setText("");
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            sortContacts();
        }
    };

    public void sortContacts() {
        String searchText = filter.getText().toString().trim().toLowerCase();
        if (searchText.equals("")) {
            listContacts.clear();
            listContacts.addAll(contacts);
            adapter.notifyDataSetChanged();
        } else {
            listContacts.clear();
            for (ContactBean contact : contacts) {
                if (contact.getContactName().toLowerCase().contains(searchText) || contact.getMobileNo().contains(searchText)) {
                    listContacts.add(contact);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}

