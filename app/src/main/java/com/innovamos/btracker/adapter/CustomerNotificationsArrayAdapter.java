package com.innovamos.btracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innovamos.btracker.R;
import com.innovamos.btracker.dto.VisitsDTO;

import java.util.List;

public class CustomerNotificationsArrayAdapter<T> extends ArrayAdapter<T> {

    public CustomerNotificationsArrayAdapter(Context context, List<T> objects) {
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
                    R.layout.notifications_list_view,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView storeDescription = (TextView)listItemView.findViewById(R.id.storeDescription);
        TextView zoneName = (TextView)listItemView.findViewById(R.id.zoneName);
        TextView triggerTime = (TextView)listItemView.findViewById(R.id.triggerTime);

        //Obteniendo instancia del producto en la posición actual
        VisitsDTO item = (VisitsDTO)getItem(position);

        // Fijando valores a los componentes de la lista
        storeDescription.setText(item.getDescription());
        zoneName.setText(item.getName());
        triggerTime.setText( item.getTrigger_time() );
        //Devolver al ListView la fila creada
        return listItemView;
    }
}
