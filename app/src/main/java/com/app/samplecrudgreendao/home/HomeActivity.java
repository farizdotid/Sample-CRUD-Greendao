package com.app.samplecrudgreendao.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.samplecrudgreendao.R;
import com.app.samplecrudgreendao.create.CreateActivity;
import com.app.samplecrudgreendao.edit.EditDialogFragment;
import com.app.samplecrudgreendao.utils.FunctionHelper;
import com.app.samplecrudgreendao.utils.database.DaoHandler;
import com.app.samplecrudgreendao.utils.database.DaoSession;
import com.app.samplecrudgreendao.utils.database.TblPengeluaran;
import com.app.samplecrudgreendao.utils.database.TblPengeluaranDao;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements PengeluaranAdapter.PengeluaranAdapterCallback,
        EditDialogFragment.EditDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvNote)
    RecyclerView rvNote;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;
    @BindView(R.id.tvTotal)
    TextView tvTotal;

    private DaoSession daoSession;
    private PengeluaranAdapter pengeluaranAdapter;

    private List<TblPengeluaran> tblPengeluaranList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        daoSession = DaoHandler.getInstance(this);
        setSupportActionBar(toolbar);

        /*
        Fungi untuk READ data dari database. Contoh disini memanggil data yang berada dalam
        tabel TblPengeluaran.
         */
        tblPengeluaranList = daoSession.getTblPengeluaranDao().queryBuilder().list();
        pengeluaranAdapter = new PengeluaranAdapter(tblPengeluaranList, this);
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setItemAnimator(new DefaultItemAnimator());
        rvNote.setAdapter(pengeluaranAdapter);
        pengeluaranAdapter.notifyDataSetChanged();

        tvTotal.setText(FunctionHelper.convertRupiah(getTotal()));

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CreateActivity.class));
            }
        });
    }

    /*
    Fungsi untuk mengirim data dari adapter ke edit dialog.
    Disini memanggil EditDialogFragment dengan parameter id, pembelian, dan nominal.
     */
    @Override
    public void onLongClick(int position) {
        long id = tblPengeluaranList.get(position).getIdTblPengeluaran();
        String pembelian = tblPengeluaranList.get(position).getPengeluaran();
        int nominal = tblPengeluaranList.get(position).getNominal();

        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(id, pembelian, nominal);
        editDialogFragment.show(fm, "dialog_edit");
    }

    /*
    Fungsi delete data. Sebelum menghapus data ada semacam popup terlebih dahulu agar meyakinkan user.
     */
    @Override
    public void onDelete(int position) {
        String name = tblPengeluaranList.get(position).getPengeluaran();
        showDialogDelete(position, name);
    }

    /*
    Fungsi untuk men-totalkan semua nominal yang ada didalam tabel TblPengeluaran.
     */
    private int getTotal(){
        int total = 0;
        for (int i = 0; i < tblPengeluaranList.size(); i++){
            int nominal = tblPengeluaranList.get(i).getNominal();
            total = total + nominal;
        }
        return total;
    }

    /*
    Fungsi untuk memanggil Alert Dialog. Alert dialog ini berfungsi untuk meyakinkan user kembali
    apakah datanya ingin dihapus atau tidak.
     */
    private void showDialogDelete(final int position, String name){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
        builder1.setMessage("Yakin untuk menghapus item "+ name + " ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                        Fungsi delete suatu data bedasarkan idnya.
                         */
                        long idTbl = tblPengeluaranList.get(position).getIdTblPengeluaran();
                        daoSession.getTblPengeluaranDao().deleteByKey(idTbl);

                        tblPengeluaranList.remove(position);
                        pengeluaranAdapter.notifyItemRemoved(position);
                        pengeluaranAdapter.notifyItemRangeChanged(position, tblPengeluaranList.size());

                        tvTotal.setText(FunctionHelper.convertRupiah(getTotal()));

                        dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /*
    Fungsi ini untuk menerima data yang dikirimkan dari EditDialogFragment ke HomeActivity.
    Data yang dikirimkan dari EditDialogFragment ini ada id, pembelian, dan nominal. Lalu
    setelah mendapatkan datanya panggil fungsi update dari Greendao.
     */
    @Override
    public void requestUpdate(long id, String pembelian, int nominal) {
        TblPengeluaran tblPengeluaran = daoSession.getTblPengeluaranDao().load(id);
        tblPengeluaran.setPengeluaran(pembelian);
        tblPengeluaran.setNominal(nominal);
        daoSession.getTblPengeluaranDao().update(tblPengeluaran);

        pengeluaranAdapter.notifyDataSetChanged();
        tvTotal.setText(FunctionHelper.convertRupiah(getTotal()));
    }
}
