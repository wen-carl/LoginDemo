package com.seven.logindemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener
import kotlinx.android.synthetic.main.activity_wb.*
import java.util.logging.Logger

private const val WB_APPKEY = "2071408846"
private const val REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"
private const val SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"

class WBActivity : AppCompatActivity(), WbAuthListener, RequestListener {

    private val TAG = WBActivity::class.java.simpleName

    private lateinit var mSsoHandler: SsoHandler
    private var mAccessToken: Oauth2AccessToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wb)

        WbSdk.install(this, AuthInfo(this, WB_APPKEY, REDIRECT_URL, SCOPE))
        mSsoHandler = SsoHandler(this)

        btn_login.setOnClickListener {
            mSsoHandler.authorize(this)
        }

        btn_logout.setOnClickListener {
            AccessTokenKeeper.clear(this)
            btn_login.isEnabled = true
            updateInfo()
        }

        btn_refresh.setOnClickListener {
            AccessTokenKeeper.refreshToken(WB_APPKEY, this, this)
            updateInfo()
        }

        mAccessToken = AccessTokenKeeper.readAccessToken(this)
        if (mAccessToken != null) {
            btn_login.isEnabled = false
            if (mAccessToken!!.isSessionValid()) {
                updateInfo()
            }
        }
        else
        {
            btn_logout.isEnabled = false
            btn_refresh.isEnabled = false
        }

    }

    private fun updateInfo() {
        val bundle = mAccessToken?.bundle
        if (null != bundle) {
            tv_info.text = (mAccessToken!!.toString() + "\n" + bundle.toString())
        }
        else
        {
            tv_info.text = mAccessToken!!.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    // login call back
    override fun onSuccess(p0: Oauth2AccessToken?) {
        Log.i(TAG, p0.toString())
        btn_login.isEnabled = false
        mAccessToken = p0
        AccessTokenKeeper.writeAccessToken(this, p0)
        runOnUiThread {
            updateInfo()
        }
    }

    override fun onFailure(p0: WbConnectErrorMessage?) {
        Log.i(TAG, "onFailure\n" + p0?.errorMessage)
    }

    override fun cancel() {
        Log.i(TAG, "onCancel")
    }

    // refresh call back
    override fun onWeiboException(p0: WeiboException?) {
        Log.i(TAG, "onWeiboException\n" + p0?.localizedMessage)
    }

    override fun onComplete(p0: String?) {
        Log.i(TAG, "onComplete\n" + p0)
        mAccessToken = Oauth2AccessToken.parseAccessToken(p0)
        updateInfo()
    }
}
