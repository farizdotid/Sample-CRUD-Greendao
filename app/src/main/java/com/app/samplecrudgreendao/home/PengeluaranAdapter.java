package com.app.samplecrudgreendao.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.samplecrudgreendao.R;
import com.app.samplecrudgreendao.utils.database.TblPengeluaran;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fariz Ramadhan.
 * website : https://farizdotid.com/
 * github : https://github.com/farizdotid
 * linkedin : https://www.linkedin.com/in/farizramadhan/
 */
public class PengeluaranAdapter extends
        RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    private static final String TAG = PengeluaranAdapter.class.getSimpleName();

    private List<TblPengeluaran> list;
    private PengeluaranAdapterCallback mAdapterCallback;

    public PengeluaranAdapter(List<TblPengeluaran> list, PengeluaranAdapterCallback adapterCallback) {
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengeluaran,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TblPengeluaran item = list.get(position);

        String pengeluaran = item.getPengeluaran();
        int nominal = item.getNominal();

        holder.tvPengeluaran.setText(pengeluaran);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tvNominal.setText(formatRupiah.format(nominal));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPengeluaran)
        TextView tvPengeluaran;
        @BindView(R.id.tvNominal)
        TextView tvNominal;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onDelete(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAdapterCallback.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public interface PengeluaranAdapterCallback {
        void onLongClick(int position);
        void onDelete(int position);
    }
}