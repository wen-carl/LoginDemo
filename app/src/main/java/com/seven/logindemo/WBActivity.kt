package com.seven.logindemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import kotlinx.android.synthetic.main.activity_wb.*

private const val WB_APPKEY = "2071408846"
private const val REDIRECT_URL = "http://www.sina.com"
private const val SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"

class WBActivity : AppCompatActivity(), WbAuthListener {

    private val TAG = WBActivity::class.java.simpleName

    private lateinit var mSsoHandler: SsoHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wb)

        WbSdk.install(this, AuthInfo(this, WB_APPKEY, REDIRECT_URL, SCOPE))
        mSsoHandler = SsoHandler(this)

        btn_login.setOnClickListener {
            mSsoHandler.authorize(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun onSuccess(p0: Oauth2AccessToken?) {
        Log.i(TAG, p0.toString())
    }

    override fun onFailure(p0: WbConnectErrorMessage?) {
        Log.i(TAG, p0.toString())
    }

    override fun cancel() {
        Log.i(TAG, "onCancel")
    }
}
