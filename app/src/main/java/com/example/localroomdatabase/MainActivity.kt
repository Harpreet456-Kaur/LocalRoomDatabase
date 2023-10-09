package com.example.localroomdatabase

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.localroomdatabase.databinding.ActivityMainBinding
import java.nio.channels.AsynchronousByteChannel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userDatabase: UserDatabase
    lateinit var arrayAdapter: ArrayAdapter<UserEntity>
    var arrayList = ArrayList<UserEntity>()
    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        userDatabase = UserDatabase.getInstance(this)
        binding.list.adapter = arrayAdapter

        getList()

        binding.list.setOnItemClickListener { parent, view, position, id ->
            this.position = position
            var userList = arrayList[position]
            binding.name.setText(userList.name)
            binding.info.setText(userList.information)
        }

        binding.addBtn.setOnClickListener {
            if (binding.name.text.isNullOrEmpty() || binding.info.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter information", Toast.LENGTH_SHORT).show()
            } else {
                if (position > -1) {
                    arrayList[position].name = binding.name.text.toString()
                    arrayList[position].information = binding.info.text.toString()

                    class UpdateData : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void?): Void? {
                            userDatabase.userDao().update(arrayList[position])
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            getList()
                        }
                    }
                    UpdateData().execute()
                } else {
                    var userEntity = UserEntity(
                        name = binding.name.text.toString(),
                        information = binding.info.text.toString()
                    )

                    class SaveData : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg p0: Void?): Void? {
                            userDatabase.userDao().insert(userEntity)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            getList()
                        }
                    }
                    SaveData().execute()
                }
            }
        }

        binding.list.setOnItemLongClickListener { parent, view, position, id ->
            class DeleteData : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    userDatabase.userDao().delete(arrayList[position])
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    getList()
                }
            }
            DeleteData().execute()
            return@setOnItemLongClickListener true
        }

        setContentView(binding.root)
    }

    fun getList(){
        binding.name.text.clear()
        binding.info.text.clear()
        position = -1
        arrayList.clear()

        class getData : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                arrayList.addAll(userDatabase.userDao().getAll())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayAdapter.notifyDataSetChanged()
            }
        }
        getData().execute()
    }

}