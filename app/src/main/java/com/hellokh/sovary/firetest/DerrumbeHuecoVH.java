package com.hellokh.sovary.firetest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DerrumbeHuecoVH extends RecyclerView.ViewHolder
{
    public TextView lblCanton,lblDistrito, lblSeveridad, lblEstado, lblFecha, txt_option;
    public DerrumbeHuecoVH(@NonNull View itemView)
    {
        super(itemView);
        lblCanton = itemView.findViewById(R.id.lblCanton);
        lblDistrito = itemView.findViewById(R.id.lblDistrito);
        lblSeveridad = itemView.findViewById(R.id.lblSeveridad);
        lblEstado = itemView.findViewById(R.id.lblEstado);
        lblFecha = itemView.findViewById(R.id.lblFecha);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
