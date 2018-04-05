package com.dam.spbike;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RecoveryActivity extends Activity{
    private MiBaseDatos MDB;
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText editEmail, sub, msg,editContr1,editContr2;
    TextView mensinfo;
    Button cambiarclave;
    String rec, subject, textMessage;
    String cadenaaleatoria;
    String email,contrasena1,contrasena2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        MDB = new MiBaseDatos(getApplicationContext());
        context = this;

    }

    public void IrComprobar(View v) {
        editEmail = (EditText) findViewById(R.id.input_email);
        email = editEmail.getText().toString();
        if (email.length() > 0) {
            if (!validarEmail(email)) {
                editEmail.setError("Email no valido");
            } else {
                if (!MDB.compruebaemailBD(email)) {
                    editEmail.setError("Email no registrado");
                } else {
                    //Falta comprobar que el email esta en nuestra BD.(HACER)
                    cadenaaleatoria = generaClaveAleatoria();
                    rec = email;
                    subject = "Recuperar Contraseña Spbike";
                    textMessage = "Su nueva contraseña es: " + cadenaaleatoria;

                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");

                    session = Session.getDefaultInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("esspbike@gmail.com", "spbike123");
                        }
                    });

                    pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

                    RetreiveFeedTask task = new RetreiveFeedTask();
                    task.execute();

                    mensinfo = (TextView) findViewById(R.id.texto_conectar2);
                    mensinfo.setVisibility(View.VISIBLE);

                    editContr1 = (EditText) findViewById(R.id.input_contrasena);
                    editContr2 = (EditText) findViewById(R.id.input_contrasena2);
                    cambiarclave = (Button) findViewById(R.id.boton_cambiarclave);

                    editContr1.setVisibility(View.VISIBLE);
                    editContr2.setVisibility(View.VISIBLE);
                    cambiarclave.setVisibility(View.VISIBLE);
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Inserte información", Toast.LENGTH_SHORT).show();
        }
    }

    public void IrCambiar(View v) {
        contrasena1 = editContr1.getText().toString();
        contrasena2 = editContr2.getText().toString();


        if(!contrasena1.equals(cadenaaleatoria))
        {
            editContr1.setError("la contraseña no corresponde");
        }
        else {
            if (contrasena2.length() < 8) {
                editContr2.setError("La contraseña al menos debe tener 8 caracteres");
            } else {
                //Se actualizara la nueva contraseña en el usuario con el email correspondiente
                if (!MDB.compruebaemailBD(email)) {
                    editEmail.setError("Email no registrado");
                } else {
                    MDB.modificarUSUARIO(contrasena2, email);

                    Toast.makeText(getApplicationContext(),
                            "Se ha actualizado la nueva contraseña", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
            }
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("joselucrack@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            //editEmail.setText("");
            //msg.setText("");
            //sub.setText("");
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public String generaClaveAleatoria(){
            char[] elementos={'0','1','2','3','4','5','6','7','8','9' ,'a',
                    'b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t',
                    'u','v','w','x','y','z'};

            char[] conjunto = new char[8];
            String pass;
            for(int i=0;i<8;i++){
                int el = (int)(Math.random()*37);
                conjunto[i]=(char)elementos[el];
            }
            pass = new String(conjunto);
            System.out.println("La cadena aleatoria creada es: " + pass);
            return pass;
        }



    protected void sendEmail(String TO, String clave) {
        //String[] TO = {"joseluisgbur@gmail.com"}; //aquí pon tu correo
        String[] CC = {"joseluisgbur@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        // Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recuperación clave de acceso Spbike");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Su nueva clave de acceso es" + clave);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
            Toast.makeText(getApplicationContext(),
                    "Se ha enviado un email con su nueva clave", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

}