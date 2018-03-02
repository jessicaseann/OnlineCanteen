package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuListAdapter extends ArrayAdapter<Product> {

    HashMap<String, Integer> Order = new HashMap<>();
    ArrayList<Cart> cart;
    Product product;
    Cart cartItem;

    public MenuListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.menu_adapter_list, products);
    }

    public View getView(int position,View view,ViewGroup parent) {
        final OrderHolder holder;
        if(view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.menu_adapter_list, parent,false);

            holder = new OrderHolder();

            // Initialize views
            holder.txtTitle = view.findViewById(R.id.itemName);
            holder.imageView = view.findViewById(R.id.icon);
            holder.extratxt = view.findViewById(R.id.price);
            holder.seller = view.findViewById(R.id.seller);
            holder.quantityOrder = view.findViewById(R.id.quantityOrder);
            holder.increaseOrder = view.findViewById(R.id.increaseOrder);
            holder.decreaseOrder = view.findViewById(R.id.decreaseOrder);
            holder.increaseOrder.setTag(position);

            view.setTag(holder);
        }else{
            holder = (OrderHolder)view.getTag();
        }
        // Get product
        product = getItem(position);

        cart = new ArrayList<>();
        cartItem = new Cart(product.getName(), product.getPrice(), 0);

        //Initialize Array List for Cart
        for(int i=0; i<=getCount(); i++){
            cart.add(cartItem);
        }

        //Initialize HashMap for Order Quantity
        if(Order.get(product.getName()) == null) {
            Order.put(product.getName(), 0);
        }

        //Set Texts
        holder.txtTitle.setText(product.getName());
        holder.extratxt.setText("Rp " + product.getPrice());
        holder.seller.setText("Seller : " + product.getTokoId());
        holder.quantityOrder.setText(String.valueOf(Order.get(holder.txtTitle.getText().toString())));

        // Get image
        if(product.getImageUrl() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.imageView);
        }

        //Increase Order
        holder.increaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                //Change views
                Order.put(holder.txtTitle.getText().toString(), Order.get(holder.txtTitle.getText().toString()) + 1);
                holder.quantityOrder.setText(String.valueOf(Order.get(holder.txtTitle.getText().toString())));

                //Change item's quantity in array list
                int qty = Order.get(holder.txtTitle.getText().toString());
                Cart cartItem = new Cart(product.getName(), product.getPrice(), qty);
                cart.set(getPosition(product), cartItem); //PERBAIKIN LAGI
            }
        });

        //Decrease Order
        holder.decreaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                if(Order.get(holder.txtTitle.getText().toString()) > 0) {
                    Order.put(holder.txtTitle.getText().toString(), Order.get(holder.txtTitle.getText().toString()) - 1);
                    holder.quantityOrder.setText(String.valueOf(Order.get(holder.txtTitle.getText().toString())));
                }
            }
        });
        return view;
    }

    //Return cart arraylist
    public ArrayList<Cart> getList(){
        return cart;
    }

    static class OrderHolder {
        TextView quantityOrder;
        Button decreaseOrder;
        Button increaseOrder;
        TextView txtTitle;
        ImageView imageView;
        TextView extratxt;
        TextView seller;
    }
}