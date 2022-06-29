package com.hellokh.sovary.firetest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    ArrayList<DerrumbeHueco> list = new ArrayList<>();

    public RVAdapter(Context ctx)
    {
        this.context = ctx;
    }
    public void setItems(ArrayList<DerrumbeHueco> dh)
    {
        list.addAll(dh);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new DerrumbeHuecoVH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        DerrumbeHueco e = null;
        this.onBindViewHolder(holder,position,e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, DerrumbeHueco e)
    {
        DerrumbeHuecoVH vh = (DerrumbeHuecoVH) holder;
        DerrumbeHueco dh = e==null? list.get(position):e;

        Picasso.with(context)
                        .load(dh.getImage())
                                .fit().centerInside().into(vh.imageView);
        vh.lblCanton.setText(dh.getCanton());
        vh.lblDistrito.setText(dh.getDistrito());
        vh.lblSeveridad.setText(dh.getSeveridad());
        vh.lblEstado.setText(dh.getEstado());
        vh.lblFecha.setText(dh.getFecha());
        vh.txt_option.setOnClickListener(v->
        {
            PopupMenu popupMenu =new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item->
            {
                switch (item.getItemId())
                {
                    case R.id.menu_edit:
                        Intent intent=new Intent(context,FormActivity.class);
                        intent.putExtra("EDITAR",dh);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        DAODerrumbeHueco dao=new DAODerrumbeHueco();
                        dao.remove(dh.getKey()).addOnSuccessListener(suc->
                        {
                            Toast.makeText(context, "Registro eliminado", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            list.remove(dh);
                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                        break;
                }
                return false;
            });
            popupMenu.show();
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FormActivity.class);
                intent.putExtra("DETALLES",dh);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
