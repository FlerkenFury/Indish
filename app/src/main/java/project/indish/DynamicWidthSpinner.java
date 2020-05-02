package project.indish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import project.indish.adapter.UnitAdapter;
import project.indish.model.Unit;

@SuppressLint("AppCompatCustomView")
public class DynamicWidthSpinner extends Spinner {

    public DynamicWidthSpinner(Context context) {
        super(context);
    }

    public DynamicWidthSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicWidthSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setAdapter(UnitAdapter adapter) {
        super.setAdapter(adapter != null ? new WrapperSpinnerAdapter(adapter) : null);
    }


    public final class WrapperSpinnerAdapter implements SpinnerAdapter {

        private final UnitAdapter mBaseAdapter;


        public WrapperSpinnerAdapter(UnitAdapter baseAdapter) {
            mBaseAdapter = baseAdapter;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            return mBaseAdapter.getView(getSelectedItemPosition(), convertView, parent);
        }

        public final SpinnerAdapter getBaseAdapter() {
            return mBaseAdapter;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        public int getCount() {
            return mBaseAdapter.getCount();
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return mBaseAdapter.getDropDownView(position, convertView, parent);
        }

        public Object getItem(int position) {
            return mBaseAdapter.getItem(position);
        }

        public long getItemId(int position) {
            return mBaseAdapter.getItemId(position);
        }

        public int getItemViewType(int position) {
            return mBaseAdapter.getItemViewType(position);
        }

        public int getViewTypeCount() {
            return mBaseAdapter.getViewTypeCount();
        }

        public boolean hasStableIds() {
            return mBaseAdapter.hasStableIds();
        }

        public boolean isEmpty() {
            return mBaseAdapter.isEmpty();
        }


        public void unregisterDataSetObserver(DataSetObserver observer) {
            mBaseAdapter.unregisterDataSetObserver(observer);
        }

    }
}
