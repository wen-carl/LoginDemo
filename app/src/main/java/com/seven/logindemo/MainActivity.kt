package com.seven.logindemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private var mQQLogin: QQLogin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_qq.setOnClickListener {
            if (null == mQQLogin) {
                mQQLogin = QQLogin(this, txtInfo)
            }

            mQQLogin?.logIn()
        }
    }

    override fun onDestroy() {
        mQQLogin?.logOut()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mQQLogin)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
