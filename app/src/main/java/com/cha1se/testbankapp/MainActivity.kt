package com.cha1se.testbankapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var typeTxt: TextView
    lateinit var brandTxt: TextView
    lateinit var prepaidTxt: TextView
    lateinit var lengthTxt: TextView
    lateinit var luhnTxt: TextView
    lateinit var emojiTxt: TextView
    lateinit var nameTxt: TextView
    lateinit var latitudeLongitudeTxt: TextView
    lateinit var bankNameAndCityTxt: TextView
    lateinit var urlTxt: TextView
    lateinit var phoneTxt: TextView
    lateinit var binNum: AutoCompleteTextView
    lateinit var checkBtn: Button

    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        typeTxt = findViewById(R.id.typeTextView)
        brandTxt = findViewById(R.id.brandTextView)
        prepaidTxt = findViewById(R.id.prepaidTextView)
        lengthTxt = findViewById(R.id.lengthTextView)
        luhnTxt = findViewById(R.id.luhnTextView)
        emojiTxt = findViewById(R.id.emojiTextView)
        nameTxt = findViewById(R.id.nameTextView)
        latitudeLongitudeTxt = findViewById(R.id.latitudeLongitudeTextView)
        bankNameAndCityTxt = findViewById(R.id.bankNameAndCity)
        urlTxt = findViewById(R.id.urlTextView)
        phoneTxt = findViewById(R.id.phoneTextView)
        binNum = findViewById(R.id.binNumber)
        checkBtn = findViewById(R.id.checkButton)

        var binList: MutableList<Int> = mutableListOf()

        val db = DBHelper(this, null)

        val cursor = db.getBin()
        cursor!!.moveToFirst()

        while (cursor.moveToNext()) {
            binList.add(cursor.getString(cursor.getColumnIndex(DBHelper.BIN_COL)).toInt())
        }
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, binList)
        binNum.setAdapter(adapter)

        checkBtn.setOnClickListener(View.OnClickListener {
            if (!binNum.text.toString().equals("")) {

                db.addBin(binNum.text.toString())

                cursor.moveToLast()
                binList.add(cursor.getString(cursor.getColumnIndex(DBHelper.BIN_COL)).toInt())

                binNum.setAdapter(ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, binList))

                val dBInterface = DBInterface.create().getBankInfo(binNum.text.toString())

                dBInterface.enqueue( object : Callback<DataList> {
                    override fun onResponse(call: Call<DataList>?, response: Response<DataList>?) {

                        if(response?.body() != null) {

                            typeTxt.text = response.body()?.type.toString()
                            brandTxt.text = response.body()?.brand.toString()
                            if (response.body()?.prepair!!) {
                                prepaidTxt.setText("Yes")
                            } else {
                                prepaidTxt.setText("No")
                            }
                            lengthTxt.text = response.body()?.number?.length.toString()
                            if (response.body()?.prepair!!) {
                                luhnTxt.setText("Yes")
                            } else {
                                luhnTxt.setText("No")
                            }
                            emojiTxt.text = response.body()?.country?.emoji.toString()
                            nameTxt.text = response.body()?.country?.name.toString()
                            latitudeLongitudeTxt.text = "(latitude: ${response.body()?.country?.latitude.toString()}, longitude: ${response.body()?.country?.longitude.toString()})"
                            bankNameAndCityTxt.text = "${response.body()?.bank?.name}, ${response.body()?.bank?.city.toString()}"
                            urlTxt.text = response.body()?.bank?.url.toString()
                            phoneTxt.text = response.body()?.bank?.phone.toString()

                            urlTxt.setOnClickListener(View.OnClickListener {
                                val url = "https://" + urlTxt.text.toString()
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                            })

                            phoneTxt.setOnClickListener(View.OnClickListener {
                                val intent = Intent(Intent.ACTION_DIAL);
                                intent.data = Uri.parse("tel:" + phoneTxt.text.toString())
                                startActivity(intent)
                            })

                            latitudeLongitudeTxt.setOnClickListener(View.OnClickListener {
                                val uri =
                                    String.format(Locale.ENGLISH, "geo:%f,%f", response.body()?.country?.latitude.toString().toFloat(), response.body()?.country?.longitude.toString().toFloat())
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                startActivity(intent)
                            })

                        }
                    }

                    override fun onFailure(call: Call<DataList>?, t: Throwable?) {
                        Log.e("TAG", t?.message.toString())
                    }
                })

            }
        })

    }

    override fun onPause() {
        mSharedPref = getPreferences(MODE_PRIVATE)
        val mEditor: Editor = mSharedPref.edit()
        mEditor.putString("BIN", binNum.getText().toString())
        mEditor.apply()
        super.onPause()
    }

    override fun onResume() {
        mSharedPref = getPreferences(MODE_PRIVATE)
        val saved_pass = mSharedPref.getString("BIN", "")
        binNum.setText(saved_pass)
        super.onResume()
    }
}