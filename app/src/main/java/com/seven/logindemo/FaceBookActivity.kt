package com.seven.logindemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_face_book.*
import org.json.JSONObject

class FaceBookActivity : AppCompatActivity(), FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    private val TAG = FaceBookActivity::class.java.simpleName

    private var mCallbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_book)

        FacebookSdk.sdkInitialize(applicationContext)
        mCallbackManager = CallbackManager.Factory.create()
        btn_facebook.setReadPermissions("email")
        btn_facebook.registerCallback(mCallbackManager, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(result: LoginResult?) {
        Log.i(TAG, result.toString())

        val request = GraphRequest.newMeRequest(result?.accessToken, this)
        val parameters = Bundle()
        parameters.putString(
            "fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name"
        )
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onCancel() {
        Log.i(TAG, "onCancel")
    }

    override fun onError(error: FacebookException?) {
        Log.i(TAG, "onError", error)
    }

    override fun onCompleted(json: JSONObject?, response: GraphResponse?) {
        Log.i(TAG, json.toString())
        val pic = json?.optJSONObject("picture")
        runOnUiThread {
            txtInfo.text = json.toString()
        }
    }
}
