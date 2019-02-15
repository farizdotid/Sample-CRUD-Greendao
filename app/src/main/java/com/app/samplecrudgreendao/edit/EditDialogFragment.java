package com.app.samplecrudgreendao.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.samplecrudgreendao.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Fariz Ramadhan.
 * website : https://farizdotid.com/
 * github : https://github.com/farizdotid
 * linkedin : https://www.linkedin.com/in/farizramadhan/
 */
public class EditDialogFragment extends DialogFragment {

    @BindView(R.id.etPembelian)
    EditText etPembelian;
    @BindView(R.id.etNominal)
    EditText etNominal;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private Unbinder unbinder;
    private EditDialogListener editDialogListener;

    private static final String ARGS_ID = "args_id";
    private static final String ARGS_PEMBELIAN = "args_pembelian";
    private static final String ARGS_NOMINAL = "args_nominal";

    private long mId;
    private String mPembelian;
    private int mNominal;

    public EditDialogFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editDialogListener = (EditDialogListener) context;
    }

    public static EditDialogFragment newInstance(long id, String pembelian, int nominal) {
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARGS_ID, id);
        args.putString(ARGS_PEMBELIAN, pembelian);
        args.putInt(ARGS_NOMINAL, nominal);
        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

        if (getArguments() != null){
            mId = getArguments().getLong(ARGS_ID);
            mPembelian = getArguments().getString(ARGS_PEMBELIAN);
            mNominal = getArguments().getInt(ARGS_NOMINAL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_edit, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPembelian.setText(mPembelian);
        etNominal.setText(String.valueOf(mNominal));

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pembelian = etPembelian.getText().toString();
                String nominal = etNominal.getText().toString();

                editDialogListener.requestUpdate(mId, pembelian, Integer.parseInt(nominal));
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface EditDialogListener {
        void requestUpdate(long id, String pembelian, int nominal);
    }
}