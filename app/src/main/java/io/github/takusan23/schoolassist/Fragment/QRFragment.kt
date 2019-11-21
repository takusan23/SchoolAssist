package io.github.takusan23.schoolassist.Fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.takusan23.schoolassist.DataBase.TimeTableSQLiteHelper
import io.github.takusan23.schoolassist.MainActivity
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.fragment_qr.*
import org.json.JSONArray
import android.R.attr.data
import com.google.zxing.EncodeHintType
import com.google.zxing.integration.android.IntentResult
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*


class QRFragment :Fragment(){

    lateinit var timeTableSQLiteHelper: TimeTableSQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //初期化
        timeTableSQLiteHelper = TimeTableSQLiteHelper(context)
        sqLiteDatabase = timeTableSQLiteHelper.writableDatabase
        timeTableSQLiteHelper.setWriteAheadLoggingEnabled(false)

        //QRコード生成

        val writer = QRCodeWriter()
        val encodeHint = mutableMapOf<EncodeHintType, String>()
        encodeHint.put(EncodeHintType.CHARACTER_SET,"shiftjis") //日本語対応させる

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(loadTimeTable(), BarcodeFormat.QR_CODE, 1000, 1000,encodeHint)
        fragment_qr_imageview.setImageBitmap(bitmap)

        fragment_qr_camera.setOnClickListener {
            //結果はMainActivity.ktに
            IntentIntegrator(activity).initiateScan();
        }
    }

    fun loadTimeTable():String {
        val jsonArray = JSONArray()
        for (i in 0 until 5){
            val list = arrayListOf("月", "火", "水", "木", "金")
            val cursor = sqLiteDatabase.query(
                "timetable_db",
                arrayOf("subject"),
                "week=?",
                arrayOf(list[i]),
                null,
                null,
                null
            )
            cursor.moveToFirst()
            //JSON
            if (cursor.count != 0) {
                val json = JSONArray(cursor.getString(0))
                jsonArray.put(json)
            } else {
                Toast.makeText(context, "未登録です", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
        return jsonArray.toString()
    }

}