package com.matrix_maeny.decisionmaker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class DecisionDialog extends AppCompatDialogFragment {

    private AppCompatButton addBtn, okBtn;
    EditText enteredDecision;
    ListView listView;
    ArrayList<String> decisionList = null;
    ListAdapter adapter = null;
    DecisionDataBase dataBase = null;

    private DecisionListener listener = null;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View root = layoutInflater.inflate(R.layout.dilaog_decision_maker_layout, null);
        builder.setView(root);

        initialize(root);


        return builder.create();
    }

    private void initialize(@NonNull View root) {
        addBtn = root.findViewById(R.id.dialogAddBtn);
        okBtn = root.findViewById(R.id.dialogOkBtn);
        enteredDecision = root.findViewById(R.id.enterDecision);
        listView = root.findViewById(R.id.dialogDecisionList);

        addBtn.setOnClickListener(addBtnListener);
        okBtn.setOnClickListener(okBtnListener);

        decisionList = new ArrayList<>();
        decisionList.add("Don't take any thing now");
        adapter = new ListAdapter(decisionList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(listViewLongClickListener);
        loadList();

    }

    private void loadList() {
        dataBase = new DecisionDataBase(getContext());
        Cursor cursor = dataBase.getData();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                decisionList.add(cursor.getString(0));
            }
        }

        adapter.notifyDataSetChanged();
        dataBase.close();

    }

    View.OnClickListener addBtnListener = v -> {
        // add btn
        decisionAddBtn();
    };

    View.OnClickListener okBtnListener = v -> {
        // ok  btn
        setOkBtn();
    };

    AdapterView.OnItemLongClickListener listViewLongClickListener = (parent, view, position, id) -> {

        if (deleteFromDataBase(decisionList.get(position))) {
            decisionList.remove(position);
        }
        return true;
    };

    private boolean deleteFromDataBase(@NonNull String decision) {

        if (decision.equals("Don't take any thing now")) {
            return false;
        }

        dataBase = new DecisionDataBase(getContext());

        if (!dataBase.deleteData(decision)) {
            Toast.makeText(getContext(), "Error deleting data: contact matrix", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
        dataBase.close();
        return true;
    }

    public void decisionAddBtn() {
        // add btn programs

        String tempTxt = "";

        try {
            tempTxt = enteredDecision.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Please enter decision", Toast.LENGTH_SHORT).show();
        }

        enteredDecision.setText("");

        if (tempTxt.equals("")) {
            Toast.makeText(getContext(), "Please enter decision", Toast.LENGTH_SHORT).show();
            return;
        }

        addToDataBase(tempTxt);


    }

    @Deprecated
    private void removeFirstDecision() {
        if (decisionList.get(0).equals("Don't take any thing now")) {
            decisionList.remove(0);
        }
    }

    private void addToDataBase(String decision) {
        dataBase = new DecisionDataBase(getContext());

        if (!dataBase.insertData(decision)) {
            Toast.makeText(getContext(), "The decision is already exists", Toast.LENGTH_SHORT).show();
        }else{
            decisionList.add(decision);
            adapter.notifyDataSetChanged();

        }
        dataBase.close();
    }

    public void setOkBtn() {
        // ok btn programs
        listener.getList(this.decisionList);
        dismiss();

    }

    private class ListAdapter extends BaseAdapter {

        ArrayList<String> list;

        public ListAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @NonNull
        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_view_model, null);
            }

            TextView view = convertView.findViewById(R.id.decisionTextView);
            view.setText(decisionList.get(position));

            return convertView;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DecisionListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface DecisionListener {
        void getList(ArrayList<String> list);
    }
}
