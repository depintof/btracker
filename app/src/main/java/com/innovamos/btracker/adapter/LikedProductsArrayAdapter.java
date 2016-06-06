package com.innovamos.btracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innovamos.btracker.R;
import com.innovamos.btracker.dto.CustomerProductsDTO;
import com.innovamos.btracker.utils.Common;

import java.util.List;

public class LikedProductsArrayAdapter<T> extends ArrayAdapter<T> {

    public LikedProductsArrayAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(
                    R.layout.wish_list_view,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView productName = (TextView)listItemView.findViewById(R.id.productItem);
        TextView productDescription = (TextView)listItemView.findViewById(R.id.descriptionItem);
        TextView discountPrice = (TextView)listItemView.findViewById(R.id.discountPriceItem);
        TextView discountAmount = (TextView)listItemView.findViewById(R.id.discountAmountItem);

        //Obteniendo instancia del producto en la posición actual
        CustomerProductsDTO item = (CustomerProductsDTO)getItem(position);

        // Fijando valores a los componentes de la lista
        productName.setText(item.getName());
        productDescription.setText(item.getDescription());
        discountPrice.setText( "$" + Common.FormatCurrency(this.getContext(), item.getFinalPrice()));
                /*Integer.toString((int) Math.ceil(
                Double.parseDouble(String.valueOf(item.getPrice())) - Double.parseDouble(String.valueOf(item.getPrice())) * Double.parseDouble(String.valueOf(item.getDiscount())) / 100)) ));*/
        discountAmount.setText( "%" + Double.parseDouble(String.valueOf(item.getDiscount())));
        //Devolver al ListView la fila creada
        return listItemView;
    }
}
