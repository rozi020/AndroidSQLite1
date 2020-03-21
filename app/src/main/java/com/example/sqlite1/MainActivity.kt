package com.example.sqlite1

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite1.`object`.EmpModelClass
import com.example.sqlite1.helper.MyAdapter
import com.example.sqlite1.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Types.NULL

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewRecord()
    }
// FUNGSI UNTUK MENYIMPAN DATA
    fun saveRecord(view: View){
    //UNTUK MENGAMBIL TEXT DARI FORM DAN MENYIMPAN KE VARIABLE BARU
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
    //KONFIRMASI INPUT DATA NAMA DAN EMAIL
        if( name.trim()!="" && email.trim()!=""){
            val status = databaseHandler.addEmployee(EmpModelClass(NULL,name, email))
            if(status > -1){
                //NOTIFIKASI JIKA FORM TELAH TERISI
                Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
                //MENGKOSONGKAN INPUTAN SEPERTI SEMULA
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
                viewRecord()
            }
        }else{
            //NOTIFIKASI JIKA ADA FORM YANG KOSONG
            Toast.makeText(applicationContext,"id dan email tidak diperbolehkan kosong", Toast.LENGTH_LONG).show()
        }

    }

    // fungsi untuk membaca data dari database dan menampilkannya dari listview
    fun viewRecord(){
        // membuat instanisasi databasehandler
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)

        // memamnggil fungsi viewemployee dari databsehandler untuk mengambil data
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        var index = 0

        // setiap data yang didapatkan dari database akan dimasukkan ke array
        for(e in emp){
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            index++
        }


        // membuat customadapter untuk view UI
        val myListAdapter = MyAdapter(this,empArrayId,empArrayName,empArrayEmail)
        listView.adapter = myListAdapter
        listView.setOnItemClickListener{
        parent, view, position, id ->
            u_id.setText(empArrayId[position])
            u_email.setText(empArrayEmail[position])
            u_name.setText(empArrayName[position])

        }

    }

    // fungsi untuk memperbarui data sesuai id user
    fun updateRecord(view: View){
            //UNTUK MENGAMBIL TEXT DARI FORM DAN MENYIMPAN KE VARIABLE BARU
            val updateId = u_id.text.toString()
            val updateName = u_name.text.toString()
            val updateEmail = u_email.text.toString()
            //MEMANGGIL OBJECT DARI HANDLER DATABASE
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            //KONFIRMASI JIKA ADA FORM YANG BELUM TERISI
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){

                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terupdate",Toast.LENGTH_LONG).show()
                    //MENGKOSONGKAN INPUTAN SEPERTI SEMULA
                    u_id.text.clear()
                    u_name.text.clear()
                    u_email.text.clear()
                    viewRecord()
                }
            }else{
                //NOTIFIKASI JIKA ADA FORM YANG BELUM TERISI
                Toast.makeText(applicationContext,"id, nama, dan email tidak diperbolehkan kosong",Toast.LENGTH_LONG).show()
            }

    }

    // fungsi untuk menghapus data berdasarkan id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id

            val deleteId = u_id.text.toString()
            //MEMANGGIL OBJECT DARI HANDLER DATABASE
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            //KONFIRMASI JIKA ADA DATA YANG BELUM TERISI
            if(deleteId.trim()!=""){

                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"",""))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terhapus",Toast.LENGTH_LONG).show()
                    //MENGKOSONGKAN INPUTAN SEPERTI SEMULA
                    u_id.text.clear()
                    u_name.text.clear()
                    u_email.text.clear()
                    viewRecord()
                }
            }else{
                //NOTIFIKASI JIKA ADA ID YANG KOSONG
                Toast.makeText(applicationContext,"id tidak boleh kosong",Toast.LENGTH_LONG).show()
            }

    }

    fun clearForm(view: View) {
        //MENGKOSONGKAN DATA PADA FORM
        u_id.text.clear()
        u_name.text.clear()
        u_email.text.clear()
        viewRecord()
    }


}
