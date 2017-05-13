package com.example.labsoftware1.personasbasededatos;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Registrar extends AppCompatActivity {
    private EditText cajaCedula;
    private EditText cajaNombre;
    private EditText cajaApellido;
    private RadioButton rdMasculino;
    private RadioButton rdFemenino;
    private CheckBox chkProgramar;
    private CheckBox chkLeer;
    private CheckBox chkBailar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        cajaCedula = (EditText)findViewById(R.id.txtcedula);
        cajaNombre = (EditText)findViewById(R.id.txtNombre);
        cajaApellido = (EditText)findViewById(R.id.txtApellido);
        rdMasculino= (RadioButton) findViewById(R.id.r1);
        rdFemenino= (RadioButton) findViewById(R.id.r2);
        chkProgramar = (CheckBox)findViewById(R.id.chkProgramar);
        chkLeer = (CheckBox)findViewById(R.id.chkLeer);
        chkBailar = (CheckBox)findViewById(R.id.chkBailar);
    }

    public boolean validarTodo(){
        if(cajaCedula.getText().toString().isEmpty()){
            cajaCedula.setError("Digite la cédula");
            cajaCedula.requestFocus();
            return false;
        }
        if(cajaNombre.getText().toString().isEmpty()){
            cajaNombre.setError("Digite el Nombre");
            cajaNombre.requestFocus();
            return false;
        }
        if(cajaApellido.getText().toString().isEmpty()){
            cajaApellido.setError("Digite el Apellido");
            cajaApellido.requestFocus();
            return false;
        }

        if((!chkProgramar.isChecked())&& (!chkLeer.isChecked())&& (!chkBailar.isChecked())){
            new AlertDialog.Builder(this).setMessage("Seleccione por lo menos un pasatiempo").setCancelable(true).show();
            return false;
        }
        return true;
    }

    public void guardar(View v){
        String foto,cedula,nombre,apellido,sexo,pasatiempo ="";
        Persona p;

        if(validarTodo()){
            cedula = cajaCedula.getText().toString();
            foto = String.valueOf(fotoAleatoria());
            nombre = cajaNombre.getText().toString();
            apellido=cajaApellido.getText().toString();
            if(rdMasculino.isChecked()) sexo = getResources().getString(R.string.Masculino);
            else sexo = getResources().getString(R.string.femenino);

            if(chkProgramar.isChecked()){
                pasatiempo = getResources().getString(R.string.programar)+", ";
            }
            if(chkLeer.isChecked()){
                pasatiempo = pasatiempo+getResources().getString(R.string.leer)+", ";
            }

            if(chkBailar.isChecked()){
                pasatiempo = pasatiempo+getResources().getString(R.string.bailar)+", ";
            }

            pasatiempo = pasatiempo.substring(0,pasatiempo.length()-2);
            p = new Persona(foto,cedula,nombre,apellido,sexo,pasatiempo);
            p.guardar(getApplicationContext());

            new AlertDialog.Builder(this).
                    setMessage("Persona Guardada Exitosamente!").
                    setCancelable(true).show();


            limpiar();

        }
    }

    public int fotoAleatoria(){
        int fotos[] = {R.drawable.images,R.drawable.images2,R.drawable.images3};
        int numero = (int)(Math.random() * 3);
        return fotos[numero];
    }
    public boolean validarCedula() {
        if (cajaCedula.getText().toString().isEmpty()) {
            cajaCedula.setError("Digite la cédula");
            cajaCedula.requestFocus();
            return false;
        }
        return true;
    }

    public void limpiar(){
        cajaCedula.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        rdMasculino.setChecked(true);
        chkProgramar.setChecked(false);
        chkLeer.setChecked(false);
        chkBailar.setChecked(false);

        cajaCedula.requestFocus();

    }

    public void limpiar(View v){
        limpiar();
    }

    public void buscar(View v){
        Persona p;
        String pasatiempos;
        if(validarCedula()) {
            p = Datos.buscarPersona(getApplicationContext(), cajaCedula.getText().toString());
            if(p!=null){
                cajaNombre.setText(p.getNombre());
                cajaApellido.setText(p.getApellido());
                if(p.getSexo().equalsIgnoreCase(getResources().getString(R.string.Masculino)))rdMasculino.setChecked(true);
                else rdFemenino.setChecked(true);

                pasatiempos = p.getPasatiempo();
                if(pasatiempos.contains(getResources().getString(R.string.programar))) chkProgramar.setChecked(true);
                if(pasatiempos.contains(getResources().getString(R.string.leer))) chkLeer.setChecked(true);
                if(pasatiempos.contains(getResources().getString(R.string.bailar))) chkBailar.setChecked(true);
            }
        }
        }

    public void eliminar(View v){
        Persona p;
        if(validarCedula()) {
            p = Datos.buscarPersona(getApplicationContext(), cajaCedula.getText().toString());
            if(p!=null){
               AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle("Confirmación");
                ventana.setMessage("¿Está seguro que desea eliminar esta persona?");
                ventana.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Persona p;
                        p = Datos.buscarPersona(getApplicationContext(), cajaCedula.getText().toString());

                        p.eliminar(getApplicationContext());
                        limpiar();
                        Toast.makeText(getApplicationContext(), "Persona Eliminada Exitosamente",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                ventana.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cajaCedula.requestFocus();
                    }
                });

                   ventana.show();
            }
        }
    }





}
