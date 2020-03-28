package com.example.coffeetimefoods;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //#########################################################################################     //Variables que se traen del mismO dispositivo
    public static String local_sTelefono;
    public static List<String> local_listProductos;
    public static List<Adicionales_Entidad> local_listAdicionales;
    public static List<Opciones_Entidad> local_listOpciones;
    public static List<Presentaciones_Entidad> local_listPresentacion;
    //#########################################################################################     //Keys de la base de datos

    //#########################################################################################     //Objetos del Layout
    EditText oetCliente,oetRequerimiento;
    ImageButton oibIzquierda,oibDerecha;
    Button obAgregar,obBorrar,obEnviar;
    TextView otvNombreProducto,otvPrecioFinal;

    RadioGroup orgPresentación;
    RadioButton orbPresentación_1,orbPresentación_2;

    CheckBox ocbAdicional_1,ocbAdicional_2,ocbAdicional_3,
            ocbAdicional_4,ocbAdicional_5,ocbAdicional_6;
    RadioGroup orgOpciones;
    RadioButton orbOpción_1,orbOpción_2,orbOpción_3,
            orbOpción_4,orbOpción_5,orbOpción_6;

    ListView olvResumenOrden;
    Spinner osHora;

    //#########################################################################################     Variables Globales
    String sTelefono;
    List<String> listProductos;
    List<Adicionales_Entidad> listAdicionales;
    List<Adicionales_Entidad> AdicionalesActual=new ArrayList<>();
    List<Opciones_Entidad> listOpciones;
    List<Opciones_Entidad> OpcionesActual=new ArrayList<>();
    List<Presentaciones_Entidad> listPresentacion;
    List<Presentaciones_Entidad> PresentacionActual=new ArrayList<>();

    int iConteoProducto=0;

    Context context;



    //#########################################################################################################################################
    //#########################################################################################     ON CREATE
    //#########################################################################################################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //#####################################################################################     Relación de objetos con Layout
        oetCliente=(EditText)findViewById(R.id.etCliente);
        oetRequerimiento=(EditText)findViewById(R.id.etCliente);

        oibIzquierda=(ImageButton)findViewById(R.id.ibIzquierda);
        oibDerecha=(ImageButton)findViewById(R.id.ibDerecha);
        obAgregar=(Button)findViewById(R.id.bAgregar);
        obEnviar=(Button)findViewById(R.id.bEnviar);
        otvNombreProducto=(TextView)findViewById(R.id.tvProducto);
        otvPrecioFinal=(TextView)findViewById(R.id.tvPrecioFinal);
        orgPresentación=(RadioGroup)findViewById(R.id.rgPresentación);
        orbPresentación_1=(RadioButton)findViewById(R.id.rbPresentación_1);
        orbPresentación_2=(RadioButton)findViewById(R.id.rbPresentación_2);
        ocbAdicional_1=(CheckBox)findViewById(R.id.cbAdicional_1);
        ocbAdicional_2=(CheckBox)findViewById(R.id.cbAdicional_2);
        ocbAdicional_3=(CheckBox)findViewById(R.id.cbAdicional_3);
        ocbAdicional_4=(CheckBox)findViewById(R.id.cbAdicional_4);
        ocbAdicional_5=(CheckBox)findViewById(R.id.cbAdicional_5);
        ocbAdicional_6=(CheckBox)findViewById(R.id.cbAdicional_6);
        orgOpciones=(RadioGroup)findViewById(R.id.rgOpciones);
        orbOpción_1=(RadioButton)findViewById(R.id.rbOpción_1);
        orbOpción_2=(RadioButton)findViewById(R.id.rbOpción_2);
        orbOpción_3=(RadioButton)findViewById(R.id.rbOpción_3);
        orbOpción_4=(RadioButton)findViewById(R.id.rbOpción_4);
        orbOpción_5=(RadioButton)findViewById(R.id.rbOpción_5);
        orbOpción_6=(RadioButton)findViewById(R.id.rbOpción_6);

        olvResumenOrden=(ListView)findViewById(R.id.lvResumenOrden);
        osHora=(Spinner)findViewById(R.id.sHora);

        //##################################################################################         Acciones iniciales
        orgOpciones.setVisibility(View.INVISIBLE);
        orgPresentación.check(0);

        fDatosTienda();             //Adquirir numero de telefono para enviar los mensajes
        fDatosTiposDeProductos();   //Primero recoge productos, luego adicionales, luego opciones,
                                    //Luego presentación, todo lo hace en cadena



        //Acciones de botones
        oibIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iConteoProducto--;
                if (iConteoProducto<=0){
                    iConteoProducto=listProductos.size();
                }
                fLlamarCargues();
                fOperar();
            }
        });
        oibDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iConteoProducto++;
                if (iConteoProducto>listProductos.size()){
                    iConteoProducto=1;
                }
                fLlamarCargues();
                fOperar();
            }
        });

        orgPresentación.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (orbPresentación_2.isChecked()){
                    orgOpciones.setVisibility(View.VISIBLE);
                    orgOpciones.check(orbOpción_1.getId());
                }
                else{
                    orgOpciones.setVisibility(View.INVISIBLE);

                }
                fOperar();
            }
        });

        ocbAdicional_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });
        ocbAdicional_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });
        ocbAdicional_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });
        ocbAdicional_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });
        ocbAdicional_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });
        ocbAdicional_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fOperar();
            }
        });





    }
    //llamado de funciones para llenar formulario
    void fLlamarCargues(){

        otvNombreProducto.setText(listProductos.get(iConteoProducto-1));
        orgOpciones.setVisibility(View.INVISIBLE);
        fBorrarActuales();
        fCargarPresentaciones(iConteoProducto-1);
        fCargarAdicionales(iConteoProducto-1);
        fCargarOpciones(iConteoProducto-1);
    }


    // ############################################################################################     Recoleccióin de datos del BD
    void fDatosTienda(){
        String sPath = "Negocios/Cra 21";
        DocumentReference bd_Datos=FirebaseFirestore.getInstance().document(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> aKeys = new ArrayList<String>(document.getData().keySet());
                        ArrayList aValues = new ArrayList(document.getData().values());

                        for(int i=0;i<aValues.size();i++){
                            if (aKeys.get(i).equals("Telefono")){
                                sTelefono=aValues.get(i).toString();
                            }
                        }
                        //Toast.makeText(MainActivity.this,"Telefono: "+sTelefono,Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Distribuidor", "No such document");
                    }
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
    }
    void fDatosTiposDeProductos(){
        String sPath = "Negocios/Cra 21/Productos";
        CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listProductos=new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList aDatos = new ArrayList(document.getData().values());
                        listProductos.add(aDatos.get(0).toString());
                    }
                    fPresentacionProductos();
                    fAdicionalesProductos();
                    fOpcionesProductos();
                    otvNombreProducto.setText(listProductos.get(0));
                    fCargarPresentaciones(0);
                    fCargarAdicionales(0);
                    fCargarOpciones(0);
                    //Toast.makeText(MainActivity.this,"Documentos: "+listProductos.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
    }
    void fPresentacionProductos(){
        listPresentacion=new ArrayList<>();

        for(int i=0;i<listProductos.size();i++){
            String sPath = "Negocios/Cra 21/Productos/"+listProductos.get(i)+"/Presentación";
            CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);
            final int iConteo=i;
            bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Collection colKeys=document.getData().keySet();
                            Collection colValues=document.getData().values();
                            List listKeys=new ArrayList<>(colKeys);
                            List listValues=new ArrayList<>(colValues);
                            int iPosNombre,iPosPrecio; //Posiciones de Nombre y Precio. depende de como los encuentre en la base de datos
                            if(listKeys.get(0).toString().equals("Nombre")){
                                iPosNombre=0; iPosPrecio=1;
                            }
                            else{
                                iPosNombre=1; iPosPrecio=0;
                            }
                            listPresentacion.add(new Presentaciones_Entidad(
                                    listProductos.get(iConteo),
                                    Integer.valueOf(document.getId().substring(13)),
                                    listValues.get(iPosNombre).toString(),
                                    Integer.valueOf(listValues.get(iPosPrecio).toString())));
                            Log.d("Campos: ", listPresentacion.toString());

                        }
                        /*if(listPresentacion.size()>0 && listProductos.size()-1==iConteo && task.isComplete()){
                            fCargarPresentaciones(0);
                        }*/
                    } else {
                        Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                    }

                }
            });
        }

    }
    void fAdicionalesProductos(){
        listAdicionales=new ArrayList<>();

        for(int i=0;i<listProductos.size();i++){
            String sPath = "Negocios/Cra 21/Productos/"+listProductos.get(i)+"/Adicionales";
            CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);
            final int iConteo=i;
            bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Collection colKeys=document.getData().keySet();
                            Collection colValues=document.getData().values();
                            List listKeys=new ArrayList<>(colKeys);
                            List listValues=new ArrayList<>(colValues);
                            int iPosNombre,iPosPrecio;
                            if(listKeys.get(0).toString().equals("Nombre")){
                                iPosNombre=0; iPosPrecio=1;
                            }
                            else{
                                iPosNombre=1; iPosPrecio=0;
                            }
                            listAdicionales.add(new Adicionales_Entidad(
                                    listProductos.get(iConteo),
                                    Integer.valueOf(document.getId().substring(10)),
                                    listValues.get(iPosNombre).toString(),
                                    Integer.valueOf(listValues.get(iPosPrecio).toString())));

                            Log.d("Campos: ", listAdicionales.toString());
                        }
                        /*if(listAdicionales.size()>0 && listProductos.size()-1==iConteo && task.isComplete()){
                            fCargarAdicionales(0);
                        }*/
                    } else {
                        Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                    }

                }

            });
        }

    }
    void fOpcionesProductos(){
        listOpciones=new ArrayList<>();

        for(int i=0;i<listProductos.size();i++){
            String sPath = "Negocios/Cra 21/Productos/"+listProductos.get(i)+"/Opciones";
            CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);
            final int iConteo=i;
            bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Collection colKeys=document.getData().keySet();
                            Collection colValues=document.getData().values();
                            List listKeys=new ArrayList<>(colKeys);
                            List listValues=new ArrayList<>(colValues);
                            int iPosNombre,iPosPrecio;
                            if(listKeys.get(0).toString().equals("Nombre")){
                                iPosNombre=0; iPosPrecio=1;
                            }
                            else{
                                iPosNombre=1; iPosPrecio=0;
                            }
                            listOpciones.add(new Opciones_Entidad(
                                    listProductos.get(iConteo),
                                    Integer.valueOf(document.getId().substring(7)),
                                    listValues.get(iPosNombre).toString(),
                                    Integer.valueOf(listValues.get(iPosPrecio).toString())));
                            Log.d("Campos: ", listOpciones.toString());
                        }
                    } else {
                        Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                    }
                }
            });
        }

    }

    // ############################################################################################     Visualización de valores en pantalla
    void fCargarPresentaciones(int iProducto){
        String sProducto=listProductos.get(iProducto);
        String sNombre;
        int iNumero,iPrecio;
        float fPrecio;
        String sPrecio;

        //Agregar los adicionales
        orbPresentación_1.setVisibility(View.INVISIBLE);
        orbPresentación_2.setVisibility(View.INVISIBLE);

        for (int i=0;i<listPresentacion.size();i++){
            if (listPresentacion.get(i).getsProducto().equals(sProducto)){
                sNombre = listPresentacion.get(i).getsNombre();
                iNumero=listPresentacion.get(i).getiNumPresentacion();
                iPrecio=listPresentacion.get(i).getiPrecio();
                fPrecio=Float.valueOf(iPrecio)/1000;
                sPrecio="$"+fPrecio;

                PresentacionActual.add(new Presentaciones_Entidad(
                        sProducto,iNumero,sNombre,iPrecio));

                if(!sNombre.equals("")){
                    switch (iNumero){
                        case 1:orbPresentación_1.setText(sNombre+" ("+sPrecio+")");
                            orbPresentación_1.setVisibility(View.VISIBLE);break;
                        case 2:orbPresentación_2.setText(sNombre+" ("+sPrecio+")");
                            orbPresentación_2.setVisibility(View.VISIBLE);break;
                    }
                }
            }
        }

    }
    void fCargarAdicionales(int iProducto){
        String sProducto=listProductos.get(iProducto);
        String sNombre;
        int iNumero,iPrecio;
        float fPrecio;
        String sPrecio;

        //Agregar los adicionales
        ocbAdicional_1.setVisibility(View.INVISIBLE);
        ocbAdicional_2.setVisibility(View.INVISIBLE);
        ocbAdicional_3.setVisibility(View.INVISIBLE);
        ocbAdicional_4.setVisibility(View.INVISIBLE);
        ocbAdicional_5.setVisibility(View.INVISIBLE);
        ocbAdicional_6.setVisibility(View.INVISIBLE);

        for (int i=0;i<listAdicionales.size();i++){
            if (listAdicionales.get(i).getsProducto().equals(sProducto)){
                sNombre = listAdicionales.get(i).getsNombre();
                iNumero=listAdicionales.get(i).getiNumAdicional();
                iPrecio=listAdicionales.get(i).getiPrecio();
                fPrecio=Float.valueOf(iPrecio)/1000;
                sPrecio="$"+fPrecio;

                AdicionalesActual.add(new Adicionales_Entidad(
                        sProducto,iNumero,sNombre,iPrecio));

                if(!sNombre.equals("")){
                    switch (iNumero){
                        case 1:ocbAdicional_1.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_1.setVisibility(View.VISIBLE);break;
                        case 2:ocbAdicional_2.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_2.setVisibility(View.VISIBLE);break;
                        case 3:ocbAdicional_3.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_3.setVisibility(View.VISIBLE);break;
                        case 4:ocbAdicional_4.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_4.setVisibility(View.VISIBLE);break;
                        case 5:ocbAdicional_5.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_5.setVisibility(View.VISIBLE);break;
                        case 6:ocbAdicional_6.setText(sNombre+" ("+sPrecio+")");
                            ocbAdicional_6.setVisibility(View.VISIBLE);break;
                    }
                }
            }
        }

    }
    void fCargarOpciones(int iProducto){
        String sProducto=listProductos.get(iProducto);
        String sNombre;
        int iNumero,iPrecio;
        float fPrecio;
        String sPrecio;

        //Agregar los adicionales
        orgOpciones.setVisibility(View.INVISIBLE);
        orbOpción_1.setVisibility(View.INVISIBLE);
        orbOpción_2.setVisibility(View.INVISIBLE);
        orbOpción_3.setVisibility(View.INVISIBLE);
        orbOpción_4.setVisibility(View.INVISIBLE);
        orbOpción_5.setVisibility(View.INVISIBLE);
        orbOpción_6.setVisibility(View.INVISIBLE);

        for (int i=0;i<listOpciones.size();i++){
            if (listOpciones.get(i).getsProducto().equals(sProducto)){
                sNombre = listOpciones.get(i).getsNombre();
                iNumero=listOpciones.get(i).getiNumOpcion();
                iPrecio=listOpciones.get(i).getiPrecio();
                fPrecio=Float.valueOf(iPrecio)/1000;
                sPrecio="$"+fPrecio;

                OpcionesActual.add(new Opciones_Entidad(
                        sProducto,iNumero,sNombre,iPrecio));
                if(!sNombre.equals("")){
                    switch (iNumero){
                        case 1:orbOpción_1.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_1.setVisibility(View.VISIBLE);break;
                        case 2:orbOpción_2.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_2.setVisibility(View.VISIBLE);break;
                        case 3:orbOpción_3.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_3.setVisibility(View.VISIBLE);break;
                        case 4:orbOpción_4.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_4.setVisibility(View.VISIBLE);break;
                        case 5:orbOpción_5.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_5.setVisibility(View.VISIBLE);break;
                        case 6:orbOpción_6.setText(sNombre+" ("+sPrecio+")");
                            orbOpción_6.setVisibility(View.VISIBLE);break;
                    }
                }

            }
        }

    }
    void fBorrarActuales(){
        PresentacionActual.clear();
        AdicionalesActual.clear();
        OpcionesActual.clear();
    }

    // ############################################################################################     OPERAR
    void fOperar(){
        int iPrecioBase=0,iPrecioAdicionales=0,iPrecioOpciones=0,iPrecioFinal=0;
        int iAdicional_1,iAdicional_2,iAdicional_3,iAdicional_4,iAdicional_5,iAdicional_6;

        ////////////////////////
        //Precio base dependiendo de la presentación
        if (orbPresentación_1.isChecked()){
            iPrecioBase=PresentacionActual.get(0).getiPrecio();
        }
        if (orbPresentación_2.isChecked()){
            iPrecioBase=PresentacionActual.get(1).getiPrecio();
        }
        ///////////////////////
        //Precio de Adicionales
        if (ocbAdicional_1.isChecked()){
            iAdicional_1=AdicionalesActual.get(0).getiPrecio();
        }
        else{
            iAdicional_1=0;
        }
        if (ocbAdicional_2.isChecked()){
            iAdicional_2=AdicionalesActual.get(1).getiPrecio();
        }
        else{
            iAdicional_2=0;
        }
        if (ocbAdicional_3.isChecked()){
            iAdicional_3=AdicionalesActual.get(2).getiPrecio();
        }
        else{
            iAdicional_3=0;
        }
        if (ocbAdicional_4.isChecked()){
            iAdicional_4=AdicionalesActual.get(3).getiPrecio();
        }
        else{
            iAdicional_4=0;
        }
        if (ocbAdicional_5.isChecked()){
            iAdicional_5=AdicionalesActual.get(4).getiPrecio();
        }
        else{
            iAdicional_5=0;
        }
        if (ocbAdicional_6.isChecked()){
            iAdicional_6=AdicionalesActual.get(5).getiPrecio();
        }
        else{
            iAdicional_6=0;
        }

        iPrecioAdicionales=iAdicional_1+iAdicional_2+iAdicional_3+
                            iAdicional_4+iAdicional_5+iAdicional_6;

        ///////////////////////
        //Precio de opciones
        if (orbOpción_1.isChecked()){
            iPrecioOpciones=OpcionesActual.get(0).getiPrecio();
        }
        if (orbOpción_2.isChecked()){
            iPrecioOpciones=OpcionesActual.get(1).getiPrecio();
        }
        if (orbOpción_3.isChecked()){
            iPrecioOpciones=OpcionesActual.get(2).getiPrecio();
        }
        if (orbOpción_4.isChecked()){
            iPrecioOpciones=OpcionesActual.get(3).getiPrecio();
        }
        if (orbOpción_5.isChecked()){
            iPrecioOpciones=OpcionesActual.get(4).getiPrecio();
        }
        if (orbOpción_6.isChecked()){
            iPrecioFinal=OpcionesActual.get(5).getiPrecio();
        }

        iPrecioFinal=iPrecioBase+iPrecioAdicionales+iPrecioOpciones;
        DecimalFormat decimalFormat=new DecimalFormat("#,##0");
        String sPrecioFinal="$ "+decimalFormat.format(iPrecioFinal);

        otvPrecioFinal.setText(sPrecioFinal);



    }



    // ############################################################################################     GUARDAR EN VARIABLES LOCALES DEL DISPOSITIVO

    void fGuardarEnLocal(){
        /*
        //Crear lista de productos
        String  slistProductos="";
        for (int i=0;i<listProductos.size();i++){
            slistProductos=slistProductos+listProductos.get(i)+",";
        }
        //Crear lista de Adicionales primer


        //Telefono
        SharedPreferences sharedTelefono =getPreferences(context.MODE_PRIVATE);
        SharedPreferences.Editor editorTelefono=sharedTelefono.edit();
        editorTelefono.putString("Telefono",sTelefono);
        editorTelefono.commit();
        //listProductos
        for (int i=0;i<listProductos.size();i++){
            SharedPreferences sharedListProductos =getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editorListProductos=sharedListProductos.edit();
            editorListProductos.putString("Producto_"+(i+1),listProductos.get(i));
            editorListProductos.commit();
        }
        //listAdicionales
        for (int i=0;i<listProductos.size();i++){
            SharedPreferences sharedListProductos =getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editorListProductos=sharedListProductos.edit();
            editorListProductos.putString("Adiconales del Producto_"+(i+1),listProductos.get(i));
            editorListProductos.commit();
            for (int j=0;j<listAdicionales.size();j++){
                if(listAdicionales.get(j).sProducto.equals(listProductos.get(i)))
                SharedPreferences sharedListAdicionales =getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editorListAdicionales=sharedListAdicionales.edit();
                editorListAdicionales.putString("Adiconal_"+listAdicionales,listAdicionales.get(i));
                editorListAdicionales.commit();
            }
        }
        */








       /*SharedPreferences sharedsTelefono=getPreferences(context.MODE_PRIVATE);
        gsUsuario=sharedUsuario.getString("Usuario","No hay Usuario");
        SharedPreferences sharedTienda=getPreferences(context.MODE_PRIVATE);
        gsTienda=sharedTienda.getString("Tienda","No hay Tienda");

        public static String local_sTelefono;
        public static List<String> local_listProductos;
        public static List<Adicionales_Entidad> local_listAdicionales;
        public static List<Opciones_Entidad> local_listOpciones;
        public static List<Presentaciones_Entidad> local_listPresentacion;
        */
    }


}
