package com.example.jakub.osobistytrenerbiegacza;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;
import com.example.jakub.osobistytrenerbiegacza.model.User;
import com.example.jakub.osobistytrenerbiegacza.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlanFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.OnScrollListener{
    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListAdapter mAdapter;

    private int selectedItem;
    /**
     * Previous checked element in list.
     */
    private View previousViewItem;

    public static PlanFragment newInstance(int sectionNumber) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlanFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Plan fragment on create");

        Plan plan = Singleton.getInstance().getUser().getPlan();
        Date startDate = plan.getStartDate();
        final List<Training> list = plan.getTrainings();
        mAdapter = new PlanArrayAdapter(getActivity(), R.layout.plan_list_item, android.R.id.text2, list, startDate);
        selectedItem = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Plan fragment on createview ");
        View view = inflater.inflate(R.layout.fragment_training, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

//        mListViewsetItemsCanFocus(true);
//        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        TextView tvNoHistory = (TextView) view.findViewById(R.id.fragment_history_list_tv_no_history);
        int visibility = Singleton.getInstance().getUser().getPlan().getNumberOfTrainings() != 0 ? View.GONE : View.VISIBLE;
        tvNoHistory.setVisibility(visibility);

        return view;
        }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    /**
     * Z niewiadomych przyczyn kliknięcie jednego elementu powoduje jednoczesne zaznaczenie kilku
     * innych. Ta metoda temu zapobiega.
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
        if (view.getChildAt(0) != null && view.getChildAt(visibleItemCount-1) != null) {
            CheckedTextView ctvFirstVisible = (CheckedTextView) ((ViewGroup) view.getChildAt(0)).getChildAt(0);
            CheckedTextView ctvLastVisible = (CheckedTextView) ((ViewGroup) view.getChildAt(visibleItemCount - 1)).getChildAt(0);

            if (ctvFirstVisible.isChecked() && firstVisibleItem != selectedItem) {
                ctvFirstVisible.setChecked(false);
            }

            if (ctvLastVisible.isChecked() && lastVisibleItem != selectedItem) {
                ctvLastVisible.setChecked(false);
            }

            if (!ctvFirstVisible.isChecked() && firstVisibleItem == selectedItem) {
                ctvFirstVisible.setChecked(true);
            }

            if (!ctvFirstVisible.isChecked() && lastVisibleItem == selectedItem) {
                ctvLastVisible.setChecked(true);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (previousViewItem != null) {
            CheckedTextView text1 =(CheckedTextView)((ViewGroup)previousViewItem).getChildAt(0);
            text1.setChecked(false);
        }

        CheckedTextView text1 = (CheckedTextView) ((ViewGroup)view).getChildAt(0);
        text1.setChecked(true);

        selectedItem = position;
        previousViewItem = view;

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //sends training id
            mListener.onFragmentInteraction(position);
        }
    }

//    /**
//     * The default content for this Fragment has a TextView that is shown when
//     * the list is empty. If you would like to change the text, call this method
//     * to supply the text it should use.
//     */
//    public void setEmptyText(CharSequence emptyText) {
//        View emptyView = mListView.getEmptyView();
//
//        if (emptyView instanceof TextView) {
//            ((TextView) emptyView).setText(emptyText);
//        }
//    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int id);
    }

}

class PlanArrayAdapter extends ArrayAdapter<Training> {
    private List<Training> items;

    private Date startDate;

    public PlanArrayAdapter(Context context, int resource, int id, List<Training> items, Date startDate) {
        super(context, resource, id, items);
        this.items = items;
        this.startDate = startDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = (LinearLayout)super.getView(position, convertView, parent);

        CheckedTextView text1 = (CheckedTextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

        Date date = DateFormatter.addDays(startDate, items.get(position).getTrainingNumber()-1);
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
        text1.setText(dt1.format(date));
        String text = "";
        for(TrainingType tt: items.get(position).getParts()) {
            text += tt.getDescription()+"\n";
        }
        text2.setText(text);

        return view;
    }
}