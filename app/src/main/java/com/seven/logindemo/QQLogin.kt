package com.seven.logindemo

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONException
import org.json.JSONObject

const val QQ_APPID = "1106970090"

class QQLogin(val activity: Activity, val txtInfo: TextView) : IUiListener {

    private val TAG = QQLogin::class.java.simpleName

    private var mType = ListenerType.NONE
    private val mTencent: Tencent = Tencent.createInstance(QQ_APPID, activity.applicationContext)

    fun logIn() {
        mType = ListenerType.LOG_IN
        mTencent.login(activity, "all", this)
    }

    fun logOut() {
        mTencent.logout(activity)
    }

    override fun onComplete(p0: Any?) {
        Log.i(TAG, p0.toString())

        when (mType) {
            ListenerType.LOG_IN -> {
                try {
                    val json = JSONObject(p0.toString())
                    val openid = json.getString("openid")
                    mTencent.openId = openid
                    val token = json.getString("access_token")
                    val time = json.getString("expires_in")
                    mTencent.setAccessToken(token, time)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                val token = mTencent.qqToken
                val userInfo = UserInfo(activity.applicationContext, token)
                mType = ListenerType.USER_INFO
                userInfo.getUserInfo(this)
            }
            ListenerType.USER_INFO -> {
                activity.runOnUiThread {
                    txtInfo.text = p0.toString()
                }
            }
            ListenerType.NONE -> {}
        }
    }

    override fun onCancel() {
        Log.d(TAG, "onCancel")
        activity.runOnUiThread {
            txtInfo.text = "onCancel"
        }
    }

    override fun onError(p0: UiError?) {
        Log.d(TAG, p0.toString())
        activity.runOnUiThread {
            txtInfo.text = p0.toString()
        }
    }
}

enum class ListenerType {
    NONE,
    LOG_IN,
    USER_INFO
}