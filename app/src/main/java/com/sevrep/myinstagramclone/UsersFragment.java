package com.sevrep.myinstagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;

public class UsersFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView txtLoading;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(UsersFragment.this);
        listView.setOnItemLongClickListener(UsersFragment.this);

        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        txtLoading = view.findViewById(R.id.txtLoading);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground((users, e) -> {
            if (e == null) {
                if (users.size() > 0) {
                    for (ParseUser user : users) {
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                    txtLoading.animate().alpha(0).setDuration(500);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UsersPostsActivity.class);
        intent.putExtra("username", arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));
        parseQuery.getFirstInBackground((user, e) -> {
            if (user != null && e == null) {
                final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                prettyDialog.setTitle(user.getUsername() + "'s Profile");
                prettyDialog.setMessage(user.get("bio") + "\n" + user.get("profession") + "\n" + user.get("hobbies") + "\n" + user.get("sport"));
                prettyDialog.setIcon(R.drawable.person);
                prettyDialog.addButton("OK", R.color.pdlg_color_white, R.color.pdlg_color_green, prettyDialog::dismiss);
                prettyDialog.show();
            }
        });
        return true;
    }
}