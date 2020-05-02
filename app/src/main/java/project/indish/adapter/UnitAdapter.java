package project.indish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import project.indish.R;
import project.indish.model.Unit;

public class UnitAdapter extends ArrayAdapter<Unit> {

    public UnitAdapter(Context context, ArrayList<Unit> units) {
        super(context, 0, units);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.unit_spinner_row, parent, false
            );
        }

        TextView spinnerUnit = convertView.findViewById(R.id.spinner_unit);

        Unit currItem = getItem(position);

        if (currItem != null){
            spinnerUnit.setText(currItem.getName());
        }

        return convertView;
    }

}
