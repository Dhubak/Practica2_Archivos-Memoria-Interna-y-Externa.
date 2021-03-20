package mx.tecnm.tepic.ladm_u1_practica2

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var archivo = ""

        guardar.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 0
                )
            }

           archivo = nombre.text.toString()

            if (!interna.isChecked && !externa.isChecked) {
                AlertDialog.Builder(this).setTitle("ERROR")
                    .setMessage("PRIMERO ELIJE UNA MEMORIA PARA GUARDAR")
                    .setPositiveButton("OK") { d, _ -> d.dismiss() }
                    .show()
            }

            if (externa.isChecked) {
                if (guardarMemoriaExterna(archivo) == true) {
                    AlertDialog.Builder(this).setTitle("LISTO")
                        .setMessage("ARCHIVO GUARDADO EN SD")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE PUDO GUARDAR EL ARCHIVO EN SD")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                }
            }
            if(interna.isChecked){
                if(guardarMemoriaInterna(archivo)==true){
                    AlertDialog.Builder(this).setTitle("LISTO")
                        .setMessage("ARCHIVO GUARDADO EN MEMORIA INTERNA")
                        .setPositiveButton("OK"){d,_->d.dismiss()}
                        .show()
                }else{
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE PUDO GUARDAR EL ARCHIVO EN MEMORIA INTERNA")
                        .setPositiveButton("OK"){d,_->d.dismiss()}
                        .show()
                }
            }
        }

        abrir.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }

            archivo = nombre.text.toString()

            if(!interna.isChecked && !externa.isChecked) {
                AlertDialog.Builder(this).setTitle("ERROR")
                    .setMessage("PRIMERO ELIJE UNA MEMORIA")
                    .setPositiveButton("OK"){d,_->d.dismiss()}
                    .show()
            }
            if(externa.isChecked){
                if(leerMemoriaExterna(archivo) == false) {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("ARCHIVO "+archivo+" NO ENCONTRADO EN LA SD")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                }
            }
            if(interna.isChecked){
                if(leerMemoriaInterna(archivo)==false){
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("ARCHIVO "+archivo+" NO ENCONTRADO EN MEMORIA INTERNA")
                        .setPositiveButton("OK"){d,_->d.dismiss()}
                        .show()
                }
            }
        }
    }



        private fun guardarMemoriaInterna(nombreArchivo: String): Boolean {
            try {
                var flujoSalida = OutputStreamWriter(openFileOutput(nombreArchivo, Context.MODE_PRIVATE))
                var data = frase.text.toString()

                flujoSalida.write(data)
                flujoSalida.flush()
                flujoSalida.close()

                frase.setText("")
                nombre.setText("")
            } catch (io: IOException) {
                return false
            }
            return true
        }

        private fun leerMemoriaInterna(archivo: String): Boolean {
            try {
                var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(archivo)))
                var data = flujoEntrada.readLine()

                frase.setText(data)
                flujoEntrada.close()

            } catch (io: IOException) {
                return false
            }
            return true
        }

        private fun guardarMemoriaExterna(archivo: String): Boolean {
            try {
                if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                    //AQUI ENTRA SI NO HAY MEMORIA SD MONTADA
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO EXISTE MEMORIA SECUNDARIA INSERTADA")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                    return false
                }
                var rutaSD = Environment.getExternalStorageDirectory()
                var archivoSD = File(rutaSD.absolutePath, archivo)
                var flujoSalida = OutputStreamWriter(FileOutputStream(archivoSD))

                flujoSalida.write(frase.text.toString())
                flujoSalida.flush()
                flujoSalida.close()

                frase.setText("")
                nombre.setText("")
            } catch (io: IOException) {
                return false
            }
            return true
        }

        private fun leerMemoriaExterna(archivo: String): Boolean {
            try {
                if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                    //AQUI ENTRA SI NO HAY MEMORIA SD MONTADA
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO EXISTE MEMORIA SECUNDARIA INSERTADA")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                    return false
                }
                var rutaSD = Environment.getExternalStorageDirectory()
                var flujo = File(rutaSD.absolutePath, archivo)
                var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(flujo)))
                var data = flujoEntrada.readLine()

                frase.setText(data)
                flujoEntrada.close()
            } catch (io: IOException) {
                return false
            }
            return true
        }
    }
