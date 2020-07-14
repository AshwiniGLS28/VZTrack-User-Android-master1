package vztrack.gls.com.vztrack_user.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.ContactHeaderAdapter;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Rainbow_Contact_Fragment extends Fragment {
    private RecyclerView recyclerView;//,recyclerView1;
    private LinearLayout noDataLayout;
    private RecyclerView.LayoutManager layoutManager;
    private ContactHeaderAdapter adapter;
    private Context context;
    private TextView rainbowTextHeaderAdmin, rainbowTextHeaderUser;
    private LinearLayout adminContact;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.rainbow_contact, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.bottom_menu_item3));
        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.contactRecyclerView);
        noDataLayout = root.findViewById(R.id.NoDataLayout);
        adminContact = root.findViewById(R.id.adminContact);
        rainbowTextHeaderAdmin = root.findViewById(R.id.rainbowTextHeaderAdmin);
        rainbowTextHeaderUser = root.findViewById(R.id.rainbowTextHeaderUser);

        context = getActivity();
        progressDialog = progressDialog.show(context,"","Loading...");

        // for admin contact
        adapter = new ContactHeaderAdapter(getActivity(), "CONTACT");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, 0));

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);
                progressDialog.dismiss();
                updateLayout();
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        boolean type = SheredPref.getType(getContext());
        // If type true then this is Company
        if (!type) {
            searchView.setQueryHint("Search By Name Or House No.");
        }else{
            searchView.setQueryHint("Search By Name Or Employee No.");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                if(query.equals("")){
                    adapter.updateContacts();
//                    adapter1.updateContacts();
                }else {
                    adapter.updateSearchedContacts(query.toLowerCase());
                }
                return true;
            }
        });
    }

    private void setDataLayout(){
        adminContact.setVisibility(View.VISIBLE);
//        userContact.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.INVISIBLE);
    }
    private void setNoDataLayoutAdmin() {
        noDataLayout.setVisibility(View.INVISIBLE);
        rainbowTextHeaderUser.setVisibility(View.VISIBLE);
//        userContact.setVisibility(View.VISIBLE);
        rainbowTextHeaderAdmin.setVisibility(View.INVISIBLE);
        adminContact.setVisibility(View.INVISIBLE);
    }

    private void updateLayout(){
        int count = 0;
        //  int count1 = 0;
        if (adapter != null ) {
            count = adapter.getItemCount();
        }

//        if(adapter1 != null){
//            count1 = adapter1.getItemCount();
//        }

        if(count == 0 ) {
            setNoDataLayoutAdmin();
        }else{
            setDataLayout();
        }
    }
}